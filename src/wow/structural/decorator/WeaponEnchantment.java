package wow.structural.decorator;

/** Decorator pattern - Base Decorator: wraps another weapon and passes calls to it. */
public abstract class WeaponEnchantment implements Weapon {

    protected final Weapon weapon;

    protected WeaponEnchantment(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public String getName() {
        return weapon.getName();
    }

    @Override
    public int getDamage() {
        return weapon.getDamage();
    }
}
