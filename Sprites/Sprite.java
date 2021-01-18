package Sprites;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Sprite {
    Rectangle rectangle;

    public Sprite(Color color, double x, double y, double width, double height) {
        rectangle = new Rectangle(x, y, width, height);
        rectangle.setFill(color);
    }

    public Shape getShape() {
        return rectangle;
    }

    public void updateColor(Color color) {
        rectangle.setFill(color);
    }
}
