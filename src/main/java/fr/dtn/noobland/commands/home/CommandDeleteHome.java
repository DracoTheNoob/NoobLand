package fr.dtn.noobland.commands.home;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.command.CommandExecutor;
import fr.dtn.noobland.command.PluginCommand;
import fr.dtn.noobland.completers.HomesCompleter;
import fr.dtn.noobland.feature.home.Home;
import fr.dtn.noobland.feature.home.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@PluginCommand(name = "delhome", description = "Delete the home specified by the player", completer = HomesCompleter.class)
public class CommandDeleteHome extends CommandExecutor {
    public CommandDeleteHome(Plugin plugin){ super(plugin); }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(message.get("sender.not_player"));
            return true;
        }

        UUID owner = player.getUniqueId();
        HomeManager manager = plugin.getHomeManager();

        if(args.length == 0){
            Inventory ui = Bukkit.createInventory(null, 45, "Choix du home");
            List<String> homes = manager.listHomesName(owner);

            if(homes.size() == 0){
                player.sendMessage(message.get("home.empty_list"));
                return true;
            }

            for(int i = 0; i < homes.size(); i++){
                Home home = manager.getHome(owner, homes.get(i));
                ui.setItem( (i > 3) ? ( 28 + (2 * (i - 4))) : (10 + 2 * i), home.getIcon());
            }

            player.openInventory(ui);
            return true;
        }else if(args.length != 1){
            player.sendMessage(message.get("requires.x").replace("%x%", "1").replace("%args%", "§6HOME_NAME"));
            return true;
        }

        String name = args[0];
        for(int i = 0; i < name.length(); i++) {
            if (!HomeManager.acceptedChars.contains(name.substring(i, i + 1))) {
                player.sendMessage(message.get("syntax.only_roman").replace("%arg%", name));
                return true;
            }
        }

        if(!manager.exists(owner, name) || !manager.deleteHome(owner, name)){
            player.sendMessage(message.get("home.missing").replace("%home%", name));
            return true;
        }

        player.sendMessage(message.get("home.delete").replace("%home%", name));
        return true;
    }
}