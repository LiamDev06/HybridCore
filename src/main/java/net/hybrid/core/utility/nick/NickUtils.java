package net.hybrid.core.utility.nick;

import com.mojang.authlib.GameProfile;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.utility.*;
import net.hybrid.core.utility.bookgui.BookUtil;
import net.hybrid.core.utility.enums.NickRank;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class NickUtils {

    public static ItemStack getNickStartBook(){
        return BookUtil.writtenBook()
                .author("Hybrid").title("Nick").pages(new BookUtil.PageBuilder()
                        .add("Nicking allows you to disguise yourself under another name, skin and rank.").newLine().newLine()
                        .add("All network rules still apply and must be followed.")
                        .newLine().newLine()
                        .add(BookUtil.TextBuilder.of("Click here to start").style(ChatColor.UNDERLINE)
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_jh48f73hajksjdfu4857fh")).build())
                        .build()
                )
                .build();
    }

    public static ItemStack getNickRankBook(){
        return BookUtil.writtenBook()
                .author("Hybrid").title("Nick").pages(new BookUtil.PageBuilder()
                        .add("Which rank would you like to appear as while nicked?").newLine().newLine()

                        .add(BookUtil.TextBuilder.of("» Member").color(ChatColor.GREEN).style(ChatColor.BOLD)
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_ghdfjhk798645iyukghdfs798igh_member")).build()).newLine()

                        .add(BookUtil.TextBuilder.of("» Iron").color(ChatColor.WHITE).style(ChatColor.BOLD)
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_ghdfjhk798645iyukghdfs798igh_iron")).build()).newLine()

                        .add(BookUtil.TextBuilder.of("» Diamond").color(ChatColor.AQUA).style(ChatColor.BOLD)
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_ghdfjhk798645iyukghdfs798igh_diamond")).build()).newLine()

                        .build()
                )
                .build();
    }

    public static ItemStack getNickSkinBook(){
        return BookUtil.writtenBook()
                .author("Hybrid").title("Nick").pages(new BookUtil.PageBuilder()
                        .add("Which skin would you like to appear as while nicked?").newLine().newLine()

                        .add(BookUtil.TextBuilder.of("» Steve skin")
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_8487djdji3947ufhash4_steve")).build()).newLine()

                        .add(BookUtil.TextBuilder.of("» Random skin")
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_8487djdji3947ufhash4_random")).build()).newLine()

                        .add(BookUtil.TextBuilder.of("» My own skin")
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_8487djdji3947ufhash4_own")).build()).newLine()

                        .build()
                )
                .build();
    }

    public static ItemStack getNickNameBook(){
        return BookUtil.writtenBook()
                .author("Hybrid").title("Nick").pages(new BookUtil.PageBuilder()
                        .add("Would you like a random name or create a custom name?").newLine().newLine()

                        .add(BookUtil.TextBuilder.of("» Random name")
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_jlk989856jk546kjdf985_random")).build()).newLine()

                        .add(BookUtil.TextBuilder.of("» Custom name")
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_jlk989856jk546kjdf985_custom")).build()).newLine()

                        .build()
                )
                .build();
    }

    public static ItemStack getNickCustomEnteredBook(String name){
        return BookUtil.writtenBook()
                .author("Hybrid").title("Nick").pages(new BookUtil.PageBuilder()
                        .add("You entered a name that looks like this:").newLine().newLine()
                        .add("§6§l" + name).newLine().newLine()

                        .add(BookUtil.TextBuilder.of("» Use name")
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_7789978345hjkkjhdfghjk345_use")).build()).newLine()

                        .add(BookUtil.TextBuilder.of("» Enter new")
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_7789978345hjkkjhdfghjk345_new")).build()).newLine()

                        .build()
                )
                .build();
    }

    public static ItemStack getNickInvalidBook(){
        return BookUtil.writtenBook()
                .author("Hybrid").title("Nick").pages(new BookUtil.PageBuilder()
                        .add("          §c§lOH NO!").newLine().add("§rThe name you entered is not valid.")
                        .newLine().newLine()

                        .add(BookUtil.TextBuilder.of("§3§l» Try again")
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_jlk989856jk546kjdf985_custom")).build()).newLine()

                        .build()
                )
                .build();
    }

    public static ItemStack getRandomNickGeneratedBook(String name){
        return BookUtil.writtenBook()
                .author("Hybrid").title("Nick").pages(new BookUtil.PageBuilder()
                        .add("A random nickname was generated, here you go:").newLine().newLine()
                        .add(name).newLine().newLine()

                        .add(BookUtil.TextBuilder.of("§a§l» Use name")
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_hkj897543khjdfs9807khj345_use")).build()).newLine()

                        .add(BookUtil.TextBuilder.of("§c§l» Generate new")
                                .onClick(BookUtil.ClickAction.runCommand("/hiddennick token_hkj897543khjdfs9807khj345_new")).build()).newLine()

                        .build()
                )
                .build();
    }

    public static ItemStack getNickDone(String fullNick){
        return BookUtil.writtenBook()
                .author("Hybrid").title("Nick").pages(new BookUtil.PageBuilder()
                        .add("You are now finished with creating your nickname!").newLine().newLine()

                        .add(CC.translate("You now appear as " + fullNick + " &ron the network."))

                        .newLine().newLine().newLine().newLine()

                        .add("Type §2§l/unnick §rto go back to your usual self.")

                        .build()
                )
                .build();
    }

    public static void anvilBookGui(Player player) {
        AnvilGUI gui = new AnvilGUI(player, event -> {
            if (event.getSlot() != AnvilGUI.AnvilSlot.OUTPUT) {
                event.setWillClose(false);
                event.setWillDestroy(false);
                return;
            }

            event.setWillDestroy(true);
            event.setWillClose(true);

            new BukkitRunnable() {
                @Override
                public void run() {
                    String input = event.getName().trim();

                    if (passedFilter(input)) {
                        BookUtil.openPlayer(player, NickUtils.getNickCustomEnteredBook(input));
                        new HybridPlayer(player.getUniqueId()).getDisguiseManager().getNick().setNickname(input).save();
                    } else {
                        BookUtil.openPlayer(player, NickUtils.getNickInvalidBook());
                    }

                }
            }.runTaskLater(CorePlugin.getInstance(), 5L);
        });

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.NAME_TAG).setDisplayName(" ").build());
        gui.open();
    }

    public static String generateRandomNickname() {
        boolean lastWasThe = false;
        boolean lastSpecialWords = false;

        StringBuilder nickname = new StringBuilder();
        String[] startingWords = {"Its", "Itz", "The", "_"};
        String[] specialWords = {"Epic", "Fantastic", "Awesome", "Cool", "Insane", "Crazy", "Deadly", "Dangerous", "Stone", "Iron", "Diamond", "Hyper", "Frosty", "Flaming", "Evil", "Mega", "Fighter", "Solider", "Emerald"};
        String[] names = {"Peter", "Alex", "Lisa", "Nova", "Simon", "Philip", "Edward", "Micheal", "Sonic", "Felicity", "Amelia", "Harper", "Olivia", "Clara", "Dylan", "Reuben", "Elliot"};
        String[] animals = {"Monkey", "Lion", "Zebra", "Dog", "Cat"};

        Random random = new Random();

        if (randomTrueFalse()) {
            if (randomTrueFalse()) {
                nickname.append(startingWords[random.nextInt(startingWords.length)]);
            } else {
                nickname.append(startingWords[random.nextInt(startingWords.length)].toLowerCase());
            }

            lastWasThe = true;
        }

        if (!lastWasThe && randomTrueFalse()) {
            if (randomTrueFalse()) {
                nickname.append("The");
            } else {
                nickname.append("the");
            }
        }

        if (randomTrueFalse()) {
            lastSpecialWords = true;
            if (randomTrueFalse()) {
                nickname.append(specialWords[random.nextInt(specialWords.length)]);
            } else {
                nickname.append(specialWords[random.nextInt(specialWords.length)].toLowerCase());
            }
        } else if (randomTrueFalse()) {
            nickname.append(names[random.nextInt(names.length)]);
        } else {
            nickname.append(names[random.nextInt(names.length)].toLowerCase());
        }

        if (lastSpecialWords) {
            if (randomTrueFalse()) {
                if (randomTrueFalse()) {
                    nickname.append(names[random.nextInt(names.length)]);
                } else {
                    nickname.append(names[random.nextInt(names.length)].toLowerCase());
                }
            } else if (randomTrueFalse()) {
                nickname.append(animals[random.nextInt(animals.length)]);
            } else {
                nickname.append(animals[random.nextInt(animals.length)].toLowerCase());
            }
        }

        if (randomTrueFalse()) {
            if (randomTrueFalse()) {
                nickname.append("Plays");
            } else {
                nickname.append("Playz");
            }
        }

        if (randomTrueFalse()) {
            int randomNumber = (int)(Math.random() * 9999.0D + 0.0D);
            nickname.append(randomNumber);
        }

        if (randomTrueFalse()) {
            nickname.append("_");
        }

        if (nickname.length() > 16 || !passedFilter(nickname.toString().trim())) {
            nickname = new StringBuilder().append(generateRandomNickname());
        }

        return nickname.toString().trim();
    }

    private static boolean randomTrueFalse() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public static NickRank getRandomNickRank() {
        ArrayList<NickRank> ranks = new ArrayList<>();
        ranks.add(NickRank.MEMBER);
        ranks.add(NickRank.IRON);
        ranks.add(NickRank.DIAMOND);

        int size = ranks.size();
        int random = new Random().nextInt(size);

        return ranks.get(random);
    }

    public static boolean passedFilter(String nickname) {

        /*
        Nickname length check to make sure it follows Minecraft usernames
        The "< 4" can be 3 but basically no 3 letters exists anymore :shrug:
         */
        if (nickname.length() > 16 || nickname.length() < 4) {
            return false;
        }

        /*
        Checking if the nickname contains any " " (spaces)
         */
        if (nickname.matches(".*([ \t]).*")) {
            return false;
        }

        /*
        Checking if the nickname contains any special characters (except _, underscore)
         */
        String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^`{|}";
        for (int i = 0; i < nickname.length(); i++) {
            char c = nickname.charAt(i);

            if (specialCharactersString.contains(Character.toString(c))) {
                return false;
            }
        }

        /*
        Checking if the nickname contains any pre-blacklisted names, e.g. staff names
         */
        ArrayList<String> blacklistedNames = new ArrayList<>();
        blacklistedNames.add("LiamHBest");
        blacklistedNames.add("MrAlfi");
        blacklistedNames.add("Farstar09");
        blacklistedNames.add("AlmondComic");
        blacklistedNames.add("Hexilion");
        blacklistedNames.add("Sqonge");
        blacklistedNames.add("PvP_v2");
        blacklistedNames.add("ashw24");
        blacklistedNames.add("hypixel");
        blacklistedNames.add("PewDiePie");

        if (blacklistedNames.contains(nickname)) {
            return false;
        }

        /*
        Running it through the network wide bad-words filter to make sure it doesn't include any of those
         */
        for (String s : BadWordsFilter.getBadWords()) {
            if (nickname.contains(s)) {
                return false;
            }
        }

        /*
        Making sure so the nick is not equal to any name that has joined the server before / is currently playing
         */
        if (nameHasJoinedServerBefore(nickname)) {
            return false;
        }

        return true;
    }

    public static boolean nameHasJoinedServerBefore(String name) {
        Document document = CorePlugin.getInstance().getMongo().loadDocument(
                "serverData", "serverDataType", "playerDataList"
        );

        Collection<Object> keys = document.values();
        Iterator iterator = keys.iterator();
        boolean value = false;

        while (iterator.hasNext()){
            Object key = iterator.next();

            if (key instanceof String) {
                String s = (String) key;

                if (s.equalsIgnoreCase(name)) {
                    value = true;
                    break;
                }
            }

        }

        return value;
    }

    public static File getRandomNickFile() {
        File[] listfiles = new File(skinsPath()).listFiles();

        int size = listfiles.length;
        int random = new Random().nextInt(size);

        return listfiles[random];
    }

    public static String skinsPath() {
        return CorePlugin.getInstance().getDataFolder() + "/nickskins";
    }
}










