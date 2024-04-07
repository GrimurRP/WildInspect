package com.bgsoftware.wildinspect.hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import phonon.nodes.constants.TownPermissions;
import phonon.nodes.utils.Permissions;

public final class ClaimsProvider_Nodes implements ClaimsProvider {

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.NODES;
    }

    @Override
    public boolean hasRole(Player player, Location location, String... roles) {
        try {
            if (Permissions.Companion.hasTownPermissions(TownPermissions.TRUSTED, player)) {
                return true;
            }
        } catch (Exception ignored) {
        }

        return false;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        try {
            if (Permissions.Companion.permissionsCheck(TownPermissions.TRUSTED, location, player, false)) {
               return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

}
