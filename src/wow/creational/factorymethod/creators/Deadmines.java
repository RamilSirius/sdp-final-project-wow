package wow.creational.factorymethod.creators;

import wow.creational.factorymethod.Dungeon;
import wow.creational.factorymethod.Enemy;
import wow.structural.decorator.BaseWeapon;
import wow.structural.decorator.Weapon;

/** Factory Method pattern - Concrete Creator: pirates of the Defias Brotherhood. */
public class Deadmines extends Dungeon {

    @Override
    public String getName() {
        return "The Deadmines";
    }

    @Override
    public int getExperienceReward() {
        return 100;
    }

    @Override
    public int getTrashMobCount() {
        return 3;
    }

    @Override
    protected Enemy createTrashMob() {
        return new Enemy("Defias Pirate", 150, 14, false);
    }

    @Override
    protected Enemy createBoss() {
        return new Enemy("Edwin VanCleef", 700, 40, true);
    }

    @Override
    protected Weapon createBossLoot() {
        return new BaseWeapon("Cruel Barb", 30);
    }
}
