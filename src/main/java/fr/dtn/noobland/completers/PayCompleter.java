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

public class PayCompleter extends TabCompleter{
    public PayCompleter(Plugin plugin) { super(plugin); }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player author))
            return List.of();

        if(args.length == 1){
            List<String> players = new ArrayList<>();

            Bukkit.getOnlinePlayers().forEach(player -> {
                if(player.getName().toLowerCase().contains(args[0].toLowerCase()))
                    if(!player.getUniqueId().equals(author.getUniqueId()))
                        players.add(player.getName());
            });

            return players;
        }

        return List.of();
    }
}