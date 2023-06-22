package fr.dtn.noobland.completers;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.command.TabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RankCompleter extends TabCompleter{
    public RankCompleter(Plugin plugin) { super(plugin); }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))
            return List.of();

        if(args.length == 1){
            List<String> players = new ArrayList<>();

            Bukkit.getOnlinePlayers().forEach(player -> {
                if(player.getName().toLowerCase().contains(args[0].toLowerCase()))
                    players.add(player.getName());
            });

            return players;
        }

        if(args.length == 2){
            List<String> ranks = new ArrayList<>();

            plugin.getRankManager().getRanks().forEach(rank -> {
                if(rank.getId().toLowerCase().contains(args[1].toLowerCase()))
                    ranks.add(rank.getId());
            });

            return ranks;
        }

        return List.of();
    }
}