package fr.dtn.noobland;

import fr.dtn.noobland.command.Command;
import fr.dtn.noobland.command.PluginCommand;
import fr.dtn.noobland.commands.CommandSpawn;
import fr.dtn.noobland.commands.economy.CommandMarket;
import fr.dtn.noobland.commands.economy.CommandPay;
import fr.dtn.noobland.commands.home.CommandDeleteHome;
import fr.dtn.noobland.commands.home.CommandHome;
import fr.dtn.noobland.commands.home.CommandHomes;
import fr.dtn.noobland.commands.home.CommandSetHome;
import fr.dtn.noobland.commands.ranks.CommandRank;
import fr.dtn.noobland.db.Database;
import fr.dtn.noobland.feature.economy.EconomyManager;
import fr.dtn.noobland.feature.fight.FightManager;
import fr.dtn.noobland.feature.home.HomeManager;
import fr.dtn.noobland.feature.levels.ExperienceManager;
import fr.dtn.noobland.feature.rank.Rank;
import fr.dtn.noobland.feature.rank.RankManager;
import fr.dtn.noobland.listener.Listener;
import fr.dtn.noobland.listener.block.BlockPlaceListener;
import fr.dtn.noobland.listener.block.BlockBreakListener;
import fr.dtn.noobland.listener.entity.EntityDamageByEntityListener;
import fr.dtn.noobland.listener.player.*;
import fr.dtn.noobland.listener.entity.EntityDamageListener;
import fr.dtn.noobland.listener.entity.EntityDeathListener;
import fr.dtn.noobland.listener.entity.EntitySpawnListener;
import fr.dtn.noobland.listener.ui.InventoryClickListener;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.command.CommandExecutor;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

public class Plugin extends JavaPlugin{
    private Database database;
    private MessageManager messageManager;
    private HomeManager homeManager;
    private RankManager rankManager;
    private EconomyManager economyManager;
    private FightManager fightManager;
    private ExperienceManager experienceManager;
    private ScoreBoardManager scoreboardManager;

    @Override
    public void onLoad() {
        this.database = new Database();

        File file = new File(getDirectory(), "messages.yml");

        try {
            if(!file.exists() && file.createNewFile())
                System.out.println("Created empty 'messages.yml' configuration file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.messageManager = new MessageManager(file);
        this.homeManager = new HomeManager(this);
        this.rankManager = new RankManager(this);
        this.economyManager = new EconomyManager(this);
        this.fightManager = new FightManager(this);
        this.experienceManager = new ExperienceManager(this);
        this.scoreboardManager = new ScoreBoardManager(this);

        registerCommands(
                CommandSpawn.class,
                CommandSetHome.class, CommandDeleteHome.class, CommandHome.class, CommandHomes.class,
                CommandPay.class, CommandMarket.class,
                CommandRank.class
        );

        economyManager.getShopUI(0);
    }

    @Override
    public void onEnable() {
        fightManager.onEnable();

        registerListeners(
                PlayerJoinListener.class, PlayerQuitListener.class, PlayerChatListener.class, PlayerDeathListener.class,
                InventoryClickListener.class,
                EntitySpawnListener.class, EntityDeathListener.class, EntityDamageListener.class, EntityDamageByEntityListener.class,
                BlockPlaceListener.class, BlockBreakListener.class
        );

        Bukkit.getServer().getWorlds().forEach(world -> {
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        });
    }

    @Override
    public void onDisable() {
        this.database.close();
    }

    public Connection getDbConnection() { return database.getConnection(); }
    public MessageManager getMessageManager() { return messageManager; }
    public HomeManager getHomeManager() { return homeManager; }
    public RankManager getRankManager() { return rankManager; }
    public EconomyManager getEconomyManager() { return economyManager; }
    public FightManager getFightManager() { return fightManager; }
    public ExperienceManager getExperienceManager() { return experienceManager; }
    public ScoreBoardManager getScoreboardManager() { return scoreboardManager; }

    public void updateTabList(Player player){
        Rank rank = rankManager.getRank(player.getUniqueId());
        int level = experienceManager.getLevel(player.getUniqueId());

        player.setPlayerListHeader("§6Noob Land\n");
        player.setPlayerListName(" " + rank.getDisplay() + " §c" + level + "§7 - §6" + player.getDisplayName() + " ");
        player.setPlayerListFooter("\n§c" + Bukkit.getOnlinePlayers().size() + " §7/ §c" + Bukkit.getMaxPlayers());
    }

    public File getDirectory(){
        File directory = new File(getServer().getWorldContainer(), "plugins/Noobland");

        if(!directory.exists() && !directory.mkdir())
            throw new RuntimeException("Unable to make plugin directory '" + directory.getPath() + "'");

        return directory;
    }

    @SafeVarargs
    final void registerCommands(@NotNull Class<? extends CommandExecutor>... classes){
        CraftServer server = (CraftServer) getServer();

        for(Class<? extends CommandExecutor> c : classes){
            try{
                PluginCommand annotation = c.getAnnotation(PluginCommand.class);
                CommandExecutor executor = c.getConstructor(Plugin.class).newInstance(this);
                Command command = new Command(annotation.name(), annotation.description(), executor, annotation.aliases());

                server.getCommandMap().register(getName(), command);
                System.out.println("[REGISTRATION][COMMAND]: '" + command.getName() + "' from '" + c.getName() + "'");
            }catch(NoSuchMethodException e){
                throw new RuntimeException("Unable to load command from '" + c.getName() + "' : Unable to find constructor");
            }catch(InstantiationException | InvocationTargetException e){
                throw new RuntimeException("Unable to load command from '" + c.getName() + "' : Unable to create an instance");
            }catch(IllegalAccessException e){
                throw new RuntimeException("Unable to load command from '" + c.getName() + "' : Constructor is not public");
            }
        }
    }

    @SafeVarargs
    final void registerListeners(@NotNull Class<? extends Listener>... classes){
        for(Class<? extends Listener> c : classes){
            try{
                Listener listener = c.getConstructor(Plugin.class).newInstance(this);
                getServer().getPluginManager().registerEvents(listener, this);
                System.out.println("[REGISTRATION][LISTENER]: '" + c.getName() + "'");
            }catch(NoSuchMethodException e){
                throw new RuntimeException("Unable to load command from '" + c.getName() + "' : Unable to find constructor");
            }catch(InstantiationException | InvocationTargetException e){
                throw new RuntimeException("Unable to load command from '" + c.getName() + "' : Unable to create an instance");
            }catch(IllegalAccessException e){
                throw new RuntimeException("Unable to load command from '" + c.getName() + "' : Constructor is not public");
            }
        }
    }
}