package Algorithms;

import Grid.CellState;
import Grid.Vertice;
import Grid.Field;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class DFS {
    LinkedList<Vertice> queue = new LinkedList<>();
    Vertice current;

    public void animationSetup(Field parentGrid) {
        HashMap<Integer, Vertice> map = parentGrid.getMap();

        for (Vertice v : map.values()) {
            v.visited = false;
        }

        queue.clear();
        current = map.get(0);
    }

    public boolean animationStep(Field parentGrid) {
        HashMap<Integer, Vertice> map = parentGrid.getMap();
        Vertice next = null;

        if (current.code == parentGrid.getSizeX()*parentGrid.getSizeY()-1) {
            parentGrid.changeCellState(current.code,CellState.VISITED);
            return false;
        }

        if (current.cellState != CellState.VISITED) {
            parentGrid.changeCellState(current.code,CellState.VISITED);

            for(int code : current.getNeighboursCodes(parentGrid.getSizeX(), parentGrid.getSizeY())) {
                Vertice v = map.get(code);
                if (v.cellState == CellState.EMPTY) {
                    v.parent = current.code;
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
            return animationStep(parentGrid);
        }

        return false;

    }
}
