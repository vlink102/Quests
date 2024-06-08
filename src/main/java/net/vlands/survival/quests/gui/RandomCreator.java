package net.vlands.survival.quests.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import lombok.Getter;
import lombok.Setter;
import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RandomCreator {
    private final Main plugin;

    public RandomCreator(Main plugin) {
        this.plugin = plugin;
        claimed = new ArrayList<>();
        completed = false;
    }

    @Getter
    private PlayerQuest currentQuest;

    @Getter
    private final List<UUID> claimed;

    @Getter
    @Setter
    private boolean completed;

    public Gui preloadRandomQuestGui(UUID uuid) {
        org.bukkit.entity.Player player = Bukkit.getPlayer(uuid);
        if (player == null) return null;

        Gui gui = Gui.gui().title(Util.colorizeComponent("&3Daily Quest")).rows(3).create();
        gui.getFiller().fillBetweenPoints(1, 1, 3,9, ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Util.colorizeComponent("&8")).asGuiItem());

        DailyCreator.setDefaultClickAction(gui);
        DailyCreator.addClosedItem(gui);

        currentQuest = new PlayerQuest(new Random(), PlayerQuest.Status.VIEWABLE, 0, uuid);
        plugin.getMenuCreator().setItem(gui, new Slot(2, 5), currentQuest);

        return gui;
    }

    @Getter
    public static class RandomQuest {
        // todo
        public enum Status {
            COMPLETED,
            IN_PROGRESS,
            VIEWABLE
        }

        private final Quest quest;
        private Status status;
        private int value;

        public RandomQuest(Quest quest) {
            this.quest = quest;
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
            case DIE -> {
                Objective.DieType dieType = Objective.DieType.values()[random.nextInt(Objective.DieType.values().length)];
                amount = switch (dieType) {
                    case ANY -> random(50, 100);
                    case FALL -> random(25, 50);
                    case FIRE, LAVA, HOT_FLOOR, ENTITY_ATTACK -> random(15, 30);
                    case PROJECTILE -> {
                        Objective.ProjectileType projectileType = Objective.ProjectileType.values()[random.nextInt(Objective.ProjectileType.values().length)];
                        yield switch (projectileType) {
                            case ARROW -> random(10, 20);
                            case TRIDENT -> random(3, 6);
                        };
                    }
                    case VOID -> random(5, 15);
                    case MAGIC, FREEZE, THORNS, WITHER, CRAMMING, DROWNING, EXPLOSION -> random(5, 10);
                    case CONTACT -> {
                        Objective.ContactType contactType = Objective.ContactType.values()[random.nextInt(Objective.ContactType.values().length)];
                        yield switch (contactType) {
                            case CACTUS, STALAGMITE -> random(10, 20);
                            case BERRY_BUSH -> random(5, 15);
                        };
                    }
                    case LIGHTNING, STARVATION -> random(1, 3);
                    case SONIC_BOOM, FALLING_BLOCK -> random(3, 6);
                    case SUFFOCATION, FLY_INTO_WALL -> random(10, 15);
                };
                relative.add(dieType);
            }
            case FEED, EAT -> {
                amount = random(32, 64);
                List<Material> materials = Arrays.stream(Material.values()).filter(Material::isEdible).toList();
                relative.add(materials.get(random.nextInt(materials.size() - 1)));
            }
            case COOK -> {
                amount = random(128, 512);
                List<Material> materials = Arrays.stream(Material.values()).filter(Material::isBurnable).toList();
                relative.add(materials.get(random.nextInt(materials.size() - 1)));
            }
            case BREW -> {
                amount = random(18, 36);
                List<Material> materials = Arrays.stream(Material.values()).filter(material -> material == Material.POTION || material == Material.LINGERING_POTION || material == Material.SPLASH_POTION).toList();
                relative.add(materials.get(random.nextInt(materials.size() - 1)));
            }
            case KILL -> {
                amount = random(50, 100);
                List<EntityType> materials = Arrays.stream(EntityType.values()).filter(EntityType::isAlive).toList();
                relative.add(materials.get(random.nextInt(materials.size() - 1)));
            }
            case MINE -> {
                amount = random(500, 1000);
                List<Material> materials = Arrays.stream(Material.values()).filter(Material::isBlock).filter(Material::isSolid).filter(material -> material.getHardness() < 15000000).toList();
                relative.add(materials.get(random.nextInt(materials.size() - 1)));
            }
            case TAME -> {
                amount = random(5, 15);
                List<EntityType> materials = Arrays.stream(EntityType.values()).filter(entityType -> {
                    assert entityType.getEntityClass() != null;
                    return entityType.getEntityClass().isInstance(Tameable.class);
                }).toList();
                relative.add(materials.get(random.nextInt(materials.size() - 1)));
            }
            case BREED -> {
                amount = random(8, 24);
                List<EntityType> materials = Arrays.stream(EntityType.values()).filter(entityType -> {
                    assert entityType.getEntityClass() != null;
                    return entityType.getEntityClass().isInstance(Breedable.class);
                }).toList();
                relative.add(materials.get(random.nextInt(materials.size() - 1)));
            }
            case CRAFT -> {
                amount = random(15, 45);
                List<Material> materials = Arrays.stream(Material.values()).filter(material -> Bukkit.getRecipesFor(new ItemStack(material)).isEmpty()).toList();
                relative.add(materials.get(random.nextInt(materials.size() - 1)));
            }
            case DRINK -> {
                amount = random(10, 30);
                List<Material> materials = Arrays.stream(Material.values()).filter(Material::isEdible).toList();
                relative.add(materials.get(random.nextInt(materials.size() - 1)));
            }
            case PLACE -> {
                amount = random(250, 500);
                List<Material> materials = Arrays.stream(Material.values()).filter(Material::isBlock).filter(Material::isItem).toList();
                relative.add(materials.get(random.nextInt(materials.size() - 1)));
            }
        }

        return new Objective(
                randomType,
                amount,
                relative
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
                    "q0_daily",
                    "&9Daily Quest &8- &dRandomized",
                    0,
                    getReasonable()
            );
        }
    }
}
