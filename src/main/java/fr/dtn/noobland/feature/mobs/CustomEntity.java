package fr.dtn.noobland.feature.mobs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Objects;

public abstract class CustomEntity<E extends LivingEntity> {
    private static final HashMap<Class<? extends LivingEntity>, Double> baseHealths;
    private static final HashMap<Class<? extends LivingEntity>, Double> baseSpeeds;
    private static final HashMap<Class<? extends LivingEntity>, Double> baseAttacks;

    static{
        baseHealths = new HashMap<>();
        baseSpeeds = new HashMap<>();
        baseAttacks = new HashMap<>();
    }

    private final double baseMaxHealth, baseAttack, baseSpeed;
    private final E entity;

    public CustomEntity(E entity, int level){
        this.entity = entity;

        if(entity instanceof Ageable ageable)
            ageable.setAdult();

        String first = entity.getName().substring(0, 1);

        this.baseMaxHealth = getMaxHealth();
        this.baseAttack = getAttack();
        this.baseSpeed = getSpeed();

        if(!baseHealths.containsKey(entity.getClass()))
            baseHealths.put(entity.getClass(), baseMaxHealth);
        if(!baseAttacks.containsKey(entity.getClass()))
            baseAttacks.put(entity.getClass(), baseAttack);
        if(!baseSpeeds.containsKey(entity.getClass()))
            baseSpeeds.put(entity.getClass(), baseSpeed);

        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(calculateHealth(level));
        entity.setHealth(getMaxHealth());
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(calculateAttack(level));
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(calculateSpeed(level));

        String name = (level == 20 ? "§9" : "§6") + entity.getName().replaceFirst(first, first.toUpperCase()) + " §7[lvl. §c" + level + "§7] " + (int)getHealth() + "§4❤";
        entity.setCustomName(name);
        entity.setCustomNameVisible(true);

        if(level == 20){
            Location l = entity.getLocation();
            String cn = entity.getCustomName();
            assert cn != null;
            Bukkit.broadcastMessage(cn.substring(0, cn.lastIndexOf("] ")) + "§9 est apparu en §c" + l.getX() + "§9;§c" + l.getY() + "§9;§c" + l.getZ() + "§9 !");
        }
    }

    public abstract void init();

    protected abstract double calculateHealth(int level);
    protected abstract double calculateAttack(int level);
    protected abstract double calculateSpeed(int level);

    public static double getBaseMaxHealthOf(Class<? extends LivingEntity> entity){ return baseHealths.getOrDefault(entity, -1.0); }
    public static double getBaseAttackOf(Class<? extends LivingEntity> entity){ return baseAttacks.getOrDefault(entity, -1.0); }
    public static double getBaseSpeedOf(Class<? extends LivingEntity> entity){ return baseSpeeds.getOrDefault(entity, -1.0); }

    public final double getBaseMaxHealth() { return baseMaxHealth; }
    public final double getBaseAttack() { return baseAttack; }
    public final double getBaseSpeed() { return baseSpeed; }

    public final double getMaxHealth(){ return Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue(); }
    public final double getAttack(){ return Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getBaseValue(); }
    public final double getSpeed(){ return Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).getBaseValue(); }

    public double getHealth() { return entity.getHealth(); }
}