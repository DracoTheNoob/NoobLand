package fr.dtn.noobland.listener;

import fr.dtn.noobland.MessageManager;
import fr.dtn.noobland.Plugin;

public class Listener implements org.bukkit.event.Listener {
    protected final Plugin plugin;
    protected final MessageManager message;

    public Listener(Plugin plugin){
        this.plugin = plugin;
        this.message = plugin.getMessageManager();
    }
}