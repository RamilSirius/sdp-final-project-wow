package wow.structural.decorator.decorators;

import wow.structural.decorator.Weapon;
import wow.structural.decorator.WeaponEnchantment;

/** Decorator pattern - Concrete Decorator: a fire enchantment. */
public class FieryEnchantment extends WeaponEnchantment {

    public static final int BONUS_DAMAGE = 12;

    public FieryEnchantment(Weapon weapon) {
        super(weapon);
    }

    @Override
    public String getName() {
        return super.getName() + " + Fiery";
    }

    @Override
    public int getDamage() {
        return super.getDamage() + BONUS_DAMAGE;
    }
}
