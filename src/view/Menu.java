package view;

import java.util.Scanner;

public class Menu {

    public final static int OK = 0;
    public final static int NULL_CONTENT = 1;
    public final static int OUT_OF_RANGE = 2;
    public final static int INVALID_OPTION = 3;

    private String option;
    private int errorCode;

    public Menu() {
        errorCode = NULL_CONTENT;
    }

    public void showMainMenu() {

        //Print menu
        System.out.println("");
        System.out.println("1. Configurar magatzem");
        System.out.println("2. Carregar productes");
        System.out.println("3. Distribuir productes");
        System.out.println("4. Servir comanda");
        System.out.println("5. Sortir");

        //Get user option
        Scanner sc = new Scanner(System.in).useDelimiter("\n");
        System.out.print("Opció: ");

        //Check if there is an input
        String userInput = sc.nextLine();
        if(userInput.isEmpty() || userInput.startsWith("\n")) {
            errorCode = NULL_CONTENT;
        } else {
            try {
                option = userInput;
                int intOption = Integer.parseInt(userInput);
                if(intOption <= 0 || intOption > 5) {
                    errorCode = OUT_OF_RANGE;
                } else {
                    errorCode = OK;
                }
            } catch(NumberFormatException e) {
                errorCode = INVALID_OPTION;
            }
        }


    }

    public void askForWarehouseFile() {

        //Print menu and get file name
        System.out.println("");
        Scanner sc = new Scanner(System.in).useDelimiter("\n");
        System.out.print("Fitxer del magatzem (partint de la carpeta 'raw'): ");

        //Check if there is an input
        String userInput = sc.nextLine();
        if(userInput.isEmpty() || userInput.startsWith("\n")) {
            errorCode = NULL_CONTENT;
        } else {
            option = userInput;
            errorCode = OK;
        }

    }

    public void askForProductListFile() {

        //Print menu and get file name
        System.out.println("");
        Scanner sc = new Scanner(System.in).useDelimiter("\n");
        System.out.print("Fitxer del llistat de productes (partint de la carpeta 'raw'): ");

        //Check if there is an input
        String userInput = sc.nextLine();
        if(userInput.isEmpty() || userInput.startsWith("\n")) {
            errorCode = NULL_CONTENT;
        } else {
            option = userInput;
            errorCode = OK;
        }

    }

    public void askForProductDependencyFile() {

        //Print menu and get file name
        System.out.println("");
        Scanner sc = new Scanner(System.in).useDelimiter("\n");
        System.out.print("Fitxer de dependència de productes (partint de la carpeta 'raw'): ");

        //Check if there is an input
        String userInput = sc.nextLine();
        if(userInput.isEmpty() || userInput.startsWith("\n")) {
            errorCode = NULL_CONTENT;
        } else {
            option = userInput;
            errorCode = OK;
        }

    }

    public String getOption() {
        return option;
    }

    public int getErrorCode() {
        return errorCode;
    }

}