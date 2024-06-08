package net.vlands.survival.quests.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;

public class MenuCreator {
    private final Main plugin;
    
    public MenuCreator(Main plugin) {
        this.plugin = plugin;
    }

    public Gui createAchievementMenu(UUID uuid) {
        return preloadMenu(uuid);
    }

    public Gui preloadMenu(UUID uuid) {
        org.bukkit.entity.Player player = Bukkit.getPlayer(uuid);
        if (player == null) return null;

        Gui gui = createQuestGui(1);
        Gui gui2 = createQuestGui(2);
        Gui gui3 = createQuestGui(3);

        updateAllItems(gui, gui2, gui3, uuid);

        generateStaticItems(gui, gui2, gui3);
        return gui;
    }

    private void fillGui(Gui gui) {
        gui.getFiller().fillBetweenPoints(1,1,6,9, ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Util.colorizeComponent("&8")).asGuiItem());
    }

    private Gui createQuestGui(int page) {
        Gui newGui = Gui.gui().title(Util.colorizeComponent("&6Quests &8(Page &a" + page + "&8)")).rows(6).create();
        fillGui(newGui);
        return newGui;
    }

    private void setDefaultClickAction(Gui gui) {
        gui.setDefaultTopClickAction(event -> event.setCancelled(true));
        gui.setOutsideClickAction(event -> {
            for (HumanEntity viewer : new ArrayList<>(event.getViewers())) {
                viewer.closeInventory();
                Util.playWoodenDoorClose(viewer);
            }
        });
    }

    private void addCloseAction(Gui gui) {
        gui.addSlotAction(6,5, event -> {
            for (HumanEntity viewer : new ArrayList<>(event.getViewers())) {
                viewer.closeInventory();
                Util.playWoodenDoorClose(viewer);
            }
        });
        addClosedItem(gui);
    }

    private void addNextAction(Gui gui, Gui next) {
        gui.addSlotAction(6,6, event -> {
            next.open(event.getWhoClicked());
            Entity clicker = event.getWhoClicked();
            Util.playUISound(clicker);
        });
        addNextItem(gui);
    }

    private void addPreviousAction(Gui gui, Gui previous) {
        gui.addSlotAction(6,4, event -> {
            previous.open(event.getWhoClicked());

            Entity clicker = event.getWhoClicked();
            Util.playUISound(clicker);
        });
        addPreviousItem(gui);
    }

    private void generateStaticItems(Gui gui, Gui gui2, Gui gui3) {
        addNextAction(gui, gui2);
        addPreviousAction(gui2, gui);
        addNextAction(gui2, gui3);
        addPreviousAction(gui3, gui2);

        addCloseAction(gui);
        addCloseAction(gui2);
        addCloseAction(gui3);

        setDefaultClickAction(gui);
        setDefaultClickAction(gui2);
        setDefaultClickAction(gui3);
    }

    private void setQuestItem(Gui gui, Gui gui2, Gui gui3, PlayerQuest quest) {
        int id = quest.getQuest().getId();
        if (id > 62) {
            throw new IllegalArgumentException("Id is over max quest id");
        }
        if (id > 42) {
            setItem(gui3, quest);
            return;
        }
        if (id > 20) {
            setItem(gui2, quest);
            return;
        }

        setItem(gui, quest);
    }

    private void setLootItem(Gui gui, Gui gui2, Gui gui3, PlayerLoot loot) {
        int id = loot.getLoot().getId();
        if (id > 62) {
            throw new IllegalArgumentException("Id is over max quest id");
        }
        if (id > 42) {
            setItem(gui3, loot);
            return;
        }
        if (id > 20) {
            setItem(gui2, loot);
            return;
        }

        setItem(gui, loot);
    }

    public void setItem(Gui gui, Slot slot, PlayerQuest quest) {
        quest.updateDailyStatus(plugin, quest.getBind());

        gui.updateItem(slot.row(), slot.column(), ItemBuilder.from(quest.toItemStack(plugin)).asGuiItem(event -> updateItem(event, gui, quest)));
    }

    public void setItem(Gui gui, PlayerQuest quest) {
        Slot slot = plugin.getSlotMap().getLocation(quest.getQuest().getId());
        quest.updateStatus(plugin, quest.getBind());
        gui.updateItem(slot.row(), slot.column(), ItemBuilder.from(quest.toItemStack(plugin)).asGuiItem(event -> updateItem(event, gui, quest)));
    }

    private void setItem(Gui gui, PlayerLoot loot) {
        Slot slot = plugin.getSlotMap().getLocation(loot.getLoot().getId());
        loot.updateStatus(plugin, loot.getBind());
        gui.updateItem(slot.row(), slot.column(), ItemBuilder.from(loot.toItemStack(plugin)).asGuiItem(event -> updateItem(event, gui, loot)));
    }

    private void updateItem(InventoryClickEvent event, Gui gui, PlayerLoot loot) {
        if (event.getWhoClicked() instanceof org.bukkit.entity.Player player) {
            if (loot.getStatus() == PlayerLoot.Status.COMPLETE) {
                loot.claimRewards(plugin, player);
                gui.updateItem(event.getSlot(), loot.toItemStack(plugin));
            } else {
                Util.playFlintAndSteel(player);
            }
        }
    }

    private void updateItem(InventoryClickEvent event, Gui gui, PlayerQuest quest) {
        if (event.getWhoClicked() instanceof org.bukkit.entity.Player player) {
            if (quest.getStatus() == PlayerQuest.Status.COMPLETED) {
                quest.claimRewards(plugin, player);
                gui.updateItem(event.getSlot(), quest.toItemStack(plugin));
            } else {
                Util.playFlintAndSteel(player);
            }
        }
    }

    private void updateAllItems(Gui gui, Gui gui2, Gui gui3, UUID uuid) {
        Player vPlayer = plugin.getRegisteredPlayers().get(uuid);
        vPlayer.getQuestList().forEach(quest -> setQuestItem(gui, gui2, gui3, quest));
        vPlayer.getLootList().forEach(loot -> setLootItem(gui, gui2, gui3, loot));
    }

    private void addNextItem(Gui gui) {
        gui.setItem(6,6, ItemBuilder.from(Material.ARROW).name(Util.colorizeComponent("&bNext Page")).asGuiItem());
    }

    private void addPreviousItem(Gui gui) {
        gui.setItem(6,4, ItemBuilder.from(Material.ARROW).name(Util.colorizeComponent("&bPrevious Page")).asGuiItem());
    }

    private void addClosedItem(Gui gui) {
        gui.setItem(6,5, ItemBuilder.from(Material.BARRIER).name(Util.colorizeComponent("&cClose")).asGuiItem());
    }
}