package controller;

import model.*;
import org.json.JSONException;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class WarehouseManager {

    //File path
    private final static String PATH = "raw/";

    //Shelves orientation
    private final static char HORIZONTAL = 'H';
    private final static char VERTICAL = 'V';

    //Entrance, shelves columns and warehouse matrix
    private Point entrance;
    private ArrayList<ShelvesColumn> shelvesColumns;
    private boolean[][] warehouse;

    //Products data
    private ArrayList<Product> products;
    private double[][] graph;

    //Score (relative to graph params and distance between products)
    private double score;

    //Aux
    private int round;

    public WarehouseManager() {}

    public boolean init(String fileName) {

        //Reset data
        entrance = null;
        shelvesColumns = null;
        warehouse = null;
        products = null;
        graph = null;
        score = -1.0;

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
            warehouse = new boolean[dimension.width][dimension.height];

            //Get warehouse entrance
            entrance = jsonManager.getEntrance();

            //Get warehouse config
            ArrayList<ShelfConfig> shelvesConfig = jsonManager.getShelvesConfig();

            //Get warehouse shelves
            shelvesColumns = new ArrayList<>();
            int totalShelvesPattern = jsonManager.getTotalShelvesPattern();
            for(int i = 0; i < totalShelvesPattern; i++) {

                ShelfPattern shelfPattern = jsonManager.getShelfPattern(i);
                Point startPoint = shelfPattern.getStartPoint();
                char orientation = shelfPattern.getOrientation();
                int id = shelfPattern.getShelfId();

                for(ShelfConfig shelfConfig : shelvesConfig) {

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

                        for(int x = startPoint.x; x <= endPoint.x; x++) {
                            for(int y = startPoint.y; y <= endPoint.y; y++) {
                                warehouse[x][y] = true;
                                ShelvesColumn shelvesColumn = new ShelvesColumn(new Point(x, y), entrance);
                                shelvesColumns.add(shelvesColumn);
                            }
                        }

                        break;

                    }

                }

            }

            //Order shelf columns (closer --> further relative to entrance)
            Collections.sort(shelvesColumns);

            return true;

        } catch(IOException | JSONException e) {
            entrance = null;
            shelvesColumns = null;
            warehouse = null;
            return false;
        }

    }

    public boolean hasShelves() {
        return shelvesColumns != null && entrance != null && warehouse != null;
    }

    public boolean[][] getWarehouse() {
        return warehouse;
    }

    public Point getEntrance() {
        return entrance;
    }

    public ArrayList<ShelvesColumn> getShelvesColumns() {
        return shelvesColumns;
    }

    public ArrayList<Shelf> getCloserShelves(int maxShelves) {
        ArrayList<Shelf> closerShelves = new ArrayList<>();
        int totalShelves = 0;
        for(int i = 0; i < Integer.min(maxShelves / ShelvesColumn.SHELVES_PER_COLUMN + 1, shelvesColumns.size()); i++) {
            Shelf[] shelves = shelvesColumns.get(i).getShelves();
            for(int j = 0; j < shelves.length && totalShelves < maxShelves; j++) {
                closerShelves.add(shelves[j]);
                totalShelves++;
            }
        }
        return closerShelves;
    }

    public void setProductData(ArrayList<Product> products, double[][] graph) {
        if(hasShelves()) {
            this.products = products;
            this.graph = graph;
        }
    }

    public boolean hasProducts() {
        return products != null && graph != null;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void distributeProducts() {
        if(hasProducts()) {

            System.out.println();
            System.out.println("Carregant dades...");

            //Reset shelves
            for(int i = 0; i < shelvesColumns.size(); i++) {
                Shelf[] shelves = shelvesColumns.get(i).getShelves();
                for(int j = 0; j < shelves.length; j++) {
                    shelves[j].setProduct(null);
                }
            }
            score = -1.0;
            round = 0;  //TODO
            int[] result = new int[products.size()];

            //Backtracking
            productBacktracking(new int[products.size()], 0, new int[products.size()], result);

            //Update shelves with their products
            ArrayList<Shelf> shelves = getCloserShelves(products.size());
            for(int i = 0; i < result.length; i++) {
                shelves.get(result[i]).setProduct(products.get(i));
            }

        }
    }

    private void productBacktracking(int[] config, int index, int[] marks, int[] result) {

        config[index] = -1;                                                         //preparaRecorregut
        while(config[index] < marks.length - 1) {                                   //hiHaSeguentGerma

            round++;    //TODO
            System.out.println("Round: " + round);  //TODO

            config[index]++;                                                        //seguentGerma
            marks[config[index]]++;                                                 //marcar

            if(index == config.length - 1) {                                        //solucio
                if(marks[config[index]] <= 1) {                                     //factible = bona
                    updateResult(config, getCloserShelves(config.length), result);  //tractarSolucio
                }
            } else {                                                                //!solucio
                if(marks[config[index]] <= 1) {                                     //completable = bona
                    productBacktracking(config, index + 1, marks, result);     //backtracking
                }
            }

            marks[config[index]]--;                                                 //desmarcar

        }

    }

    private void updateResult(int[] config, ArrayList<Shelf> shelves, int[] result) {
        if(Double.compare(score, 0.0) < 0) {
            result = new int[config.length];
            System.arraycopy(config, 0, result, 0, result.length);
            score = calculateScore(config, shelves);
        } else {
            double newScore = calculateScore(config, shelves);
            if(Double.compare(score, newScore) > 0) {
                System.arraycopy(config, 0, result, 0, result.length);
                score = newScore;
            }
        }
    }

    private double calculateScore(int[] config, ArrayList<Shelf> shelves) {

        double score = 0.0;

        for(int i = 0; i < config.length; i++) {
            Shelf shelf1 = shelves.get(config[i]);
            Point3D startPoint = shelf1.getPoint();
            for(int j = i + 1; j < config.length; j++) {
                Shelf shelf2 = shelves.get(config[j]);
                Point3D endPoint = shelf2.getPoint();
                score += Math.pow(graph[i][j] * startPoint.distance(endPoint), 2);
            }
        }

        return score;

    }

    public double getScore() {
        return score;
    }

    public boolean readyForRequests() {
        return Double.compare(score, 0.0) >= 0;
    }

}