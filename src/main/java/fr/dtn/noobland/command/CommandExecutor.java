package fr.dtn.noobland.command;

import fr.dtn.noobland.MessageManager;
import fr.dtn.noobland.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class CommandExecutor implements org.bukkit.command.CommandExecutor {
    protected final Plugin plugin;
    protected final MessageManager message;

    public CommandExecutor(Plugin plugin){
        this.plugin = plugin;
        this.message = plugin.getMessageManager();
    }

    @Override
    public abstract boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
}