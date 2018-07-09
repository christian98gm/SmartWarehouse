package model;

import java.util.ArrayList;

public class ProductManager {

    ArrayList<Product> products;

    public ProductManager() {
        products = new ArrayList<>();
    }

    public boolean isInit() {
        return !products.isEmpty();
    }

    public void reset() {
        products.clear();
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

}