package com.bgsoftware.wildinspect.command;

import com.bgsoftware.wildinspect.WildInspect;
import com.bgsoftware.wildinspect.config.Config;
import com.bgsoftware.wildinspect.config.Message;
import com.bgsoftware.wildinspect.coreprotect.LookupType;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider;
import com.bgsoftware.wildinspect.utils.InspectPlayers;
import com.bgsoftware.wildinspect.utils.StringUtils;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@SuppressWarnings("unused")
public final class InspectCommand implements Listener {

    private final WildInspect plugin;

    public InspectCommand(WildInspect plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        String cmdLabel = "";
        for (String label : Config.COMMANDS.getStringList()) {
            if (e.getMessage().equalsIgnoreCase("/" + label) || e.getMessage().startsWith("/" + label + " ")) {
                cmdLabel = label;
                break;
            }
        }

        if (cmdLabel.equals(""))
            return;

        e.setCancelled(true);

        String label = e.getMessage().split(" ")[0];
        String[] args = e.getMessage().replace(label + " ", "").split(" ");
        Player pl = e.getPlayer();
        String inspectPermission = Config.PERM_INSPECT.getString();
        if (!inspectPermission.isEmpty()
                && !pl.hasPermission(inspectPermission)) {
            pl.sendMessage(Message.PREFIX.getMessage() + Message.NO_PERMISSION.getMessage(inspectPermission));
            return;
        }

        if (args.length > 2) {
            pl.sendMessage(Message.PREFIX.getMessage() + Message.COMMAND_USAGE.getMessage(cmdLabel, "page"));
            return;
        }

        if (args.length == 2) {
            if (!InspectPlayers.isInspectEnabled(pl) || !InspectPlayers.hasBlock(pl)) {
                pl.sendMessage(Message.PREFIX.getMessage() + Message.NO_BLOCK_SELECTED.getMessage(label + " " + args[0]));
                return;
            }

            int page;

            try {
                page = Integer.parseInt(args[1]);
            } catch (IllegalArgumentException ex) {
                pl.sendMessage(Message.PREFIX.getMessage() + Message.SPECIFY_PAGE.getMessage());
                return;
            }

            if (page < 1) {
                pl.sendMessage(Message.PREFIX.getMessage() + Message.SPECIFY_PAGE.getMessage());
                return;
            }

            Block bl = InspectPlayers.getBlock(pl);

            ClaimsProvider.ClaimPlugin claimPlugin = plugin.getHooksHandler().getRegionAt(pl, bl.getLocation());
            if (!plugin.getHooksHandler().hasRole(claimPlugin, pl, bl.getLocation(), Config.REQUIRED_ROLES.getStrings())) {
                pl.sendMessage(Message.PREFIX.getMessage() + Message.REQUIRED_ROLE.getMessage(StringUtils.format(Config.REQUIRED_ROLES.getStrings())));
                return;
            }

            Action clickMode = Action.LEFT_CLICK_BLOCK;
            if (InspectPlayers.hasClickMode(pl))
                clickMode = InspectPlayers.getClickMode(pl);

            if (clickMode == Action.LEFT_CLICK_BLOCK) {
                plugin.getCoreProtect().performLookup(LookupType.BLOCK_LOOKUP, pl, bl, page);
            } else if (clickMode == Action.RIGHT_CLICK_BLOCK) {
                if (bl.getState() instanceof Container) {
                    plugin.getCoreProtect().performLookup(LookupType.CONTAINER_LOOKUP, pl, bl, page);
                } else {
                    plugin.getCoreProtect().performLookup(LookupType.CLICK_LOOKUP, pl, bl, page);
                }
                InspectPlayers.setClickMode(e.getPlayer(), Action.RIGHT_CLICK_BLOCK);
            }
            return;
        }

        if (InspectPlayers.isInspectEnabled(pl)) {
            InspectPlayers.disableInspectMode(pl);
            pl.sendMessage(Message.PREFIX.getMessage() + Message.INSPECTOR_OFF.getMessage());
        } else {
            InspectPlayers.enableInspectMode(pl);
            pl.sendMessage(Message.PREFIX.getMessage() + Message.INSPECTOR_ON.getMessage());
        }


    }

}
