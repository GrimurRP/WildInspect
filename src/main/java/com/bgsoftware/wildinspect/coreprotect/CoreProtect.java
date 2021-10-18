package com.bgsoftware.wildinspect.coreprotect;

import com.bgsoftware.wildinspect.Locale;
import com.bgsoftware.wildinspect.WildInspect;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider;
import com.bgsoftware.wildinspect.utils.InspectPlayers;
import com.bgsoftware.wildinspect.utils.StringUtils;
import net.coreprotect.database.Database;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CoreProtect {

    private static final Pattern NO_DATA_PATTERN = Pattern.compile("CoreProtect - No (.*) found for (.*)\\.");
    private static final Pattern DATA_HEADER_PATTERN = Pattern.compile("----- (.*) ----- \\(x(.*)/y(.*)/z(.*)\\)");
    private static final Pattern DATA_LINE_PATTERN = Pattern.compile("(.*) - (.\\S*) (.*) (.*)\\.");
    private static final Pattern DATA_FOOTER_PATTERN = Pattern.compile("(.*) ([0-9]+)\\/([0-9]+)(.*) \"(.*)\"\\.");

    private final WildInspect plugin;

    public CoreProtect(WildInspect plugin) {
        this.plugin = plugin;
    }

    public void performLookup(LookupType type, Player pl, Block bl, int page) {
        ClaimsProvider.ClaimPlugin claimPlugin = plugin.getHooksHandler().getRegionAt(pl, bl.getLocation());

        if (claimPlugin == ClaimsProvider.ClaimPlugin.NONE) {
            Locale.NOT_INSIDE_CLAIM.send(pl);
            return;
        }

        if (!plugin.getHooksHandler().hasRole(claimPlugin, pl, bl.getLocation(), plugin.getSettings().requiredRoles)) {
            Locale.REQUIRED_ROLE.send(pl, StringUtils.format(plugin.getSettings().requiredRoles));
            return;
        }

        if (InspectPlayers.isCooldown(pl)) {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            Locale.COOLDOWN.send(pl, df.format(InspectPlayers.getTimeLeft(pl) / 1000));
            return;
        }

        if (plugin.getSettings().cooldown != -1)
            InspectPlayers.setCooldown(pl);

        InspectPlayers.setBlock(pl, bl);

        if (plugin.getSettings().historyLimitPage < page) {
            Locale.LIMIT_REACH.send(pl);
            return;
        }
        try (Statement statement = Database.getConnection(false, 1000).createStatement()) {
            String[] lookup = new String[]{};
            if (type == LookupType.CONTAINER_LOOKUP) {
                lookup = CoreProtectHook.performChestLookup(statement, pl, bl, page);
            } else if (type == LookupType.CLICK_LOOKUP) {
                lookup = CoreProtectHook.performInteractLookup(statement, pl, bl, page);
            } else if (type == LookupType.BLOCK_LOOKUP) {
                lookup = CoreProtectHook.performBlockLookup(statement, pl, bl.getState(), page);
            }
            Matcher matcher;
            StringBuilder message = new StringBuilder();
            boolean empty = true;
            for (String line : lookup) {
                String stripped = ChatColor.stripColor(line);
                if ((matcher = NO_DATA_PATTERN.matcher(stripped)).matches()) {
                    switch (matcher.group(1)) {
                        case "player interactions" -> message.append("\n").append(Locale.NO_BLOCK_INTERACTIONS.getMessage(matcher.group(2)));
                        case "block data" -> message.append("\n").append(Locale.NO_BLOCK_DATA.getMessage(matcher.group(2)));
                        case "container transactions" -> message.append("\n").append(Locale.NO_CONTAINER_TRANSACTIONS.getMessage(matcher.group(2)));
                    }
                    pl.sendMessage("match no pattern");
                } else if ((matcher = DATA_HEADER_PATTERN.matcher(stripped)).matches()) {
                    message.append("\n").append(Locale.INSPECT_DATA_HEADER.getMessage(matcher.group(2), matcher.group(3), matcher.group(4)));
                } else if ((matcher = DATA_LINE_PATTERN.matcher(stripped)).matches()) {
                    if (!plugin.getSettings().hiddenPlayers.isEmpty() && plugin.getSettings().hiddenPlayers.stream().anyMatch(matcher.group(2)::equalsIgnoreCase))
                        continue;
                    double days = Double.parseDouble(matcher.group(1).split("/")[0].replace(",", ".")) / 24;
                    if (plugin.getSettings().historyLimitDate >= days) {
                        empty = false;
                        message.append("\n").append(Locale.INSPECT_DATA_ROW.getMessage(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4)));
                    }
                } else if ((matcher = DATA_FOOTER_PATTERN.matcher(stripped)).matches()) {
                    int linePage = Integer.parseInt(matcher.group(2));
                    message.append("\n").append(Locale.INSPECT_DATA_FOOTER.getMessage(Math.max(linePage, 1),
                            Math.min(Integer.parseInt(matcher.group(3)), plugin.getSettings().historyLimitPage)));
                }
            }
            pl.sendMessage(empty ? Locale.NO_BLOCK_DATA.getMessage("that page") : message.substring(1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
