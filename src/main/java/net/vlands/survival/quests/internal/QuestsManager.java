package net.vlands.survival.quests.internal;

import net.vlands.survival.quests.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestsManager {
    private final Main plugin;

    private final List<Quest> registeredQuests = new ArrayList<>();

    public QuestsManager(Main plugin) {
        this.plugin = plugin;
    }

    public void registerQuest(Quest quest) {
        if (!registeredQuests.contains(quest)) {
            registeredQuests.add(quest);
        }
    }
    public List<Quest> getRegisteredQuests() {
        return registeredQuests;
    }

    public PlayerQuest convert(Quest quest, UUID uuid) {
        return new PlayerQuest(quest, uuid, PlayerQuest.Status.HIDDEN, 0);
    }

    public List<PlayerQuest> generatePlayerQuests(UUID uuid) {
        List<PlayerQuest> playerQuestList = new ArrayList<>();
        registeredQuests.forEach(quest -> playerQuestList.add(convert(quest, uuid)));
        return playerQuestList;
    }

}
