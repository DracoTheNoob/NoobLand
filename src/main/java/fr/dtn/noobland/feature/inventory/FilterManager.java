package fr.dtn.noobland.feature.inventory;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.Manager;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FilterManager extends Manager{
    public FilterManager(Plugin plugin) { super(plugin); }

    public List<Filter> getFilters(UUID player){
        List<Filter> filters = new ArrayList<>();
        Connection connection = plugin.getDbConnection();

        try{
            PreparedStatement query = connection.prepareStatement("SELECT filter FROM filters WHERE id = ?");
            query.setString(1, player.toString());
            ResultSet set = query.executeQuery();

            while(set.next()){
                String filter = set.getString("filter");
                int index = filter.indexOf('-');
                FilterType type = FilterType.valueOf(filter.substring(0, index));
                String target = filter.substring(index + 1);

                filters.add(new Filter(type, target));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return filters;
    }

    public List<Filter> getFilters(UUID player, FilterType requiredType){
        List<Filter> filters = new ArrayList<>();
        Connection connection = plugin.getDbConnection();

        try{
            PreparedStatement query = connection.prepareStatement("SELECT filter FROM filters WHERE id = ?");
            query.setString(1, player.toString());
            ResultSet set = query.executeQuery();

            while(set.next()){
                String filter = set.getString("filter");
                int index = filter.indexOf('-');
                FilterType type = FilterType.valueOf(filter.substring(0, index));

                if(type != requiredType)
                    continue;

                String target = filter.substring(index + 1);
                filters.add(new Filter(type, target));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return filters;
    }

    private boolean delete(UUID player, Filter filter){
        Connection connection = plugin.getDbConnection();

        try{
            PreparedStatement query = connection.prepareStatement("DELETE FROM filters WHERE (id = ? AND filter = ?)");
            query.setString(1, player.toString());
            query.setString(2, filter.toSql());
            return query.executeUpdate() != 0;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void save(UUID player, Filter filter){
        delete(player, filter);
        Connection connection = plugin.getDbConnection();

        try{
            PreparedStatement query = connection.prepareStatement("INSERT INTO filters VALUES (?, ?)");
            query.setString(1, player.toString());
            query.setString(2, filter.toSql());
            query.execute();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void addFilter(UUID player, Filter filter){
        save(player, filter);
    }

    public boolean removeFilter(UUID player, Filter filter){
        return delete(player, filter);
    }

    public void clearFilters(UUID player){
        getFilters(player).forEach(filter -> removeFilter(player, filter));
    }

    public void clearFilters(UUID player, FilterType type){
        getFilters(player, type).forEach(filter -> removeFilter(player, filter));
    }

    public boolean accept(UUID player, ItemStack stack){
        for(Filter filter : getFilters(player))
            if(filter.accept(stack))
                return false;

        return true;
    }
}