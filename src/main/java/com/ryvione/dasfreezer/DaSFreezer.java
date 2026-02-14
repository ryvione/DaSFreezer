package com.ryvione.dasfreezer;

import com.ryvione.dasfreezer.commands.FreezeCommands;
import com.ryvione.dasfreezer.listeners.FreezeListener;
import org.bukkit.plugin.java.JavaPlugin;

public class DaSFreezer extends JavaPlugin {

    private FreezeManager freezeManager;

    @Override
    public void onEnable() {
        this.freezeManager = new FreezeManager(this);

        getCommand("freeze").setExecutor(new FreezeCommands(freezeManager, this));
        getCommand("unfreeze").setExecutor(new FreezeCommands(freezeManager, this));
        getServer().getPluginManager().registerEvents(new FreezeListener(freezeManager), this);
        getLogger().info("DaSFreezer has been enabled");
    }

    @Override
    public void onDisable() {
        if (freezeManager != null) {
        }
        getLogger().info("DaSFreezer has been disabled");
    }
}
