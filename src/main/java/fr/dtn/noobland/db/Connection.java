package fr.dtn.noobland.db;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    private final String path, user, password;
    private java.sql.Connection connection;

    public Connection(String host, String user, String password, String database){
        this.path = getPath(host, database);
        this.user = user;
        this.password = password;

        this.connect();
    }

    public void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(path, user, password);

            System.out.println("Successfully connected to database");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        try {
            if(connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public java.sql.Connection getConnection(){
        try {
            if(connection != null && !connection.isClosed())
                return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.connect();
        return connection;
    }

    private static String getPath(String host, String database){ return "jdbc:mysql://" + host + ":3306/" + database; }
}