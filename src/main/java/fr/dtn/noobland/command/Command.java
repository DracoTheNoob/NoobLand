package fr.dtn.noobland.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Command extends org.bukkit.command.Command {
    private final CommandExecutor executor;

    public Command(String name, String description, CommandExecutor executor, String... aliases){
        super(name, description, "", List.of(aliases));

        this.executor = executor;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return executor.onCommand(sender, this, label, args);
    }
}