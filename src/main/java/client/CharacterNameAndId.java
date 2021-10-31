package client;

import com.github.mrzhqiang.maplestory.domain.DCharacter;

public class CharacterNameAndId {

    public final DCharacter character;
    private final String group;

    public CharacterNameAndId(DCharacter character, String group) {
        this.character = character;
        this.group = group;
    }

    public int getId() {
        return character.id;
    }

    public String getName() {
        return character.name;
    }

    public String getGroup() {
        return group;
    }

    public int getLevel() {
        return character.level;
    }

    public int getJob() {
        return character.job;
    }
}
