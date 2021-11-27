package net.hybrid.core.commands;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import net.hybrid.core.utility.enums.LanguageType;
import org.bukkit.entity.Player;

public class LangCommand extends PlayerCommand {

    public LangCommand() {
        super("lang", "language");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        if (args.length == 0) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "missing_arguments_lang"));
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "available_languages") + getLanguages());
            return;
        }

        try {
            LanguageType type = LanguageType.valueOf(args[0].toUpperCase());
            hybridPlayer.getMetadataManager().setLanguageType(type);
            SoundManager.playSound(player, "NOTE_PLING");
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "language_updated").replace("%%language%%", type.name()));

        } catch (Exception exception) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "invalid_language_type") + getLanguages());
        }
    }

    private String getLanguages() {
        StringBuilder lang = new StringBuilder();
        lang.append(" ");

        for (LanguageType type : LanguageType.values()) {
            lang.append(type.name()).append(", ");
        }

        return lang.substring(0, lang.length() - 2);
    }

}
















