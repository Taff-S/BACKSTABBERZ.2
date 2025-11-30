public class NeedleCharm extends Charm {
    public NeedleCharm() {
        super("The Needle", "Steal 1 HP from your resting partner.", 50);
    }

    @Override
    public void onRest(Player p1) {
        Player partner = p1.getPartner();
        if (partner != null && (partner.getRestType() == RestChoice.PEACEFUL)){
            partner.antiHeal(-3);
            p1.heal(3);
            p1.sendMessage(p1.getName() + " used The Needle to siphon health from " + partner.getName());
            partner.sendMessage("You feel a little weaker (-3hp)");
        }
    }
}