package wow.creational.builder;

import java.util.ArrayList;
import java.util.List;

import wow.creational.builder.attributes.Faction;
import wow.creational.builder.attributes.HeroClass;
import wow.creational.builder.attributes.Race;
import wow.structural.decorator.Weapon;

/** Builder pattern - Product: a hero, created only through CharacterBuilder. */
public class Character {

    private static final int HEALTH_PER_LEVEL = 25;
    private static final int DAMAGE_PER_LEVEL = 3;
    private static final int XP_PER_LEVEL = 100;

    private final String name;
    private final Race race;
    private final HeroClass heroClass;
    private final String guild;
    private final String title;
    private final List<Weapon> bag = new ArrayList<>();
    private int level;
    private int experience;
    private int currentHealth;
    private Weapon weapon;

    // package-private: only CharacterBuilder (same package) can create a Character
    Character(CharacterBuilder builder) {
        this.name = builder.getName();
        this.race = builder.getRace();
        this.heroClass = builder.getHeroClass();
        this.guild = builder.getGuild();
        this.title = builder.getTitle();
        this.level = builder.getLevel();
        this.weapon = builder.getWeapon();
        this.experience = 0;
        this.currentHealth = getMaxHealth();
    }

    public int getMaxHealth() {
        return heroClass.getBaseHealth() + (level - 1) * HEALTH_PER_LEVEL;
    }

    public int getDamage() {
        return heroClass.getBaseDamage() + weapon.getDamage() + (level - 1) * DAMAGE_PER_LEVEL;
    }

    public int getHealingPower() {
        return heroClass.getHealingPower();
    }

    public String getFullName() {
        if (title.isEmpty()) {
            return name;
        }
        return name + " " + title;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public void takeDamage(int amount) {
        currentHealth = Math.max(0, currentHealth - amount);
    }

    public void heal(int amount) {
        currentHealth = Math.min(getMaxHealth(), currentHealth + amount);
    }

    public void restoreHealth() {
        currentHealth = getMaxHealth();
    }

    public void equip(Weapon newWeapon) {
        this.weapon = newWeapon;
    }

    public void receiveLoot(Weapon item) {
        if (item.getDamage() > weapon.getDamage()) {
            bag.add(weapon); // the old weapon goes to the bag
            equip(item);
            System.out.println("[Loot] " + name + " equips " + item.getName() + " (" + item.getDamage() + " dmg)");
        } else {
            bag.add(item);
            System.out.println("[Loot] " + name + " puts " + item.getName() + " in the bag");
        }
    }

    /** Adds experience and returns how many levels were gained. */
    public int gainExperience(int xp) {
        experience = experience + xp;
        int levelsGained = 0;
        while (experience >= XP_PER_LEVEL && level < CharacterBuilder.MAX_LEVEL) {
            experience = experience - XP_PER_LEVEL;
            level++;
            levelsGained++;
        }
        if (levelsGained > 0) {
            restoreHealth();
        }
        return levelsGained;
    }

    public String describe() {
        String text = getFullName() + " - Level " + level + " " + race.getDisplayName() + " "
                + heroClass.getDisplayName() + " (" + heroClass.getRole() + ", "
                + getFaction().getDisplayName() + ")"
                + " | HP " + currentHealth + "/" + getMaxHealth()
                + " | DMG " + getDamage()
                + " | Weapon: " + weapon.getName() + " (" + weapon.getDamage() + ")"
                + " | Bag: " + bag.size();
        if (!guild.isEmpty()) {
            text = text + " | Guild: <" + guild + ">";
        }
        return text;
    }

    public Faction getFaction() {
        return race.getFaction();
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
