package net.vlands.survival.quests.listeners;

import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;

public class BrewListener implements Listener {
    private final Main plugin;

    public BrewListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        for (Entity entity : event.getBlock().getWorld().getNearbyEntities(event.getBlock().getLocation(), 10, 10, 10)) {
            if (entity instanceof Player) {
                entity.sendMessage(Util.colorize("&cPotion Brewing has not yet been implemented to quests due to minecraft's limitations."));
            }
        }
    }
}
