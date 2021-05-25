package com.bgsoftware.wildinspect.coreprotect;

import net.coreprotect.database.Lookup;
import net.coreprotect.database.lookup.BlockLookup;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("JavaReflectionMemberAccess")
public final class CoreProtectHook {

    private static final Pattern COMPONENT_PATTERN = Pattern.compile("<COMPONENT>(.*)\\|(.*)\\|(.*)</COMPONENT>");

    public static String[] performInteractLookup(Statement statement, Player player, Block block, int page) {
        return parseResult(Lookup.interactionLookup(null, statement, block, player, 0, page, 7));
    }

    public static String[] performBlockLookup(Statement statement, Player player, BlockState blockState, int page) {
        return parseResult(BlockLookup.results(null, statement, blockState, player, 0, page, 7));
    }

    public static String[] performChestLookup(Statement statement, Player player, Block block, int page) {
        return parseResult(Lookup.chestTransactions(null, statement, block.getLocation(), player, page, 7, false));
    }

    private static String[] parseResult(String result) {
        Matcher matcher = COMPONENT_PATTERN.matcher(result);

        if (matcher.find())
            result = matcher.replaceAll("$3");

        return result.split("\n");
    }

}
