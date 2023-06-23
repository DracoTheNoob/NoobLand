package fr.dtn.noobland.feature.mobs.loot;

import org.bukkit.Material;

import java.util.HashMap;

public class SkeletonLoots extends CustomLoots{
    public SkeletonLoots() { super(Material.SKELETON_SPAWN_EGG); }

    @Override
    protected HashMap<Material, Integer> generateLoots(HashMap<Material, Integer> loots, int level) {
        loots.put(Material.BONE_MEAL, getAtLeastAmount(level, 2));
        loots.put(Material.BONE, getAmount(level, 4));
        loots.put(Material.SKELETON_SKULL, getAmount(level, 11));

        return loots;
    }
}