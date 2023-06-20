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

    public Home(Player owner, String name){ this(owner.getUniqueId(), name, owner.getLocation()); }

    public Home(File directory, UUID owner, String name){
        File file = getFile(directory, owner, name);
        YamlConfiguration home = YamlConfiguration.loadConfiguration(file);

        World world = Bukkit.getWorld(Objects.requireNonNull(home.getString("world")));
        double x = home.getDouble("x");
        double y = home.getDouble("y");
        double z = home.getDouble("z");
        float yaw = (float) home.getDouble("yaw");
        float pitch = (float) home.getDouble("pitch");

        this.owner = owner;
        this.name = name;
        this.location = new Location(world, x, y, z, yaw, pitch);
    }

    public void save(File directory){
        File file = getFile(directory, owner, name);
        YamlConfiguration home = YamlConfiguration.loadConfiguration(file);

        home.set("world", Objects.requireNonNull(location.getWorld()).getName());
        home.set("x", location.getX());
        home.set("y", location.getY());
        home.set("z", location.getZ());
        home.set("yaw", location.getYaw());
        home.set("pitch", location.getPitch());

        try {
            home.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(File directory){
        File home = getFile(directory, owner, name);

        if(!home.delete())
            throw new RuntimeException("Unable to delete home file '" + home.getPath() + "'");
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