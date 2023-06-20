package fr.dtn.noobland.completers;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.command.TabCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class HomesCompleter extends TabCompleter {
    public HomesCompleter(Plugin plugin) { super(plugin); }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        System.out.println("ZOB");

        if(!(sender instanceof Player player))
            return null;

        if(args.length == 1)
            return plugin.getHomeManager()
                    .listHomesName(player.getUniqueId())
                    .stream().filter(home -> args[0].startsWith(home))
                    .collect(Collectors.toList());

        return null;
    }
}