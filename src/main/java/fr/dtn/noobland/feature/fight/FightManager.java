package fr.dtn.noobland.feature.fight;

import fr.dtn.noobland.Plugin;
import org.bukkit.Bukkit;

import java.util.*;

public class FightManager {
    private final Plugin plugin;
    private final HashMap<UUID, List<UUID>> fights;
    private final HashMap<UUID, Integer> timers;

    public FightManager(Plugin plugin){
        this.plugin = plugin;
        this.fights = new HashMap<>();
        this.timers = new HashMap<>();
    }

    public void onEnable(){
        Bukkit.getScheduler().runTaskTimer(plugin, new FightTimer(this), 20, 20);
    }

    public void addEnemy(UUID player, UUID enemy){
        if(!fights.containsKey(player))
            fights.put(player, new ArrayList<>());

        fights.get(player).remove(enemy);
        fights.get(player).add(enemy);

        timers.remove(player);
        timers.put(player, 30);
    }

    public void clearPlayer(UUID player){
        fights.remove(player);
        Objects.requireNonNull(Bukkit.getPlayer(player)).sendMessage(plugin.getMessageManager().get("fight.end"));
    }
    public void clearEnemy(UUID enemy){ fights.keySet().removeIf(enemy::equals); }

    public boolean isInFight(UUID player){
        if(!fights.containsKey(player))
            return false;

        return fights.get(player).size() > 0;
    }

    public HashMap<UUID, Integer> getTimers(){ return timers; }
}