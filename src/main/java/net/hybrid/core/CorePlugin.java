package net.hybrid.core;

import net.hybrid.core.commands.*;
import net.hybrid.core.commands.gmcommands.GmaCommand;
import net.hybrid.core.commands.gmcommands.GmcCommand;
import net.hybrid.core.commands.gmcommands.GmsCommand;
import net.hybrid.core.commands.gmcommands.GmspCommand;
import net.hybrid.core.data.Language;
import net.hybrid.core.data.Mongo;
import net.hybrid.core.data.MongoListener;
import net.hybrid.core.managers.CommandListener;
import net.hybrid.core.managers.NetworkChatManager;
import net.hybrid.core.rank.RankCommand;
import net.hybrid.core.utility.BadWordsFilter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CorePlugin extends JavaPlugin {

    private static CorePlugin INSTANCE;
    private Mongo mongo;

    @Override
    public void onEnable(){
        long time = System.currentTimeMillis();

        INSTANCE = this;
        mongo = new Mongo(this);

        new AllChatCommand();

        new RankCommand();
        new ItemCommand();
        new SetChatColorCommand();
        new StaffNotifyCommand();
        new StaffHubCommand();
        new BuildModeCommand();
        new ChatChannelCommand();
        new TpHereCommand();
        new TpAllCommand();

        new GmaCommand();
        new GmcCommand();
        new GmsCommand();
        new GmspCommand();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MongoListener(), this);
        pm.registerEvents(new CommandListener(), this);
        pm.registerEvents(new NetworkChatManager(), this);

        Language.initLanguageManager();

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        File file = new File(getDataFolder(), "bad-words.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!file.exists() || !config.getStringList("words").equals(BadWordsFilter.getBadWords())) {
            config.set("words", "");
            config.set("words", BadWordsFilter.getBadWords());

            try {
                config.save(file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        getLogger().info("Hybrid Core system has been SUCCESSFULLY loaded in " + (System.currentTimeMillis() - time) + "ms!");
    }

    @Override
    public void onDisable(){
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

        INSTANCE = null;
        getLogger().info("Hybrid Core system has SUCCESSFULLY been disabled.");
    }

    public static CorePlugin getInstance() {
        return INSTANCE;
    }

    public Mongo getMongo() {
        return mongo;
    }
}
