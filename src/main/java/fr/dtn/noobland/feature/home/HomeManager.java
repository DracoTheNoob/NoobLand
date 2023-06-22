package fr.dtn.noobland.feature.home;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeManager extends Manager {
    public static final List<String> acceptedChars = List.of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".split(""));

    public HomeManager(Plugin plugin) { super(plugin); }

    public boolean createHome(UUID owner, String name, Location location) {
        if (!canCreateHome(owner))
            return false;

        new Home(plugin.getDbConnection(), owner, name, location);
        plugin.getScoreboardManager().updateScoreboard(owner);
        return true;
    }

    public Home getHome(UUID owner, String name) {
        if (!exists(owner, name))
            return null;

        return new Home(plugin.getDbConnection(), owner, name);
    }

    public boolean deleteHome(UUID owner, String name) {
        try {
            new Home(plugin.getDbConnection(), owner, name).delete(plugin.getDbConnection());
            plugin.getScoreboardManager().updateScoreboard(owner);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean exists(UUID owner, String name) {
        try {
            new Home(plugin.getDbConnection(), owner, name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public List<String> listHomesName(UUID owner) {
        List<String> homes = new ArrayList<>();

        try{
            Connection connection = plugin.getDbConnection();
            PreparedStatement query = connection.prepareStatement("SELECT id FROM homes WHERE id LIKE ?");
            query.setString(1, owner.toString() + "%");
            ResultSet set = query.executeQuery();

            while(set.next())
                homes.add(set.getString("id").substring(set.getString("id").lastIndexOf('/')+1));
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return homes;
    }

    public boolean canCreateHome(UUID owner) {
        return listHomesName(owner).size() < 8;
    }

    public Inventory getHomesUI(UUID owner) {
        Inventory ui = Bukkit.createInventory(null, 45, "§cChoix du home");
        List<String> homes = listHomesName(owner);

        if (homes.size() == 0)
            return null;

        for (int i = 0; i < homes.size(); i++) {
            Home home = getHome(owner, homes.get(i));
            ui.setItem((i > 3) ? (28 + (2 * (i - 4))) : (10 + 2 * i), home.getIcon());
        }

        return ui;
    }
}