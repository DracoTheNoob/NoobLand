package fr.dtn.noobland.listener.mob;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.mobs.loot.WolfLoots;
import fr.dtn.noobland.feature.mobs.loot.ZombieLoots;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityDeathListener extends Listener{
    public EntityDeathListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        LivingEntity entity = event.getEntity();
        plugin.getFightManager().clearEnemy(entity.getUniqueId());

        if(entity.getCustomName() == null)
            return;

        event.getDrops().clear();

        String name = entity.getCustomName();
        String strLevel = name.substring(name.indexOf(". ")+4, name.indexOf(']')-2);
        int level = Integer.parseInt(strLevel);

        List<ItemStack> loots = Arrays.asList(switch(event.getEntityType()){
            case WOLF -> new WolfLoots().getLoots(entity, level);
            case ZOMBIE -> new ZombieLoots().getLoots(entity, level);
            default -> new ItemStack[0];
        });

        if(entity.getEquipment() != null){
            List<ItemStack> equipment = new ArrayList<>();

            for(ItemStack item : entity.getEquipment().getArmorContents())
                if(item != null)
                    equipment.add(item);

            if(entity.getEquipment().getItemInMainHand().getType() != Material.AIR)
                equipment.add(entity.getEquipment().getItemInMainHand());

            if(entity.getEquipment().getItemInOffHand().getType() != Material.AIR)
                equipment.add(entity.getEquipment().getItemInOffHand());

            event.getDrops().addAll(equipment);
        }

        event.getDrops().addAll(loots);
    }
}