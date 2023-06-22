package fr.dtn.noobland.listener.entity;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class EntityPickupItemListener extends Listener{
    public EntityPickupItemListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event){
        if(!(event.getEntity() instanceof Player player))
            return;

        if(plugin.getFilterManager().accept(player.getUniqueId(), event.getItem().getItemStack()))
            return;

        event.setCancelled(true);
    }
}