package com.bgsoftware.wildinspect.hooks;

import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.plot.Plot;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class ClaimsProvider_PlotSquared implements ClaimsProvider {

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.PLOTSQUARED;
    }

    @Override
    public boolean hasRole(Player player, Location bukkitLocation, String... role) {
        Plot plot = BukkitUtil.adaptComplete(bukkitLocation).getOwnedPlot();
        return plot != null && Arrays.asList(role).contains(plot.isOwner(player.getUniqueId()) ? "OWNER" : "TRUSTED");
    }

    @Override
    public boolean hasRegionAccess(Player player, Location bukkitLocation) {
        Plot plot = BukkitUtil.adaptComplete(bukkitLocation).getOwnedPlot();
        return plot != null && (plot.isAdded(player.getUniqueId()) || plot.isOwner(player.getUniqueId()));
    }

}
