package com.bgsoftware.wildinspect.hooks;

import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.Plot;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

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
        com.plotsquared.core.location.Location loc = new com.plotsquared.core.location.Location(
                Objects.requireNonNull(location.getWorld()).getName(),
                location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Plot plot = PlotSquared.get().getPlotAreaManager().getPlotArea(loc).getOwnedPlot(loc);
        if (plot == null) return false;
        return plot.isAdded(player.getUniqueId()) || plot.isOwner(player.getUniqueId());
    }

}
