package wow.creational.factorymethod.creators;

import wow.creational.factorymethod.Dungeon;
import wow.creational.factorymethod.Enemy;
import wow.structural.decorator.BaseWeapon;
import wow.structural.decorator.Weapon;
import wow.structural.decorator.decorators.FieryEnchantment;

/** Factory Method pattern - Concrete Creator: worgen and their master Arugal. */
public class ShadowfangKeep extends Dungeon {

    @Override
    public String getName() {
        return "Shadowfang Keep";
    }

    @Override
    public int getExperienceReward() {
        return 150;
    }

    @Override
    public int getTrashMobCount() {
        return 3;
    }

    @Override
    protected Enemy createTrashMob() {
        return new Enemy("Shadowfang Worgen", 260, 22, false);
    }

    @Override
    protected Enemy createBoss() {
        return new Enemy("Archmage Arugal", 1100, 55, true);
    }

    @Override
    protected Weapon createBossLoot() {
        // a different concrete product: an already enchanted weapon (30 + 12 = 42 dmg)
        return new FieryEnchantment(new BaseWeapon("Shadowfang", 30));
    }
}
