package fr.dtn.noobland.feature.economy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Wallet {
    private final UUID owner;
    private double balance;

    public Wallet(Connection connection, UUID owner){
        this.owner = owner;

        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM economy WHERE id = ?");
            query.setString(1, owner.toString());
            ResultSet set = query.executeQuery();

            if(set.next()){
                this.balance = set.getDouble("balance");
            }else{
                this.balance = 100;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.save(connection);
    }

    public void save(Connection connection){
        try{
            delete(connection);

            PreparedStatement query = connection.prepareStatement("INSERT INTO economy VALUES(?, ?)");
            query.setString(1, owner.toString());
            query.setDouble(2, balance);
            query.execute();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void delete(Connection connection) throws SQLException{
        PreparedStatement query = connection.prepareStatement("DELETE FROM economy WHERE id = ?");
        query.setString(1, owner.toString());
        query.execute();
    }

    public void add(Connection connection, double money){
        this.balance += money;
        this.balance = (int)(balance * 1000) / 1000.0;
        this.save(connection);
    }
    public void subtract(Connection connection, double money){
        this.balance -= money;
        this.balance = (int)(balance * 1000) / 1000.0;
        this.save(connection);
    }
    public double getBalance(){ return balance; }
    public UUID getOwner() { return owner; }
}