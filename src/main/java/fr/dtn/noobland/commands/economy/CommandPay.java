package fr.dtn.noobland.commands.economy;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.command.CommandExecutor;
import fr.dtn.noobland.command.PluginCommand;
import fr.dtn.noobland.feature.economy.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@PluginCommand(name = "pay", description = "Send an amount of money to another player.")
public class CommandPay extends CommandExecutor {
    public CommandPay(Plugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args){
        if(args.length != 2){
            sender.sendMessage(message.get("requires.x").replace("%x%", "2").replace("%args%", "§9TARGET§7, §9MONEY"));
            return true;
        }

        String strTarget = args[0];
        Player payed = Bukkit.getPlayer(strTarget);

        if(payed == null){
            sender.sendMessage(message.get("target.missing"));
            return true;
        }


        String strMoney = args[1];
        double money;

        try{
            money = Double.parseDouble(strMoney);
        }catch(NumberFormatException e){
            sender.sendMessage(message.get("economy.int").replace("%money%", strMoney));
            return true;
        }

        EconomyManager manager = plugin.getEconomyManager();

        if(sender instanceof Player payer){
            if(payed.getName().equals(payer.getName())){
                payer.sendMessage(message.get("economy.self"));
                return true;
            }

            if(!manager.pay(payer.getUniqueId(), payed.getUniqueId(), money)){
                payer.sendMessage(message.get("economy.exceed"));
                return true;
            }

            payer.sendMessage(message.get("economy.pay").replace("%money%", String.valueOf(money)).replace("%player%", payed.getDisplayName()));
            payed.sendMessage(message.get("economy.paid").replace("%money%", String.valueOf(money)).replace("%player%", payed.getDisplayName()));
            return true;
        }

        manager.pay(null, payed.getUniqueId(), money);
        sender.sendMessage(message.get("economy.pay").replace("%money%", String.valueOf(money)).replace("%player%", payed.getDisplayName()));
        payed.sendMessage(message.get("economy.paid").replace("%money%", String.valueOf(money)).replace("%player%", "la console"));
        return true;
    }
}