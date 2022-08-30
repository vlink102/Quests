package net.vlands.survival.quests.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.PlayerQuest;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Objects;
import java.util.UUID;

public class MenuCreator {
    private final Main plugin;

    public MenuCreator(Main plugin) {
        this.plugin = plugin;
    }

    public PaginatedGui createAchievementMenu(UUID uuid) {
        PaginatedGui gui = Gui.paginated()
                .title(Util.colorizeComponent("&6Quests"))
                .rows(6)
                .pageSize(45)
                .create();
        gui.getFiller().fillBetweenPoints(1,1,6,9,ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.setItem(6,4,ItemBuilder.from(Material.ARROW).name(Util.colorizeComponent("&a&m-->&r &aPrevious Page")).asGuiItem(event -> gui.previous()));
        gui.setItem(6,6,ItemBuilder.from(Material.ARROW).name(Util.colorizeComponent("&aNext Page &m-->")).asGuiItem(event -> gui.next()));
        gui.setItem(6,5, ItemBuilder.from(Material.BARRIER).name(Util.colorizeComponent("&cClose")).asGuiItem(event -> gui.close(Objects.requireNonNull(Bukkit.getPlayer(uuid)))));

        /*

        This code is gonna generate the gui stuff ignore it

        List<PlayerQuest> pq = plugin.getPlayerManager().getSavedPlayers().get(uuid).getQuestList();

        PlayerQuest pq1 = Objects.requireNonNull(pq).get(0);

        gui.setItem(2,2, new GuiItem(pq1.toItemStack()));
         */

        return gui;
    }
}
