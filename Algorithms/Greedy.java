package Algorithms;

import Grid.CellState;
import Grid.Field;
import Grid.Vertice;
import Grid.VerticeDistanceComparator;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Greedy {
    PriorityQueue<Vertice> queue;


    public void setup(Field parentGrid, int startingCode) {
        HashMap<Integer, Vertice> map = parentGrid.getMap();
        for(Vertice v : map.values()) {
            v.visited = false;
            v.parent = -1;
            if (v.cellState != CellState.OBSTRUCTED) {
                parentGrid.changeCellState(v.code, CellState.EMPTY);
            }
        }

        // Priority Queue sorted by rising distance to objective (lower right corner)
        queue = new PriorityQueue<Vertice>(parentGrid.getSizeX()*parentGrid.getSizeY(), new VerticeDistanceComparator(parentGrid.getSizeX(), parentGrid.getSizeY()));
        queue.add(map.get(startingCode));
        map.get(startingCode).visited = true;
        parentGrid.changeCellState(startingCode, CellState.VISITED);
    }

    // True if more steps needed, false if objective achieved
    public boolean step(Field parentGrid) {
        HashMap<Integer, Vertice> map = parentGrid.getMap();

        if (!queue.isEmpty()) {
            Vertice v = queue.poll();                                   // Select closest to objective from queue
            parentGrid.changeCellState(v.code, CellState.VISITED);

            if (v.code == parentGrid.getSizeX()*parentGrid.getSizeY()-1) { // If objective reached then return
                return false;
            }

            for(int code : v.getNeighboursCodes(parentGrid.getSizeX(), parentGrid.getSizeY())) { // Put all non obstructed neighbours in queue
                Vertice u = map.get(code);
                if (!u.visited && u.cellState != CellState.OBSTRUCTED) {
                    queue.add(u);
                    u.visited = true;
                    u.parent = v.code;
                }
            }
            return true;
        }

        System.out.println("We got broke."); // Normal error handling should be added
        return false;
    }
}
