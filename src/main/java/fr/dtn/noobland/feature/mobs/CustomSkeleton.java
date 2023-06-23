package fr.dtn.noobland.feature.mobs;

import fr.dtn.noobland.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CustomSkeleton extends CustomEntity<Skeleton> {
    private static final Material[] stuff;
    private static boolean taskRan = false;
    private static final List<UUID> skeletons;

    static{
        skeletons = new ArrayList<>();

        stuff = new Material[]{
                Material.LEATHER_BOOTS,
                Material.LEATHER_HELMET,
                Material.LEATHER_LEGGINGS,
                Material.LEATHER_CHESTPLATE,
                Material.WOODEN_SWORD,
                Material.CHAINMAIL_BOOTS,
                Material.CHAINMAIL_HELMET,
                Material.CHAINMAIL_LEGGINGS,
                Material.CHAINMAIL_CHESTPLATE,
                Material.STONE_SWORD,
                Material.IRON_BOOTS,
                Material.IRON_HELMET,
                Material.IRON_LEGGINGS,
                Material.IRON_CHESTPLATE,
                Material.IRON_SWORD,
                Material.DIAMOND_BOOTS,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_SWORD,
                Material.NETHERITE_BOOTS,
                Material.NETHERITE_HELMET,
                Material.NETHERITE_LEGGINGS,
                Material.NETHERITE_CHESTPLATE,
                Material.NETHERITE_SWORD
        };
    }

    private final Plugin plugin;

    public CustomSkeleton(Plugin plugin, Skeleton skeleton, int level) {
        super(skeleton, level);
        this.plugin = plugin;

        Random random = new Random();
        if(skeleton.getEquipment() == null)
            return;

        skeleton.getEquipment().clear();

        int min = level / 5;
        for(int i = 0; i < random.nextInt(min, 5); i++){
            Material material = stuff[random.nextInt(Math.max(0, level - 8), level + 3)];
            ItemStack item = new ItemStack(material);

            if(material.name().contains("BOOTS"))
                skeleton.getEquipment().setBoots(item);
            else if(material.name().contains("LEGGINGS"))
                skeleton.getEquipment().setLeggings(item);
            else if(material.name().contains("CHESTPLATE"))
                skeleton.getEquipment().setChestplate(item);
            else if(material.name().contains("HELMET"))
                skeleton.getEquipment().setHelmet(item);
            else if(material.name().contains("SWORD"))
                skeleton.getEquipment().setItemInMainHand(item);
        }

        skeletons.add(skeleton.getUniqueId());
    }

    @Override public void init(){
        if(!taskRan){
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                for(UUID uuid : skeletons.toArray(new UUID[0])){
                    Entity entity = plugin.getServer().getEntity(uuid);
                    if(plugin.getServer().getEntity(uuid) == null) {
                        skeletons.remove(uuid);
                        continue;
                    }

                    Skeleton skeleton = (Skeleton) entity;
                    if(skeleton == null)
                        return;

                    List<LivingEntity> targets = new ArrayList<>();
                    skeleton.getWorld().getEntities().forEach(e -> {
                        if(!(e instanceof LivingEntity living))
                            return;

                        if(living instanceof Skeleton)
                            return;

                        if(living.getLocation().distance(skeleton.getLocation()) > 30)
                            return;

                        if(e.getCustomName() != null){
                            String targetName = e.getCustomName();
                            int targetLevel = Integer.parseInt(targetName.substring(targetName.indexOf(". ")+4, targetName.indexOf(']')-2));

                            String skeletonName = skeleton.getCustomName();
                            assert skeletonName != null;
                            int skeletonLevel = Integer.parseInt(skeletonName.substring(skeletonName.indexOf(". ")+4, skeletonName.indexOf(']')-2));

                            if(skeletonLevel <= targetLevel)
                                return;
                        }

                        targets.add(living);
                    });

                    targets.forEach(target -> {
                        if(skeleton.getTarget() != null && skeleton.getServer().getEntity(skeleton.getTarget().getUniqueId()) == null)
                            skeleton.setTarget(null);

                        if(skeleton.getTarget() != null)
                            return;

                        if(target instanceof Player player){
                            if(player.getGameMode() == GameMode.SURVIVAL){
                                skeleton.setTarget(player);
                            }
                        }else{
                            skeleton.setTarget(target);
                        }
                    });
                }
            }, 0, 20);

            taskRan = true;
        }
    }
    @Override protected double calculateHealth(int level) { return (int)Math.pow((level-1), 1.4)+1; }
    @Override protected double calculateAttack(int level) { return (int)Math.pow((level-1), 1.1)+1; }
    @Override protected double calculateSpeed(int level) { return 0.2 + 0.01 * level; }
}