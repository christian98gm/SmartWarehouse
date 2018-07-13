package model;

public class Shelf {

    private Point3D point;
    private double distance;
    private Product product;

    public Shelf(Point3D shelfPoint, Point3D entrancePoint) {
        this.point = shelfPoint;
        distance = shelfPoint.distance(entrancePoint);
    }

    public Point3D getPoint() {
        return point;
    }

    public void setPoint(Point3D point) {
        this.point = point;
    }

    public double getDistance() {
        return distance;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}