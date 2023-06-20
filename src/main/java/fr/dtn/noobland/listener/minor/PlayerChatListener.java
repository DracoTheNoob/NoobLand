package fr.dtn.noobland.listener.minor;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.rank.Rank;
import fr.dtn.noobland.feature.rank.RankManager;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener extends Listener{
    public PlayerChatListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        RankManager manager = plugin.getRankManager();
        Player player = event.getPlayer();
        Rank rank = manager.getRank(player.getUniqueId());
        event.setCancelled(true);
        Bukkit.broadcastMessage(rank.getDisplay().substring(0, rank.getDisplay().lastIndexOf(" ")-1) + "[§6" + player.getDisplayName() + "§7]: §f" + event.getMessage());
    }
}