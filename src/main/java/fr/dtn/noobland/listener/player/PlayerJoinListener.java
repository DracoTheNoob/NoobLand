package fr.dtn.noobland.listener.player;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends Listener {
    public PlayerJoinListener(Plugin plugin){
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(player.getName().length() > 20){
            player.kickPlayer("Your username length is greater than 20 chars");
            event.setJoinMessage(null);
            return;
        }

        Bukkit.getOnlinePlayers().forEach(plugin::updateTabList);
        event.setJoinMessage(message.get("event.join").replace("%player%", player.getDisplayName()));

        plugin.getScoreboardManager().updateScoreboard(player);
        plugin.getExperienceManager().applyEffects(player, false);
    }
}