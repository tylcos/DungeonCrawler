package game.inventory;

import core.ImageManager;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import utilities.RandomUtil;
import utilities.TimerUtil;

import java.util.function.Consumer;

/**
 * Stores weapon info and utility methods
 */
public class Weapon {
    private String     name;
    private WeaponType type;
    private int        damage;
    private double     fireRate;

    private Image image;

    private double radiusOffset;
    private double angleOffset;
    private double rotationOffset;

    public static final double WEAPON_HOLD_DISTANCE = 90d;
    public static final double SWORD_ATTACK_ANGLE   = 60d;
    public static final double SPEAR_ATTACK_RANGE   = 150d;
    public static final double SPEAR_ATTACK_ANGLE   = 10d;
    public static final double BOW_OFFSET           = -30d;

    /**
     * Creates an instance of a weapon.
     *
     * @param type the type
     * @param tier the tier
     */
    public Weapon(WeaponType type, int tier) {
        this(getRandomWeaponName(type), type, tier);
    }

    /**
     * Creates an instance of a weapon.
     *
     * @param name the name
     * @param type the type
     * @param tier the tier
     */
    public Weapon(String name, WeaponType type, int tier) {
        this.name = name;
        this.type = type;

        if (tier > 11) {
            tier = 11;
        }

        switch (type) {
        case Sword:
            angleOffset = 45;

            damage = tier / 4 + 1;
            fireRate = 1d - .25d * (tier % 4);
            break;
        case Spear:
        case Staff:
            damage = 2 * (tier / 4 + 1);
            fireRate = 1.5d - .25d * (tier % 4);
            break;
        case Bow:
            radiusOffset = BOW_OFFSET;
            rotationOffset = 180;

            damage = tier / 4 + 1;
            fireRate = 1d - .25d * (tier % 4);
            break;
        default:
            throw new IllegalArgumentException("Illegal Weapon type: " + type);
        }

        image = ImageManager.getSprite("weapons.png", type.getValue(), tier, 32, 3);
    }

    public void attack() {
        Consumer<Double> attackAction;

        switch (type) {
        case Sword:
            double direction = angleOffset >= 0 ? 1 : -1;
            attackAction = t -> angleOffset = direction * -SWORD_ATTACK_ANGLE * (2 * t - 1);
            break;
        case Spear:
            attackAction = t -> radiusOffset = SPEAR_ATTACK_RANGE * (1d - 2 * Math.abs(t - .5d));
            break;
        case Bow:
            attackAction = t -> radiusOffset = -50d * (.5d - Math.abs(t - .5d)) + BOW_OFFSET;
            break;
        case Staff:
            attackAction = t -> radiusOffset = -50d * (.5d - Math.abs(t - .5d));
            break;
        default:
            throw new IllegalArgumentException("Illegal Weapon type: " + type);
        }

        TimerUtil.lerp(fireRate / 8d, attackAction);
    }

    public Point3D getOffsets() {
        return new Point3D(radiusOffset, angleOffset, rotationOffset);
    }

    public Point2D getAttackRange() {
        double weaponSize = Math.hypot(image.getWidth(), image.getHeight()) / 2d;

        switch (type) {
        case Sword:
            return new Point2D(WEAPON_HOLD_DISTANCE + weaponSize, SWORD_ATTACK_ANGLE);
        case Spear:
            return new Point2D(WEAPON_HOLD_DISTANCE + SPEAR_ATTACK_RANGE + weaponSize,
                               SPEAR_ATTACK_ANGLE);
        default:
            return Point2D.ZERO;
        }
    }

    /**
     * Returns the name of the weapon.
     *
     * @return the name of the weapon
     */
    public String getName() {
        return name;
    }

    /**
     * The type of the weapon like ranged or sword.
     *
     * @return the type of the weapon
     */
    public WeaponType getType() {
        return type;
    }

    /**
     * Returns how much damage the weapon does.
     *
     * @return the amount of damage the weapon does
     */
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        if (damage < 0) {
            this.damage = Integer.MAX_VALUE;
            return;
        }

        this.damage = damage;
    }

    public double getFireRate() {
        return fireRate;
    }

    public Image getImage() {
        return image;
    }

    /**
     * The String output of the weapon.
     *
     * @return the String display of the weapon
     */
    @Override
    public String toString() {
        return String.format("%s %s, %d dmg %.2fs fire rate", name, type, damage, fireRate);
    }

    private static final String[][] WEAPON_NAMES = {
        {"Divine Light",
         "Storm-Weaver",
         "The Oculus",
         "Blood Infused Mageblade",
         "Vengeful Spellblade",
         "Desolation Copper Quickblade",
         "Blood Infused Skeletal Protector",
         "Shadowsteel-Call of Mourning",
         "Oblivion-Sabre of Magic",
         "Nethersbane-Runed Blade of Inception",
         "Abyssal Shard",
         "Ghostwalker",
         "Eternal Harmony",
         "Soul-Forged Mageblade",
         "Wind-Forged Slicer",
         "Venom Iron Sword",
         "Ghostly Steel Reaver",
         "Grasscutter-Bond of Trembling Hands",
         "The End-Betrayer of Shifting Sands",
         "Treachery-Soul of Torment"
        },
        {"Doombringer",
         "Crimson",
         "Peacemaker",
         "Engraved Axe",
         "Heartless Dualblade",
         "Firesoul Iron Battle Axe",
         "Greedy Obsidian Ravager",
         "Pride-Reaper of Magic",
         "Ashes-Doomblade of Blood",
         "Reign-Gift of the Incoming Storm",
         "Peacekeeper",
         "Catastrophe",
         "The End",
         "Wind's Chopper",
         "Liar's War Axe",
         "Reincarnated Steel Battle Axe",
         "Eerie Glass Edge",
         "Massacre-Gift of the Leviathan",
         "Epilogue-Axe of the South",
         "Peacemaker-War Axe of the Harvest"
        },
        {"Bolt",
         "Falling Star",
         "Honed Yew Bow",
         "Haunted Willow Heavy Crossbow",
         "Hurricane-Voice of the Caged Mind",
         "Arcus-Might of the Lone Wolf",
         "Twister-Dawn of the Sky",
         "Needle Threader",
         "Windlass",
         "Meteor",
         "Driftwood Hunting Bow",
         "Bone Crushing Ashwood Arbalest",
         "Hollow Warpwood Bolter",
         "Snipe-Piercer of Zeal",
         "Phantom Strike-Chord of the Talon",
         "Shadow Strike-Dawn of the Lost"
        },
        {"Dreamcaller",
         "Maelstrom",
         "Omen",
         "Venom Greatstaff",
         "Frost Warden Staff",
         "Grieving Sagewood Spiritstaff",
         "Destiny's Iron Warden Staff",
         "Treachery-Defender of Time",
         "Catastrophe-Executioner of Power",
         "Storm-Tribute of the Earth",
         "Anarchy",
         "Cloudscorcher",
         "Corrupted Will",
         "Mage's Scepter",
         "Bonecarvin Spire",
         "Barbarian Bone Energy Staff",
         "Cursed Sagewood Spiritstaff",
         "Lifebinder-Oath of the Beast",
         "The Void-Reach of Unholy Might",
         "Torment-Favor of the Blessed"
        }
    };

    public static String getRandomWeaponName(WeaponType type) {
        String[] tierNames = WEAPON_NAMES[type.getValue()];
        return tierNames[RandomUtil.getInt(tierNames.length)];
    }
}
