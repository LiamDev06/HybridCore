package net.hybrid.core.utility;

import net.hybrid.core.CorePlugin;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.UUID;

public class MetadataManager {

    private final UUID uuid;

    public MetadataManager(UUID uuid) {
        this.uuid = uuid;
    }

    public ChatColor getChatColor() {
        Document document = CorePlugin.getInstance().getMongo().loadDocument("playerData", uuid);
        return ChatColor.valueOf(document.getString("chatColor").toUpperCase());
    }

    public void setChatColor(ChatColor chatColor) {
        Document document = CorePlugin.getInstance().getMongo().loadDocument("playerData", uuid);

        document.replace("chatColor", chatColor.name());

        CorePlugin.getInstance().getMongo().saveDocument("playerData", document, uuid);
    }

    public boolean hasStaffNotify() {
        Document document = CorePlugin.getInstance().getMongo().loadDocument("playerData", uuid);
        return document.getBoolean("staffNotifyMode");
    }

    public void setStaffNotify(boolean value) {
        Document document = CorePlugin.getInstance().getMongo().loadDocument("playerData", uuid);

        document.replace("staffNotifyMode", value);

        CorePlugin.getInstance().getMongo().saveDocument("playerData", document, uuid);
    }

    public boolean isInBuildMode() {
        Document document = CorePlugin.getInstance().getMongo().loadDocument("playerData", uuid);
        return document.getBoolean("staffBuildMode");
    }

    public void setBuildMode(boolean value) {
        Document document = CorePlugin.getInstance().getMongo().loadDocument("playerData", uuid);

        document.replace("staffBuildMode", value);

        CorePlugin.getInstance().getMongo().saveDocument("playerData", document, uuid);
    }

}











