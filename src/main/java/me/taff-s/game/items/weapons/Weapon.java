public class Weapon extends Item implements Equippable{
    private int damage;
    public WeaponClass wClass;
    public DamageType dType;

    //weapon constructor
    public Weapon(String itemName, String description, int price, int damage, WeaponClass wc, DamageType dt, int max){
        super(itemName, description, price);
        this.damage = damage;
        this.wClass = wc;
        this.dType = dt;
        setDurability(max);
    }

    public String getWeaponName() {
        return itemName;
    }

    public void setWeaponName(String name){
        itemName = name;
    }
    
    public int getDamage(){
        return damage;
    }

    public void setDamageType(DamageType dt){
        this.dType = dt;
    }

    public void setWeaponClass(WeaponClass wc){
        this.wClass = wc;
    }

    public WeaponClass getWeaponClass(){
        return wClass;
    }

    public DamageType getDamageType(){
        return dType;
    }

    @Override
    public void equip(Player player) {
        player.getEquipment().setWeapon(this);
        player.sendMessage("Equipped weapon: " + itemName);
    }
    
    @Override
    public void unequip(Player player) {
        Weapon weapon = player.equipment.getEquippedWeapon();
        if (weapon != NoWeapon.getInstance()) {
            player.inventory.addItem(weapon);
            player.equipment.setWeapon(NoWeapon.getInstance());
        } else {
            player.sendMessage("No weapon equipped");
        }
    }

}
