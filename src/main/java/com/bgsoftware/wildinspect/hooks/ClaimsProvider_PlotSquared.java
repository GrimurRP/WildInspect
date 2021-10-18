package com.bgsoftware.wildinspect.hooks;

import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.Plot;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ClaimsProvider_PlotSquared implements ClaimsProvider {

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.PLOTSQUARED;
    }

    @Override
    public boolean hasRole(Player player, Location location, String... roles) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        com.plotsquared.core.location.Location loc = com.plotsquared.core.location.Location.at(
                location.getWorld().getName(),
                location.getBlockX(), location.getBlockY(), location.getBlockZ(), 0F, 0F);
        Plot plot = PlotSquared.get().getPlotAreaManager().getPlotArea(loc).getOwnedPlot(loc);
        if (plot == null) return false;
        return plot.isAdded(player.getUniqueId()) || plot.isOwner(player.getUniqueId());
    }

}
