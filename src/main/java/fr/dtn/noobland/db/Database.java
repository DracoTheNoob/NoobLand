package fr.dtn.noobland.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class Database {
    private static final String host, user, password, database;

    static{
        File auth = new File("C:/noobland/db.auth");

        try(FileReader read = new FileReader(auth); BufferedReader reader = new BufferedReader(read)){
            host = reader.readLine();
            user = reader.readLine();
            password = reader.readLine();
            database = reader.readLine();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    private final Connection connection;

    public Database(){
        this.connection = new Connection(host, user, password, database);
        java.sql.Connection c = connection.getConnection();

        try {
            c.prepareStatement("CREATE TABLE IF NOT EXISTS homes(id VARCHAR(56) PRIMARY KEY, world VARCHAR(36), x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT)")
                    .execute();
            c.prepareStatement("CREATE TABLE IF NOT EXISTS ranks(id VARCHAR(56) PRIMARY KEY, player_rank VARCHAR(20))")
                    .execute();
            c.prepareStatement("CREATE TABLE IF NOT EXISTS economy(id VARCHAR(36) PRIMARY KEY, balance DOUBLE)")
                    .execute();
            c.prepareStatement("CREATE TABLE IF NOT EXISTS levels(id VARCHAR(36) PRIMARY KEY, level INT, experience FLOAT)")
                    .execute();
            c.prepareStatement("CREATE TABLE IF NOT EXISTS placed_blocks(world VARCHAR(36), x INT, y INT, z INT)")
                    .execute();
            c.prepareStatement("CREATE TABLE IF NOT EXISTS market(id INT PRIMARY KEY, material VARCHAR(56), price DOUBLE)")
                    .execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        this.connection.close();
    }

    public java.sql.Connection getConnection(){ return connection.getConnection(); }
}