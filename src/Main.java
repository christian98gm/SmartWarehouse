import controller.OptionManager;
import view.Menu;

public class Main {

    public static void main(String[] args) {

        //Init basic config
        OptionManager optionManager = new OptionManager();
        Menu menu = new Menu();

        //Option from menu
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
                case Menu.OK:               //Do something
                    optionManager.optionChosen(option);
                    break;
                case Menu.NULL_CONTENT:     //Nothing was introduced
                    System.out.println(System.lineSeparator() + "No s'ha introduït res!");
                    break;
                case Menu.OUT_OF_RANGE:     //Wrong option range
                    System.out.println(System.lineSeparator() + "L'opció introduïda està fora del rang [1, 5]!");
                    break;
                case Menu.INVALID_OPTION:   //Not an integer value
                    System.out.println(System.lineSeparator() + "L'opció introduïda no és un número!");
                    break;
            }

        } while(option != OptionManager.EXIT);

    }

}