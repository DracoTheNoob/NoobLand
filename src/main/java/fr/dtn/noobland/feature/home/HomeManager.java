package fr.dtn.noobland.feature.home;

import fr.dtn.noobland.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HomeManager {
    public static final List<String> acceptedChars = List.of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".split(""));

    private final File directory;

    public HomeManager(Plugin plugin) {
        this.directory = new File(plugin.getDirectory(), "homes");

        if (!directory.exists() && !directory.mkdir())
            throw new RuntimeException("Unable to create homes directory '" + directory.getPath() + "'");
    }

    public boolean createHome(UUID owner, String name, Location location) {
        if (!canCreateHome(owner))
            return false;

        Home home = new Home(owner, name, location);
        home.save(directory);
        return true;
    }

    public Home getHome(UUID owner, String name) {
        if (!exists(owner, name))
            return null;

        return new Home(directory, owner, name);
    }

    public boolean deleteHome(UUID owner, String name) {
        try {
            Home home = new Home(directory, owner, name);
            home.delete(directory);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean exists(UUID owner, String name) {
        try {
            new Home(directory, owner, name);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public List<String> listHomesName(UUID owner) {
        List<String> homes = new ArrayList<>();

        for (String path : Objects.requireNonNull(this.directory.list()))
            if (path.startsWith(owner.toString()))
                homes.add(path.substring(path.lastIndexOf('_') + 1, path.lastIndexOf('.')));

        return homes;
    }

    public boolean canCreateHome(UUID owner) {
        return listHomesName(owner).size() < 8;
    }

    public Inventory getUi(UUID owner) {
        Inventory ui = Bukkit.createInventory(null, 45, "Choix du home");
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