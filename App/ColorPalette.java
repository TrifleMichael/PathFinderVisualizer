package App;

import Grid.CellState;
import javafx.scene.paint.Color;

public class ColorPalette {

    public static Color getColor(CellState cellState) {
        switch (cellState) {
            case EMPTY:
                return Color.BLACK;
            case VISITED:
                return Color.DARKRED;
            case OBSTRUCTED:
                return Color.BLUE;
            case CHOSEN:
                return Color.GOLD;
            default:
                return Color.WHITE;
        }
    }


    public static String legend() {
        return "Empty: BLACK"+"\n"+
                "Visited: RED"+"\n"+
                "Obstructed: BLUE"+"\n"+
                "Path: GOLD";
    }
}
