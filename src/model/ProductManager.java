package model;

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
    private double[][] dependencyMatrix;
    private HashMap<Long, Integer> hashMap;

    public ProductManager() {}

    public boolean initProducts(String productListFile, String productDependencyFile) {

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

            //Prepare product dependency matrix
            int size = products.size();
            dependencyMatrix = new double[size][size];
            hashMap = new HashMap<>();
            for(int i = 0; i < size; i++) {
                Product product = products.get(i);
                hashMap.put(product.getId(), i);
            }

            //Get products dependency
            br.close();
            br = new BufferedReader(new FileReader(PATH + productDependencyFile));
            line = br.readLine();

            while(line != null && (!line.isEmpty() || line.startsWith("\n"))) {

                //Obtain line information
                String[] content = line.split(" ");
                int i = hashMap.get(Long.parseLong(content[0]));
                int j = hashMap.get(Long.parseLong(content[1]));
                dependencyMatrix[i][j] = Double.parseDouble(content[2]);

                //Prepare next line
                line = br.readLine();

            }

            return true;

        } catch(IOException | JSONException | NumberFormatException e) {
            reset();
            return false;
        }

    }

    public boolean isInit() {
        return products != null && hashMap != null && dependencyMatrix != null;
    }

    public void reset() {
        products = null;
        hashMap = null;
        dependencyMatrix = null;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public double[][] getDependencyMatrix() {
        return dependencyMatrix;
    }

}