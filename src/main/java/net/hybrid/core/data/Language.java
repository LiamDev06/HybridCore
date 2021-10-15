package net.hybrid.core.data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.enums.LanguageType;
import org.bson.Document;

import java.util.UUID;

public class Language {

    private static final Mongo mongo = CorePlugin.getInstance().getMongo();

    public static String get(UUID uuid, String key) {
        Document document = mongo.loadDocument("playerData", uuid);
        LanguageType type = LanguageType.valueOf(document.getString("userLanguage").toUpperCase());

        MongoCollection<Document> collection = mongo.getCoreDatabase().getCollection("translationData");
        Document languageDoc = collection.find(Filters.eq("languageType", type.name())).first();

        if (languageDoc == null) {
            return CC.translate("&c&l[ERROR CODE 243X] &cThe user language you have selected caused an error. " +
                    "Please report this to staff as soon as possible!");
        }

        return languageDoc.getString(key);
    }

    public static String get(LanguageType languageType, String key) {
        MongoCollection<Document> collection = mongo.getCoreDatabase().getCollection("translationData");
        Document languageDoc = collection.find(Filters.eq("languageType", languageType.name())).first();

        if (languageDoc == null) {
            return CC.translate("&c&l[ERROR CODE 243X] &cThe user language you have selected caused an error. " +
                    "Please report this to staff as soon as possible!");
        }

        return languageDoc.getString(key);
    }

    public static void initLanguageManager(){
        MongoCollection<Document> languageCollection = mongo.getCoreDatabase().getCollection("translationData");
        for (LanguageType language : LanguageType.values()) {
            Document document = languageCollection.find(Filters.eq("languageType", language.name())).first();

            /**
             * Append all default lines that will need to be translated later
             */

            if (document == null) {
                document = new Document()
                        .append("languageType", language.name())
                        .append("no_permission", "&cYou do not have permission to perform this!")
                        .append("requires_admin", "&cYou must be an admin or above to perform this!")
                        .append("requires_sr_moderator", "&cYou must be a senior moderator or above to perform this!")
                        .append("requires_moderator", "&cYou must be a moderator or above to perform this!")
                        .append("requires_helper", "&cYou must be a helper or above to perform this!")
                        .append("missing_arguments", "&cMissing required arguments!")
                        .append("specify_player", "&cYou need to specify a player!")
                        .append("missing_arguments_lang", "&cMissing arguments! Please specify a language with &6/lang <language>&c.")
                        .append("available_languages", "&cThe available languages are:")
                        .append("invalid_language_type", "&cInvalid language type! Use &6/lang <language>&c. The available languages are:")
                        .append("language_updated", "&aYour language was updated to &6%%language%%&a.");
            }

            mongo.saveDocument("translationData", document, "languageType", language.name());
        }
    }

}








