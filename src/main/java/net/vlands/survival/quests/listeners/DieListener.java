package net.vlands.survival.quests.listeners;

import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Player;
import net.vlands.survival.quests.internal.PlayerQuest;
import net.vlands.survival.quests.internal.Util;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DieListener implements Listener {
    private final Main plugin;

    public DieListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockDeath(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof org.bukkit.entity.Player)) return;
        if (((org.bukkit.entity.Player) event.getEntity()).getHealth() <= event.getFinalDamage()) {
            Player vPlayer = plugin.getRegisteredPlayers().get(event.getEntity().getUniqueId());

            if (vPlayer == null) {
                System.out.println("Error, player is not registered (" + event.getEntity().getName() + ")");
                return;
            }

            vPlayer.updateAllQuests();

            for (PlayerQuest quest : vPlayer.getActiveQuests()) {
                Objective objective = quest.getQuest().getObjective();
                if (objective.getType() != Objective.Type.DIE) continue;
                if (objective.getRelative().length >= 1) {
                    if (!(objective.getRelative()[0] instanceof Objective.DieType type)) continue;
                    if (type != Objective.DieType.CONTACT) continue;
                    if (objective.getRelative().length == 2) {
                        if (!(objective.getRelative()[1] instanceof Objective.ContactType contactType)) continue;
                        if (event.getDamager() == null) continue;
                        if (event.getDamager().getType() != contactType.getMaterial()) continue;
                        Util.incrementQuest(plugin, quest);
                    } else if (objective.getRelative().length == 1) {
                        Util.incrementQuest(plugin, quest);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof org.bukkit.entity.Player)) return;
        if (((org.bukkit.entity.Player) event.getEntity()).getHealth() <= event.getFinalDamage()) {
            Player vPlayer = plugin.getRegisteredPlayers().get(event.getEntity().getUniqueId());

            if (vPlayer == null) {
                System.out.println("Error, player is not registered (" + event.getEntity().getName() + ")");
                return;
            }

            vPlayer.updateAllQuests();

            for (PlayerQuest quest : vPlayer.getActiveQuests()) {
                Objective objective = quest.getQuest().getObjective();
                if (objective.getType() != Objective.Type.DIE) continue;
                if (objective.getRelative().length >= 1) {
                    if (objective.getRelative()[0] instanceof Objective.DieType) {
                        EntityDamageEvent.DamageCause expectedCause = ((Objective.DieType) objective.getRelative()[0]).getCause();
                        EntityDamageEvent.DamageCause actualCause = event.getCause();

                        if (objective.getRelative()[0] == Objective.DieType.ANY) {
                            Util.incrementQuest(plugin, quest);
                        } else if (actualCause != EntityDamageEvent.DamageCause.ENTITY_ATTACK && actualCause != EntityDamageEvent.DamageCause.PROJECTILE && actualCause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK && actualCause != EntityDamageEvent.DamageCause.CONTACT) {
                            if (objective.getRelative()[0] == Objective.DieType.EXPLOSION) {
                                if (actualCause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || actualCause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                                    Util.incrementQuest(plugin, quest);
                                }
                            } else if (objective.getRelative()[0] == Objective.DieType.FIRE) {
                                if (actualCause == EntityDamageEvent.DamageCause.FIRE || actualCause == EntityDamageEvent.DamageCause.FIRE_TICK) {
                                    Util.incrementQuest(plugin, quest);
                                }
                            } else {
                                if (actualCause == expectedCause) {
                                    Util.incrementQuest(plugin, quest);
                                }
                            }
                        }
                    } else {
                        Util.incrementQuest(plugin, quest);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof org.bukkit.entity.Player)) return;
        if (((org.bukkit.entity.Player)event.getEntity()).getHealth() <= event.getFinalDamage()) {
            Player vPlayer = plugin.getRegisteredPlayers().get(event.getEntity().getUniqueId());

            if (vPlayer == null) {
                System.out.println("Error, player is not registered (" + event.getEntity().getName() + ")");
                return;
            }

            vPlayer.updateAllQuests();

            for (PlayerQuest quest : vPlayer.getActiveQuests()) {
                Objective objective = quest.getQuest().getObjective();
                if (objective.getType() != Objective.Type.DIE) continue;
                if (objective.getRelative().length >= 1) {
                    if (!(objective.getRelative()[0] instanceof Objective.DieType dieType)) continue;
                    if (dieType != Objective.DieType.ENTITY_ATTACK && dieType != Objective.DieType.PROJECTILE) continue;
                    if (dieType == Objective.DieType.ENTITY_ATTACK) {
                        if (objective.getRelative().length >= 2) {
                            if (!(objective.getRelative()[1] instanceof EntityType type)) continue;
                            if (type != event.getDamager().getType()) continue;
                            Util.incrementQuest(plugin, quest);
                        } else if (objective.getRelative().length == 1) {
                            Util.incrementQuest(plugin, quest);
                        }
                    }
                    if (dieType == Objective.DieType.PROJECTILE) {
                        if (event.getDamager() instanceof Projectile) {
                            if (objective.getRelative().length >= 2) {
                                if (!(objective.getRelative()[1] instanceof Objective.ProjectileType type)) continue;
                                if (type.getType() != event.getDamager().getType()) continue;
                                if (objective.getRelative().length == 3) {
                                    if (!(objective.getRelative()[2] instanceof EntityType shooterType)) continue;
                                    if (!(((Projectile) event.getDamager()).getShooter() instanceof Entity shooter)) continue;
                                    if (shooter.getType() != shooterType) continue;
                                    Util.incrementQuest(plugin, quest);
                                } else if (objective.getRelative().length == 2) {
                                    Util.incrementQuest(plugin, quest);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
