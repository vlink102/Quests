package net.vlands.survival.quests.listeners;

import lombok.Getter;
import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.PlayerQuest;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class TravelLogger {
    private final Main plugin;

    public TravelLogger(Main plugin) {
        this.plugin = plugin;
    }

    private final HashMap<Objective.TravelType, DistanceMap> travelMaps = new HashMap<>() {{
        Arrays.stream(Objective.TravelType.values()).forEach(travelType -> this.put(travelType, new DistanceMap()));
    }};

    private final HashMap<UUID, Integer> totalDistanceTravelled = new HashMap<>();

    @Getter
    public static class DistanceMap {
        private final HashMap<UUID, Integer> distanceAtLastLog = new HashMap<>();
        private final HashMap<UUID, Integer> distanceTravelled = new HashMap<>();


    }
    //distance boated etc
    //add map param

    private void managePlayer(UUID uuid, int distance, Objective.TravelType travelType) {
        HashMap<UUID, Integer> distanceAtLastLog = travelMaps.get(travelType).getDistanceAtLastLog();
        HashMap<UUID, Integer> distanceTravelled = travelMaps.get(travelType).getDistanceTravelled();

        if (!distanceAtLastLog.containsKey(uuid) || !distanceTravelled.containsKey(uuid)) {
            distanceAtLastLog.put(uuid, distance);
            distanceTravelled.put(uuid, 0);
            totalDistanceTravelled.put(uuid, 0);
        } else if (distance > distanceAtLastLog.get(uuid)) { // 210 > 205
            distanceTravelled.put(uuid, distanceTravelled.get(uuid) + (distance - distanceAtLastLog.get(uuid))); // travelled(5 + (210 - 205)) travelled(10)
            distanceAtLastLog.put(uuid, distance); // lastlog(210)
            if (!totalDistanceTravelled.containsKey(uuid)) {
                totalDistanceTravelled.put(uuid, distanceTravelled.get(uuid));
            } else {
                totalDistanceTravelled.put(uuid, totalDistanceTravelled.get(uuid) + distanceTravelled.get(uuid));
            }
        }
    }

    public void updatePlayer(Player player) {
        for (Objective.TravelType travelType : Objective.TravelType.values()) {
            if (travelType != Objective.TravelType.ALL) {
                int totalStatValue = player.getStatistic(travelType.getStatistic()) / 100;
                managePlayer(player.getUniqueId(), totalStatValue, travelType);
            }
        }
    }

    public void startLogging() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                net.vlands.survival.quests.internal.Player vPlayer = plugin.getRegisteredPlayers().get(player.getUniqueId());

                if (vPlayer == null) {
                    System.out.println("Error, player is not registered (" + player.getName() + ")");
                    return;
                }

                updatePlayer(player);
                vPlayer.updateAllQuests();

                for (PlayerQuest quest : vPlayer.getActiveQuests()) {
                    Objective objective = quest.getQuest().getObjective();
                    if (!objective.getType().equals(Objective.Type.TRAVEL)) continue;

                    if (objective.getRelative().length == 1 && objective.getRelative()[0] instanceof Objective.TravelType travelType) {
                        if (travelType == Objective.TravelType.ALL) {
                            Util.setQuestVal(plugin, quest, totalDistanceTravelled.get(quest.getBind()));
                        } else {
                            Util.setQuestVal(plugin, quest, travelMaps.get(travelType).getDistanceTravelled().get(quest.getBind()));
                        }
                    }
                }
            }
        }, 0L, 40L);
    }
}
