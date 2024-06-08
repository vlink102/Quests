package net.vlands.survival.quests;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.dvs.Pattern;
import dev.dejvokep.boostedyaml.dvs.segment.Segment;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.vlands.survival.quests.gui.DailyCreator;
import net.vlands.survival.quests.gui.MenuCreator;
import net.vlands.survival.quests.gui.RandomCreator;
import net.vlands.survival.quests.internal.*;
import net.vlands.survival.quests.listeners.*;
import net.vlands.survival.quests.quests.*;
import net.vlands.survival.quests.quests.die.*;
import net.vlands.survival.quests.quests.die.entityattack.Zombie;
import net.vlands.survival.quests.quests.kill.Cow;
import net.vlands.survival.quests.quests.loot.LootChestTest;
import net.vlands.survival.quests.quests.place.On;
import net.vlands.survival.quests.quests.travel.*;
import net.vlands.survival.quests.rewards.PermissionReward;
import net.vlands.survival.quests.vlandsquests.Quests;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class Main extends JavaPlugin implements Listener {

    @Getter
    public enum PermissionsManager {
        AQUA_CORE("setperm %player% %permission% true " + SERVER_ID),
        LUCKPERMS("lp user %player% permission set %permission% true " + SERVER_ID);

        private final String command;

        PermissionsManager(String command) {
            this.command = command;
        }

        public static String getPreferredCommand(String permission) {
            return preferredManager.command.replaceAll("%permission%", permission);
        }
    }

    public static final String SERVER_ID = "Survival";
    public static final PermissionsManager preferredManager = PermissionsManager.LUCKPERMS;

    @Getter
    private MenuCreator menuCreator;
    private DailyCreator dailyCreator;
    @Getter
    private RandomCreator randomCreator;

    @Getter private RomanNumber number;

    @Getter private SlotMap slotMap;
    @Getter private HashMap<UUID, Player> registeredPlayers = new HashMap<>();
    @Getter private QuestsManager manager;

    @Getter
    private boolean aquaCore;

    @Getter
    private boolean luckPerms;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final Path jsonPath = Paths.get(getDataFolder() + File.separator + "dump.json");
    private final ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();

    private void loadDependencies() {
        switch (preferredManager) {
            case LUCKPERMS -> {
                luckPerms = getServer().getPluginManager().getPlugin("LuckPerms") != null;
                if (luckPerms) {
                    log("Hooked onto LuckPerms! - Permission System");
                } else {
                    getLogger().severe("No LuckPerms dependency found, permission rewards disabled");
                }
            }
            case AQUA_CORE -> {
                aquaCore = getServer().getPluginManager().getPlugin("AquaCore") != null;
                if (aquaCore) {
                    log("Hooked onto AquaCore! - Permission System.");
                } else {
                    getLogger().severe("No AquaCore dependency found, permission rewards disabled.");
                }
            }
        }
    }

    @Override
    public void onEnable() {
        loadDependencies();
        manager = new QuestsManager(this);
        menuCreator = new MenuCreator(this);
        dailyCreator = new DailyCreator(this);
        randomCreator = new RandomCreator(this);
        slotMap = new SlotMap();
        number = new RomanNumber();

        registerListeners();
        //registerDevQuests();
        registerQuests();
        registerCommands();

        generateDataFiles();
        fromYaml();

        Bukkit.getOnlinePlayers().forEach(player -> registerPlayer(player.getUniqueId()));

        toYaml();

        TravelLogger travelLogger = new TravelLogger(this);
        travelLogger.startLogging();
    }

    public void registerQuests() {
        manager.registerQuest(new Quests.GoldDigger());
        manager.registerQuest(new Quests.RockBottom());
        manager.registerQuest(new Quests.SightSeer());
        manager.registerQuest(new Quests.Poseidon());
        manager.registerQuest(new Quests.EnchantedMelons());
        manager.registerLoot(new Quests.MilestoneOne());
        manager.registerQuest(new Quests.JacketPotato());
        manager.registerQuest(new Quests.Prickly());
        manager.registerQuest(new Quests.SlaughterHouse());
        manager.registerQuest(new Quests.Assassin());
        manager.registerQuest(new Quests.Pain());
        manager.registerLoot(new Quests.MilestoneTwo());
    }

    @Override
    public void onDisable() {
        toYaml();
    }

    public void log(String message) {
        consoleCommandSender.sendMessage(Util.colorize("&6" + message));
    }

    public void log1(String message) {
        log("&8 - &7" + message);
    }

    private void registerListeners() {
        log("Registering listeners...");
        List<Listener> listeners = new ArrayList<>();
        listeners.add(new MineListener(this));
        listeners.add(new CraftListener(this));
        listeners.add(new ConsumeListener(this));
        listeners.add(new CookListener(this));
        listeners.add(new DieListener(this));
        listeners.add(new KillListener(this));
        listeners.add(new TameListener(this));
        listeners.add(new FeedListener(this));
        listeners.add(new BreedListener(this));
        listeners.add(new BrewListener(this));
        listeners.add(new PlaceListener(this));
        listeners.add(this);

        listeners.forEach(listener -> {
            getServer().getPluginManager().registerEvents(listener,this);
            log1("Registered listener: " + listener.getClass().getSimpleName());
        });
    }

    private void registerDevQuests() {
        log("Registering quests... (This may take a while)");
        manager.registerQuest(new Quests.GoldDigger());
        manager.registerQuest(new DrinkTest());
        manager.registerQuest(new EatTest());
        manager.registerQuest(new FeedTest());
        manager.registerQuest(new BrewTest());
        manager.registerQuest(new CookTest());
        manager.registerQuest(new CraftTest());
        manager.registerQuest(new DieTest());
        manager.registerQuest(new KillTest());
        manager.registerQuest(new PlaceTest());
        manager.registerQuest(new On());
        manager.registerQuest(new BreedTest());
        manager.registerQuest(new FeedTest());
        manager.registerQuest(new TameTest());
        manager.registerQuest(new TravelTest());

        manager.registerQuest(new Aviate());
        manager.registerQuest(new Boat());
        manager.registerQuest(new Climb());
        manager.registerQuest(new Crouch());
        manager.registerQuest(new Fall());
        manager.registerQuest(new Horse());
        manager.registerQuest(new Minecart());
        manager.registerQuest(new Pig());
        manager.registerQuest(new Sprint());
        manager.registerQuest(new Strider());
        manager.registerQuest(new Swim());
        manager.registerQuest(new Walk());
        manager.registerQuest(new WalkOnWater());
        manager.registerQuest(new WalkUnderWater());

        manager.registerQuest(new EntityAttackTest());
        manager.registerQuest(new ExplosionTest());
        manager.registerQuest(new ContactTest());
        manager.registerQuest(new CrammingTest());
        manager.registerQuest(new DrowningTest());
        manager.registerQuest(new FallTest());
        manager.registerQuest(new FallingBlockTest());
        manager.registerQuest(new FireTest());
        manager.registerQuest(new FlyIntoWallTest());
        manager.registerQuest(new FreezeTest());
        manager.registerQuest(new HotFloorTest());
        manager.registerQuest(new LavaTest());
        manager.registerQuest(new LightningTest());
        manager.registerQuest(new MagicTest());
        manager.registerQuest(new ProjectileTest());
        manager.registerQuest(new SonicBoomTest());
        manager.registerQuest(new StarvationTest());
        manager.registerQuest(new SuffocationTest());
        manager.registerQuest(new ThornsTest());
        manager.registerQuest(new VoidTest());
        manager.registerQuest(new WitherTest());

        manager.registerQuest(new Zombie());

        manager.registerQuest(new Cow());

        manager.registerQuest(new HiddenTest());
        manager.registerQuest(new PermissionRewardTest());

        manager.registerLoot(new LootChestTest());
        log("Quests and loot carts registered!");
    }

    private void registerCommands() {
        log("Registering commands...");
        Objects.requireNonNull(getCommand("quests")).setExecutor(this);
        Objects.requireNonNull(getCommand("quest")).setExecutor(this);
        log("Command registration complete!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = registeredPlayers.get(event.getPlayer().getUniqueId());
        if (player == null) {
            registerPlayer(event.getPlayer().getUniqueId());
            log("Registered new player: " + event.getPlayer().getUniqueId() + "(" + event.getPlayer().getName() + ")");
        } else {
            log("Player is already registered: " + event.getPlayer().getUniqueId() + "(" + event.getPlayer().getName() + ")");
        }
        Player vPlayer = registeredPlayers.get(event.getPlayer().getUniqueId());

        if (!vPlayer.onJoinLoot.isEmpty()) {
            event.getPlayer().sendMessage(Util.colorize("&aYou have &6" + vPlayer.onJoinLoot.size() + "&a unclaimed loot chests!"));
            vPlayer.onJoinLoot.clear();
        }
        if (!vPlayer.onJoin.isEmpty()) {
            event.getPlayer().sendMessage(Util.colorize("&aYou have &6" + vPlayer.onJoin.size() + "&a unclaimed quests!"));
            vPlayer.onJoin.clear();
        }
        menuCreator.preloadMenu(vPlayer.getBind());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = registeredPlayers.get(event.getPlayer().getUniqueId());
        if (player == null) {
            System.out.println("Leaving player " + event.getPlayer().getUniqueId() + " is null.");
        } else {
            System.out.println("Saving player data: " + event.getPlayer().getUniqueId() + " (" + event.getPlayer().getName() + ")");
            toYaml(player);
        }
    }

    private void toJson() {
        log("Dumping to JSON format...");
        try {
            String json = gson.toJson(registeredPlayers);
            java.nio.file.Files.writeString(jsonPath, json);
            log("Dump complete!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("all")
    private void fromJson() {
        try {
            Reader reader = Files.newBufferedReader(jsonPath);
            Map<UUID, Player> playerHashMap = gson.fromJson(reader, new TypeToken<Map<UUID, Player>>() {}.getType());
            if (playerHashMap != null) {
                registeredPlayers = new HashMap<>(playerHashMap);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toYaml(Player player) {
        try {
            player.updateAllQuests();
            File dataFile = new File(getDataFolder() + File.separator + "playerdata", player.getBind() + ".yml");
            InputStream resource = getResource("player.yml");
            if (resource == null) {
                log("Error occurred, playerFile resource unavailable. (Did you delete the .jar?)");
                return;
            }

            YamlDocument playerFile = YamlDocument.create(dataFile, resource,
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setAutoSave(true).setVersioning(new Pattern(Segment.range(1, Integer.MAX_VALUE), Segment.literal("."), Segment.range(0, 10)), "file-version").build());

            playerFile.set("uuid", player.getBind().toString());
            playerFile.set("completed-quests", registeredPlayers.get(player.getBind()).getCompletedQuestsVal());

            playerFile.set("quests.claimed", null);
            playerFile.set("quests.active", null);
            playerFile.set("quests.progress", null);
            playerFile.set("quests.completed", null);

            for (PlayerQuest quest : player.getQuestList()) {
                switch (quest.getStatus()) {
                    case CLAIMED -> playerFile.set("quests.claimed." + quest.getQuest().getInternalName(), quest.getValue());
                    case VIEWABLE -> playerFile.set("quests.active." + quest.getQuest().getInternalName(), quest.getValue());
                    case IN_PROGRESS -> playerFile.set("quests.progress." + quest.getQuest().getInternalName(), quest.getValue());
                    case COMPLETED -> playerFile.set("quests.completed." + quest.getQuest().getInternalName(), quest.getValue());
                }
            }
            List<String> lootList = new ArrayList<>();
            List<String> lootClaimed = new ArrayList<>();

            for (PlayerLoot loot : player.getLootList()) {
                switch (loot.getStatus()) {
                    case COMPLETE -> lootList.add(loot.getLoot().getInternalName());
                    case CLAIMED -> lootClaimed.add(loot.getLoot().getInternalName());
                }
            }
            playerFile.set("loot.complete", lootList);
            playerFile.set("loot.claimed", lootClaimed);

            playerFile.save();

            log("Saved player data for: " + player.getBind() + "(" + Bukkit.getOfflinePlayer(player.getBind()).getName() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toYaml() {
        log("Saving data...");
        for (Player player : registeredPlayers.values()) {
            toYaml(player);
        }
        log("Saved all registered players!");
    }

    private void fromYaml() {
        log("Retrieving saved data...");
        registeredPlayers = new HashMap<>();
        try {
            File dataFolder = new File(getDataFolder() + File.separator + "playerdata");
            File[] files = dataFolder.listFiles();
            if (files == null) {
                log("No data found, skipping retrieval...");
                return;
            }

            for (File file : files) {
                if (file.getName().endsWith(".yml")) {
                    InputStream resource = getResource("player.yml");
                    if (resource == null) {
                        log("Error occurred, playerFile resource unavailable. (Did you delete the .jar?)");
                        return;
                    }
                    YamlDocument playerFile = YamlDocument.create(file, resource,
                            GeneralSettings.DEFAULT,
                            LoaderSettings.builder().setAutoUpdate(true).build(),
                            DumperSettings.DEFAULT,
                            UpdaterSettings.builder().setAutoSave(true).setVersioning(new Pattern(Segment.range(1, Integer.MAX_VALUE), Segment.literal("."), Segment.range(0, 10)), "file-version").build());
                    UUID uuid = UUID.fromString(playerFile.getString("uuid"));
                    registerPlayer(uuid);
                    Player player = registeredPlayers.get(uuid);

                    Section active = playerFile.getSection("quests.active");
                    Section completed = playerFile.getSection("quests.completed");
                    Section progress = playerFile.getSection("quests.progress");
                    Section claimed = playerFile.getSection("quests.claimed");

                    List<String> lootComplete = playerFile.getStringList("loot.complete");
                    List<String> lootClaimed = playerFile.getStringList("loot.claimed");

                    for (PlayerQuest quest : player.getQuestList()) {
                        if (active != null) {
                            for (Object key : active.getKeys()) {
                                if (key.toString().equalsIgnoreCase(quest.getQuest().getInternalName())) {
                                    quest.setValue(playerFile.getInt("quests.active." + key));
                                    quest.setStatus(PlayerQuest.Status.VIEWABLE);
                                    player.getActiveQuests().add(quest);
                                    break;
                                }
                            }
                        }
                        if (progress != null) {
                            for (Object key : progress.getKeys()) {
                                if (key.toString().equalsIgnoreCase(quest.getQuest().getInternalName())) {
                                    quest.setValue(playerFile.getInt("quests.progress." + key));
                                    quest.setStatus(PlayerQuest.Status.IN_PROGRESS);
                                    player.getActiveQuests().add(quest);
                                    break;
                                }
                            }
                        }
                        if (completed != null) {
                            for (Object key : completed.getKeys()) {
                                if (key.toString().equalsIgnoreCase(quest.getQuest().getInternalName())) {
                                    quest.setValue(playerFile.getInt("quests.completed." + key));
                                    quest.setStatus(PlayerQuest.Status.COMPLETED);
                                    player.addCompletedQuest(quest);
                                    break;
                                }
                            }
                        }
                        if (claimed != null) {
                            for (Object key : claimed.getKeys()) {
                                if (key.toString().equalsIgnoreCase(quest.getQuest().getInternalName())) {
                                    quest.setValue(playerFile.getInt("quests.claimed." + key));
                                    quest.setStatus(PlayerQuest.Status.CLAIMED);
                                    player.addClaimedQuest(quest);
                                    break;
                                }
                            }
                        }
                    }

                    for (PlayerLoot loot : player.getLootList()) {
                        if (lootComplete != null) {
                            for (String key : lootComplete) {
                                if (key.equalsIgnoreCase(loot.getLoot().getInternalName())) {
                                    loot.setStatus(PlayerLoot.Status.COMPLETE);
                                    player.addCompleteLoot(loot);
                                    break;
                                }
                            }
                        }

                        if (lootClaimed != null) {
                            for (String key : lootClaimed) {
                                if (key.equalsIgnoreCase(loot.getLoot().getInternalName())) {
                                    loot.setStatus(PlayerLoot.Status.CLAIMED);
                                    player.addClaimedLoot(loot);
                                    break;
                                }
                            }
                        }
                    }

                    player.updateAllQuests();

                    log("Retrieved player data for: " + uuid + "(" + Bukkit.getOfflinePlayer(uuid).getName() + ")");
                }
            }

            log("Data retrieval complete!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void registerPlayer(UUID uuid) {
        if (!registeredPlayers.containsKey(uuid)) {
            registeredPlayers.put(uuid, new Player(uuid, manager.generatePlayerQuests(uuid), manager.generatePlayerLoot(uuid)));
        }
    }

    private void generateDataFiles() {
        if (!getDataFolder().exists()) {
            log("No plugin folder found, creating...");
            if (getDataFolder().mkdir()) {
                log("Plugin folder created successfully");
            } else {
                log("Plugin folder not created, please check your files");
            }
        }
        File playerDataFolder = new File(getDataFolder() + File.separator + "playerdata");
        if (!playerDataFolder.exists()) {
            log("No player data folder found, creating...");
            if (playerDataFolder.mkdir()) {
                log("Player data folder created successfully");
            } else {
                log("Player data folder not created, please check your files");
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof org.bukkit.entity.Player player)) {
            sender.sendMessage("You cannot run that as console.");
            return true;
        }

        Player vPlayer = this.getRegisteredPlayers().get(player.getUniqueId());

        if (args.length == 0) {
            menuCreator.createAchievementMenu(player.getUniqueId()).open(player);
            return true;
        } else if (args[0].equalsIgnoreCase("daily")) {
            dailyCreator.openDaily(player);
            return true;
        } else if (args[0].equalsIgnoreCase("hourly")) {
            randomCreator.preloadRandomQuestGui(player.getUniqueId()).open(player);
            return true;
        } else {
            if (player.hasPermission("vlands.quests.admin")) {
                if (args[0].equalsIgnoreCase("dump")) {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("json")) {
                            toJson();
                            return true;
                        } else if (args[1].equalsIgnoreCase("yml")) {
                            toYaml();
                            return true;
                        } else {
                            player.sendMessage(Util.colorize("&cInvalid dump option: " + args[1]));
                            return true;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("chatlog")) {
                    for (PlayerQuest quest : vPlayer.getQuestList()) {
                        player.sendMessage("Quest " + quest.getQuest().getInternalName() + ": " + quest.getStatus() + ", " + quest.getValue());
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("dmgtest")) {
                    player.setHealth(1);
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("air")) {
                            player.setRemainingAir(0);
                        } else if (args[1].equalsIgnoreCase("food")) {
                            player.setFoodLevel(0);
                        } else if (args[1].equalsIgnoreCase("freeze")) {
                            player.setFreezeTicks(0);
                        }
                    }
                    return true;
                } else {
                    player.sendMessage(Util.colorize("&cInvalid parameters: " + args[0]));
                    return true;
                }
            }
        }
        return true;
    }
}
