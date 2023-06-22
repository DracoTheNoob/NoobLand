package fr.dtn.noobland.feature.mobs;

import fr.dtn.noobland.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomWolf extends CustomEntity<Wolf>{
    private static final List<UUID> wolfs = new ArrayList<>();
    private static boolean taskRan = false;
    private final Plugin plugin;

    public CustomWolf(Plugin plugin, Wolf wolf, int level) {
        super(wolf, level);
        this.plugin = plugin;
        wolf.setAngry(level > 5);

        if(wolf.isAngry())
            wolfs.add(wolf.getUniqueId());
    }

    @Override
    public void init() {
        if(!taskRan){
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                for(UUID uuid : wolfs.toArray(new UUID[0])){
                    Entity entity = plugin.getServer().getEntity(uuid);
                    if(plugin.getServer().getEntity(uuid) == null) {
                        wolfs.remove(uuid);
                        continue;
                    }

                    Wolf wolf = (Wolf) entity;
                    if(wolf == null)
                        return;

                    wolf.setAngry(true);
                    if(wolf.getTarget() != null)
                        continue;

                    Location l = wolf.getLocation();
                    wolf.getNearbyEntities(l.getX(), l.getY(), l.getZ()).forEach(target -> {
                        if(target instanceof Player player && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE))
                            wolf.setTarget(player);
                    });
                }
            }, 0, 20);

            taskRan = true;
        }
    }



    @Override protected double calculateHealth(int level) { return (int)(Math.pow(1.25, level-1) + level); }
    @Override protected double calculateAttack(int level) { return (int)(Math.pow(1.18, level-1) + level / 2.0); }
    @Override protected double calculateSpeed(int level) { return 0.2 + 0.01 * level; }
}