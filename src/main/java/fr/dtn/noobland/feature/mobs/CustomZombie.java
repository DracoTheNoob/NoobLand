package fr.dtn.noobland.feature.mobs;

import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomZombie extends CustomEntity<Zombie>{
    private static final Material[] stuff;

    static{
        stuff = new Material[]{
                Material.LEATHER_BOOTS,
                Material.LEATHER_HELMET,
                Material.LEATHER_LEGGINGS,
                Material.LEATHER_CHESTPLATE,
                Material.WOODEN_SWORD,
                Material.CHAINMAIL_BOOTS,
                Material.CHAINMAIL_HELMET,
                Material.CHAINMAIL_LEGGINGS,
                Material.CHAINMAIL_CHESTPLATE,
                Material.STONE_SWORD,
                Material.IRON_BOOTS,
                Material.IRON_HELMET,
                Material.IRON_LEGGINGS,
                Material.IRON_CHESTPLATE,
                Material.IRON_SWORD,
                Material.DIAMOND_BOOTS,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_SWORD,
                Material.NETHERITE_BOOTS,
                Material.NETHERITE_HELMET,
                Material.NETHERITE_LEGGINGS,
                Material.NETHERITE_CHESTPLATE,
                Material.NETHERITE_SWORD
        };
    }

    public CustomZombie(Zombie zombie, int level) {
        super(zombie, level);

        Random random = new Random();
        if(zombie.getEquipment() == null)
            return;

        zombie.getEquipment().clear();

        int min = level / 4;
        for(int i = 0; i < (min == 5 ? 5 : random.nextInt(min, 5)); i++){
            Material material = stuff[random.nextInt(Math.max(0, level - 8), level + 3)];
            ItemStack item = new ItemStack(material);

            if(material.name().contains("BOOTS"))
                zombie.getEquipment().setBoots(item);
            else if(material.name().contains("LEGGINGS"))
                zombie.getEquipment().setLeggings(item);
            else if(material.name().contains("CHESTPLATE"))
                zombie.getEquipment().setChestplate(item);
            else if(material.name().contains("HELMET"))
                zombie.getEquipment().setHelmet(item);
            else if(material.name().contains("SWORD"))
                zombie.getEquipment().setItemInMainHand(item);
        }
    }

    @Override public void init() {}

    @Override protected double calculateHealth(int level) { return (int)Math.pow(level - 1, 1.65) + 1; }
    @Override protected double calculateAttack(int level) { return (int)Math.pow(level - 1, 1.03) + 1; }
    @Override protected double calculateSpeed(int level) { return 0.15 + 0.01 * level; }
}