package net.vlands.survival.quests.listeners;

import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Player;
import net.vlands.survival.quests.internal.PlayerQuest;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.Objects;

public class ConsumeListener implements Listener {
    private final Main plugin;

    public ConsumeListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        Player player = plugin.getRegisteredPlayers().get(event.getPlayer().getUniqueId());

        if (player == null) {
            System.out.println("Error, player is not registered (" + event.getPlayer().getName() + ")");
            return;
        }

        player.updateAllQuests();

        for (PlayerQuest quest : player.getActiveQuests()) {
            Objective objective = quest.getQuest().getObjective();
            if (objective.getType() != Objective.Type.EAT && objective.getType() != Objective.Type.DRINK) continue;
            for (Object o : objective.getRelative()) {
                if (o instanceof Material) {
                    if (o != event.getItem().getType()) continue;
                }
                if (o instanceof PotionType) {
                    if (event.getItem().getType() != Material.POTION) continue;
                    PotionMeta meta = (PotionMeta) event.getItem().getItemMeta();
                    if (o != Objects.requireNonNull(meta).getBasePotionData().getType()) continue;
                }
                Util.incrementQuest(plugin, quest);
            }
        }
    }
}
