package fr.dtn.noobland.feature.experience;

import fr.dtn.noobland.MessageManager;
import fr.dtn.noobland.Plugin;
import fr.dtn.noobland.feature.Manager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.UUID;

public class ExperienceManager extends Manager{
    private static final PotionEffect[] effects;

    static{
        effects = new PotionEffect[]{
                effect(PotionEffectType.NIGHT_VISION, 0),
                effect(PotionEffectType.LUCK, 0),
                effect(PotionEffectType.DOLPHINS_GRACE, 0),
                effect(PotionEffectType.SPEED, 0),
                effect(PotionEffectType.FAST_DIGGING, 0),
                effect(PotionEffectType.FIRE_RESISTANCE, 0),
                effect(PotionEffectType.LUCK, 1),
                effect(PotionEffectType.DAMAGE_RESISTANCE, 0),
                effect(PotionEffectType.LUCK, 2),
                effect(PotionEffectType.FAST_DIGGING, 1),
                effect(PotionEffectType.REGENERATION, 0)
        };
    }

    private static PotionEffect effect(PotionEffectType type, int amplifier){ return new PotionEffect(type, -1, amplifier, true, false); }

    public ExperienceManager(Plugin plugin){ super(plugin); }

    private PlayerLevel getPlayer(UUID player){ return new PlayerLevel(plugin.getDbConnection(), player); }

    public int getLevel(UUID player){ return getPlayer(player).getLevel(); }
    public float getExperience(UUID player){ return getPlayer(player).getExperience(); }
    public void addExperience(UUID id, float experience){
        Player player = Objects.requireNonNull(Bukkit.getPlayer(id));
        int levels = getPlayer(id).addExperience(plugin.getDbConnection(), experience);
        player.setAbsorptionAmount(player.getAbsorptionAmount() + 4 * levels);
        plugin.updateTabList(player);

        int playerLevel = getLevel(player.getUniqueId());
        for(int i = 0; i < levels; i++)
            player.sendMessage(plugin.getMessageManager().get("level.up").replace("%level%", String.valueOf(playerLevel - levels + i + 1)));

        applyEffects(player, levels > 0);
        plugin.getScoreboardManager().updateScoreboard(id);
    }
    public float getExperienceKilling(int level){ return (int)(Math.pow(1.4, level - 7) * 100) / 100f; }
    public float getExperienceTo(int level){ return (int)(Math.pow(2 * level, 1 + level * 0.01) * 100) / 100f; }

    public void applyEffects(Player player, boolean levelUp){
        int level = getLevel(player.getUniqueId());
        int hearts = level / 10;
        int effectsAmount = Math.min(level / 5, effects.length);

        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(20 + 2*hearts);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        for(int i = 0; i < effectsAmount; i++)
            player.addPotionEffect(effects[i]);

        if(levelUp){
            MessageManager message = plugin.getMessageManager();

            if(level % 10 == 0)
                player.sendMessage(message.get("level.heart"));

            if(level % 5 == 0) {
                PotionEffect effect = effects[effectsAmount-1];
                String name = effect.getType().getName();
                String amplifier = String.valueOf(effect.getAmplifier());
                player.sendMessage(message.get("level.effect").replace("%effect%", name).replace("%amplifier%", amplifier));
            }
        }
    }
}