package fr.dtn.noobland.uihandler;

import fr.dtn.noobland.Plugin;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface UiHandler{
    void handle(Plugin plugin, InventoryClickEvent event);
}