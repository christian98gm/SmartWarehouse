package controller;

import model.*;
import view.Menu;
import view.WarehouseView;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class OptionManager {

    //Possible options
    public final static int CONFIG_WAREHOUSE = 1;
    public final static int LOAD_PRODUCTS = 2;
    public final static int DISTRIBUTE_PRODUCTS = 3;
    public final static int SERVE_ORDER = 4;
    public final static int EXIT = 5;

    //Warehouse and product managers
    private WarehouseManager warehouseManager;
    private ProductManager productManager;
    private WarehouseView warehouseView;

    public OptionManager() {
        warehouseManager = new WarehouseManager();
        productManager = new ProductManager();
    }

    public void optionChosen(int option) {
        switch (option) {
            case CONFIG_WAREHOUSE:
                configWarehouse();
                break;
            case LOAD_PRODUCTS:
                loadProducts();
                break;
            case DISTRIBUTE_PRODUCTS:
                distributeProducts();
                break;
            case SERVE_ORDER:
                if (warehouseManager.readyForRequests()) {
                    showRobotPath();
                } else {
                    System.out.println(System.lineSeparator() + "No hi ha cap producte al magatzem!");
                }
                break;
            case EXIT:
                System.out.println(System.lineSeparator() + "Gràcies per usar els nostres serveis!");
                break;
        }
    }

    private void showRobotPath() {

        //Loads the command file
        if (loadCommand()) {
            warehouseManager.setCommandProducts(productManager.getCommandProducts());

            // Looks for the best path to take all products
            warehouseManager.serveProducts();
            ArrayList<Point> path = warehouseManager.getBestTrace();

            // Paints the path in yellow
            for (Point p : path) {
                warehouseView.paintCell(p.x, p.y, Color.YELLOW);
            }

            // Shows how many steps the robot makes to take all products
            warehouseView.setTrackCost(warehouseManager.getBestSteps());
        }
    }

    private void configWarehouse() {

        //Reset product data
        productManager.reset();

        //Show menu
        Menu menu = new Menu();
        boolean warehouseError;
        do {
            menu.askForWarehouseFile();
            if (menu.getErrorCode() == Menu.NULL_CONTENT) {
                warehouseError = true;
                System.out.println(System.lineSeparator() + "No s'ha introduït res!");
            } else {
                warehouseError = false;
                if (warehouseManager.init(menu.getOption())) {
                    System.out.println(System.lineSeparator() + "El magatzem ja està disponible!");
                } else {
                    System.out.println(System.lineSeparator() + "El magatzem no s'ha carregat correctament!");
                }
            }
        } while (warehouseError);

    }

    private boolean loadCommand() {
        //Show menu
        Menu menu = new Menu();
        boolean productError;
        do {
            menu.askForCommandFile();
            if (menu.getErrorCode() == Menu.NULL_CONTENT) {
                productError = true;
                System.out.println(System.lineSeparator() + "No s'ha introduït res!");
            } else {
                productError = false;
                if (productManager.initCommand(menu.getOption())) {
                    System.out.println(System.lineSeparator() + "Comanda carregada amb èxit!");
                } else {
                    System.out.println(System.lineSeparator() + "La comanda no s'han carregat correctament!");
                    return false;
                }
            }
        } while (productError);
        return true;
    }

    private void loadProducts() {

        //Check if warehouse is loaded
        if (!warehouseManager.hasShelves()) {
            System.out.println(System.lineSeparator() + "El magatzem no està disponible!");
            return;
        }

        //Show menu
        Menu menu = new Menu();
        boolean productError;
        do {
            menu.askForProductListFile();
            if (menu.getErrorCode() == Menu.NULL_CONTENT) {
                productError = true;
                System.out.println(System.lineSeparator() + "No s'ha introduït res!");
            } else {
                productError = false;
                if (productManager.initProducts(menu.getOption())) {
                    System.out.println(System.lineSeparator() + "Productes carregats amb èxit!");
                    printProducts(productManager.getProducts());    //TODO
                    menu = new Menu();  //Reset menu to avoid conflicts
                    do {
                        menu.askForProductDependencyFile();
                        if (menu.getErrorCode() == Menu.NULL_CONTENT) {
                            productError = true;
                            System.out.println(System.lineSeparator() + "No s'ha introduït res!");
                        } else {
                            productError = false;
                            if (productManager.initGraph(menu.getOption())) {
                                warehouseManager.setProductData(productManager.getProducts(), productManager.getGraph());
                                System.out.println(System.lineSeparator() + "Graph carregat amb èxit!");
                                printGraph(productManager.getGraph());          //TODO
                            } else {
                                productManager.reset();
                                System.out.println(System.lineSeparator() + "El graph no s'han carregat correctament!");
                            }
                        }
                    } while (productError);
                } else {
                    System.out.println(System.lineSeparator() + "Els productes no s'han carregat correctament!");
                }
            }
        } while (productError);

    }

    private void distributeProducts() {
        if (warehouseManager.hasProducts()) {
            //Distribute products
            warehouseManager.distributeProducts();
            //Show view
            showWarehouse();
        } else {
            System.out.println(System.lineSeparator() + "No hi ha cap producte per emmagatzemar!");
        }
    }

    private void showWarehouse() {

        //Close existent view
        if (warehouseView != null) {
            warehouseView.dispatchEvent(new WindowEvent(warehouseView, WindowEvent.WINDOW_CLOSING));
        }

        //Config view
        Point entrance = warehouseManager.getEntrance();
        warehouseView = new WarehouseView(warehouseManager.getWarehouse(), entrance.x, entrance.y);
        BoxListener controller = new BoxListener(warehouseView, warehouseManager.getShelvesColumns());
        warehouseView.setMapMouseListener(controller);

        //Set score
        warehouseView.setScoreInfo(warehouseManager.getScore());

        //Show view
        warehouseView.setVisible(true);

    }

    /**
     * Serveix per mostrar per consola els productes disponibles
     *
     * @param products Productes
     */
    private void printProducts(ArrayList<Product> products) {
        System.out.println();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.print("Producte " + i + ": ");
            System.out.print(product.getId() + " ");
            System.out.println(product.getName());
        }
    }

    /**
     * Serveix per mostrar el graph de dependència
     *
     * @param graph Matriu a mostrar
     */
    private void printGraph(double[][] graph) {
        System.out.println();
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                System.out.print(graph[j][i] + " ");
            }
            System.out.println();
        }
    }

}