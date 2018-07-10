package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;

public class JsonManager {

    //Dimension params
    private final static String DIMENSION = "dim";
    private final static String DIMENSION_X = "max_x";
    private final static String DIMENSION_Y = "max_y";

    //Entrance params
    private final static String ENTRANCE = "entrance";
    private final static String ENTRANCE_X = "x";
    private final static String ENTRANCE_Y = "y";

    //Shelves config params
    private final static String CONFIG = "shelves_config";
    private final static String CONFIG_ID = "id";
    private final static String CONFIG_LENGTH = "length";

    //Shelves params
    private final static String SHELVES = "shelves";
    private final static String SHELVES_CONFIG = "config";
    private final static String SHELVES_X = "x_start";
    private final static String SHELVES_Y = "y_start";
    private final static String SHELVES_ORIENTATION = "orientation";

    //Product params
    private final static String PRODUCT_ID = "id";
    private final static String PRODUCT_NAME = "name";

    private String jsonContent;

    public JsonManager(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public Dimension getDimension() throws JSONException {
        Dimension dimension = new Dimension();
        JSONObject object = new JSONObject(jsonContent);
        JSONObject jsonObject = object.getJSONObject(DIMENSION);
        dimension.setSize(jsonObject.getInt(DIMENSION_X), jsonObject.getInt(DIMENSION_Y));
        return dimension;
    }

    public Point getEntrance() throws JSONException {
        Point entrance = new Point();
        JSONObject object = new JSONObject(jsonContent);
        JSONObject jsonObject = object.getJSONObject(ENTRANCE);
        entrance.x = jsonObject.getInt(ENTRANCE_X);
        entrance.y = jsonObject.getInt(ENTRANCE_Y);
        return entrance;
    }

    public ArrayList<ShelfConfig> getShelvesConfig() throws JSONException {

        ArrayList<ShelfConfig> shelvesConfig = new ArrayList<>();
        JSONObject object = new JSONObject(jsonContent);
        JSONArray jsonArray = object.getJSONArray(CONFIG);
        int total = jsonArray.length();

        for(int i = 0; i < total; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            ShelfConfig shelfConfig = new ShelfConfig();
            shelfConfig.setId(jsonObject.getInt(CONFIG_ID));
            shelfConfig.setLength(jsonObject.getInt(CONFIG_LENGTH));
            shelvesConfig.add(shelfConfig);
        }

        return shelvesConfig;

    }

    public int getTotalShelvesPattern() throws JSONException {
        JSONObject object = new JSONObject(jsonContent);
        return object.getJSONArray(SHELVES).length();
    }

    public ShelfPattern getShelfPattern(int position) throws JSONException {
        ShelfPattern shelfPattern = new ShelfPattern();
        JSONObject object = new JSONObject(jsonContent);
        JSONObject jsonObject = object.getJSONArray(SHELVES).getJSONObject(position);
        shelfPattern.setShelfId(jsonObject.getInt(SHELVES_CONFIG));
        shelfPattern.setStartPoint(new Point(jsonObject.getInt(SHELVES_X), jsonObject.getInt(SHELVES_Y)));
        shelfPattern.setOrientation(jsonObject.getString(SHELVES_ORIENTATION).toCharArray()[0]);
        return shelfPattern;
    }

    public ArrayList<Product> getProductList() throws JSONException {

        ArrayList<Product> products = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonContent);
        int total = jsonArray.length();

        for(int i = 0; i < total; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Product product = new Product();
            product.setId(jsonObject.getLong(PRODUCT_ID));
            product.setName(jsonObject.getString(PRODUCT_NAME));
            products.add(product);
        }

        return products;

    }

}