package fr.dtn.noobland.commands.home;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.command.CommandExecutor;
import fr.dtn.noobland.command.PluginCommand;
import fr.dtn.noobland.feature.home.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@PluginCommand(name = "homes", description = "Send a message that list all the homes of a the player that use the command.")
public class CommandHomes extends CommandExecutor {
    public CommandHomes(Plugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args){
        if(!(sender instanceof Player player)){
            sender.sendMessage(message.get("sender.not_player"));
            return true;
        }

        HomeManager manager = plugin.getHomeManager();
        List<String> homes = manager.listHomesName(player.getUniqueId());

        if(homes.size() == 0)
            player.sendMessage(message.get("home.empty_list"));
        else
            player.sendMessage(message.get("home.list").replace("%homes%", display(homes, "§c", "§6")));
        return true;
    }

    private String display(List<String> homes, String home, String coma){
        StringBuilder string = new StringBuilder();

        for(int i = 0; i < homes.size(); i++){
            string.append(home).append(homes.get(i));

            if(i != homes.size() - 1)
                string.append(coma).append(", ");
        }

        return string.toString();
    }
}