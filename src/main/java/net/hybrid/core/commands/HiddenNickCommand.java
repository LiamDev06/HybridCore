package net.hybrid.core.commands;

import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.ServerType;
import net.hybrid.core.utility.nick.NickUtils;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.bookgui.BookUtil;
import net.hybrid.core.utility.enums.NickRank;
import net.hybrid.core.utility.enums.NickSkinType;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class HiddenNickCommand extends PlayerCommand {

    public HiddenNickCommand() {
        super("hiddennick");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (!player.getName().equalsIgnoreCase("LiamHBest")) {
            return;
        }

        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().hasRank(PlayerRank.TWITCH_STREAMER)) return;
        if (hybridPlayer.getServerManager().getServerType() != ServerType.HUB) return;

        if (args[0].contains("token_jh48f73hajksjdfu4857fh")) {
            hybridPlayer.sendMessage("&7Starting the nicking process...");
            BookUtil.openPlayer(player, NickUtils.getNickRankBook());
            return;
        }

        if (args[0].contains("token_ghdfjhk798645iyukghdfs798igh")) {
            if (args[0].endsWith("_member")) {
                hybridPlayer.getDisguiseManager().getNick().setNickRank(NickRank.MEMBER).save();
                hybridPlayer.sendMessage("&7You selected the member rank...");
            }

            if (args[0].endsWith("_iron")) {
                hybridPlayer.getDisguiseManager().getNick().setNickRank(NickRank.IRON).save();
                hybridPlayer.sendMessage("&7You selected the iron rank...");
            }

            if (args[0].endsWith("_diamond")) {
                hybridPlayer.getDisguiseManager().getNick().setNickRank(NickRank.DIAMOND).save();
                hybridPlayer.sendMessage("&7You selected the diamond rank...");
            }

            BookUtil.openPlayer(player, NickUtils.getNickSkinBook());
            return;
        }

        if (args[0].contains("token_8487djdji3947ufhash4")) {
            if (args[0].endsWith("_steve")) {
                hybridPlayer.getDisguiseManager().getNick().setNickSkinType(NickSkinType.STEVE).save();
                hybridPlayer.sendMessage("&7You selected the steve skin...");
            }

            if (args[0].endsWith("_random")) {
                hybridPlayer.getDisguiseManager().getNick().setNickSkinType(NickSkinType.RANDOM).save();
                hybridPlayer.sendMessage("&7You selected the steve skin...");
            }

            if (args[0].endsWith("_own")) {
                hybridPlayer.getDisguiseManager().getNick().setNickSkinType(NickSkinType.OWN).save();
                hybridPlayer.sendMessage("&7You selected the steve skin...");
            }

            BookUtil.openPlayer(player, NickUtils.getNickNameBook());
            return;
        }

        if (args[0].contains("token_jlk989856jk546kjdf985")) {
            if (args[0].endsWith("_random")) {
                hybridPlayer.sendMessage("&7Generating a random nickname...");

                String random = NickUtils.generateRandomNickname();
                BookUtil.openPlayer(player, NickUtils.getRandomNickGeneratedBook(random));

                hybridPlayer.getDisguiseManager().getNick().setNickname(random).save();
            }

            if (args[0].endsWith("_custom")) {
                hybridPlayer.sendMessage("&7Please enter a custom nickname...");
                NickUtils.anvilBookGui(player);
            }
        }

        if (args[0].contains("hkj897543khjdfs9807khj345")) {
            if (args[0].endsWith("_new")) {
                player.chat("/hiddennick token_jlk989856jk546kjdf985_random");
            }

            if (args[0].endsWith("_use")) {
                String nickname = hybridPlayer.getDisguiseManager().getNick().getNickname();
                NickRank nickRank = hybridPlayer.getDisguiseManager().getNick().getNickRank();

                String full = nickRank.getPrefix() + " " + nickname;
                BookUtil.openPlayer(player, NickUtils.getNickDone(full));

                hybridPlayer.getDisguiseManager().nick(hybridPlayer.getDisguiseManager().getNick());

                hybridPlayer.sendMessage("&aYou are now nicked! Perform &6/unnick &ato get back to your usual self!");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 2);
            }
        }

        if (args[0].contains("token_7789978345hjkkjhdfghjk345")) {
            if (args[0].endsWith("_new")) {
                player.chat("/hiddennick token_jlk989856jk546kjdf985_custom");
            }

            if (args[0].endsWith("_use")) {
                String nickname = hybridPlayer.getDisguiseManager().getNick().getNickname();
                NickRank nickRank = hybridPlayer.getDisguiseManager().getNick().getNickRank();

                String full = nickRank.getPrefix() + " " + nickname;
                BookUtil.openPlayer(player, NickUtils.getNickDone(full));

                hybridPlayer.getDisguiseManager().nick(hybridPlayer.getDisguiseManager().getNick());

                hybridPlayer.sendMessage("&aYou are now nicked! Perform &6/unnick &ato get back to your usual self!");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 2);
            }
        }
    }
}

















