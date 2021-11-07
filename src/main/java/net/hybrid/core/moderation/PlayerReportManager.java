package net.hybrid.core.moderation;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Mongo;
import org.apache.commons.lang.RandomStringUtils;
import org.bson.Document;

import java.util.UUID;

public class PlayerReportManager {

    public static void createNewReport(UUID issuer, UUID against, String reason, long created) {
        Mongo mongo = CorePlugin.getInstance().getMongo();
        Document document = new Document();

        document.append("reportId", RandomStringUtils.randomAlphanumeric(10));
        document.append("status", "open");
        document.append("punishmentIssued", false);
        document.append("punishmentIssuedId", "");
        document.append("issuerUUID", issuer.toString());
        document.append("againstUUID", against.toString());
        document.append("reason", reason.trim());
        document.append("created", created);

        mongo.saveDocument("playerReports", document);
    }



}
