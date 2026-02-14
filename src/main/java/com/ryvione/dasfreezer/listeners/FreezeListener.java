package com.ryvione.dasfreezer.listeners;

import com.ryvione.dasfreezer.FreezeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class FreezeListener implements Listener {

    private final FreezeManager freezeManager;

    public FreezeListener(FreezeManager freezeManager) {
        this.freezeManager = freezeManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (freezeManager.isFrozen(player.getUniqueId())) {
            Location from = event.getFrom();
            Location to = event.getTo();

            if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
                event.setCancelled(true);
                Location frozenLoc = freezeManager.getFreezeLocation(player.getUniqueId());
                if (frozenLoc != null) {
                    event.setTo(frozenLoc);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (freezeManager.isFrozen(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Component.text("You are frozen and cannot interact.", NamedTextColor.RED));
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (freezeManager.isFrozen(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (freezeManager.isFrozen(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (freezeManager.isFrozen(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (freezeManager.isFrozen(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player && freezeManager.isFrozen(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && freezeManager.isFrozen(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (freezeManager.isFrozen(event.getPlayer().getUniqueId())) {
            freezeManager.showFrozenEffects(event.getPlayer());
            event.getPlayer().sendMessage(Component.text("You are still FROZEN.", NamedTextColor.RED));

            Location loc = freezeManager.getFreezeLocation(event.getPlayer().getUniqueId());
            if (loc != null) {
                event.getPlayer().teleport(loc);
            }
        }
    }
}
