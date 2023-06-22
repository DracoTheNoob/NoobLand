package fr.dtn.noobland.listener.player;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.economy.EconomyManager;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Objects;

public class PlayerDeathListener extends Listener{
    public PlayerDeathListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        event.getDrops().clear();
        event.setDroppedExp(0);
        event.setDeathMessage("");

        Player player = event.getEntity();
        player.setGameMode(GameMode.SPECTATOR);

        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
        player.getInventory().forEach(item -> {
            if(item != null && item.getType() != Material.AIR)
                player.getWorld().dropItem(player.getLocation(), item);
        });

        player.teleport(Objects.requireNonNull(plugin.getServer().getWorld("world")).getSpawnLocation());
        player.setGameMode(GameMode.SURVIVAL);

        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);

        plugin.getFightManager().removeEntity(player.getUniqueId());
        Player killer = player.getKiller();
        if(killer == null)
            return;

        EconomyManager economy = plugin.getEconomyManager();
        double money = (int)(economy.getBalance(player.getUniqueId()) * 5) / 100.0;
        economy.pay(player.getUniqueId(), killer.getUniqueId(), money);

        player.sendMessage(message.get("kill.stiller").replace("%player%", killer.getName()).replace("%money%", String.valueOf(money)));
        killer.sendMessage(message.get("kill.stole").replace("%player%", player.getName()).replace("%money%", String.valueOf(money)));

        event.setDeathMessage(message.get("kill.message").replace("%killer%", killer.getName()).replace("%killed%", player.getName()));
    }
}