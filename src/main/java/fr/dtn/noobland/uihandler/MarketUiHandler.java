package fr.dtn.noobland.uihandler;

import fr.dtn.noobland.MessageManager;
import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.economy.EconomyManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MarketUiHandler implements UiHandler{
    @Override
    public void handle(Plugin plugin, InventoryClickEvent event, @NotNull ItemStack clicked) {
        if(clicked.getType() == Material.GRAY_STAINED_GLASS_PANE)
            return;

        if(event.getClick() != ClickType.LEFT && event.getClick() != ClickType.SHIFT_LEFT)
            return;
        
        String title = event.getView().getTitle();
        int page = Integer.parseInt(title.substring(title.indexOf('(')+1, title.indexOf(')')));

        EconomyManager economy = plugin.getEconomyManager();
        
        if(clicked.getType() == Material.ARROW){
            int change = Objects.requireNonNull(clicked.getItemMeta()).getDisplayName().contains("<-") ? -1 : 1;
            
            event.getWhoClicked().openInventory(economy.getShopUI(page + change));
            return;
        }
        
        Material product = clicked.getType();
        double price = economy.getPriceOf(product);

        Player player = (Player)event.getWhoClicked();
        MessageManager message = plugin.getMessageManager();

        int amount = event.getClick() == ClickType.SHIFT_LEFT ? 64 : 1;

        if(economy.getBalance(player.getUniqueId()) < price * amount){
            player.sendMessage(message.get("economy.exceed"));
            return;
        }

        economy.subtract(player.getUniqueId(), price * amount);
        String name = Objects.requireNonNull(clicked.getItemMeta()).getDisplayName().replace("§6", "");

        ItemStack stack = new ItemStack(product, amount);
        player.sendMessage(message.get("economy.market_buy").replace("%item%", name).replace("%amount%", String.valueOf(amount)));

        if(player.getInventory().first(new ItemStack(product, amount)) == -1 && player.getInventory().firstEmpty() == -1){
            player.getWorld().dropItem(player.getLocation(), stack);
            player.sendMessage(message.get("economy.market_drop").replace("%item%", name));
            return;
        }

        player.getInventory().addItem(stack);
    }
}