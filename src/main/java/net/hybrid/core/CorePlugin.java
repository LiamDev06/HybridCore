package net.hybrid.core;

import net.hybrid.core.commands.*;
import net.hybrid.core.commands.admin.*;
import net.hybrid.core.commands.admin.gmcommands.GmaCommand;
import net.hybrid.core.commands.admin.gmcommands.GmcCommand;
import net.hybrid.core.commands.admin.gmcommands.GmsCommand;
import net.hybrid.core.commands.admin.gmcommands.GmspCommand;
import net.hybrid.core.commands.staff.*;
import net.hybrid.core.data.Mongo;
import net.hybrid.core.data.MongoListener;
import net.hybrid.core.managers.*;
import net.hybrid.core.managers.tabmanagers.TabListManager_1_17_R1;
import net.hybrid.core.managers.tabmanagers.TabListManager_1_8_R3;
import net.hybrid.core.moderation.commands.*;
import net.hybrid.core.rank.RankCommand;
import net.hybrid.core.utility.BadWordsFilter;
import net.hybrid.core.utility.ServerVersion;
import net.hybrid.core.utility.skinapi.SkinsAPI;
import net.hybrid.core.utility.skinapi.skins.SkinsManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class CorePlugin extends JavaPlugin {

    private static CorePlugin INSTANCE;
    public static ServerVersion VERSION;
    private Mongo mongo;

    @Override
    public void onEnable(){
        long time = System.currentTimeMillis();

        INSTANCE = this;
        mongo = new Mongo(this);

        new AllChatCommand();
        // new LangCommand();
        new ReportCommand();
        new PlayCommand();
        new ChatChannelCommand();
        new EmoteCommand();

        new RankCommand();
        new ItemCommand();
        new SetChatColorCommand();
        new StaffNotifyCommand();
        new StaffHubCommand();
        new BuildModeCommand();
        new TpHereCommand();
        new TpAllCommand();
        new CoreVersionCommand();
        new NMSVersionCommand();
        new VanishCommand();
        new NickCommand();
        new UnnickCommand();
        new KaboomCommand();
        new HealCommand();
        new FeedCommand();
        new HiddenNickCommand();
        new LoopCommand();

        new BanCommand();
        new UnbanCommand();
        new KickCommand();
        new WarnCommand();
        new BadNameCommand();
        new RemoveBadNameCommand();
        new MuteCommand();
        new UnMuteCommand();
        new TempBanCommand();
        new ClearChatCommand();

        new SetNetworkLevel();
        new CheckNetworkLevel();
        new SetNetworkExp();
        new CheckNetworkExp();

        new GmaCommand();
        new GmcCommand();
        new GmsCommand();
        new GmspCommand();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MongoListener(), this);
        pm.registerEvents(new CommandListener(), this);
        pm.registerEvents(new NetworkChatManager(), this);
        pm.registerEvents(new JoinManager(), this);
        pm.registerEvents(new NetworkLevelListener(), this);
        pm.registerEvents(new PurchaseManager(), this);

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        SkinsAPI.getInstance().setPlugin(this);
        SkinsAPI.getInstance().enable();
        try {
            SkinsManager manager = SkinsAPI.getInstance().getSkinsManager();

            File[] files = new File(getDataFolder() + "/nickskins").listFiles();
            for (File file : files) {
                manager.loadSkin(file);
            }

            getLogger().info("Skins to the core for the nick system loaded WITHOUT ISSUES! Works!");
        } catch (Exception exception) {
            getLogger().info("Loading skins to the core for the nick system FAILED!");
        }

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

        VERSION = ServerVersion.valueOf(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
        if (VERSION == ServerVersion.v1_8_R3) {
            new TabListManager_1_8_R3().init();
        }

        if (VERSION == ServerVersion.v1_17_R1) {
            new TabListManager_1_17_R1().init();
        }

        getLogger().info("Hybrid Core system has been SUCCESSFULLY loaded in " + (System.currentTimeMillis() - time) + "ms! This server is running version " + VERSION);
    }

    @Override
    public void onDisable(){
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

        SkinsAPI.getInstance().disable();
        mongo.getMongoClient().close();

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
