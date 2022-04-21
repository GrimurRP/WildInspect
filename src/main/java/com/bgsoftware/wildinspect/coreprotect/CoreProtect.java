package com.bgsoftware.wildinspect.coreprotect;

import com.bgsoftware.wildinspect.WildInspect;
import com.bgsoftware.wildinspect.config.Config;
import com.bgsoftware.wildinspect.config.Message;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider;
import com.bgsoftware.wildinspect.utils.InspectPlayers;
import com.bgsoftware.wildinspect.utils.StringUtils;
import net.coreprotect.database.Database;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
    private static final Pattern DATA_LINE_PATTERN = Pattern.compile("(.*) (-|\\+) (.\\S*) (.*) (.*)\\.");
    private static final Pattern DATA_FOOTER_PATTERN = Pattern.compile("(.*) ([0-9]+)\\/([0-9]+)(.*) \"(.*)\"\\.");

    private final WildInspect plugin;

    public CoreProtect(WildInspect plugin) {
        this.plugin = plugin;
    }

    public void performLookup(LookupType type, Player pl, Block bl, int page) {
        ClaimsProvider.ClaimPlugin claimPlugin = plugin.getHooksHandler().getRegionAt(pl, bl.getLocation());

        if (claimPlugin == ClaimsProvider.ClaimPlugin.NONE) {
            pl.sendMessage(Message.PREFIX.getMessage() + Message.NOT_INSIDE_CLAIM.getMessage());
            return;
        }

        if (!plugin.getHooksHandler().hasRole(claimPlugin, pl, bl.getLocation(), Config.REQUIRED_ROLES.getStrings())) {
            pl.sendMessage(Message.PREFIX.getMessage() + Message.REQUIRED_ROLE.getMessage(StringUtils.format(Config.REQUIRED_ROLES.getStrings())));
            return;
        }

        if (InspectPlayers.isCooldown(pl)) {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            pl.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Message.COOLDOWN.getMessage(df.format(Math.max(1, InspectPlayers.getTimeLeft(pl) / 1000L)))));
            return;
        }

        if (Config.COOLDOWN.getInt() != -1)
            InspectPlayers.setCooldown(pl);

        InspectPlayers.setBlock(pl, bl);
        int historyLimitPage = Config.HISTORY_LIMIT_PAGE.getInt() != -1 ? Config.HISTORY_LIMIT_PAGE.getInt() : Integer.MAX_VALUE;
        if (historyLimitPage < page) {
            pl.sendMessage(Message.PREFIX.getMessage() + Message.LIMIT_REACH.getMessage());
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
                        case "player interactions" -> message.append("\n").append(Message.NO_BLOCK_INTERACTIONS.getMessage(matcher.group(2)));
                        case "block data" -> message.append("\n").append(Message.NO_BLOCK_DATA.getMessage(matcher.group(2)));
                        case "container transactions" -> message.append("\n").append(Message.NO_CONTAINER_TRANSACTIONS.getMessage(matcher.group(2)));
                    }
                    pl.sendMessage("match no pattern");
                } else if ((matcher = DATA_HEADER_PATTERN.matcher(stripped)).matches()) {
                    message.append("\n").append(Message.INSPECT_DATA_HEADER.getMessage(matcher.group(2), matcher.group(3), matcher.group(4)));
                } else if ((matcher = DATA_LINE_PATTERN.matcher(stripped)).matches()) {
                    if (!Config.HIDDEN_NAMES.getStringList().isEmpty() && Config.HIDDEN_NAMES.getStringList().stream().anyMatch(matcher.group(2)::equalsIgnoreCase))
                        continue;
                    double days = Double.parseDouble(matcher.group(1).split("/")[0].replace(",", ".")) / 24;
                    if (Config.HISTORY_LIMIT_DATE.getInt() >= days) {
                        empty = false;
                        message.append("\n").append(Message.INSPECT_DATA_ROW.getMessage(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4)));
                    }
                } else if ((matcher = DATA_FOOTER_PATTERN.matcher(stripped)).matches()) {
                    int linePage = Integer.parseInt(matcher.group(2));
                    message.append("\n").append(Message.INSPECT_DATA_FOOTER.getMessage(Math.max(linePage, 1),
                            Math.min(Integer.parseInt(matcher.group(3)), historyLimitPage)));
                }
            }
            pl.sendMessage(empty ? Message.NO_DATA.getMessage() : message.substring(1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
