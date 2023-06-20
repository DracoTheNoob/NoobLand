package fr.dtn.noobland.listener.minor;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.economy.EconomyManager;
import fr.dtn.noobland.feature.rank.RankManager;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends Listener {
    public PlayerJoinListener(Plugin plugin){ super(plugin); }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(player.getName().length() > 15){
            player.kickPlayer("Your username length is greater than 20 chars");
            event.setJoinMessage(null);
            return;
        }

        RankManager rank = plugin.getRankManager();
        EconomyManager economy = plugin.getEconomyManager();

        plugin.getEconomyManager().register(player.getUniqueId());
        player.setPlayerListHeader("§6Noob Land\n");
        player.setPlayerListName(rank.getRank(player.getUniqueId()).getDisplay() + "§6" + player.getDisplayName() + "§7 - §6" + (int)economy.getBalance(player.getUniqueId()) + " $");
        player.setPlayerListFooter("\n§c" + Bukkit.getOnlinePlayers().size() + " §7/ §c" + Bukkit.getMaxPlayers());
        event.setJoinMessage(message.get("event.join").replace("%player%", player.getDisplayName()));
    }
}