package wow.creational.builder.attributes;

/** Hero classes with their base stats and starter weapon. */
public enum HeroClass {
    WARRIOR("Warrior", "Tank", 320, 22, 0, "Worn Shortsword", 6),
    MAGE("Mage", "Damage", 170, 34, 0, "Apprentice Staff", 5),
    PRIEST("Priest", "Healer", 190, 12, 28, "Acolyte Mace", 4),
    HUNTER("Hunter", "Damage", 210, 30, 0, "Worn Shortbow", 6),
    ROGUE("Rogue", "Damage", 200, 32, 0, "Worn Dagger", 5);

    private final String displayName;
    private final String role;
    private final int baseHealth;
    private final int baseDamage;
    private final int healingPower;
    private final String starterWeaponName;
    private final int starterWeaponDamage;

    HeroClass(String displayName, String role, int baseHealth, int baseDamage,
              int healingPower, String starterWeaponName, int starterWeaponDamage) {
        this.displayName = displayName;
        this.role = role;
        this.baseHealth = baseHealth;
        this.baseDamage = baseDamage;
        this.healingPower = healingPower;
        this.starterWeaponName = starterWeaponName;
        this.starterWeaponDamage = starterWeaponDamage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRole() {
        return role;
    }

    public int getBaseHealth() {
        return baseHealth;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getHealingPower() {
        return healingPower;
    }

    public String getStarterWeaponName() {
        return starterWeaponName;
    }

    public int getStarterWeaponDamage() {
        return starterWeaponDamage;
    }
}
