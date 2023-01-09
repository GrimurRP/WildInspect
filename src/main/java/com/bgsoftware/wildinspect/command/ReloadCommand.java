package com.bgsoftware.wildinspect.command;

import com.bgsoftware.wildinspect.config.Config;
import com.bgsoftware.wildinspect.config.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import pro.dracarys.configlib.ConfigLib;

import java.util.ArrayList;
import java.util.List;

public final class ReloadCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(Config.PERM_RELOAD.getString())) {
            sender.sendMessage(Message.PREFIX.getMessage() + Message.NO_PERMISSION.getMessage(Config.PERM_RELOAD.getString()));
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            ConfigLib.initAll();
            sender.sendMessage(Message.PREFIX.getMessage() + Message.RELOAD_SUCCESS.getMessage());
            return false;
        }

        sender.sendMessage(Message.PREFIX.getMessage() + Message.COMMAND_USAGE.getMessage("wildinspect", "reload"));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return new ArrayList<>();
    }
}
