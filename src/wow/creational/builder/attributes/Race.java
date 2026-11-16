package wow.creational.builder.attributes;

/** Playable races. The race decides the faction of a hero. */
public enum Race {
    HUMAN("Human", Faction.ALLIANCE),
    DWARF("Dwarf", Faction.ALLIANCE),
    NIGHT_ELF("Night Elf", Faction.ALLIANCE),
    ORC("Orc", Faction.HORDE),
    UNDEAD("Undead", Faction.HORDE),
    TAUREN("Tauren", Faction.HORDE),
    TROLL("Troll", Faction.HORDE);

    private final String displayName;
    private final Faction faction;

    Race(String displayName, Faction faction) {
        this.displayName = displayName;
        this.faction = faction;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Faction getFaction() {
        return faction;
    }
}
