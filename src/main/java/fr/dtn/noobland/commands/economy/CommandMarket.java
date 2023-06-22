package fr.dtn.noobland.commands.economy;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.command.CommandExecutor;
import fr.dtn.noobland.command.PluginCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@PluginCommand(name = "market", description = "Open the market.")
public class CommandMarket extends CommandExecutor{
    public CommandMarket(Plugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(message.get("sender.not_player"));
            return true;
        }

        if(args.length != 0){
            player.sendMessage(message.get("requires.not_argument"));
            return true;
        }

        player.openInventory(plugin.getEconomyManager().getShopUI(1));
        return true;
    }
}