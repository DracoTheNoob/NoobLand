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

@PluginCommand(name = "home", description = "Teleport the player to the specified home", completer = HomesCompleter.class)
public class CommandHome extends CommandExecutor {
    public CommandHome(Plugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(message.get("sender.not_player"));
            return true;
        }

        HomeManager manager = plugin.getHomeManager();
        UUID owner = player.getUniqueId();

        if(args.length == 0){
            Inventory ui = manager.getUi(owner);

            if(ui == null){
                player.sendMessage(message.get("home.empty_list"));
                return true;
            }

            player.openInventory(ui);
            return true;
        }else if(args.length != 1){
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

        if(!manager.exists(owner, name)){
            player.sendMessage(message.get("home.missing").replace("%home%", name));
            return true;
        }

        if(plugin.getFightManager().isInFight(player.getUniqueId())) {
            player.sendMessage(message.get("fight.block_tp"));
            return true;
        }

        player.teleport(manager.getHome(owner, name).getLocation());
        player.sendMessage(message.get("home.use").replace("%arg%", name));
        return true;
    }
}