package fr.dtn.noobland.listener.ui;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.listener.Listener;
import fr.dtn.noobland.uihandler.HomeChooseUiHandler;
import fr.dtn.noobland.uihandler.MarketUiHandler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener extends Listener {
    public InventoryClickListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event){
        ItemStack clicked = event.getCurrentItem();

        if(clicked == null || clicked.getType() == Material.AIR)
            return;

        String title = event.getView().getTitle();

        if(title.equals("§cChoix du home")){
            event.setCancelled(true);
            new HomeChooseUiHandler().handle(plugin, event, clicked);
        }else if(title.contains("§cMarket")){
            event.setCancelled(true);
            new MarketUiHandler().handle(plugin, event, clicked);
        }
    }
}