package fr.dtn.noobland.feature.mobs.loot;

import fr.dtn.noobland.feature.mobs.loot.CustomLoots;
import org.bukkit.Material;

import java.util.HashMap;

public class ZombieLoots extends CustomLoots{
    public ZombieLoots(){ super(Material.ZOMBIE_SPAWN_EGG); }

    @Override
    protected HashMap<Material, Integer> generateLoots(HashMap<Material, Integer> loots, int level) {
        loots.put(Material.ROTTEN_FLESH, getAtLeastAmount(level, 2));
        loots.put(Material.POTATO, getAmount(level, 6));
        loots.put(Material.IRON_INGOT, getAmount(level, 8));

        return loots;
    }
}