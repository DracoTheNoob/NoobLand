package fr.dtn.noobland.feature.mobs.loot;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ZombieLoots implements Loots{
    @Override
    public ItemStack[] getLoots(LivingEntity entity, int level) {
        List<ItemStack> loots = new ArrayList<>();

        Random random = new Random();

        int rottenFlesh = random.nextInt(0, level/2+1);
        int potatoes = random.nextInt(0, level/6 + (level < 6 ? 1 : 0));
        int ingots = random.nextInt(0, level/8 + (level < 8 ? 1 : 0));

        if(rottenFlesh != 0)
            loots.add(new ItemStack(Material.ROTTEN_FLESH, rottenFlesh));
        if(potatoes != 0 && level >= 6)
            loots.add(new ItemStack(Material.POTATO, potatoes));
        if(ingots != 0 && level >= 8)
            loots.add(new ItemStack(Material.IRON_INGOT, ingots));

        return loots.toArray(new ItemStack[0]);
    }
}