package net.vlands.survival.quests.listeners;

import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Player;
import net.vlands.survival.quests.internal.PlayerQuest;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class KillListener implements Listener {
    private final Main plugin;

    public KillListener(Main plugin) {
        this.plugin = plugin;
    }

    private final HashMap<UUID, List<UUID>> killMap = new HashMap<>();

    public HashMap<UUID, List<UUID>> getKillMap() {
        return killMap;
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        org.bukkit.entity.Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        Player vPlayer = plugin.getRegisteredPlayers().get(killer.getUniqueId());

        if (vPlayer == null) {
            System.out.println("Error, player is not registered (" + killer.getName() + ")");
            return;
        }

        Entity victim = event.getEntity();

        vPlayer.updateAllQuests();

        for (PlayerQuest quest : vPlayer.getActiveQuests()) {
            Objective objective = quest.getQuest().getObjective();
            if (!objective.getType().equals(Objective.Type.KILL)) continue;

            if (objective.getRelative().length == 0) {
                Util.incrementQuest(plugin, quest);
            } else if (objective.getRelative().length == 1) {
                if (objective.getRelative()[0] instanceof EntityType type) {
                    if (type != event.getEntityType()) continue;
                    Util.incrementQuest(plugin, quest);
                } else if (objective.getRelative()[0] instanceof Material material) {
                    if (material != killer.getInventory().getItemInMainHand().getType()) continue;
                    Util.incrementQuest(plugin, quest);
                }
            } else if (objective.getRelative().length == 2) {
                if (objective.getRelative()[0] instanceof EntityType type && objective.getRelative()[1] instanceof Material material) {
                    if (type != event.getEntityType()) continue;
                    if (material != killer.getInventory().getItemInMainHand().getType()) continue;
                    Util.incrementQuest(plugin, quest);
                    quest.updateStatus(plugin, quest.getBind());
                } else if (objective.getRelative()[0] == EntityType.PLAYER && objective.getRelative()[1] instanceof Objective.KillPlayerType killPlayerType) {
                    if (event.getEntityType() != EntityType.PLAYER) continue;
                    switch (killPlayerType) {
                        case SINGLE -> Util.incrementQuest(plugin, quest);
                        case DIFFERENT -> updateKillMap(quest, killer.getUniqueId(), victim.getUniqueId());
                    }
                }
            } else if (objective.getRelative().length == 3) {
                if (objective.getRelative()[0] == EntityType.PLAYER && objective.getRelative()[1] instanceof Material material && objective.getRelative()[2] instanceof Objective.KillPlayerType killPlayerType) {
                    if (event.getEntityType() != EntityType.PLAYER) continue;
                    if (material != killer.getInventory().getItemInMainHand().getType()) continue;
                    switch (killPlayerType) {
                        case DIFFERENT -> updateKillMap(quest, killer.getUniqueId(), victim.getUniqueId());
                        case SINGLE -> Util.incrementQuest(plugin, quest);
                    }
                }
            }
        }
    }

    private void updateKillMap(PlayerQuest quest, UUID killer, UUID victim) {
        if (killMap.containsKey(killer)) {
            if (!killMap.get(killer).contains(victim)) {
                killMap.get(killer).add(victim);
                Util.incrementQuest(plugin, quest);
            } else {

                // already killed player
            }
        } else {
            killMap.put(killer, new ArrayList<>() {{
                this.add(victim);
            }});
            Util.incrementQuest(plugin, quest);
        }
    }
}
