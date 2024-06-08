package net.vlands.survival.quests.listeners;

import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Player;
import net.vlands.survival.quests.internal.PlayerQuest;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

public class CookListener implements Listener {
    private final Main plugin;

    public CookListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTake(FurnaceExtractEvent event) {
        Player player = plugin.getRegisteredPlayers().get(event.getPlayer().getUniqueId());

        if (player == null) {
            System.out.println("Error, player is not registered (" + event.getPlayer().getName() + ")");
            return;
        }

        player.updateAllQuests();

        for (PlayerQuest quest : player.getActiveQuests()) {
            Objective objective = quest.getQuest().getObjective();
            if (!(objective.getType().equals(Objective.Type.COOK))) continue;
            for (Object o : objective.getRelative()) {
                if (!(o instanceof Material)) continue;
                if (o != event.getItemType()) continue;
                Util.incrementQuest(plugin, quest, event.getItemAmount());
            }
        }
    }
}
