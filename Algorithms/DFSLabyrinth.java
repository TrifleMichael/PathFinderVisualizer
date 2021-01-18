package Algorithms;

import Grid.CellState;
import Grid.Field;
import Grid.Vertice;

import java.util.HashMap;
import java.util.LinkedList;

public class DFSLabyrinth {

    LinkedList<Vertice> queue = new LinkedList<>();
    Vertice current;

    public void setup(Field parentGrid) {
        HashMap<Integer, Vertice> map = parentGrid.getMap();

        for(Vertice v : map.values()) {
            parentGrid.changeCellState(v.code, CellState.OBSTRUCTED);
            v.parent = -1;
            v.distance = Double.POSITIVE_INFINITY;
        }

        current = map.get(0);
    }

    public boolean step(Field parentGrid) {
        HashMap<Integer, Vertice> map = parentGrid.getMap();
        Vertice next = null;

        if (!hasMultipleEmptyNeighbours(current, parentGrid)) {
            parentGrid.changeCellState(current.code,CellState.EMPTY);

            for(int code : current.getNeighboursCodes(parentGrid.getSizeX(), parentGrid.getSizeY())) {
                Vertice v = map.get(code);
                if (v.cellState == CellState.OBSTRUCTED && !hasMultipleEmptyNeighbours(current, parentGrid)) {
                    queue.add(v);
                    next = v;
                }
            }
            if (next != null) {
                current = next;
                return true;
            }
        }
        else
            queue.remove(current);

        if (!queue.isEmpty()) {
            current = queue.getLast();
            return step(parentGrid);
        }

        map.get(parentGrid.getSizeX()*parentGrid.getSizeY()-1).cellState = CellState.EMPTY;
        map.get(parentGrid.getSizeX()*parentGrid.getSizeY()-2).cellState = CellState.EMPTY;
        return false;

    }

    public boolean hasMultipleEmptyNeighbours(Vertice v, Field parentGrid) {
        int empty = 0;
        for(int code : v.getNeighboursCodes(parentGrid.getSizeX(), parentGrid.getSizeY())) {
            if (parentGrid.getMap().get(code).cellState != CellState.OBSTRUCTED)
                empty++;
        }

        return empty > 1;
    }
}
