package fr.dtn.noobland.feature.economy;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EconomyManager extends Manager {
    private static final HashMap<EntityType, Double> prices;
    private static final ItemStack previousPage;
    private static final ItemStack nextPage;

    static{
        prices = new HashMap<>();

        prices.put(EntityType.WOLF, 1.2);
        prices.put(EntityType.ZOMBIE, 0.8);
        prices.put(EntityType.SPIDER, 0.7);

        previousPage = new ItemStack(Material.ARROW);
        ItemMeta previousMeta = previousPage.getItemMeta();
        assert previousMeta != null;
        previousMeta.setDisplayName("§9<-");
        previousPage.setItemMeta(previousMeta);

        nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextPage.getItemMeta();
        assert nextMeta != null;
        nextMeta.setDisplayName("§9->");
        nextPage.setItemMeta(nextMeta);
    }

    public EconomyManager(Plugin plugin){ super(plugin); }

    public double getPriceOf(EntityType type, int level){
        if(prices.get(type) == null)
            return 0;

        double priceModifier = prices.get(type);
        return (int)(Math.pow(1.2, level - 9) * priceModifier * 100) / 100.0;
    }

    public double getBalance(UUID owner){
        Wallet wallet = new Wallet(plugin.getDbConnection(), owner);
        return wallet.getBalance();
    }

    public boolean pay(UUID payer, UUID payed, double amount){
        Wallet payedWallet = new Wallet(plugin.getDbConnection(), payed);

        if(payer == null){
            payedWallet.add(plugin.getDbConnection(), amount);
            plugin.getScoreboardManager().updateScoreboard(payed);
            return true;
        }

        Wallet payerWallet = new Wallet(plugin.getDbConnection(), payer);
        if(payerWallet.getBalance() < amount)
            return false;

        payerWallet.subtract(plugin.getDbConnection(), amount);
        payedWallet.add(plugin.getDbConnection(), amount);

        plugin.getScoreboardManager().updateScoreboard(payer);
        plugin.getScoreboardManager().updateScoreboard(payed);

        return true;
    }

    public void add(UUID owner, double amount){
        Wallet wallet = new Wallet(plugin.getDbConnection(), owner);
        wallet.add(plugin.getDbConnection(), amount);
        plugin.getScoreboardManager().updateScoreboard(owner);
    }

    public void subtract(UUID owner, double amount){
        Wallet wallet = new Wallet(plugin.getDbConnection(), owner);
        wallet.subtract(plugin.getDbConnection(), amount);
        plugin.getScoreboardManager().updateScoreboard(owner);
    }

    public int maxPages(Connection connection){
        for(int i = 1; i < Integer.MAX_VALUE; i++){
            try{
                PreparedStatement query = connection.prepareStatement("SELECT id FROM market WHERE id BETWEEN ? AND ?");
                query.setInt(1, (i-1)*36);
                query.setInt(2, i*36-1);
                ResultSet set = query.executeQuery();

                if(!set.next())
                    return i-1;
            }catch(SQLException e){
                throw new RuntimeException(e);
            }
        }

        return -1;
    }

    public Inventory getShopUI(int page){
        Inventory inventory = Bukkit.createInventory(null, 54, "§cMarket (" + page + ")");
        Connection connection = plugin.getDbConnection();
        List<Material> materials = new ArrayList<>();
        List<Double> prices = new ArrayList<>();

        try{
            PreparedStatement query = connection.prepareStatement("SELECT material,price FROM market WHERE id BETWEEN ? AND ?");
            query.setInt(1, (page-1)*36);
            query.setInt(2, (page)*36-1);
            ResultSet set = query.executeQuery();

            while(set.next()){
                materials.add(Material.valueOf(set.getString("material")));
                prices.add(set.getDouble("price"));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        int id = 0;

        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 6; y++) {
                int loc = x + y * 9;

                if(x == 3 && y == 5 && page != 1){
                    inventory.setItem(loc, previousPage);
                }else if(x == 5 && y == 5 && page != maxPages(connection)){
                    inventory.setItem(loc, nextPage);
                }else if(y == 0 || y == 5){
                    inventory.setItem(loc, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                }else{
                    try{
                        inventory.setItem(loc, profileOf(materials.get(id), prices.get(id)));
                        id++;
                    }catch(IndexOutOfBoundsException ignored){}
                }
            }
        }

        return inventory;
    }

    private ItemStack profileOf(Material material, double price){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        String name = material.name();
        meta.setDisplayName("§6" + name.replace(name.substring(1), name.substring(1).toLowerCase()).replace("_", " "));

        meta.setLore(List.of("§7Prix 1: §c"+price+" $", "§7Prix 64: §c"+(price * 64)+" $", "§2Acheter: ", "§7Clique gauche: §21", "§7Shift Clique gauche: §c64"));
        item.setItemMeta(meta);

        return item;
    }

    public double getPriceOf(Material material){
        try{
            Connection connection = plugin.getDbConnection();
            PreparedStatement query = connection.prepareStatement("SELECT price FROM market WHERE material = ?");
            query.setString(1, material.name());
            ResultSet set = query.executeQuery();

            if(set.next())
                return set.getDouble("price");
            else
                return -1;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}