package model;

public class Shelf {

    private final static int TOTAL_PRODUCTS = 3;

    private Product[] products;

    public Shelf() {
        products = new Product[TOTAL_PRODUCTS];
    }

}