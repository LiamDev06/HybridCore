package net.hybrid.core.commands;

import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.entity.Player;

public class EmoteCommand extends PlayerCommand {

    public EmoteCommand() {
        super("emotes", "emote", "emojis", "emoji");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().hasRank(PlayerRank.IRON)) {
            hybridPlayer.sendMessage("&cYou do not have access to use emotes!");
            return;
        }

        hybridPlayer.sendMessage("&7&m-------- &6&lEmoji List &7&m--------");
        hybridPlayer.sendMessage("&8- &a:cool: &f&l-> &a&lCool");
        hybridPlayer.sendMessage("&8- &a:shrug: &f&l-> &d¯\\_(ツ)_/¯");
        hybridPlayer.sendMessage("&8- &a:wow: &f&l-> &b&lWOW");
        hybridPlayer.sendMessage("&8- &ao/ &f&l-> &5(o_o)/");
        hybridPlayer.sendMessage("&8- &a:hybrid: &f&l-> &2&lHYBRID");
        hybridPlayer.sendMessage("&8- &a:L: &f&l-> &c&lL");
        hybridPlayer.sendMessage("&8- &a:sad: &f&l-> &e◕︵◕&r");
        hybridPlayer.sendMessage("&8- &a:happy: &f&l-> &6&l◕◡◕&r");
        hybridPlayer.sendMessage("&8- &a:embarrassed: &b⊙﹏⊙&r");
        hybridPlayer.sendMessage("&8- &a:eyes: &f&l-> &aʘ.ʘ&r");
        hybridPlayer.sendMessage("&8- &a:hehe: &f&l-> &0hehe");
    }

}









