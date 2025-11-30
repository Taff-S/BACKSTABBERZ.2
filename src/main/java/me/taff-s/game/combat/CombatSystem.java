public class CombatSystem {

    public static CombatResult performAttack(Player attacker, Enemy enemy) {
        CombatResult result = new CombatResult();

        Weapon weapon = attacker.getEquipment().getEquippedWeapon();
        if (weapon.isBroken()) {
            result.addEvent(attacker.getName() + " tries to attack, but their weapon is broken!");
            return result;
        }

        int damage = weapon.getDamage();
        enemy.isHit(damage, weapon.getDamageType());
        weapon.reduceDurability(1);

        result.addEvent(attacker.getName() + " hits " + enemy.getType() + " for " + damage + " damage!");
        switch (weapon.getWeaponClass) {
            case SWORD:
                if (Math.random() < 0.2) {
                    enemy.reduceAttack(5);
                    result.addEvent(enemy.getType() + "'s attack has been reduced!");
                }
                break;
            case AXE:
                if (Math.random() < 0.2) {
                    enemy.reduceDefence(5);
                    result.addEvent(enemy.getType() + "'s defence has been reduced!");
                }
                break;
            case BLUNT:
                if (Math.random() < 0.15) {
                    enemy.applyConcussion(2);
                    result.addEvent(enemy.getType() + " is concussed and may miss their next turn!");
                }
                break;
            case HAMMER:
                if (Math.random() < 0.15) {
                    enemy.reduceArmour(10, 3);
                    result.addEvent(enemy.getType() + "'s armour has been temporarily reduced!");
                }
                break;
            case SPEAR:
                // Implement spear logic
                break;
            case BOW:
                // Implement bow logic
                break;
            case SCYTHE:
                int healAmount = damage / 4;
                attacker.heal(healAmount);
                result.addEvent(attacker.getName() + " steals " + healAmount + " health with the scythe!");
                break;
            default:
                break;
        }

        if (!enemy.living()) {
            result.addEvent(enemy.getType() + " has been defeated!");
            result.setEnemyDefeated(true);
        }
        return result;
    }

    public static CombatResult performDefend(Player player) {
        CombatResult result = new CombatResult();
        player.defend();
        result.addEvent(player.getName() + " takes a defensive stance.");
        return result;
    }

    public static CombatResult enemyAttack(Enemy enemy, Player target) {
        CombatResult result = new CombatResult();
        int dmg = enemy.attack();
        target.takeDamage(dmg, target.getEquipment().getEquippedArmour(), target.getStance(), false);
        result.addEvent(enemy.getType() + " attacks " + target.getName() + " for " + dmg + " damage!");
        if (!target.living()) {
            result.setPlayerDefeated(true);
            result.addEvent(target.getName() + " has been defeated!");
        }
        return result;
    }

    public static CombatResult playerAttack(Player attacker, Player defender, Weapon weapon, boolean defenderDefending) {
        CombatResult result = new CombatResult();
        // Calculate damage, apply to defender, add events
        // Example:
        int damage = weapon.getDamage();
        if (defenderDefending) damage /= 2;
        defender.takeDamage(damage, defender.getEquipment().getEquippedArmour(), defenderDefending, false);
        result.addEvent(attacker.getName() + " attacks " + defender.getName() + " for " + damage + " damage!");
        if (!defender.living()) {
            result.setPlayerDefeated(true);
            result.addEvent(defender.getName() + " has died!");
        }
        return result;
    }
}
