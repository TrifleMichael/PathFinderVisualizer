package Grid;

import Algorithms.DFS;
import Algorithms.DFSLabyrinth;
import Algorithms.Greedy;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;


// Handles individual cells and performs algorithms
public class Field {
    int sizeX;
    int sizeY;

    HashMap<Integer, Vertice> map = new HashMap<>();
    PriorityQueue<Vertice> queue;
    int finalPathCurrentCode;

    Simulation simulation;
    DFS dfs = new DFS();
    DFSLabyrinth dfsLabyrinth = new DFSLabyrinth();
    Greedy greedy = new Greedy();

    public Field(Simulation simulation, int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        this.simulation = simulation;

        finalPathCurrentCode = sizeX*sizeY-1;
        queue = new PriorityQueue<>(sizeX*sizeY, new VerticeComparator());
        emptyField();
    }


    public void changeSize(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        finalPathCurrentCode = sizeX*sizeY-1;
        queue = new PriorityQueue<>(sizeX*sizeY, new VerticeComparator());
        emptyField();
    }


    public Vertice getVertice(int x, int y) {
        return map.get(cellCode(x, y));
    }

    public void refresh(double obstructionRatio) {
        emptyField();
        generateRandomObstructions(obstructionRatio);
        generatePath();
        finalPathCurrentCode = sizeX*sizeY-1;
    }



    public void emptyField() {
        for(int y = 0; y < sizeY; y++) {
            for(int x = 0; x < sizeX; x++) {
                changeCellState(y*sizeX+x, CellState.EMPTY);
            }
        }
    }

    public void changeCellState(int code, CellState cellState) {
        if (simulation.getPlayerCode() == code && cellState != CellState.PLAYER) { // Don't change color if player at that position
            map.get(code).cellState = cellState;
            return;
        }

        if (!map.containsKey(code))
            map.put(code, new Vertice(code));
        else
            map.get(code).cellState = cellState;

        simulation.cellChanged(code, cellState);
    }

    public void changeCellState(int x, int y, CellState cellState) {
        changeCellState(cellCode(x,y), cellState);
    }

    public Vertice getCell(int x, int y) {
        return map.getOrDefault(cellCode(x, y), null);
    }

    public int cellCode(int x, int y) {
        return  y*sizeX+x;
    }


    public void generatePath() {
        HashMap<Integer, Vertice> map = new HashMap<>();
        for(int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                map.put(cellCode(x,y), new Vertice(cellCode(x,y)));
            }
        }

        dijkstraSearch(map, 0);
        int code = sizeX*sizeY-1;

        while (code != -1) {
            changeCellState(code, CellState.EMPTY);
            code = map.get(code).parent;
        }

    }



    public void greedySetup() {
        finalPathCurrentCode = sizeX*sizeY-1;
        greedy.setup(this, simulation.getPlayerCode());
    }

    public boolean greedyStep() {
        return greedy.step(this);
    }

    public void DFSSetup() {
        finalPathCurrentCode = sizeX*sizeY-1;
        dfs.animationSetup(this, simulation.getPlayerCode());
    }

    public boolean DFSStep() {
        return dfs.animationStep(this);
    }

    public void DFSLabyrinthSetup() {
        dfsLabyrinth.setup(this);
    }

    public boolean DFSLabyrinthStep() {
        return dfsLabyrinth.step(this);
    }




    public void dijkstraSearch(HashMap<Integer, Vertice> map, int startCode) {
        Vertice start = map.get(startCode);
        queue.clear();
        queue.add(start);
        start.distance = start.weight;

        while (!queue.isEmpty()) {
            Vertice v = queue.poll();
            v.visited = true;

            for (int code : v.getNeighboursCodes(sizeX, sizeY)) {
                Vertice u = map.get(code);
                if (!u.visited && u.distance > u.weight + v.distance) { // PROBABLY REDUNDANT CHECK
                    u.distance = u.weight + v.distance;
                    u.parent = v.code;
                    queue.add(u);
                }
            }
        }
    }

    public void dijkstraAnimationSetup(int startingCode) {
        finalPathCurrentCode = sizeX*sizeY-1;

        for (Vertice v : map.values()) {
            v.distance = Double.POSITIVE_INFINITY;
            v.visited = false;
            if (v.cellState != CellState.OBSTRUCTED) {
                changeCellState(v.code, CellState.EMPTY);
            }
        }


        queue.clear();
        Vertice start = map.get(startingCode);
        start.distance = 0;
        queue.add(start);
        changeCellState(start.code, CellState.VISITED);
    }

    public void dijkstraAnimationStep() {
        boolean cellChanged = false;

        while (!queue.isEmpty() && !cellChanged) {
            Vertice v = queue.poll();
            v.visited = true;

            for (int code : v.getNeighboursCodes(sizeX, sizeY)) {
                Vertice u = map.get(code);
                if (u.cellState != CellState.OBSTRUCTED && u.distance > v.distance + 1) {
                    queue.remove(u);
                    u.distance = 1 + v.distance;
                    u.parent = v.code;
                    queue.add(u);
                    changeCellState(u.code, CellState.VISITED);
                    cellChanged = true;

                    if (u.code == sizeX*sizeY-1) {
                        simulation.passStoppedDijkstraSignal();
                        simulation.passShowFinalPathSignal();
                    }
                }
            }
        }
    }

    public boolean showFinalPathStep() {
        changeCellState(finalPathCurrentCode, CellState.CHOSEN);
        finalPathCurrentCode = map.get(finalPathCurrentCode).parent;

        if (finalPathCurrentCode == -1) {
            finalPathCurrentCode = sizeX*sizeY-1;
            return false;
        }
        else
            return true;
    }

    public void generateRandomObstructions(double generationDensity) { // ARGUMENT SHOULD BE CHECKED IF BETWEEN 0 AND 1
        generationDensity = sizeX*sizeY*generationDensity;
        int obstructions = (int)generationDensity;

        int totalEmpty = sizeX*sizeY;
        for(int i = 0; i < obstructions; i++) {
            int n = ThreadLocalRandom.current().nextInt(0,totalEmpty--);
            changeCellState(nthFreeCellCode(n), CellState.OBSTRUCTED);
        }
    }

    public int nthFreeCellCode(int n) {
        int code = 0;
        int emptyPassed = 0;
        while(emptyPassed < n || map.get(code).cellState != CellState.EMPTY) {
            code = nextFreeCellCode(code);
            emptyPassed++;
        }

        return code;
    }

    public int nextFreeCellCode(int code) {
        code++;
        while (map.get(code).cellState != CellState.EMPTY) {
            code++;
            if(sizeX*sizeY <= code)
                return -1;
        }
        return code;
    }

    public HashMap<Integer, Vertice> getMap() {
        return map;
    }

    public PriorityQueue<Vertice> getQueue() {
        return queue;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}
