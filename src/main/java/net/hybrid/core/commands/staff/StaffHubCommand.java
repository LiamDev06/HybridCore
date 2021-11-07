package net.hybrid.core.commands.staff;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class StaffHubCommand extends PlayerCommand {

    public StaffHubCommand() {
        super("staffhub", "build", "buildserver", "staffserver");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isStaff()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&7&m----------------------------");
            hybridPlayer.sendMessage("&cYou are about to swap to the staff hub, are you sure?");
            hybridPlayer.sendMessage(" ");

            TextComponent text = new TextComponent(CC.translate("&eClick &a&lHERE &eif you still want to proceed"));
            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate("&7Click to teleport")).create()));
            text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/staffhub fdj58fjdZD6jf84jskgfj6jg5e4t5"));
            player.spigot().sendMessage(text);

            hybridPlayer.sendMessage("&7&m----------------------------");
            return;
        }

        if (args[0].equals("fdj58fjdZD6jf84jskgfj6jg5e4t5")) {
            hybridPlayer.sendMessage("&7Sending you to the staff server...");
            hybridPlayer.sendToServer("build");

        } else {
            hybridPlayer.sendMessage("&7&m----------------------------");
            hybridPlayer.sendMessage("&cYou are about to swap to the staff hub, are you sure?");
            hybridPlayer.sendMessage(" ");

            TextComponent text = new TextComponent(CC.translate("&eClick &a&lHERE &eif you still want to proceed"));
            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate("&7Click to teleport")).create()));
            text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/staffhub fdj58fjdZD6jf84jskgfj6jg5e4t5"));
            player.spigot().sendMessage(text);

            hybridPlayer.sendMessage("&7&m----------------------------");
        }
    }
}












