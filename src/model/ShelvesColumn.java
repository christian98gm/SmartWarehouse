package model;

import java.awt.*;
import java.util.Comparator;

public class ShelvesColumn implements Comparable<ShelvesColumn> {

    public final static int SHELVES_PER_COLUMN = 3;

    private Point shelvesPoint;
    private Point entrancePoint;
    private Shelf[] shelves;

    public ShelvesColumn(Point shelvesPoint, Point entrancePoint) {
        this.shelvesPoint = shelvesPoint;
        this.entrancePoint = entrancePoint;
        shelves = new Shelf[SHELVES_PER_COLUMN];
        for(int i = 0; i < SHELVES_PER_COLUMN; i++) {
            shelves[i] = new Shelf(new Point3D(shelvesPoint.x, shelvesPoint.y, i),
                    new Point3D(entrancePoint.x, entrancePoint.y, 0));
        }
    }

    public Point getShelvesPoint() {
        return shelvesPoint;
    }

    public void setShelvesPoint(Point shelvesPoint) {
        this.shelvesPoint = shelvesPoint;
    }

    public Shelf[] getShelves() {
        return shelves;
    }

    public void setShelves(Shelf[] shelves) {
        this.shelves = shelves;
    }

    @Override
    public int compareTo(ShelvesColumn o) {
        return Double.compare(entrancePoint.distance(shelvesPoint), o.entrancePoint.distance(o.shelvesPoint));
    }

    public static Comparator<ShelvesColumn> comparator = new Comparator<ShelvesColumn>() {
        @Override
        public int compare(ShelvesColumn o1, ShelvesColumn o2) {
            return o1.compareTo(o2);
        }
    };

}