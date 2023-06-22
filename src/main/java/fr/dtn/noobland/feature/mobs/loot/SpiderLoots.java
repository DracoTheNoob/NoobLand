package fr.dtn.noobland.feature.mobs.loot;

import org.bukkit.Material;

import java.util.HashMap;

public class SpiderLoots extends CustomLoots{
    public SpiderLoots() { super(Material.SPIDER_SPAWN_EGG); }

    @Override
    public HashMap<Material, Integer> generateLoots(HashMap<Material, Integer> loots, int level) {
        loots.put(Material.STRING, getAtLeastAmount(level, 2));
        loots.put(Material.SPIDER_EYE, getAmount(level, 6));
        loots.put(Material.FERMENTED_SPIDER_EYE, getAmount(level, 8));

        return loots;
    }
}