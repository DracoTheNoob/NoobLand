package fr.dtn.noobland.feature.home;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Home {
    private final UUID owner;
    private final String name;
    private final Location location;

    public Home(UUID owner, String name, Location location){
        this.owner = owner;
        this.name = name;
        this.location = location;
    }

    public Home(Connection connection, UUID owner, String name){
        this.owner = owner;
        this.name = name;

        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM homes WHERE id = ?");
            query.setString(1, owner + "/" + name);
            ResultSet set = query.executeQuery();

            if(!set.next())
                throw new IllegalArgumentException();

            World world = Objects.requireNonNull(Bukkit.getWorld(UUID.fromString(set.getString("world"))));
            double x = set.getDouble("x");
            double y = set.getDouble("y");
            double z = set.getDouble("z");
            float yaw = set.getFloat("yaw");
            float pitch = set.getFloat("pitch");

            this.location = new Location(world, x, y, z, yaw, pitch);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Connection connection){
        try{
            delete(connection);

            PreparedStatement query = connection.prepareStatement("INSERT INTO homes VALUES (?, ?, ?, ?, ?, ?, ?)");
            query.setString(1, owner + "/" + name);
            query.setString(2, Objects.requireNonNull(location.getWorld()).getUID().toString());
            query.setDouble(3, location.getX());
            query.setDouble(4, location.getY());
            query.setDouble(5, location.getZ());
            query.setFloat(6, location.getYaw());
            query.setFloat(7, location.getPitch());
            query.execute();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void delete(Connection connection){
        try{
            PreparedStatement query = connection.prepareStatement("DELETE FROM homes WHERE id = ?");
            query.setString(1, owner + "/" + name);
            query.execute();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public ItemStack getIcon(){
        Material material = switch(Objects.requireNonNull(location.getWorld()).getEnvironment()){
            case NORMAL -> Material.GRASS_BLOCK;
            case NETHER -> Material.NETHERRACK;
            case THE_END -> Material.END_STONE;
            default -> Material.BEDROCK;
        };

        String nameColor = switch(material){
            case GRASS_BLOCK -> "§2";
            case NETHERRACK -> "§4";
            case END_STONE -> "§e";
            default -> "§0";
        };

        List<String> lore = new ArrayList<>();
        lore.add("§7(§6x§7;§6y§7;§6z§7)"
                        .replace("x", String.valueOf((int)location.getX()))
                        .replace("y", String.valueOf((int)location.getY()))
                        .replace("z", String.valueOf((int)location.getZ())));
        lore.add("§cShift §7+ §cClique droit §7-> §cSupprimer");
        lore.add("§2Clique gauche §7 -> §2Utiliser");

        ItemStack icon = new ItemStack(material);
        ItemMeta meta = icon.getItemMeta();
        assert meta != null;

        meta.setDisplayName(nameColor + name);
        meta.setLore(lore);

        icon.setItemMeta(meta);
        return icon;
    }

    private static File getFile(File directory, UUID owner, String name){ return new File(directory, owner + "_" + name + ".yml"); }

    public UUID getOwner() { return owner; }
    public String getName() { return name; }
    public Location getLocation() { return location; }
}