package fr.dtn.noobland.feature.rank;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RankManager extends Manager {
    public RankManager(Plugin plugin){ super(plugin); }

    public void setRank(UUID player, Rank rank){
        new PlayerRank(plugin.getDbConnection(), player).setRank(plugin.getDbConnection(), rank);
        plugin.updateTabList(Objects.requireNonNull(Bukkit.getPlayer(player)));
        plugin.getScoreboardManager().updateScoreboard(player);
    }

    public Rank getRank(UUID player){
        return new PlayerRank(plugin.getDbConnection(), player).getRank();
    }

    public List<Rank> getRanks(){ return List.of(Rank.list); }
}