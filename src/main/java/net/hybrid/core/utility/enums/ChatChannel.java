package net.hybrid.core.utility.enums;

import net.hybrid.core.utility.CC;

import java.util.Arrays;
import java.util.List;

public enum ChatChannel {

    ALL("", Arrays.asList("a", "all", "main", "mainchat")),
    GANG(CC.translate("&eGang Chat ➤&r"), Arrays.asList("g", "gang", "gangchat", "gchat")),
    PARTY(CC.translate("&dParty Chat ➤&r"), Arrays.asList("p", "party", "partychat", "pchat")),
    STAFF(CC.translate("&2Staff Chat ➤&r"), Arrays.asList("s", "staff", "staffchat", "schat")),
    BUILDER(CC.translate("&3Builder Chat ➤&r"), Arrays.asList("b", "build", "builder", "buildteam", "builderchat", "buildchat")),
    ADMIN(CC.translate("&cAdmin Chat ➤&r"), Arrays.asList("ad", "admin", "adminchat", "adchat")),
    OWNER(CC.translate("&4Owner Chat ➤&r"), Arrays.asList("o", "owner", "ownerchat", "ochat"));

    private final String prefix;
    private final List<String> aliases;

    ChatChannel(String prefix, List<String> aliases) {
        this.prefix = prefix;
        this.aliases = aliases;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<String> getAliases() {
        return aliases;
    }

}
