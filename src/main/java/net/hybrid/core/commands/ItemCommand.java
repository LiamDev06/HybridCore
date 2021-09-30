package net.hybrid.core.commands;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand extends PlayerCommand {

    public ItemCommand(){
        super("item");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Missing arguments! Use /item <item>");
            return;
        }

        try {
            Material material = Material.valueOf(args[0].toUpperCase());

            if (args.length > 1) {
                try {
                    int amount = Integer.parseInt(args[1]);
                    ItemStack giveItem = new ItemStack(material, amount);

                    if (player.getInventory().firstEmpty() != -1) {
                        hybridPlayer.sendMessage("&aAdded " + amount + " &e" + material.name().replace("_", " ") + " &ato your inventory.");
                        SoundManager.playSound(player, Sound.NOTE_PLING, 10, 3);
                        player.getInventory().addItem(giveItem);
                        return;
                    }
                } catch (Exception exception) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        hybridPlayer.sendMessage("&cThat is not a valid number!");
                        return;
                    }

                    if (target.getInventory().firstEmpty() != -1) {
                        ItemStack giveItem = new ItemStack(material, 64);
                        HybridPlayer hybridTarget = new HybridPlayer(target.getUniqueId());

                        hybridPlayer.sendMessage(
                                "&aAdded " + "64" + " &e" + material.name().replace("_", " ") + " &ato " +
                                        hybridTarget.getRankManager().getRank().getPrefixSpace() + target.getName() + "&a's inventory."
                        );
                        hybridTarget.sendMessage(
                                hybridPlayer.getRankManager().getRank().getPrefixSpace() +
                                        player.getName() + " &aadded 64 &e" + material.name().replace("_", " ") + " &ato your inventory."
                        );
                        SoundManager.playSound(player, Sound.NOTE_PLING, 10, 3);
                        target.getInventory().addItem(giveItem);
                        return;
                    }

                    return;
                }

            } else {
                if (player.getInventory().firstEmpty() != -1) {
                    hybridPlayer.sendMessage("&aAdded " + "64" + " &e" + material.name().replace("_", " ") + " &ato your inventory.");

                    ItemStack giveItem = new ItemStack(material, 64);
                    SoundManager.playSound(player, Sound.NOTE_PLING, 10, 3);
                    player.getInventory().addItem(giveItem);
                    return;
                }
            }

            hybridPlayer.sendMessage("&cYour inventory is full!");

        } catch (Exception exception) {
            player.sendMessage(CC.RED + "The item you are looking for does not exist!");
        }
    }
}















