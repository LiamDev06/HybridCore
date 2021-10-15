package net.hybrid.core.utility;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.utility.enums.ChatChannel;
import net.hybrid.core.utility.enums.LanguageType;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.HashMap;
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

    public ChatChannel getChatChannel() {
        Document document = CorePlugin.getInstance().getMongo().loadDocument("playerData", uuid);
        return ChatChannel.valueOf(document.getString("chatChannel").toUpperCase());
    }

    public void setChatChannel(ChatChannel chatChannel) {
        Document document = CorePlugin.getInstance().getMongo().loadDocument("playerData", uuid);

        document.replace("chatChannel", chatChannel.name());

        CorePlugin.getInstance().getMongo().saveDocument("playerData", document, uuid);
    }

    public void setLanguageType(LanguageType languageType) {
        Document document = CorePlugin.getInstance().getMongo().loadDocument("playerData", uuid);

        document.replace("userLanguage", languageType.name());

        CorePlugin.getInstance().getMongo().saveDocument("playerData", document, uuid);
    }

    public LanguageType getLanguageType() {
        Document document = CorePlugin.getInstance().getMongo().loadDocument("playerData", uuid);
        return LanguageType.valueOf(document.getString("userLanguage").toUpperCase());
    }
}











