package fr.dtn.noobland.completers;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.command.TabCompleter;
import fr.dtn.noobland.feature.inventory.FilterType;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilterCompleter extends TabCompleter{
    public FilterCompleter(Plugin plugin) { super(plugin); }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player))
            return List.of();

        if(args.length == 1)
            return List.of("add", "remove", "clear");

        if(args.length == 2){
            switch(args[0]){
                case "add", "remove", "clear" -> {
                    List<String> types = new ArrayList<>();

                    for(FilterType value : FilterType.values())
                        types.add(value.name().toLowerCase());

                    return types;
                }
            }

            return List.of();
        }

        if(args.length == 3){
            if(args[0].equals("remove")){
                try{
                    List<String> filters = new ArrayList<>();

                    plugin.getFilterManager().getFilters(player.getUniqueId(), FilterType.valueOf(args[1].toUpperCase())).forEach(filter -> {
                        if(filter.getTarget().toLowerCase().contains(args[2].toLowerCase()))
                            filters.add(filter.getTarget().toLowerCase());
                    });

                    return filters;
                }catch(IllegalArgumentException ignored){}
            }else if(args[0].equals("add")){
                List<String> items = new ArrayList<>();

                switch(args[1]){
                    case "armor" -> items.addAll(List.of("leather", "chainmail", "gold", "iron", "diamond", "netherite"));
                    case "tool" -> items.addAll(List.of("wood", "stone", "gold", "iron", "diamond", "netherite"));
                    case "material" -> {
                        for(Material material : Material.values())
                            if(material.isEnabledByFeature(player.getWorld()))
                                items.add(material.name().toLowerCase());
                    }
                }

                return items;
            }

            return List.of();
        }

        return List.of();
    }
}