package net.hybrid.core.utility.enums;

import org.bukkit.ChatColor;

public enum NickRank {

    DIAMOND(PlayerRank.DIAMOND.getNtePriority(), PlayerRank.DIAMOND.getPrefix(), PlayerRank.DIAMOND.getPrefixSpace(), PlayerRank.DIAMOND.getColor()),
    IRON(PlayerRank.IRON.getNtePriority(), PlayerRank.IRON.getPrefix(), PlayerRank.IRON.getPrefixSpace(), PlayerRank.IRON.getColor()),
    MEMBER(PlayerRank.MEMBER.getNtePriority(), PlayerRank.MEMBER.getPrefix(), PlayerRank.MEMBER.getPrefixSpace(), PlayerRank.MEMBER.getColor());

    private final int nteOrder;
    private final String prefix;
    private final String prefixSpace;
    private final ChatColor color;

    NickRank(int nteOrder, String prefix, String prefixSpace, ChatColor color) {
        this.nteOrder = nteOrder;
        this.prefix = prefix;
        this.prefixSpace = prefixSpace;
        this.color = color;
    }

    public int getNteOrder() {
        return nteOrder;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPrefixSpace() {
        return prefixSpace;
    }

    public ChatColor getColor() {
        return color;
    }
}
