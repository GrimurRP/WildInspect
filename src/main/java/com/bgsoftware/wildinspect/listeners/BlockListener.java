package com.bgsoftware.wildinspect.listeners;

import com.bgsoftware.wildinspect.WildInspect;
import com.bgsoftware.wildinspect.coreprotect.LookupType;
import com.bgsoftware.wildinspect.utils.InspectPlayers;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public final class BlockListener implements Listener {

    private WildInspect plugin;

    public BlockListener(WildInspect plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent e) {
        if (!InspectPlayers.isInspectEnabled(e.getPlayer()))
            return;
        if (WildInspect.getServerVersion() > 8 && e.getHand() == EquipmentSlot.OFF_HAND)
            return;

        e.setCancelled(true);
        if (e.getClickedBlock() == null) return;
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            plugin.getCoreProtect().performLookup(LookupType.BLOCK_LOOKUP, e.getPlayer(), e.getClickedBlock(), 0);
            InspectPlayers.setClickMode(e.getPlayer(), Action.LEFT_CLICK_BLOCK);
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getState() instanceof Container) {
                plugin.getCoreProtect().performLookup(LookupType.CONTAINER_LOOKUP, e.getPlayer(), e.getClickedBlock(), 0);
                InspectPlayers.setClickMode(e.getPlayer(), Action.RIGHT_CLICK_BLOCK);
            } else if (e.getItem() != null && e.getItem().getType().isBlock()) {
                plugin.getCoreProtect().performLookup(LookupType.BLOCK_LOOKUP, e.getPlayer(), e.getClickedBlock().getRelative(e.getBlockFace()), 0);
                InspectPlayers.setClickMode(e.getPlayer(), Action.LEFT_CLICK_BLOCK);
            } else {
                plugin.getCoreProtect().performLookup(LookupType.CLICK_LOOKUP, e.getPlayer(), e.getClickedBlock(), 0);
                InspectPlayers.setClickMode(e.getPlayer(), Action.RIGHT_CLICK_BLOCK);
            }
        }
    }

}
