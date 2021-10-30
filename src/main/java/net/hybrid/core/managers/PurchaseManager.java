package net.hybrid.core.managers;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Mongo;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.bookgui.BookUtil;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PurchaseManager implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Mongo mongo = CorePlugin.getInstance().getMongo();
        Document document = mongo.loadDocument("serverData", "serverDataType", "rankPurchases");

        if (document.containsKey(player.getUniqueId().toString())) {
            HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

            PlayerRank currentRank = hybridPlayer.getRankManager().getRank();
            PlayerRank targetRank = PlayerRank.valueOf(document.getString(player.getUniqueId().toString()).toUpperCase());

            if (targetRank.getOrdering() > currentRank.getOrdering()) {
                hybridPlayer.getRankManager().setRank(targetRank);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 15, 2);

                ItemStack book = BookUtil.writtenBook()
                        .author("Hybrid").title("Rank Purchase").pages(new BookUtil.PageBuilder()
                                .add(CC.translate("Congratulations on your rank purchase from &2&lHybrid Store&r!\n" +
                                        "\nYour account has been updated with the rank: " + targetRank.getPrefix() + "&r.\n\n" +
                                        "Contact &b&nsupport@hybridplays.c&b&nom &ror open a ticket in our discord if any errors occur."))
                                .build()
                        )
                        .build();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        BookUtil.openPlayer(player, book);
                    }
                }.runTaskLater(CorePlugin.getInstance(), 15);

            } else {
               hybridPlayer.sendMessage("&c&lIMPORTANT!! &cYour latest rank purchase " +
                       "from our store went wrong and you purchased a lower than you already " +
                       "have. This is not supposed to be possible. Please reach out to " +
                       "&b&nsupport@hybridplays.com &cas soon as possible!");
            }

            document.remove(player.getUniqueId().toString());
            mongo.saveDocument("serverData", document, "serverDataType", "rankPurchases");
        }
    }

}








