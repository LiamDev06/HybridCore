package net.hybrid.core.utility;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class ServerCommand extends Command {

    public ServerCommand(String command) {
        super(command);
        init();
    }

    public ServerCommand(String command, String... aliases) {
        super(command, "", "/" + command, Arrays.asList(aliases));
        init();
    }

    private void init() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap)bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register("command", this);
        } catch (IllegalAccessException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
    }

    public abstract void onPlayerCommand(Player player, String[] args);
    public abstract void onConsoleCommand(CommandSender sender, String[] args);

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof Player) {
            this.onPlayerCommand((Player) commandSender, strings);
        } else {
            this.onConsoleCommand(commandSender, strings);
        }

        return false;
    }
}





