package net.vlands.survival.quests.internal;

import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

public class Player {
    private final UUID bind;
    private final List<PlayerQuest> questList;

    public Player(UUID uuid, List<PlayerQuest> questList) {
        this.bind = uuid;
        this.questList = questList;
    }

    public UUID getBind() {
        return bind;
    }

    public List<PlayerQuest> getQuestList() {
        return questList;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",");
        questList.forEach(playerQuest -> joiner.add(playerQuest.toString()));

        return bind.toString() + "{" + joiner + "}";
    }
}
