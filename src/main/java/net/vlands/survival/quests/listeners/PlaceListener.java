package net.vlands.survival.quests.listeners;

import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Player;
import net.vlands.survival.quests.internal.PlayerQuest;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceListener implements Listener {
    private final Main plugin;

    public PlaceListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = plugin.getRegisteredPlayers().get(event.getPlayer().getUniqueId());

        if (player == null) {
            System.out.println("Error, player is not registered (" + event.getPlayer().getName() + ")");
            return;
        }

        player.updateAllQuests();

        for (PlayerQuest quest : player.getActiveQuests()) {
            Objective objective = quest.getQuest().getObjective();
            if (!objective.getType().equals(Objective.Type.PLACE)) continue;
            if (objective.getRelative().length == 0) {
                Util.incrementQuest(plugin, quest);
            } else if (objective.getRelative().length == 1) {
                if (objective.getRelative()[0] instanceof Material material) {
                    if (material != event.getBlockPlaced().getType()) continue;
                    Util.incrementQuest(plugin, quest);
                }
            } else if (objective.getRelative().length == 2) {
                if (objective.getRelative()[0] instanceof Material material && objective.getRelative()[1] instanceof Material material1) {
                    if (material != event.getBlockPlaced().getType()) continue;
                    if (material1 != event.getBlockAgainst().getType()) continue;
                    Util.incrementQuest(plugin, quest);
                }
            }
        }
    }
}
