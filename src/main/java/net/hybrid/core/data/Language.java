package net.hybrid.core.data;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.enums.LanguageType;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.UUID;

public class Language {

    private static final Mongo mongo = CorePlugin.getInstance().getMongo();

    public static String get(UUID uuid, String key) {
        if (key.equalsIgnoreCase("requires_admin")) {
            return CC.translate("&cYou must be an admin or above to perform this!");
        }

        if (key.equalsIgnoreCase("no_permission")) {
            return CC.translate("&cYou do not have permission to perform this!");
        }

        if (key.equalsIgnoreCase("requires_helper")) {
            return CC.translate("&cYou must be a helper or above to perform this!");
        }

        if (key.equalsIgnoreCase("requires_moderator")) {
            return CC.translate("&cYou must be a moderator or above to perform this!");
        }

        if (key.equalsIgnoreCase("requires_sr_moderator")) {
            return CC.translate("&cYou must be a senior moderator or above to perform this!");
        }

        Document document = mongo.loadDocument("playerData", uuid);
        LanguageType type = LanguageType.valueOf(document.getString("userLanguage").toUpperCase());
        File file = new File(CorePlugin.getInstance().getDataFolder() + "/language", "lang_" + type.getCode() + ".json");

        JSONObject jsonObject;

        try {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse(new FileReader(file));

            jsonObject = (JSONObject) object;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "&c&lLANGUAGE ERROR! &cSomething went wrong with your language, and therefore the message could not be sent. Report this to staff NOW!!!";
        }

        if (jsonObject == null) {
            return "&c&lLANGUAGE ERROR! &cSomething went wrong with your language, and therefore the message could not be sent. Report this to staff NOW!!!";
        }

        return (String) jsonObject.get(key);
    }

    public static String get(LanguageType languageType, String key) {
        File file = new File(CorePlugin.getInstance().getDataFolder() + "/language", "lang_" + languageType.getCode() + ".json");

        JSONObject jsonObject;

        try {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse(new FileReader(file));

            jsonObject = (JSONObject) object;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "&c&lLANGUAGE ERROR! &cSomething went wrong with your language, and therefore the message could not be sent. Report this to staff NOW!!!";
        }

        if (jsonObject == null) {
            return "&c&lLANGUAGE ERROR! &cSomething went wrong with your language, and therefore the message could not be sent. Report this to staff NOW!!!";
        }

        return (String) jsonObject.get(key);
    }
}








