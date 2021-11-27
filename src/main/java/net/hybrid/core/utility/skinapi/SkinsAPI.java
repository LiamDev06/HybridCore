package net.hybrid.core.utility.skinapi;

import net.hybrid.core.utility.skinapi.exceptions.InsupportedVersionException;
import net.hybrid.core.utility.skinapi.nms.Skins_1_8_R3;
import net.hybrid.core.utility.skinapi.skins.SkinStorage;
import net.hybrid.core.utility.skinapi.skins.SkinsManager;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SkinsAPI {

    private static final SkinsAPI INSTANCE = new SkinsAPI();

    private Plugin plugin;

    private SkinsManager skinsManager;
    private boolean enabled;

    private SkinsAPI(){}

    public void setPlugin(Plugin plugin){
        Validate.notNull(plugin, "Plugin cannot be null");
        this.plugin = plugin;
    }

    public void enable(){

        if(plugin == null) return;

        Validate.isTrue(!enabled, "Plugin is already enabled");

        try {
            if(setupSkins()){
                this.plugin.getServer().getPluginManager().registerEvents(new SkinStorage(), plugin);
                this.plugin.getLogger().info("SkinsAPI setup was successful!");
                this.plugin.getLogger().info("SkinsAPI enabled.");
                this.enabled = true;
            }else{
                this.enabled = false;
            }
        } catch (InsupportedVersionException e) {
            e.printStackTrace();
        }

    }

    public void disable(){
        Validate.isTrue(enabled, "Plugin is already disabled");
        this.enabled = false;
    }

    private boolean setupSkins() throws InsupportedVersionException {

        String version;

        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException exception) {
            return false;
        }

        this.plugin.getLogger().info("Your server is running version " + version + "!");

        switch (version) {
            case "v1_8_R3":
                this.skinsManager = new SkinsManager(new Skins_1_8_R3());
                break;

            default:
                this.plugin = null;
                throw new InsupportedVersionException("Your version is not supported!");
        }
        return true;
    }

    public SkinsManager getSkinsManager(){
        if(!enabled) return null;
        return this.skinsManager;
    }

    public static SkinsAPI getInstance(){
        return INSTANCE;
    }

    public Plugin getPlugin(){
        return this.plugin;
    }

}
