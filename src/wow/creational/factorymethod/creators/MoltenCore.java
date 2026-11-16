package wow.creational.factorymethod.creators;

import wow.creational.factorymethod.Dungeon;
import wow.creational.factorymethod.Enemy;
import wow.structural.decorator.BaseWeapon;
import wow.structural.decorator.Weapon;

/** Factory Method pattern - Concrete Creator: a raid that is far too hard for a small party. */
public class MoltenCore extends Dungeon {

    @Override
    public String getName() {
        return "Molten Core";
    }

    @Override
    public int getExperienceReward() {
        return 2000;
    }

    @Override
    public int getTrashMobCount() {
        return 2;
    }

    @Override
    protected Enemy createTrashMob() {
        return new Enemy("Molten Giant", 3000, 120, false);
    }

    @Override
    protected Enemy createBoss() {
        return new Enemy("Ragnaros", 20000, 400, true);
    }

    @Override
    protected Weapon createBossLoot() {
        return new BaseWeapon("Sulfuras, Hand of Ragnaros", 120);
    }
}
