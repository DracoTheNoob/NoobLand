package fr.dtn.noobland.feature.mobs.loot;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class CustomLoots {
    private final Material spawnEgg;

    protected CustomLoots(Material spawnEgg){ this.spawnEgg = spawnEgg; }

    public ItemStack[] get(int level){
        List<ItemStack> loots = new ArrayList<>();
        HashMap<Material, Integer> mobLoots = generateLoots(new HashMap<>(), level);

        mobLoots.keySet().forEach(material -> {
            if(mobLoots.get(material) != 0)
                loots.add(new ItemStack(material, mobLoots.get(material)));
        });

        if(level == 20)
            loots.add(new ItemStack(spawnEgg));

        return loots.toArray(ItemStack[]::new);
    }

    protected int getAmount(int level, int requirement){
        if(level < requirement)
            return 0;

        return new Random().nextInt(0, level/requirement+1);
    }

    protected int getAtLeastAmount(int level, int requirement){ return new Random().nextInt(0, Math.max(level / requirement+1, 2)); }
    protected abstract HashMap<Material, Integer> generateLoots(HashMap<Material, Integer> loots, int level);
}