package fr.dtn.noobland.listener.economy;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.economy.EconomyManager;
import fr.dtn.noobland.feature.fight.FightManager;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener extends Listener{
    public EntityDamageByEntityListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void onEvent(EntityDamageByEntityEvent event){
        FightManager fight = plugin.getFightManager();

        if(event.getEntity() instanceof Player player){
            LivingEntity attacker = (LivingEntity) event.getDamager();
            if(!fight.isInFight(attacker.getUniqueId()))
                attacker.sendMessage(message.get("fight.start"));

            if(!fight.isInFight(player.getUniqueId()))
                player.sendMessage(message.get("fight.start"));

            fight.addEnemy(attacker.getUniqueId(), player.getUniqueId());
            fight.addEnemy(player.getUniqueId(), event.getDamager().getUniqueId());
            return;
        }

        if(!(event.getDamager() instanceof Player player))
            return;
        if(!(event.getEntity() instanceof LivingEntity entity))
            return;
        if(entity.getCustomName() == null || !entity.getCustomName().startsWith("§"))
            return;
        if(entity.getHealth() - event.getFinalDamage() > 0.0)
            return;

        String name = entity.getCustomName();
        int level = Integer.parseInt(name.substring(name.indexOf(". ")+4, name.indexOf(']')-2));

        if(level <= 5)
            return;

        EconomyManager manager = plugin.getEconomyManager();
        double money = manager.getPriceOf(entity.getType(), level);

        if(money == 0)
            return;

        manager.add(player.getUniqueId(), money);
        player.sendMessage(message.get("mob.slay").replace("%money%", String.valueOf(money)));
    }
}