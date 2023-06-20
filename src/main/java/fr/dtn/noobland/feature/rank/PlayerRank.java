package fr.dtn.noobland.feature.rank;

import java.io.*;
import java.util.UUID;

public class PlayerRank {
    private final UUID owner;
    private Rank rank;

    public PlayerRank(UUID owner, Rank rank){
        this.owner = owner;
        this.rank = rank;
    }

    public PlayerRank(File directory, UUID owner){
        this.owner = owner;

        File file = getFile(directory, owner);
        try(FileReader read = new FileReader(file); BufferedReader reader = new BufferedReader(read)){
            String id = reader.readLine();
            this.rank = new Rank(id);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void save(File directory){
        File file = getFile(directory, owner);

        try(FileWriter write = new FileWriter(file); BufferedWriter writer = new BufferedWriter(write)){
            writer.write(rank.getId());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Rank getRank(){ return rank; }

    private static File getFile(File directory, UUID owner){ return new File(directory, owner.toString() + ".rank"); }
}