package fr.dtn.noobland.feature.rank;

import fr.dtn.noobland.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class RankManager {
    private final HashMap<UUID, PlayerRank> ranks;
    private final Plugin plugin;

    public RankManager(Plugin plugin){
        this.ranks = new HashMap<>();
        this.plugin = plugin;
    }

    public void register(UUID player){
        try{
            ranks.put(player, new PlayerRank(plugin.getDbConnection(), player));
        }catch(IllegalArgumentException e){
            ranks.put(player, new PlayerRank(player, new Rank("player")));
        }

        ranks.get(player).save(plugin.getDbConnection());
    }

    public void setRank(UUID player, Rank rank){
        register(player);
        ranks.replace(player, new PlayerRank(player, rank));
        ranks.get(player).save(plugin.getDbConnection());
    }

    public Rank getRank(UUID player){
        register(player);
        return ranks.get(player).getRank();
    }
}