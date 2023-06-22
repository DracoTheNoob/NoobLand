package fr.dtn.noobland.listener.block;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.listener.Listener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class BlockBreakListener extends Listener {
    private static final HashMap<Material, Float> experiences;

    static{
        experiences = new HashMap<>();

        experiences.put(Material.COAL_ORE, 0.1f);
        experiences.put(Material.DEEPSLATE_COAL_ORE, experiences.get(Material.COAL_ORE) * 2);
        experiences.put(Material.COPPER_ORE, 0.12f);
        experiences.put(Material.DEEPSLATE_COPPER_ORE, experiences.get(Material.COPPER_ORE) * 2);
        experiences.put(Material.IRON_ORE, 0.3f);
        experiences.put(Material.DEEPSLATE_IRON_ORE, experiences.get(Material.IRON_ORE) * 2);

        experiences.put(Material.GOLD_ORE, 1.2f);
        experiences.put(Material.DEEPSLATE_GOLD_ORE, experiences.get(Material.GOLD_ORE) * 2);
        experiences.put(Material.LAPIS_ORE, 1f);
        experiences.put(Material.DEEPSLATE_LAPIS_ORE, experiences.get(Material.LAPIS_ORE) * 2);
        experiences.put(Material.REDSTONE_ORE, 0.4f);
        experiences.put(Material.DEEPSLATE_REDSTONE_ORE, experiences.get(Material.REDSTONE_ORE) * 2);
        experiences.put(Material.DIAMOND_ORE, 3f);
        experiences.put(Material.DEEPSLATE_DIAMOND_ORE, experiences.get(Material.DIAMOND_ORE) * 2);
        experiences.put(Material.EMERALD_ORE, 5f);
        experiences.put(Material.DEEPSLATE_EMERALD_ORE, experiences.get(Material.EMERALD_ORE) * 2);

        experiences.put(Material.NETHER_GOLD_ORE, 0.1f);
        experiences.put(Material.NETHER_QUARTZ_ORE, 0.15f);
    }
    public BlockBreakListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(isArtificial(event.getBlock())){
            delete(event.getBlock());
            return;
        }

        Material material = event.getBlock().getType();
        float exp = experiences.getOrDefault(material, -1f);
        if(exp < 0)
            return;

        String msg = message.get("level.exp").replace("%exp%", String.valueOf(exp));
        event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
        plugin.getExperienceManager().addExperience(event.getPlayer().getUniqueId(), exp);
    }

    private boolean isArtificial(Block block){
        Connection connection = plugin.getDbConnection();

        try{
            PreparedStatement query = connection.prepareStatement("SELECT x FROM placed_blocks WHERE (world = ? AND x = ? AND y = ? AND z = ?)");
            query.setString(1, block.getWorld().getUID().toString());
            query.setInt(2, block.getX());
            query.setInt(3, block.getY());
            query.setInt(4, block.getZ());
            ResultSet set = query.executeQuery();

            return set.next();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void delete(Block block){
        Connection connection = plugin.getDbConnection();

        try{
            PreparedStatement query = connection.prepareStatement("DELETE FROM placed_blocks WHERE (world=? AND x=? AND y=? AND z=?)");
            query.setString(1, block.getWorld().getUID().toString());
            query.setInt(2, block.getX());
            query.setInt(3, block.getY());
            query.setInt(4, block.getZ());
            query.execute();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}