package net.vlands.survival.quests.listeners;

import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class BreedListener implements Listener {
    private final Main plugin;

    public BreedListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreed(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BREEDING) {
            for (Entity entity : event.getEntity().getNearbyEntities(10,10,10)) {
                if (entity instanceof Player) {
                    entity.sendMessage(Util.colorize("&cBreeding has not yet been implemented to quests due to minecraft's limitations."));
                }
            }
        }
    }
}
