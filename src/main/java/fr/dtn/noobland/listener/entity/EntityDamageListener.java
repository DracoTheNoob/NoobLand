package fr.dtn.noobland.listener.entity;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener extends Listener{
    public EntityDamageListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent event){
        Entity e = event.getEntity();

        if(e.getCustomName() == null)
            return;
        if(!e.isCustomNameVisible())
            return;
        if(!e.getCustomName().startsWith("§"))
            return;
        if(!(e instanceof LivingEntity entity))
            return;

        String name = entity.getCustomName();
        String newName = name.substring(0, name.lastIndexOf(']')+2) + (int)(entity.getHealth() - event.getFinalDamage() + 1) + "§4❤";
        entity.setCustomName(newName);
    }
}