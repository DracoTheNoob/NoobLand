package fr.dtn.noobland.listener.entity;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.mobs.CustomEntity;
import fr.dtn.noobland.feature.mobs.CustomSpider;
import fr.dtn.noobland.feature.mobs.CustomWolf;
import fr.dtn.noobland.feature.mobs.CustomZombie;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnListener extends Listener {
    public EntitySpawnListener(Plugin plugin) { super(plugin); }

    private int randomLevel(){
        int lvl = 1;

        for(int i = 0; i < 19; i++){
            if(Math.random() < .7)
                lvl++;
            else
                break;
        }

        return lvl;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event){
        if(!(event.getEntity() instanceof LivingEntity entity))
            return;
        if(entity.isCustomNameVisible())
            return;

        CustomEntity<?> custom = switch(entity.getType()){
            case WOLF -> new CustomWolf(plugin, (Wolf) entity, randomLevel());
            case ZOMBIE, ZOMBIE_VILLAGER, HUSK -> new CustomZombie((Zombie) entity, randomLevel());
            case SPIDER -> new CustomSpider((Spider) entity, randomLevel());
            default -> null;
        };

        if(custom == null) {
            event.setCancelled(true);
            entity.remove();
            return;
        }

        custom.init();
    }
}