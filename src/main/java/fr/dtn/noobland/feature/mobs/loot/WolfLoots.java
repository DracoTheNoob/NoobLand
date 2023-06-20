package fr.dtn.noobland.feature.mobs.loot;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WolfLoots implements Loots{
    @Override
    public ItemStack[] getLoots(LivingEntity entity, int level){
        List<ItemStack> loots = new ArrayList<>();

        Random random = new Random();
        int leather = random.nextInt(0, level/4+1);
        int bone = random.nextInt(0, level/6+1);
        int mutton = random.nextInt(0, level/8+1);

        if(leather != 0)
            loots.add(new ItemStack(Material.LEATHER, leather));
        if(bone != 0)
            loots.add(new ItemStack(Material.BONE, bone));
        if(mutton != 0)
            loots.add(new ItemStack(entity.getFireTicks() != 0 ? Material.COOKED_MUTTON : Material.MUTTON, mutton));

        return loots.toArray(new ItemStack[0]);
    }
}