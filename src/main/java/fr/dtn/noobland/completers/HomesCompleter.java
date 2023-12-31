package fr.dtn.noobland.completers;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.command.TabCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomesCompleter extends TabCompleter {
    public HomesCompleter(Plugin plugin) { super(plugin); }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player))
            return List.of();

        if(args.length == 1) {
            List<String> homes = new ArrayList<>();

            plugin.getHomeManager().listHomesName(player.getUniqueId()).forEach(home -> {
                if(home.toLowerCase().contains(args[0].toLowerCase()))
                    homes.add(home);
            });

            return homes;
        }

        return List.of();
    }
}