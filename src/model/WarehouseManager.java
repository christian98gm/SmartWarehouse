package model;

import org.json.JSONException;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WarehouseManager {

    //File path
    private final static String PATH = "raw/";

    //Shelves orientation
    private final static char HORIZONTAL = 'H';
    private final static char VERTICAL = 'V';

    //Attributes
    private Point entrance;
    private Shelf[][] shelves;
    private boolean warehouseFull;

    public WarehouseManager() {}

    public boolean init(String fileName) {

        warehouseFull = false;
        shelves = null;
        entrance = null;

        //Read file if possible
        try {

            BufferedReader br = new BufferedReader(new FileReader(PATH + fileName));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            //Manage json file
            JsonManager jsonManager = new JsonManager(sb.toString());

            //Get warehouse dimensions
            Dimension dimension = jsonManager.getDimension();
            shelves = new Shelf[dimension.width][dimension.height];

            //Get warehouse entrance
            entrance = jsonManager.getEntrance();

            //Get warehouse config
            ArrayList<ShelfConfig> shelvesConfig = jsonManager.getShelvesConfig();

            //Get warehouse shelves
            int totalShelvesPattern = jsonManager.getTotalShelvesPattern();
            for(int i = 0; i < totalShelvesPattern; i++) {

                ShelfPattern shelfPattern = jsonManager.getShelfPattern(i);
                Point startPoint = shelfPattern.getStartPoint();
                char orientation = shelfPattern.getOrientation();
                int id = shelfPattern.getShelfId();

                for(int j = 0; j < shelvesConfig.size(); j++) {
                    ShelfConfig shelfConfig = shelvesConfig.get(j);
                    if(shelfConfig.getId() == id) {

                        //Get end point
                        Point endPoint;
                        if(orientation == HORIZONTAL) {
                            endPoint = new Point(startPoint.x + shelfConfig.getLength() - 1, startPoint.y);
                        } else if (orientation == VERTICAL) {
                            endPoint = new Point(startPoint.x, startPoint.y + shelfConfig.getLength() - 1);
                        } else {
                            return false;   //Wrong orientation value (invalid file)
                        }

                        for(int k = startPoint.x; k <= endPoint.x; k++) {
                            for(int l = startPoint.y; l <= endPoint.y; l++) {
                                shelves[k][l] = new Shelf();
                            }
                        }

                        break;

                    }
                }

            }

            return true;

        } catch(IOException | JSONException e) {
            shelves = null;
            entrance = null;
            return false;
        }

    }

    public Shelf[][] getShelves() {
        return shelves;
    }

    public boolean isWarehouseInit() {
        return shelves != null && entrance != null;
    }

    public boolean isWarehouseFull() {
        return warehouseFull;
    }

}