package fr.dtn.noobland.listener.entity;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.economy.EconomyManager;
import fr.dtn.noobland.feature.fight.FightManager;
import fr.dtn.noobland.feature.experience.ExperienceManager;
import fr.dtn.noobland.listener.Listener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityDamageByEntityListener extends Listener {
    public EntityDamageByEntityListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void onEvent(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof LivingEntity victim))
            return;
        if(!(event.getDamager() instanceof LivingEntity attacker))
            return;

        FightManager fight = plugin.getFightManager();
        if(victim.getHealth() - event.getFinalDamage() > 0){
            fight.addEntity(victim.getUniqueId());
            fight.addEntity(attacker.getUniqueId());
        }else{
            fight.removeEntity(victim.getUniqueId());
            fight.removeEntity(attacker.getUniqueId());
        }

        plugin.getScoreboardManager().updateScoreboard(victim.getUniqueId());
        plugin.getScoreboardManager().updateScoreboard(attacker.getUniqueId());

        if(attacker.getType() == EntityType.SPIDER){
            String name = attacker.getCustomName();
            assert name != null;
            int level = Integer.parseInt(name.substring(name.indexOf(". ")+4, name.indexOf(']')-2));

            if(level < 5) {
                return;
            }else if(level < 12) {
                victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (level - 5) * 40, (level - 5) / 4, true, true));
            }else if(level < 20) {
                victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, level * 10, (level - 12) / 5, true, true));
            }else{
                victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1, true, true));
                victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 1, true, true));
            }
        }

        if(!(attacker instanceof Player player))
            return;
        if(victim.getHealth() - event.getFinalDamage() > 0.0)
            return;
        if(victim.getCustomName() == null || !victim.getCustomName().startsWith("§"))
            return;

        String name = victim.getCustomName();
        int level = Integer.parseInt(name.substring(name.indexOf(". ")+4, name.indexOf(']')-2));

        ExperienceManager experience = plugin.getExperienceManager();
        float earning = experience.getExperienceKilling(level);
        experience.addExperience(player.getUniqueId(), earning);

        EconomyManager manager = plugin.getEconomyManager();
        double money = manager.getPriceOf(victim.getType(), level);

        manager.add(player.getUniqueId(), money);
        String msg = message.get("mob.slay").replace("%money%", String.valueOf(money)).replace("%exp%", String.valueOf(earning));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }
}