package fr.dtn.noobland.listener.entity;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.mobs.loot.SkeletonLoots;
import fr.dtn.noobland.feature.mobs.loot.SpiderLoots;
import fr.dtn.noobland.feature.mobs.loot.WolfLoots;
import fr.dtn.noobland.feature.mobs.loot.ZombieLoots;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EntityDeathListener extends Listener{
    public EntityDeathListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        LivingEntity entity = event.getEntity();

        if(entity instanceof Player)
            return;

        if(entity.getCustomName() == null)
            return;

        event.getDrops().clear();

        String name = entity.getCustomName();
        String strLevel = name.substring(name.indexOf(". ")+4, name.indexOf(']')-2);
        int level = Integer.parseInt(strLevel);

        List<ItemStack> loots = Arrays.asList(switch(event.getEntityType()){
            case WOLF -> new WolfLoots().get(level);
            case ZOMBIE -> new ZombieLoots().get(level);
            case SPIDER -> new SpiderLoots().get(level);
            case SKELETON -> new SkeletonLoots().get(level);
            default -> new ItemStack[0];
        });

        if(entity.getEquipment() != null){
            List<ItemStack> equipment = new ArrayList<>();

            for(ItemStack item : entity.getEquipment().getArmorContents())
                if(item != null)
                    equipment.add(material(item));

            if(entity.getEquipment().getItemInMainHand().getType() != Material.AIR)
                equipment.add(material(entity.getEquipment().getItemInMainHand()));

            if(entity.getEquipment().getItemInOffHand().getType() != Material.AIR)
                equipment.add(material(entity.getEquipment().getItemInOffHand()));

            event.getDrops().addAll(equipment);
        }

        event.getDrops().addAll(loots);
    }

    public ItemStack material(ItemStack base){
        String name = base.getType().name();
        Random random = new Random();

        if(name.contains("WOOD"))
            return new ItemStack(Material.OAK_PLANKS, random.nextInt(0, 4));
        if(name.contains("STONE"))
            return new ItemStack(Material.COBBLESTONE, random.nextInt(0, 4));
        if(name.contains("LEATHER"))
            return new ItemStack(Material.LEATHER, random.nextInt(0, 5));
        if(name.contains("CHAINMAIL"))
            return new ItemStack(Material.IRON_NUGGET, random.nextInt(0, 8));
        if(name.contains("IRON"))
            return new ItemStack(Material.IRON_INGOT, random.nextInt(0, 3));
        if(name.contains("GOLD"))
            return new ItemStack(Material.GOLD_INGOT, random.nextInt(0, 3));
        if(name.contains("DIAMOND"))
            return new ItemStack(Material.DIAMOND, random.nextInt(0, 2));
        if(name.contains("NETHERITE"))
            return new ItemStack(Material.NETHERITE_SCRAP, random.nextInt(0, 2));

        return base;
    }
}