package wow.creational.factorymethod;

/** Factory Method pattern - Product: a monster. The concrete dungeons decide which enemies are created. */
public class Enemy {

    private final String name;
    private final int maxHealth;
    private final int damage;
    private final boolean boss;
    private int currentHealth;

    public Enemy(String name, int maxHealth, int damage, boolean boss) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.damage = damage;
        this.boss = boss;
        this.currentHealth = maxHealth;
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public void takeDamage(int amount) {
        currentHealth = Math.max(0, currentHealth - amount);
    }

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isBoss() {
        return boss;
    }
}
