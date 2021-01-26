package Grid;

import java.util.Comparator;
import Grid.Vertice;


public class VerticeDistanceComparator implements Comparator<Vertice>{
    int xSize;
    int ySize;

    public VerticeDistanceComparator(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public double distance(int code) {
        int x = code % xSize;
        int y = code / xSize;
        return (double)(x-xSize)*(x-xSize)+(y-ySize)*(y-ySize);
    }

    public int compare(Vertice v1, Vertice v2) {
        if (distance(v1.code) < distance(v2.code))
            return -1;
        if (distance(v1.code) > distance(v2.code))
            return 1;
        return 0;
    }
}

