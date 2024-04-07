package com.bgsoftware.wildinspect.coreprotect;

import com.bgsoftware.wildinspect.config.Config;
import net.coreprotect.CoreProtectAPI;
import net.coreprotect.database.lookup.BlockLookup;
import net.coreprotect.database.lookup.ChestTransactionLookup;
import net.coreprotect.database.lookup.InteractionLookup;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getServer;

public final class CoreProtectHook {

    private static final Pattern COMPONENT_PATTERN = Pattern.compile("<COMPONENT>(.*?)\\|(.*?)\\|(.*?)</COMPONENT>");

    public static String[] performInteractLookup(Statement statement, Player player, Block block, int page) {
        return parseResult(InteractionLookup.performLookup(null, statement, block, player, 0, page, Config.ENTRIES_PER_PAGE.getInt()));
    }

    public static String[] performBlockLookup(Statement statement, Player player, BlockState blockState, int page) {
        return parseResult(BlockLookup.performLookup(null, statement, blockState, player, 0, page, Config.ENTRIES_PER_PAGE.getInt()));
    }

    public static String[] performChestLookup(Statement statement, Player player, Block block, int page) {
        return parseResult(ChestTransactionLookup.performLookup(null, statement, block.getLocation(), player, page, Config.ENTRIES_PER_PAGE.getInt(), false));
    }

    //private static String[] parseResult(String result) {
    //    Matcher matcher = COMPONENT_PATTERN.matcher(result);
    //    if (matcher.find()) result = matcher.replaceAll("$3");
    //    return result.split("\n");
    //}

    private static String[] parseResult(String result) {
        // Directly split the result string by new line and return
        return result.split("\n");
    }

    /*
    private static String[] parseResult(List<String> results) {
        // Initialize an ArrayList to store the parsed results
        List<String> parsedResults = new ArrayList<>();

        // Iterate through each string in the results list
        for (String result : results) {
            // Apply the regex to each string
            Matcher matcher = COMPONENT_PATTERN.matcher(result);
            if (matcher.find()) {
                // Replace the matched pattern with the third captured group
                result = matcher.replaceAll("$3");
            }
            // Add the parsed result to the list
            parsedResults.add(result);
        }

        // Convert the ArrayList to an array and return
        return parsedResults.toArray(new String[0]);
    }
     */


    private static String[] parseResult(List<String> results) {
        // Convert the List to an array and return
        return results.toArray(new String[0]);
    }

}
