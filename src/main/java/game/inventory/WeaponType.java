package game.inventory;

import utilities.RandomUtil;

public enum WeaponType {
    Sword(0), Spear(1), Staff(2), Bow(3);

    private final int value;

    private static final WeaponType[] WEAPON_TYPES = WeaponType.values();

    WeaponType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static WeaponType fromValue(int value) {
        return WEAPON_TYPES[value];
    }

    public static WeaponType random() {
        return WEAPON_TYPES[RandomUtil.getInt(WEAPON_TYPES.length)];
    }
}
