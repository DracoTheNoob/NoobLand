package fr.dtn.noobland.feature.mobs.loot;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface Loots {
    ItemStack[] getLoots(LivingEntity entity, int level);
}