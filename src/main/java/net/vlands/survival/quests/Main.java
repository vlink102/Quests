package net.vlands.survival.quests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.vlands.survival.quests.gui.MenuCreator;
import net.vlands.survival.quests.internal.Player;
import net.vlands.survival.quests.internal.QuestsManager;
import net.vlands.survival.quests.quests.GoldDigger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class Main extends JavaPlugin {

    private HashMap<UUID, Player> registeredPlayers = new HashMap<>();
    @Getter private QuestsManager manager;
    private MenuCreator menuCreator;
    @Getter private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Getter private final Path path = Paths.get(getDataFolder() + File.separator + "data.json");

    @Override
    public void onEnable() {
        manager = new QuestsManager(this);
        menuCreator = new MenuCreator(this);
        manager.registerQuest(new GoldDigger());

        Objects.requireNonNull(getCommand("quests")).setExecutor(this);
        Objects.requireNonNull(getCommand("quest")).setExecutor(this);

        generateDataFiles();
        // get from json if data is there

        Bukkit.getOnlinePlayers().forEach(player -> registerPlayer(player.getUniqueId()));

        // save data to json file every 30 seconds or something

        System.out.println(registeredPlayers);
    }

    private void toJson() {
        try {
            String json = gson.toJson(registeredPlayers);
            java.nio.file.Files.writeString(path, json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fromJson() {
        try {
            Reader reader = Files.newBufferedReader(path);
            HashMap<UUID, Player> playerHashMap = gson.fromJson(reader, HashMap.class);
            if (playerHashMap != null) {
                registeredPlayers = playerHashMap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void registerPlayer(UUID uuid) {
        if (!registeredPlayers.containsKey(uuid)) {
            registeredPlayers.put(uuid, new Player(uuid, manager.generatePlayerQuests(uuid)));
        }
    }

    private void generateDataFiles() {
        try {
            if (!getDataFolder().exists()) {
                System.out.println("No plugin folder found, creating...");
                getDataFolder().mkdir();
            }
            if (!path.toFile().exists()) {
                System.out.println("No data file found, creating...");
                path.toFile().getParentFile().mkdirs();
                path.toFile().createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            sender.sendMessage("You cannot run that as console.");
            return true;
        }

        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        menuCreator.createAchievementMenu(player.getUniqueId()).open(player);
        return true;
    }
}
