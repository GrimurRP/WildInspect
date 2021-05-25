package com.bgsoftware.wildinspect;

import com.bgsoftware.wildinspect.command.InspectCommand;
import com.bgsoftware.wildinspect.command.ReloadCommand;
import com.bgsoftware.wildinspect.coreprotect.CoreProtect;
import com.bgsoftware.wildinspect.handlers.HooksHandler;
import com.bgsoftware.wildinspect.handlers.SettingsHandler;
import com.bgsoftware.wildinspect.listeners.BlockListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class WildInspect extends JavaPlugin {

    private static WildInspect plugin;

    private SettingsHandler settingsHandler;
    private HooksHandler hooksHandler;

    private CoreProtect coreProtect;

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getScheduler().runTask(this, () -> {
            log("******** ENABLE START ********");

            getServer().getPluginManager().registerEvents(new InspectCommand(this), this);
            getServer().getPluginManager().registerEvents(new BlockListener(this), this);

            registerCommand("wildinspect", new ReloadCommand());

            settingsHandler = new SettingsHandler(this);
            hooksHandler = new HooksHandler(this);
            coreProtect = new CoreProtect(this);

            Locale.reload();

            log("******** ENABLE DONE ********");
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

    public SettingsHandler getSettings() {
        return settingsHandler;
    }

    public void setSettings(SettingsHandler settingsHandler) {
        this.settingsHandler = settingsHandler;
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

    public static WildInspect getPlugin() {
        return plugin;
    }

}
