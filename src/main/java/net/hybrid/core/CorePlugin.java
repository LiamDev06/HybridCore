package net.hybrid.core;

import net.hybrid.core.mongo.Mongo;
import org.bukkit.plugin.java.JavaPlugin;

public class CorePlugin extends JavaPlugin {

    private static CorePlugin INSTANCE;
    private Mongo mongo;

    @Override
    public void onEnable(){
        long time = System.currentTimeMillis();

        INSTANCE = this;
        mongo = new Mongo(this);

        getLogger().info("Hybrid Core system has been SUCCESSFULLY loaded in " + (System.currentTimeMillis() - time) + "ms!");
    }

    @Override
    public void onDisable(){
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
