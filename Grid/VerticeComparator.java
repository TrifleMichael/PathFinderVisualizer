package Grid;

import Grid.Vertice;

import java.util.Comparator;

public class VerticeComparator implements Comparator<Vertice> {
    public int compare(Vertice v1, Vertice v2) {
        if (v1.distance < v2.distance)
            return -1;
        else if (v1.distance > v2.distance)
            return 1;
        return 0;
    }
}

