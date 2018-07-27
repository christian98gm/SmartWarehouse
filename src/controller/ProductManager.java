package controller;

import model.Product;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductManager {

    //File path
    private final static String PATH = "raw/";

    private ArrayList<Product> products;
    private ArrayList<Product> commandProducts;
    private double[][] graph;

    public ProductManager() {}

    public boolean initProducts(String productListFile) {

        reset();

        try {

            BufferedReader br = new BufferedReader(new FileReader(PATH + productListFile));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            //Manage json file
            JsonManager fileList = new JsonManager(sb.toString());

            //Get products
            products = fileList.getProductList();

            return true;

        } catch(IOException | JSONException | NumberFormatException e) {
            reset();
            return false;
        }

    }

    public boolean initCommand(String filename){

        try {
            BufferedReader br = new BufferedReader(new FileReader(PATH + filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            //Manage json file
            JsonManager fileList = new JsonManager(sb.toString());

            commandProducts = new ArrayList<>();
            //Get products
            for(Product p:  fileList.getProductList()){
                for (int i = 0; i < products.size(); i++){
                    if(products.get(i).getId() == p.getId()){
                        commandProducts.add(products.get(i));
                    }
                }
            }

            return true;

        } catch(IOException | JSONException | NumberFormatException e) {
            return false;
        }
    }

    public boolean initGraph(String productDependencyFile) {

        try {

            //Prepare product dependency matrix
            int size = products.size();
            graph = new double[size][size];
            HashMap<Long, Integer> hashMap = new HashMap<>();
            for (int i = 0; i < size; i++) {
                Product product = products.get(i);
                hashMap.put(product.getId(), i);
            }

            //Get products dependency
            BufferedReader br = new BufferedReader(new FileReader(PATH + productDependencyFile));
            String line = br.readLine();

            while (line != null && (!line.isEmpty() || line.startsWith("\n"))) {

                //Obtain line information
                String[] content = line.split(" ");
                int i = hashMap.get(Long.parseLong(content[0]));
                int j = hashMap.get(Long.parseLong(content[1]));
                graph[i][j] = Double.parseDouble(content[2]);

                //Prepare next line
                line = br.readLine();

            }

            return true;

        } catch(IOException | NumberFormatException e) {
            reset();
            return false;
        }

    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Product> getCommandProducts() {
        return commandProducts;
    }

    public double[][] getGraph() {
        return graph;
    }

    public void reset() {
        products = null;
        graph = null;
    }

}