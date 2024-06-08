package net.vlands.survival.quests.internal;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Arrays;
import java.util.StringJoiner;

public class Objective {
    public enum Type {
        MINE,
        CRAFT,
        EAT,
        DRINK,
        TRAVEL,
        BREW,
        COOK,
        DIE,
        KILL,
        TAME,
        BREED,
        FEED,
        PLACE
    }

    public enum ProjectileType {
        ARROW(EntityType.ARROW, "arrow"),
        TRIDENT(EntityType.TRIDENT, "trident");

        private final EntityType type;
        private final String prettyName;

        ProjectileType(EntityType type, String prettyName) {
            this.type = type;
            this.prettyName = prettyName;
        }

        public EntityType getType() {
            return type;
        }

        public String getPrettyName() {
            return prettyName;
        }
    }

    public enum ContactType {
        CACTUS(Material.CACTUS, "cactus"),
        STALAGMITE(Material.POINTED_DRIPSTONE, "stalagmite"),
        BERRY_BUSH(Material.SWEET_BERRY_BUSH, "sweet berry bush");

        private final Material material;
        private final String prettyName;

        ContactType(Material material, String prettyName) {
            this.material = material;
            this.prettyName = prettyName;
        }

        public Material getMaterial() {
            return material;
        }

        public String getPrettyName() {
            return prettyName;
        }
    }

    public enum KillPlayerType {
        SINGLE,
        DIFFERENT
    }

    public enum DieType {
        ENTITY_ATTACK(EntityDamageEvent.DamageCause.ENTITY_ATTACK,"Die to###entity"),

        EXPLOSION(null, "Die to an explosion"),

        CONTACT(EntityDamageEvent.DamageCause.CONTACT, "Die to###contact"),
        CRAMMING(EntityDamageEvent.DamageCause.CRAMMING, "Die to entity cramming"),
        DROWNING(EntityDamageEvent.DamageCause.DROWNING, "Drown"),
        FALL(EntityDamageEvent.DamageCause.FALL, "Die of fall damage"),
        FALLING_BLOCK(EntityDamageEvent.DamageCause.FALLING_BLOCK, "Get squashed by a block"),

        FIRE(null, "Burn to death"),

        FLY_INTO_WALL(EntityDamageEvent.DamageCause.FLY_INTO_WALL, "Die to kinetic energy"),
        FREEZE(EntityDamageEvent.DamageCause.FREEZE, "Freeze to death"),
        HOT_FLOOR(EntityDamageEvent.DamageCause.HOT_FLOOR, "Die to magma blocks"),
        LAVA(EntityDamageEvent.DamageCause.LAVA, "Die to lava"),
        LIGHTNING(EntityDamageEvent.DamageCause.LIGHTNING, "Die to lightning"),
        MAGIC(EntityDamageEvent.DamageCause.MAGIC, "Die to magic"),
        PROJECTILE(EntityDamageEvent.DamageCause.PROJECTILE, "Die to###projectile###shot by###entity"),
        SONIC_BOOM(EntityDamageEvent.DamageCause.SONIC_BOOM, "Die to a sonic boom"),
        STARVATION(EntityDamageEvent.DamageCause.STARVATION, "Starve to death"),
        SUFFOCATION(EntityDamageEvent.DamageCause.SUFFOCATION, "Suffocate"),
        THORNS(EntityDamageEvent.DamageCause.THORNS, "Die to thorns"),
        VOID(EntityDamageEvent.DamageCause.VOID, "Fall in the void"),
        WITHER(EntityDamageEvent.DamageCause.WITHER, "Wither away"),
        ANY(null, "Die");

        private final EntityDamageEvent.DamageCause cause;
        private final String prettyName;

        DieType(EntityDamageEvent.DamageCause cause, String prettyName) {
            this.cause = cause;
            this.prettyName = prettyName;
        }

        public EntityDamageEvent.DamageCause getCause() {
            return cause;
        }

        public String getPrettyName() {
            return prettyName;
        }
    }

    @Getter
    public enum TravelType {
        WALK(Statistic.WALK_ONE_CM,"Walk"),
        SPRINT(Statistic.SPRINT_ONE_CM,"Sprint"),
        AVIATE(Statistic.AVIATE_ONE_CM,"Fly"),
        BOAT(Statistic.BOAT_ONE_CM,"Ride a boat"),
        CLIMB(Statistic.CLIMB_ONE_CM,"Climb"),
        CROUCH(Statistic.CROUCH_ONE_CM,"Crouch"),
        FALL(Statistic.FALL_ONE_CM,"Fall"),
        HORSE(Statistic.HORSE_ONE_CM,"Ride a horse"),
        MINECART(Statistic.MINECART_ONE_CM,"Ride a minecart"),
        PIG(Statistic.PIG_ONE_CM,"Ride a pig"),
        STRIDER(Statistic.STRIDER_ONE_CM,"Ride a strider"),
        SWIM(Statistic.SWIM_ONE_CM,"Swim"),
        WALK_ON_WATER(Statistic.WALK_ON_WATER_ONE_CM,"Walk on water"),
        WALK_UNDER_WATER(Statistic.WALK_UNDER_WATER_ONE_CM,"Walk underwater"),
        ALL(null, "Travel");

        private final String prettyName;
        private final Statistic statistic;

        TravelType(Statistic statistic, String prettyName) {
            this.statistic = statistic;
            this.prettyName = prettyName;
        }

    }

    private final Type type;
    private final int maxValue;
    private Object[] relative;

    public Objective(Type type, int maxValue, Object... relative) {
        this.type = type;
        this.maxValue = maxValue;
        this.relative = relative;
        if (this.relative.length == 0) {
            switch (this.type) {
                case TRAVEL -> setRelative(appendArray(this.relative, "blocks"));
                case DIE -> setRelative(appendArray(this.relative, "times"));
            }
        }
    }

    public void setRelative(Object[] relative) {
        this.relative = relative;
    }

    private Object[] appendArray(Object[] array, Object x){
        Object[] result = new Object[array.length + 1];
        System.arraycopy(array, 0, result, 0, array.length);
        result[result.length - 1] = x;
        return result;
    }

    public Type getType() {
        return type;
    }
    public int getMaxValue() {
        return maxValue;
    }
    public Object[] getRelative() {
        return relative;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",");
        Arrays.stream(relative).forEach(rel -> joiner.add(rel.toString()));
        return "{" + type.toString() + ", " + maxValue + ", {" + joiner + "}}";
    }
}
