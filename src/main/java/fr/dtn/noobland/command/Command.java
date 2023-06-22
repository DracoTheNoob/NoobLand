package fr.dtn.noobland.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Command extends org.bukkit.command.Command {
    private final CommandExecutor executor;
    private TabCompleter completer;

    public Command(String name, String description, CommandExecutor executor, String... aliases){
        super(name, description, "", List.of(aliases));

        this.executor = executor;
    }

    public void setCompleter(TabCompleter completer){ this.completer = completer; }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return executor.onCommand(sender, this, label, args);
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if(completer == null)
            return List.of();

        return completer.onTabComplete(sender, this, alias, args);
    }
}