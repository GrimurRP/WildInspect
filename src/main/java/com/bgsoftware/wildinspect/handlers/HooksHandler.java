package com.bgsoftware.wildinspect.handlers;

import com.bgsoftware.wildinspect.WildInspect;
import com.bgsoftware.wildinspect.hooks.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;

public final class HooksHandler {

    private final EnumMap<ClaimsProvider.ClaimPlugin, ClaimsProvider> claimsProviders = new EnumMap<>(ClaimsProvider.ClaimPlugin.class);

    public HooksHandler(WildInspect plugin) {
        Bukkit.getScheduler().runTask(plugin, this::loadHookups);
    }

    public boolean hasRole(ClaimsProvider.ClaimPlugin claimPlugin, Player player, Location location, String... roles) {
        ClaimsProvider claimsProvider = claimsProviders.get(claimPlugin);
        return claimsProvider == null || claimsProvider.hasRole(player, location, roles);
    }

    public ClaimsProvider.ClaimPlugin getRegionAt(Player player, Location location) {
        for (Map.Entry<ClaimsProvider.ClaimPlugin, ClaimsProvider> entry : claimsProviders.entrySet()) {
            if (entry.getValue().hasRegionAccess(player, location))
                return entry.getKey();
        }

        return claimsProviders.isEmpty() ? ClaimsProvider.ClaimPlugin.DEFAULT : ClaimsProvider.ClaimPlugin.NONE;
    }

    private void loadHookups() {
        WildInspect.log("Loading providers started...");
        long startTime = System.currentTimeMillis();
        //Checks if AcidIsland is installed
        if (Bukkit.getPluginManager().isPluginEnabled("AcidIsland")) {
            claimsProviders.put(ClaimsProvider.ClaimPlugin.ACID_ISLAND, new ClaimsProvider_AcidIsland());
            WildInspect.log(" - Using AcidIsland as ClaimsProvider.");
        }
        //Checks if ASkyBlock is installed
        if (Bukkit.getPluginManager().isPluginEnabled("ASkyBlock")) {
            claimsProviders.put(ClaimsProvider.ClaimPlugin.ASKYBLOCK, new ClaimsProvider_ASkyBlock());
            WildInspect.log(" - Using ASkyBlock as ClaimsProvider.");
        }
        //Checks if BentoBox is installed
        if (Bukkit.getPluginManager().isPluginEnabled("BentoBox")) {
            claimsProviders.put(ClaimsProvider.ClaimPlugin.BENTOBOX, new ClaimsProvider_BentoBox());
            WildInspect.log(" - Using BentoBox as ClaimsProvider.");
        }
        //Checks if Factions is installed
        if (Bukkit.getPluginManager().isPluginEnabled("Factions")) {
            if (!Bukkit.getPluginManager().getPlugin("Factions").getDescription().getAuthors().contains("drtshock")) {
                claimsProviders.put(ClaimsProvider.ClaimPlugin.MASSIVE_FACTIONS, new ClaimsProvider_MassiveFactions());
                WildInspect.log(" - Using MassiveFactions as ClaimsProvider.");
            } else {
                claimsProviders.put(ClaimsProvider.ClaimPlugin.FACTIONSUUID, new ClaimsProvider_FactionsUUID());
                WildInspect.log(" - Using FactionsUUID as ClaimsProvider.");
            }
        }
        //Checks if FactionsX is installed
        if (Bukkit.getPluginManager().isPluginEnabled("FactionsX")) {
            claimsProviders.put(ClaimsProvider.ClaimPlugin.FACTIONSX, new ClaimsProvider_FactionsX());
            WildInspect.log(" - Using FactionsX as ClaimsProvider.");
        }
        //Checks if Towny is installed
        if (Bukkit.getPluginManager().isPluginEnabled("Towny")) {
            claimsProviders.put(ClaimsProvider.ClaimPlugin.TOWNY, new ClaimsProvider_Towny());
            WildInspect.log(" - Using Towny as ClaimsProvider.");
        }
        //Checks if PlotSquared is installed
        if (Bukkit.getPluginManager().isPluginEnabled("PlotSquared")) {
            claimsProviders.put(ClaimsProvider.ClaimPlugin.PLOTSQUARED, new ClaimsProvider_PlotSquared());
            WildInspect.log(" - Using PlotSquared as ClaimsProvider.");
        }
        //Checks if GriefPrevention is installed
        if (Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
            claimsProviders.put(ClaimsProvider.ClaimPlugin.GRIEF_PREVENTION, new ClaimsProvider_GriefPrevention());
            WildInspect.log(" - Using GriefPrevention as ClaimsProvider.");
        }
        //Checks if SuperiorSkyblock is installed
        if (Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")) {
            claimsProviders.put(ClaimsProvider.ClaimPlugin.SUPERIOR_SKYBLOCK, new ClaimsProvider_SuperiorSkyblock());
            WildInspect.log(" - Using SuperiorSkyblock as ClaimsProvider.");
        }
        //Checks if Villages is installed
        if (Bukkit.getPluginManager().isPluginEnabled("Villages")) {
            claimsProviders.put(ClaimsProvider.ClaimPlugin.VILLAGES, new ClaimsProvider_Villages());
            WildInspect.log(" - Using Villages as ClaimsProvider.");
        }
        WildInspect.log("Loading providers done (Took " + (System.currentTimeMillis() - startTime) + "ms)");
    }

}
