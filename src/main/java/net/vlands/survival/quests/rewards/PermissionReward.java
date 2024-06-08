package net.vlands.survival.quests.rewards;

import lombok.Getter;
import net.vlands.survival.quests.Main;

@Getter
public class PermissionReward {
    private final String permission;
    private final String duration;
    private final String description;
    private final String command;

    public PermissionReward(String permission, String duration, String description) {
        this.permission = permission;
        this.duration = duration;
        this.description = description;
        this.command = Main.PermissionsManager.getPreferredCommand(permission);
    }

}
