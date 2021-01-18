package Grid;

import App.Main;
import Grid.CellState;
import Grid.Field;
import Grid.Vertice;
import Sprites.SpriteManager;
import javafx.scene.layout.Pane;

public class Simulation {
    SpriteManager spriteManager;
    Field field;
    Main parent;

    double resX;
    double resY;

    int x;
    int y;


    public Simulation(Main parent, Pane root, int x, int y, double resX, double resY) {
        spriteManager = new SpriteManager(root, resX, resY, x, y);
        this.resX = resX;
        this.resY = resY;
        this.x = x;
        this.y = y;
        this.parent = parent;

        field = new Field(this, x, y);

        field.refresh(0.4);
    }

    public void changeSize(int x, int y) {
        this.x = x;
        this.y = y;
        field.changeSize(x,y);
        field.refresh(0.4);
    }

    public void cellChanged(int code, CellState cellState) {
        try {
            spriteManager.updateCellColor(code, cellState);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            System.exit(-1);
        }
    }

    public void refreshSignal(int x, int y, double obstructionRatio) {
        if(field.getSizeX() != x || field.getSizeY() != y) {
            spriteManager.changeSize(x, y);
            field.changeSize(x, y);
        }

        field.refresh(obstructionRatio);
    }

    public void startDijkstra() {
        field.dijkstraAnimationSetup();
    }

    public void playDijkstraStep() {
        field.dijkstraAnimationStep();
    }

    public void startDFS() {
        field.DFSSetup();
    }

    public boolean playDFSStep() {
        return field.DFSStep();
    }

    public void startDFSLabyrinth() {
        field.DFSLabyrinthSetup();
    }

    public boolean playDFSLabyrinth() {
        return field.DFSLabyrinthStep();
    }


    public void showVerticeInfo(double x, double y) {
        int xCell = (int)x*this.x;
        xCell /= resX;

        int yCell = (int)y*this.y;
        yCell /= resY;

        Vertice v = field.getVertice(xCell, yCell);

        System.out.println("Distance: "+v.distance);
        System.out.println("Position: "+v.code % this.x+", "+v.code / this.x);
        System.out.println("State: "+v.cellState);
        System.out.println();
    }

    public void passStoppedDijkstraSignal() {
        parent.stoppedPlayingDijkstra();
    }

    public boolean playFinalPathStep() {
        return field.showFinalPathStep();
    }

    public void passShowFinalPathSignal() {
        parent.showFinalPath();
    }
}
