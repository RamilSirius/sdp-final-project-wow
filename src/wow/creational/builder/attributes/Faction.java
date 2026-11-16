package wow.creational.builder.attributes;

/** The two sides of the war. Each faction has its own capital city. */
public enum Faction {
    ALLIANCE("Alliance", "Stormwind"),
    HORDE("Horde", "Orgrimmar");

    private final String displayName;
    private final String capital;

    Faction(String displayName, String capital) {
        this.displayName = displayName;
        this.capital = capital;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCapital() {
        return capital;
    }
}
