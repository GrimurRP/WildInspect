package com.bgsoftware.wildinspect;

import com.bgsoftware.wildinspect.command.InspectCommand;
import com.bgsoftware.wildinspect.command.ReloadCommand;
import com.bgsoftware.wildinspect.config.file.ConfigFile;
import com.bgsoftware.wildinspect.config.file.MessageFile;
import com.bgsoftware.wildinspect.coreprotect.CoreProtect;
import com.bgsoftware.wildinspect.handlers.HooksHandler;
import com.bgsoftware.wildinspect.listeners.BlockListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pro.dracarys.configlib.ConfigLib;

public final class WildInspect extends JavaPlugin {

    private static WildInspect plugin;

    private HooksHandler hooksHandler;

    private CoreProtect coreProtect;

    public static int ver;

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getScheduler().runTask(this, () -> {
            checkServerVersion();

            ConfigLib.setPlugin(this);
            ConfigLib.addFile(new ConfigFile());
            ConfigLib.addFile(new MessageFile());
            ConfigLib.initAll();

            registerListeners(new InspectCommand(this), new BlockListener(this));
            registerCommand("wildinspect", new ReloadCommand());

            ConfigLib.printPluginInfo();

            hooksHandler = new HooksHandler(this);
            coreProtect = new CoreProtect(this);
        });
    }

    private void registerCommand(String command, CommandExecutor executor) {
        PluginCommand cmd = this.getCommand(command);
        if (cmd != null) {
            cmd.setExecutor(executor);
            if (executor instanceof TabCompleter)
                cmd.setTabCompleter((TabCompleter) executor);
        }
    }

    public void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            try {
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (Exception ex) {
                log("Error while registering " + listener.getClass().getName());
                ex.printStackTrace();
            }
        }
    }

    private void checkServerVersion() {
        ver = Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName()
                .replace(".", ",").split(",")[3]
                .replace("1_", "").substring(1)
                .replaceAll("_R\\d", ""));
    }

    public static int getServerVersion() {
        return ver;
    }

    public HooksHandler getHooksHandler() {
        return hooksHandler;
    }

    public CoreProtect getCoreProtect() {
        return coreProtect;
    }

    public static void log(String message) {
        plugin.getLogger().info(message);
    }

    public static WildInspect getInstance() {
        return plugin;
    }

}
