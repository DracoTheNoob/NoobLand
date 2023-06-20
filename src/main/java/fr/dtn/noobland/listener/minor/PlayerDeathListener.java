package fr.dtn.noobland.listener.minor;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.economy.EconomyManager;
import fr.dtn.noobland.listener.Listener;
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
        Player player = event.getEntity();

        player.getInventory().forEach(item -> {
            if(item != null && item.getType() != Material.AIR)
                player.getWorld().dropItem(player.getLocation(), item);
        });

        player.setLevel(Math.max(0, player.getLevel() - 5));
        player.setExp(0);
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
        player.teleport(Objects.requireNonNull(plugin.getServer().getWorld("world")).getSpawnLocation());
        player.getInventory().clear();
        
        event.setKeepLevel(true);
        event.getDrops().clear();

        plugin.getFightManager().clearPlayer(player.getUniqueId());

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