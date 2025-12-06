package me.taff_s.game.items.charms;

import me.taff_s.game.items.charms.Charm;
import me.taff_s.game.player.Player;

public class BackStabberCharm extends Charm {   
    
    public BackStabberCharm() {
        super("The Backstabber", "A dagger made not for battle, but for betrayal. Instantly kills resting partner, even if they're wary.", 50);
    }

    public void onBackstab(Player attacker, Player defender) {
        defender.setHealth(0);
        attacker.sendMessage(attacker.getName() + " used The Backstabber! " + defender.getName() + " was slain instantly.");
        defender.sendMessage(attacker.getName() + " used The Backstabber! " + defender.getName() + " was slain instantly.");

    }
}
