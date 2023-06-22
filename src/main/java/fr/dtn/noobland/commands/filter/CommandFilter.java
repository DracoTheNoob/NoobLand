package fr.dtn.noobland.commands.filter;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.command.CommandExecutor;
import fr.dtn.noobland.command.PluginCommand;
import fr.dtn.noobland.completers.FilterCompleter;
import fr.dtn.noobland.feature.inventory.Filter;
import fr.dtn.noobland.feature.inventory.FilterType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@PluginCommand(name = "filter", description = "Allow a player to filter the items he wants to clear from his inventory.", completer = FilterCompleter.class)
public class CommandFilter extends CommandExecutor{
    public CommandFilter(Plugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(message.get("sender.not_player"));
            return true;
        }

        if(args.length == 0){
            player.sendMessage(message.get("requires.min_x").replace("%x%", "1"));
            return true;
        }

        if(args.length == 1){
            switch(args[0]){
                case "add", "remove" -> player.sendMessage(message.get("requires.x")
                        .replace("%x%", "3")
                        .replace("%arg%", "§9FILTER_CATEGORY§c, §9FILTER_TARGET"));
                case "clear" -> {
                    plugin.getFilterManager().clearFilters(player.getUniqueId());
                    player.sendMessage(message.get("filter.clear"));
                } default -> player.sendMessage(message.get("requires.valid_arg").replace("%arg%", args[0]));
            }

            return true;
        }

        if(args.length == 2){
            switch(args[0]){
                case "add", "remove" -> player.sendMessage(message.get("requires.x")
                        .replace("%x%", "3")
                        .replace("%arg%", "§9FILTER_CATEGORY§c, §9FILTER_TARGET"));
                case "clear" -> {
                    String category = args[1];

                    try{
                        FilterType type = FilterType.valueOf(category.toUpperCase());
                        plugin.getFilterManager().clearFilters(player.getUniqueId(), type);
                        player.sendMessage(message.get("filter.clear_category")
                                .replace("%category%", type.name()));
                    }catch(IllegalArgumentException e){
                        player.sendMessage(message.get("requires.valid_arg").replace("%arg%", args[1]));
                    }
                } default -> player.sendMessage(message.get("requires.valid_arg").replace("%arg%", args[1]));
            }

            return true;
        }

        if(args.length == 3){
            switch(args[0]){
                case "add" -> {
                    String category = args[1];

                    try{
                        FilterType type = FilterType.valueOf(category.toUpperCase());
                        String target = args[2].toUpperCase();

                        plugin.getFilterManager().addFilter(player.getUniqueId(), new Filter(type, target));
                        player.sendMessage(message.get("filter.add"));
                    }catch(IllegalArgumentException e){
                        player.sendMessage(message.get("requires.valid_arg").replace("%arg%", args[1]));
                    }
                } case "remove" -> {
                    String category = args[1];

                    try{
                        FilterType type = FilterType.valueOf(category.toUpperCase());
                        String target = args[2].toUpperCase();

                        if(plugin.getFilterManager().removeFilter(player.getUniqueId(), new Filter(type, target)))
                            player.sendMessage(message.get("filter.remove"));
                        else
                            player.sendMessage(message.get("filter.missing"));
                    }catch(IllegalArgumentException e){
                        player.sendMessage(message.get("requires.valid_arg").replace("%arg%", args[1]));
                    }
                } default -> player.sendMessage(message.get("requires.valid_arg").replace("%arg%", args[1]));
            }

            return true;
        }

        return true;
    }
}