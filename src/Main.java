import model.Product;
import model.ProductManager;
import model.Shelf;
import model.WarehouseManager;
import view.Menu;

import java.util.ArrayList;

public class Main {

    private final static int CONFIG_WAREHOUSE = 1;
    private final static int LOAD_PRODUCTS = 2;
    private final static int DISTRIBUTE_PRODUCTS = 3;
    private final static int SERVE_ORDER = 4;
    private final static int EXIT = 5;

    public static void main(String[] args) {

        //Init basic config
        Menu menu = new Menu();
        WarehouseManager warehouseManager = new WarehouseManager();
        ProductManager productManager = new ProductManager();
        int option;

        //Show menu until the user exits
        do {

            //Get menu values
            menu.showMainMenu();
            int errorCode = menu.getErrorCode();
            try {
                option = Integer.parseInt(menu.getOption());
            } catch(NumberFormatException e) {
                option = 0;
            }

            switch(errorCode) {
                case Menu.OK:
                    switch(option) {
                        case CONFIG_WAREHOUSE:
                            productManager.reset();
                            Menu warehouseMenu = new Menu();
                            boolean warehouseError;
                            do {
                                warehouseMenu.askForWarehouseFile();
                                if(warehouseMenu.getErrorCode() == Menu.NULL_CONTENT) {
                                    System.out.println(System.lineSeparator() + "No s'ha introduït res!");
                                    warehouseError = true;
                                } else {
                                    if(warehouseManager.init(warehouseMenu.getOption())) {
                                        System.out.println(System.lineSeparator() + "El magatzem ja està disponible!");
                                        printWarehouse(warehouseManager.getShelves());
                                    } else {
                                        System.out.println(System.lineSeparator() + "El magatzem no s'ha carregat correctament!");
                                    }
                                    warehouseError = false;
                                }
                            } while(warehouseError);
                            break;
                        case LOAD_PRODUCTS:
                            if(warehouseManager.isWarehouseInit()) {
                                Menu productMenu = new Menu();
                                boolean productError;
                                do {
                                    productMenu.askForProductListFile();
                                    if(productMenu.getErrorCode() == Menu.NULL_CONTENT) {
                                        System.out.println(System.lineSeparator() + "No s'ha introduït res!");
                                        productError = true;
                                    } else {
                                        String productListFile = productMenu.getOption();
                                        productError = false;
                                        do {
                                            productMenu.askForProductDependencyFile();
                                            if(productMenu.getErrorCode() == Menu.NULL_CONTENT) {
                                                System.out.println(System.lineSeparator() + "No s'ha introduït res!");
                                                productError = true;
                                            } else {
                                                String productDependencyFile = productMenu.getOption();
                                                if(productManager.initProducts(productListFile, productDependencyFile)) {
                                                    System.out.println(System.lineSeparator() + "Els productes ja estan disponibles!");
                                                    printProducts(productManager.getProducts());
                                                    printGraph(productManager.getDependencyMatrix());
                                                } else {
                                                    System.out.println(System.lineSeparator() + "Els productes no s'han carregat correctament!");
                                                }
                                            }
                                        } while(productError);
                                    }
                                } while(productError);
                            } else {
                                System.out.println(System.lineSeparator() + "El magatzem no està disponible!");
                            }
                            break;
                        case DISTRIBUTE_PRODUCTS:
                            if(productManager.isInit()) {

                            } else {
                                System.out.println(System.lineSeparator() + "No hi ha cap producte per emmagatzemar!");
                            }
                            break;
                        case SERVE_ORDER:
                            if(warehouseManager.isWarehouseFull()) {

                            } else {
                                System.out.println(System.lineSeparator() + "No hi ha cap producte al magatzem!");
                            }
                            break;
                        case EXIT:
                            System.out.println(System.lineSeparator() + "Gràcies per usar els nostres serveis!");
                            break;
                    }
                    break;
                case Menu.NULL_CONTENT:
                    System.out.println(System.lineSeparator() + "No s'ha introduït res!");
                    break;
                case Menu.OUT_OF_RANGE:
                    System.out.println(System.lineSeparator() + "L'opció introduïda està fora del rang [1, 5]!");
                    break;
                case Menu.INVALID_OPTION:
                    System.out.println(System.lineSeparator() + "L'opció introduïda no és un número!");
                    break;
            }

        } while(option != EXIT);

    }

    /**
     * Serveix per mostrar per consola on hi ha estanteries (0 -> Sense estanteria | 1 -> Amb estanteria)
     * @param shelves Estanteries del magatzem
     */
    private static void printWarehouse(Shelf[][] shelves) {
        System.out.println();
        for(int i = 0; i < shelves.length; i++) {
            for(int j = 0; j < shelves[i].length; j++) {
                if(shelves[j][i] == null) {
                    System.out.print(0);
                } else {
                    System.out.print(1);
                }
            }
            System.out.println();
        }
    }

    /**
     * Serveix per mostrar per consola els productes disponibles
     * @param products Productes
     */
    private static void printProducts(ArrayList<Product> products) {
        System.out.println();
        for(int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.print("Producte " + i + ": ");
            System.out.print(product.getId() + " ");
            System.out.println(product.getName());
        }
    }

    /**
     * Serveix per mostrar el graph de dependència
     * @param graph Matriu a mostrar
     */
    private static void printGraph(double[][] graph) {
        System.out.println();
        for(int i = 0; i < graph.length; i++) {
            for(int j = 0; j < graph[i].length; j++) {
                System.out.print(graph[j][i] + " ");
            }
            System.out.println();
        }
    }

}