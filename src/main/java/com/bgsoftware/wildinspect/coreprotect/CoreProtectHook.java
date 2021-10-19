package com.bgsoftware.wildinspect.coreprotect;

import com.bgsoftware.wildinspect.config.Config;
import net.coreprotect.database.lookup.BlockLookup;
import net.coreprotect.database.lookup.ChestTransactionLookup;
import net.coreprotect.database.lookup.InteractionLookup;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static String[] parseResult(String result) {
        Matcher matcher = COMPONENT_PATTERN.matcher(result);
        if (matcher.find()) result = matcher.replaceAll("$3");
        return result.split("\n");
    }

}
