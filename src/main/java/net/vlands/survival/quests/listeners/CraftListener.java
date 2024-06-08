package net.vlands.survival.quests.listeners;

import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Player;
import net.vlands.survival.quests.internal.PlayerQuest;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class CraftListener implements Listener {
    private final Main plugin;

    public CraftListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(InventoryClickEvent event) {
        if (event.getSlot() != 0) return;

        Player player = plugin.getRegisteredPlayers().get(event.getWhoClicked().getUniqueId());

        if (player == null) {
            System.out.println("Error, player is not registered (" + event.getWhoClicked().getName() + ")");
            return;
        }

        player.updateAllQuests();

        for (PlayerQuest quest : player.getActiveQuests()) {
            Objective objective = quest.getQuest().getObjective();
            if (objective.getType() != Objective.Type.CRAFT) continue;
            for (Object o : objective.getRelative()) {
                if (!(o instanceof Material)) continue;
                if (event.getCurrentItem() == null) continue;
                if (o != event.getCurrentItem().getType()) continue;
                Util.incrementQuest(plugin, quest);
            }
        }
    }
}
