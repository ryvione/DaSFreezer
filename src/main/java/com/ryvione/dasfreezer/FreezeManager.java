package com.ryvione.dasfreezer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class FreezeManager {

    private final JavaPlugin plugin;
    private final Map<UUID, Location> frozenPlayers = new HashMap<>();

    private final File frozenFile;
    private FileConfiguration frozenConfig;

    public FreezeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.frozenFile = new File(plugin.getDataFolder(), "frozen_players.yml");
        loadFrozenPlayers();
        startActionBarTask();
    }

    private void loadFrozenPlayers() {
        if (!frozenFile.exists()) {
            try {
                frozenFile.getParentFile().mkdirs();
                frozenFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create frozen_players.yml", e);
            }
        }
        frozenConfig = YamlConfiguration.loadConfiguration(frozenFile);

        if (frozenConfig.contains("frozen")) {
            for (String uuidStr : frozenConfig.getConfigurationSection("frozen").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    Location loc = frozenConfig.getLocation("frozen." + uuidStr);
                    if (loc != null) {
                        frozenPlayers.put(uuid, loc);
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in frozen_players.yml: " + uuidStr);
                }
            }
        }
        plugin.getLogger().info("Loaded " + frozenPlayers.size() + " frozen players.");
    }

    private void saveFrozenPlayers() {
        frozenConfig.set("frozen", null);
        for (Map.Entry<UUID, Location> entry : frozenPlayers.entrySet()) {
            frozenConfig.set("frozen." + entry.getKey().toString(), entry.getValue());
        }
        try {
            frozenConfig.save(frozenFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save frozen_players.yml", e);
        }
    }

    public boolean isFrozen(UUID uuid) {
        return frozenPlayers.containsKey(uuid);
    }

    public Location getFreezeLocation(UUID uuid) {
        return frozenPlayers.get(uuid);
    }

    public boolean freezePlayer(Player target, CommandSender staff) {
        if (isFrozen(target.getUniqueId())) {
            staff.sendMessage(Component.text("Player " + target.getName() + " is already frozen!", NamedTextColor.RED));
            return false;
        }

        Location loc = target.getLocation();
        frozenPlayers.put(target.getUniqueId(), loc);
        saveFrozenPlayers();

        showFrozenEffects(target);

        target.sendMessage(Component.text("You have been FROZEN by staff.", NamedTextColor.RED));
        staff.sendMessage(Component.text("You have frozen " + target.getName() + ".", NamedTextColor.GREEN));

        plugin.getLogger().info("Player " + target.getName() + " was frozen by " + staff.getName());
        return true;
    }

    public boolean unfreezePlayer(OfflinePlayer target, CommandSender staff) {
        if (!isFrozen(target.getUniqueId())) {
            staff.sendMessage(Component.text(
                    "Player " + (target.getName() != null ? target.getName() : "Unknown") + " is not frozen!",
                    NamedTextColor.RED));
            return false;
        }

        frozenPlayers.remove(target.getUniqueId());
        saveFrozenPlayers();

        if (target.isOnline()) {
            Player onlineTarget = target.getPlayer();
            if (onlineTarget != null) {
                onlineTarget.clearTitle();
                onlineTarget.sendActionBar(Component.empty());
                onlineTarget.sendMessage(Component.text("You have been unfrozen.", NamedTextColor.GREEN));
            }
        }

        staff.sendMessage(Component.text(
                "You have unfrozen " + (target.getName() != null ? target.getName() : target.getUniqueId()) + ".",
                NamedTextColor.GREEN));
        plugin.getLogger().info("Player " + (target.getName() != null ? target.getName() : target.getUniqueId())
                + " was unfrozen by " + staff.getName());
        return true;
    }

    public void showFrozenEffects(Player player) {
        Title title = Title.title(
                Component.text("FROZEN", NamedTextColor.RED),
                Component.text("You cannot move or interact.", NamedTextColor.YELLOW),
                Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(200000), Duration.ofMillis(1000)));
        player.showTitle(title);
    }

    private void startActionBarTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : frozenPlayers.keySet()) {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null && p.isOnline()) {
                        p.sendActionBar(Component.text("You are currently FROZEN", NamedTextColor.RED));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 40L);
    }
}
