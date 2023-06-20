package fr.dtn.noobland.commands;

import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.command.CommandExecutor;
import fr.dtn.noobland.command.PluginCommand;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@PluginCommand(name = "spawn", description = "Teleport the player to the spawn point of the overworld.")
public class CommandSpawn extends CommandExecutor {
    public CommandSpawn(Plugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(message.get("sender.not_player"));
            return true;
        }

        World world = plugin.getServer().getWorld("world");

        if(world == null){
            sender.sendMessage(message.get("not_found.overworld"));
            return true;
        }

        if(args.length != 0){
            player.sendMessage(message.get("requires.not_argument"));
            return true;
        }

        if(plugin.getFightManager().isInFight(player.getUniqueId())) {
            player.sendMessage(message.get("fight.block_tp"));
            return true;
        }

        player.teleport(world.getSpawnLocation());
        player.sendMessage(message.get("spawn.use"));
        return true;
    }
}