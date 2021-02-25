package driver;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Weapon {
    private String name;
    private int damage;
    private int price;

    public Weapon(String name, int damage, int price) {
        this.name = name;
        this.damage = damage;
        this.price = price;
    }

    public StackPane getWeaponDisplay() {
        StackPane weaponDisplay = new StackPane();
        Circle weapon = new Circle();
        weapon.setFill(Color.BLUE);
        weapon.setRadius(35);
        Text exitText = new Text("Weapon: " + name + "\ndamage: " + damage + "\nprice: " + price);
        exitText.setFill(Color.YELLOW);
        exitText.setFont(new Font(12));
        weaponDisplay.getChildren().addAll(weapon, exitText);
        return weaponDisplay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
