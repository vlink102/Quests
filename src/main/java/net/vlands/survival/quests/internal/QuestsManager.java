package net.vlands.survival.quests.internal;

import net.vlands.survival.quests.Main;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestsManager {
    private final Main plugin;

    private final List<Quest> registeredQuests = new ArrayList<>();
    private final List<Loot> registeredLootChests = new ArrayList<>();

    public QuestsManager(Main plugin) {
        this.plugin = plugin;
    }

    public void registerQuest(Quest quest) {
        if (!registeredQuests.contains(quest)) {
            registeredQuests.add(quest);
            plugin.log1("Registered quest: " + quest.getInternalName() + " (id: " + quest.getId() + ", uc: " + quest.getUnlockCount() + ")");
        }
    }
    public void registerLoot(Loot loot) {
        if (!registeredLootChests.contains(loot)) {
            registeredLootChests.add(loot);
            plugin.log1("Registered loot: " + loot.getInternalName() + " (id: " + loot.getId() + ", uc: " + loot.getQuestsTillUnlocked() + ")");
        }
    }

    public List<Quest> getRegisteredQuests() {
        return registeredQuests;
    }

    public PlayerQuest convert(Quest quest, UUID uuid) {
        return new PlayerQuest(quest, PlayerQuest.Status.HIDDEN, 0, uuid);
    }

    public PlayerLoot convert(Loot loot, UUID uuid) {
        return new PlayerLoot(loot, uuid, PlayerLoot.Status.HIDDEN);
    }

    public List<PlayerQuest> generatePlayerQuests(UUID uuid) {
        List<PlayerQuest> playerQuestList = new ArrayList<>();
        registeredQuests.forEach(quest -> playerQuestList.add(convert(quest, uuid)));
        return playerQuestList;
    }

    public List<PlayerLoot> generatePlayerLoot(UUID uuid) {
        List<PlayerLoot> playerLootList = new ArrayList<>();
        registeredLootChests.forEach(loot -> playerLootList.add(convert(loot, uuid)));
        return playerLootList;
    }

    public Quest getQuestById(int id) {
        for (Quest quest : registeredQuests) {
            if (quest.getId() == id) {
                return quest;
            }
        }
        return null;
    }

}
