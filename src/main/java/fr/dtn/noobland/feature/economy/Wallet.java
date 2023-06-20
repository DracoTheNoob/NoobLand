package fr.dtn.noobland.feature.economy;

import java.io.*;
import java.util.UUID;

public class Wallet {
    private final UUID owner;
    private double balance;

    public Wallet(UUID owner, double balance){
        this.owner = owner;
        this.balance = balance;
    }

    public Wallet(File directory, UUID owner){
        this.owner = owner;

        try(FileReader read = new FileReader(getFile(directory, owner)); BufferedReader reader = new BufferedReader(read)){
            String line = reader.readLine();
            this.balance = Double.parseDouble(line);
        }catch(FileNotFoundException e){
            this.balance = 0;
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void save(File directory){
        File file = getFile(directory, owner);
        this.balance = (int)(balance * 1000) / 1000.0;

        try(FileWriter write = new FileWriter(file); BufferedWriter writer = new BufferedWriter(write)){
            writer.write(String.valueOf(balance));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void add(double money){
        this.balance += money;
        this.balance = (int)(balance * 1000) / 1000.0;
    }
    public void subtract(double money){
        this.balance -= money;
        this.balance = (int)(balance * 1000) / 1000.0;
    }
    public double getBalance(){ return balance; }
    public UUID getOwner() { return owner; }

    public File getFile(File directory, UUID owner){ return new File(directory, owner.toString() + ".wallet"); }
}