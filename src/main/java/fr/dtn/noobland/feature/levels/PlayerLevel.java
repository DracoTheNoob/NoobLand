package fr.dtn.noobland.feature.levels;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerLevel {
    private final UUID player;
    private int level;
    private float experience;

    public PlayerLevel(Connection connection, UUID player){
        this.player = player;

        try{
            PreparedStatement query = connection.prepareStatement("SELECT * FROM levels WHERE id = ?");
            query.setString(1, player.toString());
            ResultSet set = query.executeQuery();

            if(!set.next()) {
                this.level = 0;
                this.experience = 0;
                save(connection);
                return;
            }

            this.level = set.getInt("level");
            this.experience = set.getFloat("experience");
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    void save(Connection connection){
        try{
            delete(connection);

            PreparedStatement query = connection.prepareStatement("INSERT INTO levels VALUES (?, ?, ?)");
            query.setString(1, player.toString());
            query.setInt(2, level);
            query.setFloat(3, experience);
            query.execute();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    void delete(Connection connection) throws SQLException{
        PreparedStatement query = connection.prepareStatement("DELETE FROM levels WHERE id = ?");
        query.setString(1, player.toString());
        query.execute();
    }

    public int addExperience(Connection connection, float experience){
        this.experience += experience;

        int levels = 0;
        while(this.experience >= getExperienceFor(level+1)){
            this.experience -= getExperienceFor(level+1);
            level++;
            levels++;
        }

        this.experience = (int)(1000 * this.experience) / 1000f;
        save(connection);
        return levels;
    }

    public float getExperienceFor(int level){ return (int)(Math.pow(2 * level, 1 + level * 0.01) * 100) / 100f; }

    public UUID getPlayer() { return player; }
    public int getLevel() { return level; }
    public float getExperience() { return experience; }
}