package fr.dtn.noobland.feature.levels;

import fr.dtn.noobland.Plugin;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LevelManager {
    private final Plugin plugin;

    public LevelManager(Plugin plugin){ this.plugin = plugin; }

    private PlayerLevel getPlayer(UUID player){ return new PlayerLevel(plugin.getDbConnection(), player); }

    public int getLevel(UUID player){ return getPlayer(player).getLevel(); }
    public float getExperience(UUID player){ return getPlayer(player).getExperience(); }
    public int addExperience(UUID player, float experience){ return getPlayer(player).addExperience(plugin.getDbConnection(), experience); }

    public void applyEffects(Player player){
        // TODO : do function + add event listeners to gain exp + manage display
    }
}