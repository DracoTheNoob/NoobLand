package fr.dtn.noobland.uihandler;

import fr.dtn.noobland.MessageManager;
import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.home.Home;
import fr.dtn.noobland.feature.home.HomeManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class HomeChooseUiHandler implements UiHandler{
    @Override
    public void handle(Plugin plugin, InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();

        if(clicked == null || clicked.getType() == Material.AIR)
            return;

        MessageManager message = plugin.getMessageManager();
        HomeManager manager = plugin.getHomeManager();
        String homeName = Objects.requireNonNull(clicked.getItemMeta()).getDisplayName().substring(2);
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        if(event.getClick() == ClickType.LEFT){
            Home home = manager.getHome(player.getUniqueId(), homeName);
            player.closeInventory();
            player.sendMessage(message.get("home.use").replace("%arg%", homeName));

            if(plugin.getFightManager().isInFight(player.getUniqueId()))
                player.sendMessage(message.get("fight.block_tp"));
            else
                player.teleport(home.getLocation());
        }else if(event.getClick() == ClickType.SHIFT_RIGHT){
            manager.deleteHome(uuid, homeName);
            player.sendMessage(message.get("home.delete").replace("%home%", homeName));

            if(manager.listHomesName(uuid).size() != 0)
                player.openInventory(manager.getUi(uuid));
            else
                player.closeInventory();
        }
    }
}