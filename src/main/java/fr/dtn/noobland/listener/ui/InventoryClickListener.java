package fr.dtn.noobland.listener.ui;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.listener.Listener;
import fr.dtn.noobland.uihandler.HomeChooseUiHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener extends Listener {
    public InventoryClickListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event){
        switch(event.getView().getTitle()){
            case "Choix du home":
                event.setCancelled(true);
                new HomeChooseUiHandler().handle(plugin, event);
                break;
        }
    }
}