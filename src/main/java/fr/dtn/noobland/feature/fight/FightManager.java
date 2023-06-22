package fr.dtn.noobland.feature.fight;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.Manager;
import org.bukkit.Bukkit;

import java.util.*;

public class FightManager extends Manager {
    private final List<UUID> fights;
    private final HashMap<UUID, Integer> timers;

    public FightManager(Plugin plugin){
        super(plugin);
        this.fights = new ArrayList<>();
        this.timers = new HashMap<>();
    }

    public void onEnable(){ Bukkit.getScheduler().runTaskTimer(plugin, new FightTimer(plugin, this), 20, 20); }

    public void addEntity(UUID id){
        if(!fights.contains(id))
            fights.add(id);

        if(timers.containsKey(id))
            timers.replace(id, 30);
        else
            timers.put(id, 30);
    }

    public void removeEntity(UUID id){
        fights.remove(id);
        plugin.getScoreboardManager().updateScoreboard(id);
    }

    public boolean isInFight(UUID id){ return fights.contains(id); }
    public int getRemainingTime(UUID id){ return timers.getOrDefault(id, -1); }

    public HashMap<UUID, Integer> getTimers(){ return timers; }
}