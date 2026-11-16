package wow.structural.decorator;

/** Decorator pattern - Concrete Component: a plain weapon without enchantments. */
public class BaseWeapon implements Weapon {

    private final String name;
    private final int damage;

    public BaseWeapon(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
