package fr.dtn.noobland.feature.fight;

import java.util.HashMap;
import java.util.UUID;

public class FightTimer implements Runnable{
    private final FightManager manager;

    public FightTimer(FightManager manager){
        this.manager = manager;
    }

    @Override
    public void run(){
        HashMap<UUID, Integer> timers = manager.getTimers();
        timers.keySet().forEach(key -> timers.replace(key, timers.get(key) - 1));

        timers.keySet().forEach(key -> {
            if(timers.get(key) == 0){
                manager.clearPlayer(key);
                manager.clearEnemy(key);
            }
        });
    }
}