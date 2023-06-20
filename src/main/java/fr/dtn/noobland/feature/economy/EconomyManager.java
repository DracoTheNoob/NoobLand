package fr.dtn.noobland.feature.economy;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class EconomyManager {
    private static final HashMap<EntityType, Double> prices;

    static{
        prices = new HashMap<>();

        prices.put(EntityType.WOLF, 0.008);
        prices.put(EntityType.ZOMBIE, 0.005);
    }

    private final HashMap<UUID, Wallet> wallets;
    private final Plugin plugin;

    public EconomyManager(Plugin plugin){
        this.wallets = new HashMap<>();
        this.plugin = plugin;

        for(Player player : Bukkit.getOnlinePlayers()) {
            wallets.put(player.getUniqueId(), new Wallet(plugin.getDbConnection(), player.getUniqueId()));
            wallets.get(player.getUniqueId()).save(plugin.getDbConnection());
            updateTabList(player.getUniqueId());
        }
    }

    public double getPriceOf(EntityType type, int level){
        if(prices.get(type) == null)
            return 0;

        double price = prices.get(type);
        return price * (level - 5);
    }

    public void register(UUID owner){
        Wallet wallet = new Wallet(plugin.getDbConnection(), owner);
        wallet.save(plugin.getDbConnection());
        wallets.put(owner, wallet);
    }
    public void logout(UUID owner){ wallets.remove(owner); }
    public double getBalance(UUID owner){
        Wallet wallet = wallets.get(owner);

        if(wallet == null){
            register(owner);
            return getBalance(owner);
        }

        return wallet.getBalance();
    }

    public boolean pay(UUID payer, UUID payed, double amount){
        Wallet payedWallet = wallets.get(payed);

        if(payer == null){
            payedWallet.add(amount);
            payedWallet.save(plugin.getDbConnection());

            updateTabList(payed);
            return true;
        }

        Wallet payerWallet = wallets.get(payer);

        if(payerWallet.getBalance() < amount)
            return false;

        payerWallet.subtract(amount);
        payedWallet.add(amount);

        payerWallet.save(plugin.getDbConnection());
        payedWallet.save(plugin.getDbConnection());

        updateTabList(payer);
        updateTabList(payed);

        return true;
    }

    public void add(UUID owner, double amount){
        Wallet wallet = wallets.get(owner);
        wallet.add(amount);
        wallet.save(plugin.getDbConnection());

        updateTabList(wallet.getOwner());
    }

    public void subtract(UUID owner, double amount){
        Wallet wallet = wallets.get(owner);
        wallet.subtract(amount);
        wallet.save(plugin.getDbConnection());

        updateTabList(wallet.getOwner());
    }

    void updateTabList(UUID uuid){
        Player player = Bukkit.getPlayer(uuid);

        if(player == null)
            return;

        RankManager manager = plugin.getRankManager();

        player.setPlayerListHeader("§6Noob Land\n");
        player.setPlayerListName(manager.getRank(player.getUniqueId()).getDisplay() + "§6" + player.getDisplayName() + "§7 - §6" + (int)getBalance(player.getUniqueId()) + " $");
        player.setPlayerListFooter("\n§c" + Bukkit.getOnlinePlayers().size() + " §7/ §c" + Bukkit.getMaxPlayers());
    }
}