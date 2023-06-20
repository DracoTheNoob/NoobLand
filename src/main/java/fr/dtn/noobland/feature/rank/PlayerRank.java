package fr.dtn.noobland.feature.rank;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerRank {
    private final UUID owner;
    private final Rank rank;

    public PlayerRank(UUID owner, Rank rank){
        this.owner = owner;
        this.rank = rank;
    }

    public PlayerRank(Connection connection, UUID owner){
        this.owner = owner;

        String id;

        try{
            PreparedStatement query = connection.prepareStatement("SELECT player_rank FROM ranks WHERE id = ?");
            query.setString(1, owner.toString());
            ResultSet set = query.executeQuery();

            if(!set.next())
                throw new IllegalArgumentException();

            id = set.getString("player_rank");
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        this.rank = new Rank(id);
    }

    public void save(Connection connection){
        try{
            delete(connection);

            PreparedStatement query = connection.prepareStatement("INSERT INTO ranks VALUES (?, ?)");
            query.setString(1, owner.toString());
            query.setString(2, rank.getId());
            query.execute();
        }catch(SQLException e){
            throw new RuntimeException();
        }
    }

    public void delete(Connection connection) throws SQLException{
        PreparedStatement query = connection.prepareStatement("DELETE FROM ranks WHERE id = ?");
        query.setString(1, owner.toString());
        query.execute();
    }

    public Rank getRank(){ return rank; }

    private static File getFile(File directory, UUID owner){ return new File(directory, owner.toString() + ".rank"); }
}