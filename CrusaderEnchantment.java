package wow.structural.decorator.decorators;

import wow.structural.decorator.Weapon;
import wow.structural.decorator.WeaponEnchantment;

/** Decorator pattern - Concrete Decorator: the holy Crusader enchantment. */
public class CrusaderEnchantment extends WeaponEnchantment {

    public static final int BONUS_DAMAGE = 20;

    public CrusaderEnchantment(Weapon weapon) {
        super(weapon);
    }

    @Override
    public String getName() {
        return super.getName() + " + Crusader";
    }

    @Override
    public int getDamage() {
        return super.getDamage() + BONUS_DAMAGE;
    }
}
