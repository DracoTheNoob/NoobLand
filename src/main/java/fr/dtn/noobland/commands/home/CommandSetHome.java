package fr.dtn.noobland.commands.home;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.command.CommandExecutor;
import fr.dtn.noobland.command.PluginCommand;
import fr.dtn.noobland.command.TabCompleter;
import fr.dtn.noobland.feature.home.HomeManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@PluginCommand(name = "sethome", description = "Set a home at the current player location.")
public class CommandSetHome extends CommandExecutor {
    public CommandSetHome(Plugin plugin){ super(plugin); }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(message.get("sender.not_player"));
            return true;
        }

        if(args.length != 1){
            player.sendMessage(message.get("requires.x").replace("%x%", "1").replace("%args%", "§6HOME_NAME"));
            return true;
        }

        String name = args[0];
        for(int i = 0; i < name.length(); i++){
            if(!HomeManager.acceptedChars.contains(name.substring(i, i+1))){
                player.sendMessage(message.get("syntax.only_roman").replace("%arg%", name));
                return true;
            }
        }

        if(name.length() > 20){
            player.sendMessage(message.get("home.name").replace("%arg%", name));
            return true;
        }

        UUID owner = player.getUniqueId();
        Location location = player.getLocation();
        HomeManager homes = plugin.getHomeManager();

        if(homes.exists(owner, name)){
            player.sendMessage(message.get("home.already").replace("%home%", name));
            return true;
        }

        if(!homes.createHome(owner, name, location)){
            player.sendMessage(message.get("home.limit"));
            return true;
        }

        player.sendMessage(message.get("home.created").replace("%home%", name));
        return true;
    }
}