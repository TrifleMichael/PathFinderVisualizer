package Algorithms;

import Grid.CellState;
import Grid.Field;
import Grid.Vertice;
import javafx.scene.control.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class DFSLabyrinth {

    LinkedList<Vertice> queue = new LinkedList<>();
    Vertice current;
    LinkedList<Vertice> neighbours = new LinkedList<>();

    public void setup(Field parentGrid) {
        HashMap<Integer, Vertice> map = parentGrid.getMap();
        neighbours.clear();

        for(Vertice v : map.values()) {
            parentGrid.changeCellState(v.code, CellState.OBSTRUCTED);
            v.parent = -1;
            v.distance = Double.POSITIVE_INFINITY;
        }

        current = map.get(0);
    }

    // True if more steps needed, false if objective achieved
    public boolean step(Field parentGrid) {
        HashMap<Integer, Vertice> map = parentGrid.getMap();


        neighbours.clear();
        if (current.cellState == CellState.OBSTRUCTED && !hasMultipleEmptyNeighbours(current, parentGrid)) { //Checking if emptying cell will create loop in labyrinth
            parentGrid.changeCellState(current.code,CellState.EMPTY); //If not then emptying cell

            for(int code : current.getNeighboursCodes(parentGrid.getSizeX(), parentGrid.getSizeY())) {
                Vertice v = map.get(code);                                                              //For all neighbours v
                if (v.cellState == CellState.OBSTRUCTED && !hasMultipleEmptyNeighbours(current, parentGrid)) { //If neighbour is full and emptying wont create loop
                    queue.add(v);                                                                               // add to waiting queue
                    neighbours.add(v);                                                                          // add as possible next
                }
            }

            if (!neighbours.isEmpty()) {                                                                        // Randomise next and finish step if direct next exist
                current = neighbours.get(ThreadLocalRandom.current().nextInt(0, neighbours.size()));
                return true;
            }
        }
        else
            queue.remove(current);      // If emptying would create loop remove from queue

        if (!queue.isEmpty()) {         // If no direct neighbours are good select n
            current = queue.getLast();
            return step(parentGrid);
        }

        parentGrid.changeCellState(parentGrid.getSizeX()*parentGrid.getSizeY()-1, CellState.EMPTY);
        parentGrid.changeCellState(parentGrid.getSizeX()*parentGrid.getSizeY()-2, CellState.EMPTY);
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
