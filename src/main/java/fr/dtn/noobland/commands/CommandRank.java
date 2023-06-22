package fr.dtn.noobland.commands;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.command.CommandExecutor;
import fr.dtn.noobland.command.PluginCommand;
import fr.dtn.noobland.completers.RankCompleter;
import fr.dtn.noobland.feature.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@PluginCommand(name = "rank", description = "Set a specified rank to the targeted player.", completer = RankCompleter.class)
public class CommandRank extends CommandExecutor{
    public CommandRank(Plugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.isOp()){
            sender.sendMessage(message.get("permission.missing"));
            return true;
        }

        if(args.length != 2){
            sender.sendMessage(message.get("requires.x").replace("%x%", "2").replace("%args%", "§9TARGET§7,§9RANK"));
            return true;
        }

        String strTarget = args[0];
        Player player = Bukkit.getPlayer(strTarget);

        if(player == null){
            sender.sendMessage(message.get("target.missing"));
            return true;
        }

        String rankId = args[1];
        Rank rank = new Rank(rankId);

        if(rank.getDisplay() == null){
            sender.sendMessage(message.get("rank.missing"));
            return true;
        }

        plugin.getRankManager().setRank(player.getUniqueId(), rank);
        sender.sendMessage(message.get("rank.set_origin"));
        player.sendMessage(message.get("rank.set_target").replace("%rank%", rank.getDisplay().replace("[", "").replace("]", "")));
        return true;
    }
}