package fr.dtn.noobland.feature.rank;

import fr.dtn.noobland.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class RankManager {
    private final HashMap<UUID, PlayerRank> ranks;
    private final File directory;

    public RankManager(Plugin plugin){
        this.ranks = new HashMap<>();
        this.directory = new File(plugin.getDirectory(), "ranks");

        if(!directory.exists() && !directory.mkdir())
            throw new RuntimeException("Unable to create ranks folder at '" + directory.getPath() + "'");
    }

    public void register(UUID player){
        if(!ranks.containsKey(player))
            ranks.put(player, new PlayerRank(player, new Rank("player")));
        ranks.get(player).save(directory);
    }

    public void setRank(UUID player, Rank rank){
        register(player);
        ranks.replace(player, new PlayerRank(player, rank));
        ranks.get(player).save(directory);
    }

    public Rank getRank(UUID player){
        register(player);
        return ranks.get(player).getRank();
    }
}