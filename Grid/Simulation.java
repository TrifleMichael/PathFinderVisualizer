package Grid;

import App.Main;
import Grid.CellState;
import Grid.Field;
import Grid.Vertice;
import Sprites.SpriteManager;
import javafx.scene.layout.Pane;


// Connects Field, Player, Sprite Manager and Main
public class Simulation {
    SpriteManager spriteManager;
    Field field;
    Main parent;

    double resX;
    double resY;

    int x;
    int y;

    Player player = new Player();

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

    public void cellChangeOnClick(double x, double y) {
        x /= this.resX;
        x *= this.x;

        y /= this.resY;
        y *= this.y;


        if (field.getCell((int)x, (int)y).cellState != CellState.OBSTRUCTED)
            field.changeCellState((int)x, (int)y, CellState.OBSTRUCTED);
        else
            field.changeCellState((int)x, (int)y, CellState.EMPTY);
    }

    public void refreshSignal(int x, int y, double obstructionRatio) {
        if(field.getSizeX() != x || field.getSizeY() != y) {
            spriteManager.changeSize(x, y);
            field.changeSize(x, y);

            this.x = x;
            this.y = y;
        }

        player.moveTo(-1,-1);
        field.refresh(obstructionRatio);
        player.moveTo(0,0);
    }

    public void startGreedy() {
        field.greedySetup();
    }

    public boolean playGreedy() {
        return field.greedyStep();
    }

    public void startDijkstra() {
        field.dijkstraAnimationSetup(getPlayerCode());
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


    public void showVerticeInfo(double x, double y) { // DEBUG
        int xCell = (int)x*this.x;
        xCell /= resX;

        int yCell = (int)y*this.y;
        yCell /= resY;

        Vertice v = field.getVertice(xCell, yCell);

        System.out.println("Distance: "+v.distance);
        System.out.println("Position: "+v.code % this.x+", "+v.code / this.x);
        System.out.println("Cell code: "+v.code);
        System.out.println("State: "+v.cellState);
        System.out.println();
    }

    public void movePlayer(int dx, int dy) {
        Vertice newCell = field.getCell(player.getX()+dx, player.getY()+dy);

        if (player.getX() + dx < 0 || field.getSizeX() <= player.getX())
            return;
        if (player.getY() + dy < 0 || field.getSizeY() <= player.getY())
            return;

        if (newCell != null && newCell.cellState != CellState.OBSTRUCTED) {
            player.move(dx, dy);
            spriteManager.updateCellColor(field.cellCode(player.getX()-dx, player.getY()-dy), field.getCell(player.getX()-dx, player.getY()-dy).cellState );
            spriteManager.updateCellColor(field.cellCode(player.getX(), player.getY()), CellState.PLAYER);
        }
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

    public int getPlayerCode() {
        if (field == null) // Needed for when Field is initialized
            return  -1;


        return field.cellCode(player.getX(), player.getY());
    }
}
