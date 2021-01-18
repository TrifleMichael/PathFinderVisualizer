package Sprites;

import App.ColorPalette;
import Grid.CellState;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class SpriteManager {
    double panelX;
    double panelY;

    int xFields;
    int yFields;

    Pane root;
    HashMap<Integer, Sprite> sprites = new HashMap<>();

    public SpriteManager(Pane root, double panelX, double panelY, int xFields, int yFields) {

        this.root = root;

        this.panelX = panelX;
        this.panelY = panelY;

        this.xFields = xFields;
        this.yFields = yFields;

        for(int i = 0; i < yFields; i++) {
            for (int j = 0; j < xFields; j++) {
                Sprite newSprite = new Sprite(Color.DARKBLUE, j*panelX/xFields, i*panelY/yFields, panelX/xFields, panelY/yFields);
                sprites.put(i*xFields+j, newSprite);
                root.getChildren().add(newSprite.getShape());
            }
        }
    }

    public void changeSize(int x, int y) {
        this.xFields = x;
        this.yFields = y;

        for(int i = 0; i < yFields; i++) {
            for (int j = 0; j < xFields; j++) {
                Sprite newSprite = new Sprite(Color.DARKBLUE, j*panelX/xFields, i*panelY/yFields, panelX/xFields+1, panelY/yFields+1);
                sprites.put(i*xFields+j, newSprite);
                root.getChildren().add(newSprite.getShape());
            }
        }
    }

    Sprite getSprite(int x, int y) {
        return sprites.get(y*xFields+x);
    }

    /*
    public void updateCellColor(int x, int y, Position position) {
        getSprite(x, y).updateColor(colorPalette(position));
    }

     */

    public void updateCellColor(int code, CellState cellState) {
        if (!sprites.containsKey(code)) {
            throw new IllegalArgumentException("Sprite with key "+code+" doesn't exist");
        }
        sprites.get(code).updateColor(colorPalette(cellState));
    }

    public Color colorPalette(CellState cellState) {
        return ColorPalette.getColor(cellState);
    }


}
