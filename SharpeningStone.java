package wow.structural.decorator.decorators;

import wow.structural.decorator.Weapon;
import wow.structural.decorator.WeaponEnchantment;

/** Decorator pattern - Concrete Decorator: a sharpening stone. */
public class SharpeningStone extends WeaponEnchantment {

    public static final int BONUS_DAMAGE = 5;

    public SharpeningStone(Weapon weapon) {
        super(weapon);
    }

    @Override
    public String getName() {
        return super.getName() + " + Sharpened";
    }

    @Override
    public int getDamage() {
        return super.getDamage() + BONUS_DAMAGE;
    }
}
