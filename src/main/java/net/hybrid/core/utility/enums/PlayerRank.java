package net.hybrid.core.utility.enums;

import net.hybrid.core.utility.CC;
import org.bukkit.ChatColor;

public enum PlayerRank {

    OWNER(ChatColor.DARK_RED, CC.translate("&4[OWNER]"), CC.translate("&4[OWNER] "), CC.translate("&4Owner"), true, 11),
    ADMIN(ChatColor.RED, CC.translate("&c[ADMIN]"), CC.translate("&c[ADMIN] "), CC.translate("&cAdmin"), true, 10),
    SENIOR_MODERATOR(ChatColor.GOLD, CC.translate("&6[SR MOD]"), CC.translate("&6[SR MOD] "), CC.translate("&6Senior Moderator"), true, 9),
    MODERATOR(ChatColor.GOLD, CC.translate("&6[MOD]"), CC.translate("&6[MOD] "), CC.translate("&6Moderator"), true, 8),
    HELPER(ChatColor.DARK_AQUA, CC.translate("&3[HELPER]"), CC.translate("&3[HELPER] "), CC.translate("&3Helper"), true, 7),
    PARTNER(ChatColor.DARK_GREEN, CC.translate("&2[PARTNER]"), CC.translate("&2[PARTNER] "), CC.translate("&2Partner"), false, 6),
    YOUTUBER(ChatColor.RED, CC.translate("&c[YOU&fTUBE&c]"), CC.translate("&c[YOU&fTUBE&c] "), CC.translate("&cYouTube"), false, 5),
    TWITCH_STREAMER(ChatColor.DARK_PURPLE, CC.translate("&5[TW&fICH&5]"), CC.translate("&5[TW&fICH&5] "), CC.translate("&5Twitch"), false, 4),
    DIAMOND(ChatColor.AQUA, CC.translate("&b[DIAMOND]"), CC.translate("&b[DIAMOND] "), CC.translate("&bDiamond"), false, 3),
    IRON(ChatColor.WHITE, CC.translate("&f[IRON]"), CC.translate("&f[IRON] "), CC.translate("&fIron"), false, 2),
    MEMBER(ChatColor.GREEN, CC.translate("&a"), CC.translate("&a"), CC.translate("&aMember"), false, 1);

    private final ChatColor color;
    private final String prefix;
    private final String prefixSpace;
    private final String displayName;
    private final int ordering;
    private final boolean isStaffRank;

    PlayerRank(ChatColor color, String prefix, String prefixSpace, String displayName, boolean isStaffRank, int ordering){
        this.color = color;
        this.prefix = prefix;
        this.prefixSpace = prefixSpace;
        this.displayName = displayName;
        this.ordering = ordering;
        this.isStaffRank = isStaffRank;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPrefixSpace() {
        return prefixSpace;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isStaffRank() {
        return isStaffRank;
    }

    public int getOrdering() {
        return ordering;
    }

}







