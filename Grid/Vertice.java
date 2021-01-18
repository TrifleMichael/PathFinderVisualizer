package Grid;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Vertice {

    public double distance = Double.POSITIVE_INFINITY;
    public double weight;
    public boolean visited = false;
    public int parent = -1;
    public CellState cellState;
    public int code;

    public Vertice(int code) {
        weight = ThreadLocalRandom.current().nextDouble(0,100);
        this.code = code;
    }

    public Vertice(int code, double distance) {
        this.distance = distance;
        this.code = code;
    }

    public ArrayList<Integer> getNeighboursCodes(int sizeX, int sizeY) {
        ArrayList<Integer> list = new ArrayList<>();
        int x = code % sizeX;
        int y = code / sizeX;

        if (0 <= x-1)
            list.add(y*sizeX + x-1);
        if (x+1 < sizeX)
            list.add(y*sizeX + x+1);

        if (0 <= y-1)
            list.add((y-1)*sizeX + x);
        if (y+1 < sizeY)
            list.add((y+1)*sizeX + x);

        return  list;
    }

    public boolean equals(Object other) {
        if (other instanceof Vertice) {
            return code == ((Vertice) other).code;
        }

        return false;
    }
}
