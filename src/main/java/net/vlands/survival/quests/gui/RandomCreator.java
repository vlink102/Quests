package net.vlands.survival.quests.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Player;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.*;

public class RandomCreator {
    private final Main plugin;

    public RandomCreator(Main plugin) {
        this.plugin = plugin;
    }

    private final List<UUID> randomized = new ArrayList<>();

    public Gui preloadRandomQuestGui(UUID uuid) {
        org.bukkit.entity.Player player = Bukkit.getPlayer(uuid);
        if (player == null) return null;

        Player vPlayer = plugin.getRegisteredPlayers().get(uuid);

        if (vPlayer == null) {
            System.out.println("Error, player is not registered (" + player.getName() + ")");
            return null;
        }

        Gui gui = Gui.gui().title(Util.colorizeComponent("&3Daily Quest")).rows(3).create();
        gui.getFiller().fillBetweenPoints(1, 1, 3,9, ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Util.colorizeComponent("&8")).asGuiItem());

        DailyCreator.setDefaultClickAction(gui);
        DailyCreator.addClosedItem(gui);

        if (!randomized.contains(uuid)) {
            randomized.add(uuid);
            // setItem todo
        } else {
            // todo
        }
        return gui;
    }

    public class RandomQuest {
        // todo
        public enum Status {
            CLAIMED,
            COMPLETED,
            VIEWABLE
        }

        private final Quest quest;
        private Status status;
        private int value;
        private final UUID bind;

        public RandomQuest(Quest quest, UUID bind) {
            this.quest = quest;
            this.bind = bind;
            status = Status.VIEWABLE;
        }
    }

    public static final java.util.Random random = new java.util.Random(System.nanoTime());

    public Objective getReasonable() {
        // todo
        Objective.Type randomType = Objective.Type.values()[random.nextInt(Objective.Type.values().length)];
        int amount = 0;
        List<Object> relative = new ArrayList<>();

        switch (randomType) {
            case TRAVEL -> {
                Objective.TravelType randomTravelType = Objective.TravelType.values()[random.nextInt(Objective.TravelType.values().length)];
                amount = switch (randomTravelType) {
                    case ALL -> random(4000, 7000);
                    case PIG, STRIDER, CLIMB, SWIM, WALK -> random(500, 1000);
                    case MINECART -> random(250, 750);
                    case FALL -> random(200, 600);
                    case BOAT, HORSE -> random(1000, 2000);
                    case AVIATE -> random(2000, 3000);
                    case CROUCH -> random(200, 500);
                    case WALK_ON_WATER, WALK_UNDER_WATER -> random(150, 300);
                    case SPRINT -> random(750, 1500);
                };
            }
        }
        return new Objective(
                randomType,
                amount
        );
    }

    static public int random(int start, int end) {
        return start + random.nextInt(end - start + 1);
    }

    public class Random extends Quest {
        // todo
        public Random() {
            super(
                    -1,
                    "",
                    "&9Daily Quest &8- &dRandomized",
                    0,
                    getReasonable()
            );
        }
    }
}
