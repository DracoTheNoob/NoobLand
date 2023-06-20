package fr.dtn.noobland;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class MessageManager {
    private final HashMap<String, String> messages;

    public MessageManager(File file){
        this.messages = new HashMap<>();

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        Set<String> keys = configuration.getKeys(true);
        keys.forEach(key -> messages.put(key, configuration.getString(key)));
    }

    public String get(String message){
        return this.messages.getOrDefault(message, "§4Text '" + message + "' not found");
    }
}