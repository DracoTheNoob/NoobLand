package fr.dtn.noobland.feature.mobs.loot;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WolfLoots extends CustomLoots{
    public WolfLoots() { super(Material.WOLF_SPAWN_EGG); }

    @Override
    protected HashMap<Material, Integer> generateLoots(HashMap<Material, Integer> loots, int level) {
        loots.put(Material.BONE, getAmount(level, 4));
        loots.put(Material.MUTTON, getAmount(level, 6));

        return loots;
    }
}