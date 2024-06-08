package net.vlands.survival.quests.listeners;

import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Player;
import net.vlands.survival.quests.internal.PlayerQuest;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

public class TameListener implements Listener {
    private final Main plugin;

    public TameListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTame(EntityTameEvent event) {
        if (!(event.getOwner() instanceof org.bukkit.entity.Player)) return;
        Player player = plugin.getRegisteredPlayers().get(event.getOwner().getUniqueId());

        if (player == null) {
            System.out.println("Error, player is not registered (" + event.getOwner().getName() + ")");
            return;
        }

        player.updateAllQuests();

        for (PlayerQuest quest : player.getActiveQuests()) {
            Objective objective = quest.getQuest().getObjective();
            if (!objective.getType().equals(Objective.Type.TAME)) continue;
            if (objective.getRelative().length == 0) {
                Util.incrementQuest(plugin, quest);
            } else if (objective.getRelative().length == 1) {
                if (objective.getRelative()[0] instanceof EntityType entityType) {
                    if (event.getEntity().getType() != entityType) continue;
                    Util.incrementQuest(plugin, quest);
                }
            }
        }
    }
}
