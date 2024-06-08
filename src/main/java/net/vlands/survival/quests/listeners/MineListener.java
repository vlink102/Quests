package net.vlands.survival.quests.listeners;

import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Player;
import net.vlands.survival.quests.internal.PlayerQuest;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class MineListener implements Listener {
    private final Main plugin;

    public MineListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        Player player = plugin.getRegisteredPlayers().get(event.getPlayer().getUniqueId());

        if (player == null) {
            System.out.println("Error, player is not registered (" + event.getPlayer().getName() + ")");
            return;
        }

        player.updateAllQuests();

        for (PlayerQuest quest : player.getActiveQuests()) {
            Objective objective = quest.getQuest().getObjective();
            if (!objective.getType().equals(Objective.Type.MINE)) continue;
            if (objective.getRelative().length == 0) {
                Util.incrementQuest(plugin, quest);
            } else if (objective.getRelative().length == 1) {
                if (objective.getRelative()[0] instanceof ItemStack itemStack) {
                    if (itemStack.getType() != event.getPlayer().getInventory().getItemInMainHand().getType()) continue;
                    Util.incrementQuest(plugin, quest);
                } else if (objective.getRelative()[0] instanceof Material material) {
                    if (material != event.getBlock().getType()) continue;
                    Util.incrementQuest(plugin, quest);
                }
            } else if (objective.getRelative().length == 2) {
                if (objective.getRelative()[0] instanceof ItemStack itemStack && objective.getRelative()[1] instanceof Material material) {
                    if (material != event.getBlock().getType()) continue;
                    if (itemStack.getType() != event.getPlayer().getInventory().getItemInMainHand().getType()) continue;
                    Util.incrementQuest(plugin, quest);
                }
            }
        }
    }

}
