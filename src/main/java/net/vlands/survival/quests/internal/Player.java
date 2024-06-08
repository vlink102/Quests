package net.vlands.survival.quests.internal;

import java.util.*;

public class Player {
    private final UUID bind;
    private final List<PlayerQuest> questList;
    private final List<PlayerLoot> lootList;
    public final List<PlayerQuest> activeQuests = new ArrayList<>();
    public final List<PlayerQuest> completedQuests = new ArrayList<>();
    public final List<PlayerQuest> claimedQuests = new ArrayList<>();
    public final List<PlayerLoot> claimedLoot = new ArrayList<>();
    public final List<PlayerLoot> completedLoot = new ArrayList<>();
    public final List<PlayerQuest> onJoin = new ArrayList<>();
    public final List<PlayerLoot> onJoinLoot = new ArrayList<>();

    private int completedQuestsVal;
    private int rerollCount;

    public void decreaseReroll() {
        if (rerollCount != 0) {
            rerollCount -= 1;
        }
    }

    public int getRerollCount() {
        return rerollCount;
    }

    public void setRerollCount(int rerollCount) {
        this.rerollCount = rerollCount;
    }

    public Player(UUID uuid, List<PlayerQuest> questList, List<PlayerLoot> lootList) {
        this.bind = uuid;
        this.questList = questList;
        this.lootList = lootList;
        updateAllQuests();
        rerollCount = completedQuestsVal >= 10 ? 5 : 3;
    }

    public int getCompletedQuestsVal() {
        return completedQuestsVal;
    }

    public void setCompletedQuestsVal(int completedQuestsVal) {
        this.completedQuestsVal = completedQuestsVal;
    }

    public void updateAllQuests() {
        activeQuests.clear();
        completedQuests.clear();
        claimedQuests.clear();
        claimedLoot.clear();
        completedLoot.clear();

        questList.forEach(quest -> {
            switch (quest.getStatus()) {
                case VIEWABLE, IN_PROGRESS -> {
                    if (!activeQuests.contains(quest)) {
                        activeQuests.add(quest);
                    }
                }
                case COMPLETED -> addCompletedQuest(quest);
                case CLAIMED -> {
                    addClaimedQuest(quest);
                    removeCompletedQuest(quest);
                }
            }
        });
        lootList.forEach(loot -> {
            switch (loot.getStatus()) {
                case CLAIMED -> {
                    addClaimedLoot(loot);
                    removeCompletedLoot(loot);
                }
                case COMPLETE -> addCompleteLoot(loot);
            }
        });

        setCompletedQuestsVal(claimedQuests.size() + completedQuests.size());
    }

    public List<PlayerQuest> getActiveQuests() {
        return activeQuests;
    }

    public List<PlayerLoot> getLootList() {
        return lootList;
    }

    public UUID getBind() {
        return bind;
    }

    public void addCompletedQuest(PlayerQuest addition) {
        if (!completedQuests.contains(addition)) {
            completedQuests.add(addition);
        }
    }

    public void addClaimedQuest(PlayerQuest quest) {
        if (!claimedQuests.contains(quest)) {
            claimedQuests.add(quest);
        }
    }

    public void addCompleteLoot(PlayerLoot loot) {
        if (!completedLoot.contains(loot)) {
            completedLoot.add(loot);
        }
    }

    public void addClaimedLoot(PlayerLoot loot) {
        if (!claimedLoot.contains(loot)) {
            claimedLoot.add(loot);
        }
    }

    public void removeCompletedQuest(PlayerQuest quest) {
        completedQuests.remove(quest);
    }

    public void removeCompletedLoot(PlayerLoot loot) {
        completedLoot.remove(loot);
    }

    public void addJoinQuest(PlayerQuest quest) {
        if (!onJoin.contains(quest)) {
            onJoin.add(quest);
        }
    }

    public void removeJoinQuest(PlayerQuest quest) {
        onJoin.remove(quest);
    }

    public void addJoinLoot(PlayerLoot loot) {
        if (!onJoinLoot.contains(loot)) {
            onJoinLoot.add(loot);
        }
    }

    public void removeJoinLoot(PlayerLoot loot) {
        onJoinLoot.remove(loot);
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
