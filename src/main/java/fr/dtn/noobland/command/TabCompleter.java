package fr.dtn.noobland.command;

import fr.dtn.noobland.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class TabCompleter implements org.bukkit.command.TabCompleter {
    protected final Plugin plugin;

    public TabCompleter(Plugin plugin){ this.plugin = plugin; }

    @NotNull @Override
    public abstract List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
}