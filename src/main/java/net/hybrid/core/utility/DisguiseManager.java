package net.hybrid.core.utility;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Mongo;
import net.hybrid.core.utility.enums.NickRank;
import net.hybrid.core.utility.enums.NickSkinType;
import net.hybrid.core.utility.nick.Nick;
import net.hybrid.core.utility.nick.NickUtils;
import net.hybrid.core.utility.skinapi.SkinsAPI;
import net.hybrid.core.utility.skinapi.skins.SkinsManager;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DisguiseManager {

    public static final ArrayList<UUID> vanishedPlayersCache = new ArrayList<>();
    public static final HashMap<UUID, Nick> nicksCache = new HashMap<>();
    public static final ArrayList<UUID> nickedPlayersCache = new ArrayList<>();

    private final SkinsManager skinsManager = SkinsAPI.getInstance().getSkinsManager();
    private final Mongo mongo = CorePlugin.getInstance().getMongo();
    private final UUID uuid;

    public DisguiseManager(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isVanished() {
        return vanishedPlayersCache.contains(uuid);
    }

    public void setVanished(boolean value) {
        if (value) {
            vanishedPlayersCache.remove(uuid);
            vanishedPlayersCache.add(uuid);
        } else {
            vanishedPlayersCache.remove(uuid);
        }

        Document document = mongo.loadDocument("playerData", uuid);
        document.replace("vanished", value);
        mongo.saveDocument("playerData", document, uuid);
    }

    public void setNicked(boolean value) {
        if (value) {
            nickedPlayersCache.remove(uuid);
            nickedPlayersCache.add(uuid);
        } else {
            nickedPlayersCache.remove(uuid);
        }

        Document document = mongo.loadDocument("playerData", uuid);
        document.replace("nicked", value);
        mongo.saveDocument("playerData", document, uuid);
    }

    public boolean isNicked() {
        return nickedPlayersCache.contains(uuid);
    }

    public void nick(Nick nick) {
        Player player = Bukkit.getPlayer(uuid);
        nickedPlayersCache.add(uuid);

        if (player != null) {
            CommandSender sender = Bukkit.getConsoleSender();
            Bukkit.dispatchCommand(sender, "nte player " + player.getName() + " clear");
            GameMode gameMode = player.getGameMode();
            boolean isFlying = player.isFlying();
            boolean allowFlight = player.getAllowFlight();

            if (nick.getNickSkinType() == NickSkinType.STEVE) {
                try {
                    File file = new File(NickUtils.skinsPath(), "steve.png");
                    nick.setNickFile(file);

                    SkinsAPI.getInstance().getSkinsManager().setPlayerSkin(player,
                            SkinsAPI.getInstance().getSkinsManager().getImageSkin(file),true);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    player.sendMessage("§cThe nick-skin could not be set due to an error! Please report this.");
                }
            }

            if (nick.getNickSkinType() == NickSkinType.RANDOM) {
                try {
                    File file = NickUtils.getRandomNickFile();
                    nick.setNickFile(file);

                    SkinsAPI.getInstance().getSkinsManager().setPlayerSkin(player,
                            SkinsAPI.getInstance().getSkinsManager().getImageSkin(file),true);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    player.sendMessage("§cThe nick-skin could not be set due to an error! Please report this.");
                }
            }

            Bukkit.dispatchCommand(sender, "nte player " + player.getName() + " prefix '" + nick.getNickRank().getPrefixSpace() + "'");
            Bukkit.dispatchCommand(sender, "nte player " + player.getName() + " priority " + nick.getNickRank().getNteOrder());

            if (isVanished()) {
                Bukkit.dispatchCommand(sender, "nte player " + player.getName() + " suffix ' &5[V]'");
            }

            player.setGameMode(gameMode);
            player.setFlying(isFlying);
            player.setAllowFlight(allowFlight);

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setDisplayName(nick.getNickRank().getPrefixSpace() + nick.getNickname());

                    if (isVanished()) {
                        player.setPlayerListName(nick.getNickRank().getPrefixSpace() + nick.getNickname() + " §5[V]");
                    } else {
                        player.setPlayerListName(nick.getNickRank().getPrefixSpace() + nick.getNickname());
                    }
                }
            }.runTaskLater(CorePlugin.getInstance(), 5);

            Document document = mongo.loadDocument("playerData", uuid);
            document.replace("nicked", true);
            document.replace("nickNickname", nick.getNickname());
            document.replace("nickRank", nick.getNickRank().name());
            document.replace("nickSkinFileName", nick.getNickFile().getName());
            mongo.saveDocument("playerData", document, uuid);
        }
    }

    public void unnick() {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

            CommandSender sender = Bukkit.getConsoleSender();
            Bukkit.dispatchCommand(sender, "nte player " + player.getName() + " clear");

            try {
                skinsManager.resetPlayerSkin(player, true);
            } catch (Exception exception) {
                exception.printStackTrace();
                player.sendMessage("§cYour skin could not be reset, leave and rejoin the server again. Report this to staff ASAP!");
            }

            Bukkit.dispatchCommand(sender, "nte player " + player.getName() + " prefix '" + hybridPlayer.getRankManager().getRank().getPrefixSpace() + "'");
            Bukkit.dispatchCommand(sender, "nte player " + player.getName() + " priority " + hybridPlayer.getRankManager().getRank().getNtePriority());

            if (isVanished()) {
                Bukkit.dispatchCommand(sender, "nte player " + player.getName() + " suffix ' &5[V]'");
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setDisplayName(player.getName());
                    player.setPlayerListName(player.getName());
                }
            }.runTaskLater(CorePlugin.getInstance(), 5);

            Document document = mongo.loadDocument("playerData", uuid);
            document.replace("nicked", false);
            mongo.saveDocument("playerData", document, uuid);
        }

        nickedPlayersCache.remove(uuid);
        nicksCache.remove(uuid);
    }

    public Nick getNick() {
        if (nicksCache.containsKey(uuid)) {
            return nicksCache.get(uuid);
        }

        return new Nick(uuid, "", NickRank.MEMBER, NickSkinType.RANDOM, new File(NickUtils.skinsPath(), "steve.png")).save();
    }

    public Nick pullNickFromDatabase() {
        Document document = mongo.loadDocument("playerData", uuid);

        Nick nick = new Nick(
                uuid,
                document.getString("nickNickname"),
                NickRank.valueOf(document.getString("nickRank").toUpperCase()),
                NickSkinType.RANDOM,
                new File(NickUtils.skinsPath(), document.getString("nickSkinFileName"))
        );

        if (document.getString("nickSkinFileName").equalsIgnoreCase("steve.png")) {
            nick.setNickSkinType(NickSkinType.STEVE);
        }

        if (document.getString("nickSkinFileName").equalsIgnoreCase("self")) {
            nick.setNickSkinType(NickSkinType.OWN);
        }

        return nick;
    }

}












