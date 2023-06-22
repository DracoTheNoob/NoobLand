package fr.dtn.noobland.listener.player;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends Listener {
    public PlayerQuitListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        plugin.getFightManager().removeEntity(player.getUniqueId());
        event.setQuitMessage(message.get("event.quit").replace("%player%", player.getDisplayName()));

        Bukkit.getOnlinePlayers().forEach(plugin::updateTabList);
    }
}