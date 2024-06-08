package net.vlands.survival.quests.listeners;

import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Player;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeedListener implements Listener {
    private final Main plugin;

    public FeedListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFeed(PlayerInteractEntityEvent event) {
        Player player = plugin.getRegisteredPlayers().get(event.getPlayer().getUniqueId());

        if (player == null) {
            System.out.println("Error, player is not registered (" + event.getPlayer().getName() + ")");
            return;
        }

    }

}
