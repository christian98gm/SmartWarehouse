import model.ProductManager;
import model.Shelf;
import model.WarehouseManager;
import view.Menu;

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
                            Menu secondMenu = new Menu();
                            do {
                                secondMenu.askForFile();
                                if(secondMenu.getErrorCode() == Menu.NULL_CONTENT) {
                                    System.out.println(System.lineSeparator() + "No s'ha introduït res!");
                                } else {
                                    if(warehouseManager.init(secondMenu.getOption())) {
                                        System.out.println(System.lineSeparator() + "El magatzem ja està disponible!");
                                        printWarehouse(warehouseManager.getShelves());
                                    } else {
                                        System.out.println(System.lineSeparator() + "El magatzem no s'ha carregat correctament!");
                                    }
                                }
                            } while(secondMenu.getErrorCode() == Menu.NULL_CONTENT);
                            break;
                        case LOAD_PRODUCTS:
                            if(warehouseManager.isWarehouseInit()) {

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

}