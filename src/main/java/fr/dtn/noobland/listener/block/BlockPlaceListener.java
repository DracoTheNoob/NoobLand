package fr.dtn.noobland.listener.block;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.listener.Listener;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BlockPlaceListener extends Listener {
    public BlockPlaceListener(Plugin plugin) { super(plugin); }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Connection connection = plugin.getDbConnection();
        Block block = event.getBlock();

        try{
            PreparedStatement query = connection.prepareStatement("INSERT INTO placed_blocks VALUES (?, ?, ?, ?)");
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