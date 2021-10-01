package net.hybrid.core;

import net.hybrid.core.commands.ItemCommand;
import net.hybrid.core.commands.gmcommands.GmaCommand;
import net.hybrid.core.commands.gmcommands.GmcCommand;
import net.hybrid.core.commands.gmcommands.GmsCommand;
import net.hybrid.core.commands.gmcommands.GmspCommand;
import net.hybrid.core.data.Language;
import net.hybrid.core.data.Mongo;
import net.hybrid.core.data.MongoListener;
import net.hybrid.core.managers.CommandListener;
import net.hybrid.core.rank.RankCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CorePlugin extends JavaPlugin {

    private static CorePlugin INSTANCE;
    private Mongo mongo;

    @Override
    public void onEnable(){
        long time = System.currentTimeMillis();

        INSTANCE = this;
        mongo = new Mongo(this);

        new RankCommand();
        new ItemCommand();

        new GmaCommand();
        new GmcCommand();
        new GmsCommand();
        new GmspCommand();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MongoListener(), this);
        pm.registerEvents(new CommandListener(), this);

        Language.initLanguageManager();

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
