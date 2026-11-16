package wow.structural.facade.subsystems;

import java.util.List;

import wow.behavioral.observer.EventType;
import wow.behavioral.observer.GameEvent;
import wow.creational.builder.Character;
import wow.creational.factorymethod.Enemy;
import wow.creational.singleton.WorldServer;

/** Facade pattern - Subsystem: fights one enemy round by round. */
public class CombatSystem {

    private static final int MAX_ROUNDS = 100;

    /** Returns true if the party wins, false if the party wipes. */
    public boolean fight(List<Character> party, Enemy enemy) {
        System.out.println("[Combat] The party engages " + enemy.getName() + " (" + enemy.getMaxHealth() + " HP)");
        int round = 0;
        while (enemy.isAlive() && isPartyAlive(party) && round < MAX_ROUNDS) {
            round++;
            playRound(party, enemy);
        }
        if (enemy.isAlive()) {
            if (isPartyAlive(party)) {
                System.out.println("[Combat] " + enemy.getName() + " enrages after " + MAX_ROUNDS + " rounds");
            }
            System.out.println("[Combat] The party is wiped out after " + round + " rounds");
            publish(EventType.PARTY_WIPED, enemy.getName(), "The party was wiped out by " + enemy.getName());
            return false;
        }
        reportVictory(enemy, round);
        return true;
    }

    /** One round: the party attacks, the enemy hits the tank, the healers heal the tank. */
    private void playRound(List<Character> party, Enemy enemy) {
        enemy.takeDamage(partyDamage(party));
        if (!enemy.isAlive()) {
            return;
        }
        Character tank = firstAlive(party);
        tank.takeDamage(enemy.getDamage());
        if (!tank.isAlive()) {
            System.out.println("[Combat] " + tank.getName() + " has died");
            return;
        }
        tank.heal(partyHealing(party));
    }

    private void reportVictory(Enemy enemy, int round) {
        System.out.println("[Combat] " + enemy.getName() + " is defeated in round " + round);
        if (enemy.isBoss()) {
            publish(EventType.BOSS_DEFEATED, enemy.getName(), "The party has defeated " + enemy.getName() + "!");
        } else {
            publish(EventType.ENEMY_KILLED, enemy.getName(), enemy.getName() + " was slain");
        }
    }

    private int partyDamage(List<Character> party) {
        int total = 0;
        for (Character member : party) {
            if (member.isAlive()) {
                total = total + member.getDamage();
            }
        }
        return total;
    }

    private int partyHealing(List<Character> party) {
        int total = 0;
        for (Character member : party) {
            if (member.isAlive()) {
                total = total + member.getHealingPower();
            }
        }
        return total;
    }

    /** The first living member is the one who takes the hits (the tank). */
    private Character firstAlive(List<Character> party) {
        for (Character member : party) {
            if (member.isAlive()) {
                return member;
            }
        }
        return null;
    }

    private boolean isPartyAlive(List<Character> party) {
        return firstAlive(party) != null;
    }

    private void publish(EventType type, String subject, String message) {
        WorldServer.getInstance().getEvents().publish(new GameEvent(type, subject, message));
    }
}
