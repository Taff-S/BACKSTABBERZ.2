public class NoWeapon extends Weapon{
    private static final NoWeapon instance = new NoWeapon();

    private NoWeapon(){
        super("Fists", "You don't have a weapon", 0, 0, WeaponClass.BLUNT, DamageType.FORCE,-1);
    }

    public static NoWeapon getInstance() {
        return instance;
    }

}