package fr.dtn.noobland.uihandler;

import fr.dtn.noobland.Plugin;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface UiHandler{
    void handle(Plugin plugin, InventoryClickEvent event, @NotNull ItemStack clicked);
}