package fr.dtn.noobland.listener.mob;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.mobs.CustomEntity;
import fr.dtn.noobland.feature.mobs.CustomWolf;
import fr.dtn.noobland.feature.mobs.CustomZombie;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnListener extends Listener {
    public EntitySpawnListener(Plugin plugin) { super(plugin); }

    private int randomLevel(){
        int lvl = 1;

        for(int i = 0; i < 19; i++){
            if(Math.random() < .65)
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

        CustomEntity<?> custom = switch(entity.getType()){
            case WOLF -> new CustomWolf(plugin, (Wolf) entity, randomLevel());
            case ZOMBIE -> new CustomZombie((Zombie) entity, randomLevel());
            default -> null;
        };

        if(custom == null)
            event.setCancelled(true);
        else
            custom.init();
    }
}