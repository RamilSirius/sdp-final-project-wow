package wow.creational.factorymethod;

import java.util.ArrayList;
import java.util.List;

import wow.structural.decorator.Weapon;

/** Factory Method pattern - Creator: subclasses decide which enemies and which Weapon (Product) to create. */
public abstract class Dungeon {

    public abstract String getName();

    public abstract int getExperienceReward();

    public abstract int getTrashMobCount();

    /** Factory method: a normal enemy of this dungeon. */
    protected abstract Enemy createTrashMob();

    /** Factory method: the last boss of this dungeon. */
    protected abstract Enemy createBoss();

    /** Factory method: the item the boss drops. */
    protected abstract Weapon createBossLoot();

    /** Uses the factory methods: trash mobs first, then the boss. */
    public List<Enemy> spawnEnemies() {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < getTrashMobCount(); i++) {
            enemies.add(createTrashMob());
        }
        enemies.add(createBoss());
        return enemies;
    }

    public Weapon dropBossLoot() {
        return createBossLoot();
    }
}
