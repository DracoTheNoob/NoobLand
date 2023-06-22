package fr.dtn.noobland.feature.fight;

import fr.dtn.noobland.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class FightTimer implements Runnable{
    private final Plugin plugin;
    private final FightManager manager;

    public FightTimer(Plugin plugin, FightManager manager){
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public void run(){
        HashMap<UUID, Integer> timers = manager.getTimers();

        timers.keySet().forEach(id -> {
            timers.replace(id, timers.get(id) - 1);
            plugin.getScoreboardManager().updateScoreboard(id);
        });

        timers.keySet().forEach(id -> {
            if(timers.get(id) == 0)
                manager.removeEntity(id);
        });
    }
}