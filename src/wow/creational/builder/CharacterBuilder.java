package wow.creational.builder;

import wow.creational.builder.attributes.HeroClass;
import wow.creational.builder.attributes.Race;
import wow.structural.decorator.BaseWeapon;
import wow.structural.decorator.Weapon;

/** Builder pattern - Builder (fluent, no Director needed): creates a Character step by step and checks it. */
public class CharacterBuilder {

    public static final int MAX_LEVEL = 60;
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 12;

    private String name;
    private Race race;
    private HeroClass heroClass;
    private String guild = "";
    private String title = "";
    private int level = 1;
    private Weapon weapon;

    public CharacterBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CharacterBuilder race(Race race) {
        this.race = race;
        return this;
    }

    public CharacterBuilder heroClass(HeroClass heroClass) {
        this.heroClass = heroClass;
        return this;
    }

    public CharacterBuilder guild(String guild) {
        this.guild = guild;
        return this;
    }

    public CharacterBuilder title(String title) {
        this.title = title;
        return this;
    }

    public CharacterBuilder level(int level) {
        this.level = level;
        return this;
    }

    public CharacterBuilder weapon(Weapon weapon) {
        this.weapon = weapon;
        return this;
    }

    public Character build() {
        validate();
        return new Character(this);
    }

    private void validate() {
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("Name is missing");
        }
        if (!isValidName(name)) {
            throw new IllegalStateException("Name must be " + MIN_NAME_LENGTH + "-" + MAX_NAME_LENGTH
                    + " letters: " + name);
        }
        if (race == null) {
            throw new IllegalStateException("Race is missing");
        }
        if (heroClass == null) {
            throw new IllegalStateException("Class is missing");
        }
        if (level < 1 || level > MAX_LEVEL) {
            throw new IllegalStateException("Level must be from 1 to " + MAX_LEVEL + ": " + level);
        }
        if (guild == null || title == null) {
            throw new IllegalStateException("Guild and title cannot be null (use empty text)");
        }
    }

    private boolean isValidName(String text) {
        if (text.length() < MIN_NAME_LENGTH || text.length() > MAX_NAME_LENGTH) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            // our own class is also called Character, so we write the full name
            if (!java.lang.Character.isLetter(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    String getName() {
        return name;
    }

    Race getRace() {
        return race;
    }

    HeroClass getHeroClass() {
        return heroClass;
    }

    String getGuild() {
        return guild;
    }

    String getTitle() {
        return title;
    }

    int getLevel() {
        return level;
    }

    /** No weapon given: every call makes a new starter weapon, so the builder can be reused. */
    Weapon getWeapon() {
        if (weapon == null) {
            return new BaseWeapon(heroClass.getStarterWeaponName(), heroClass.getStarterWeaponDamage());
        }
        return weapon;
    }
}
