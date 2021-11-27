package net.hybrid.core.utility.nick;

import net.hybrid.core.utility.DisguiseManager;
import net.hybrid.core.utility.enums.NickRank;
import net.hybrid.core.utility.enums.NickSkinType;

import java.io.File;
import java.util.UUID;

public class Nick {

    private UUID uuid;
    private String nickname;
    private NickRank nickRank;
    private NickSkinType nickSkinType;
    private File nickFile;

    public Nick(UUID uuid, String nickname, NickRank nickRank, NickSkinType nickSkinType, File nickFile) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.nickRank = nickRank;
        this.nickSkinType = nickSkinType;
        this.nickFile = nickFile;
    }

    public UUID getOwner() {
        return uuid;
    }

    public Nick setOwner(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public NickSkinType getNickSkinType() {
        return nickSkinType;
    }

    public Nick setNickSkinType(NickSkinType nickSkinType) {
        this.nickSkinType = nickSkinType;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public Nick setNickname(String name) {
        this.nickname = name;
        return this;
    }

    public NickRank getNickRank() {
        return nickRank;
    }

    public Nick setNickRank(NickRank nickRank) {
        this.nickRank = nickRank;
        return this;
    }

    public File getNickFile() {
        return nickFile;
    }

    public void setNickFile(File nickFile) {
        this.nickFile = nickFile;
    }

    public Nick save() {
        if (DisguiseManager.nicksCache.containsKey(uuid)) {
            DisguiseManager.nicksCache.replace(uuid, this);
        } else {
            DisguiseManager.nicksCache.put(uuid, this);
        }

        return this;
    }

}
