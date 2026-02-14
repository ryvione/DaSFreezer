package com.ryvione.dasfreezer.commands;

import com.ryvione.dasfreezer.DaSFreezer;
import com.ryvione.dasfreezer.FreezeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FreezeCommands implements CommandExecutor {

    private final FreezeManager freezeManager;
    private final DaSFreezer plugin;

    public FreezeCommands(FreezeManager freezeManager, DaSFreezer plugin) {
        this.freezeManager = freezeManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {

        if (command.getName().equalsIgnoreCase("freeze")) {
            if (!sender.hasPermission("dasfreezer.freeze")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage(Component.text("Usage: /freeze <player>", NamedTextColor.RED));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text("Player not found or not online.", NamedTextColor.RED));
                return true;
            }

            freezeManager.freezePlayer(target, sender);
            return true;
        }

        if (command.getName().equalsIgnoreCase("unfreeze")) {
            if (!sender.hasPermission("dasfreezer.unfreeze")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage(Component.text("Usage: /unfreeze <player>", NamedTextColor.RED));
                return true;
            }

            Player onlineTarget = Bukkit.getPlayer(args[0]);
            OfflinePlayer target;

            if (onlineTarget != null) {
                target = onlineTarget;
            } else {
                target = Bukkit.getOfflinePlayer(args[0]);
            }

            freezeManager.unfreezePlayer(target, sender);
            return true;
        }

        return false;
    }
}