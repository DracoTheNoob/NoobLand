package fr.dtn.noobland;

import fr.dtn.noobland.feature.economy.EconomyManager;
import fr.dtn.noobland.feature.fight.FightManager;
import fr.dtn.noobland.feature.levels.ExperienceManager;
import fr.dtn.noobland.feature.rank.Rank;
import fr.dtn.noobland.feature.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Objects;
import java.util.UUID;

public class ScoreBoardManager {
    private final Plugin plugin;

    public ScoreBoardManager(Plugin plugin){
        this.plugin = plugin;
    }

    public void updateScoreboard(UUID uuid) {
        try {
            updateScoreboard(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
        } catch (NullPointerException ignored) {}
    }

    public void updateScoreboard(Player player){
        UUID id = player.getUniqueId();
        
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("player", Criteria.DUMMY, "zob");
        objective.setDisplayName("§6" + player.getName());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        ExperienceManager expManager = plugin.getExperienceManager();
        int lvl = expManager.getLevel(id);
        float exp = expManager.getExperience(id);
        float requires = expManager.getExperienceTo(lvl + 1);

        Score scoreBlank = objective.getScore(" ".repeat(25));
        scoreBlank.setScore(100);
        Score scoreRank = objective.getScore("§6Grade: §c" + plugin.getRankManager().getRank(id).getSimpleDisplay());
        scoreRank.setScore(30);
        Score scoreMoney = objective.getScore("§6Argent: §c" + plugin.getEconomyManager().getBalance(id) + " $");
        scoreMoney.setScore(25);
        Score scoreLevel = objective.getScore("§6Niveau: §c" + lvl);
        scoreLevel.setScore(20);
        Score scoreExperience = objective.getScore("§6Exp: §c" + exp + " §8/ §c" + requires);
        scoreExperience.setScore(15);
        Score scoreHomes = objective.getScore("§6Homes: §c" + plugin.getHomeManager().listHomesName(id).size());
        scoreHomes.setScore(10);

        Score scoreBlank2 = objective.getScore(" ".repeat(24));
        scoreBlank2.setScore(5);

        FightManager fight = plugin.getFightManager();
        Score scoreFight = objective.getScore("§6En combat: " + (fight.isInFight(id) ? "§c" + fight.getRemainingTime(id) + "s" : "§cnon"));
        scoreFight.setScore(0);

        player.setScoreboard(scoreboard);
    }
}