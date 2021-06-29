package hanu.a2_1801040141.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
    private int id;
    private String thumbnail;
    private String name;
    private int unitPrice;

//    private int cartId;
//    private int quantity;

    // Product
    public Product(int id, String thumbnail, String name, int unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    // Product in shop cart
//    public Product(int cartId, int id, String thumbnail, String name, int unitPrice, int quantity){
//        this.cartId = cartId;
//        this.id = id;
//        this.thumbnail = thumbnail;
//        this.name = name;
//        this.unitPrice = unitPrice;
//        this.quantity = quantity;
//    }

    public Product(JSONObject obj) {
        try {
            int id = obj.getInt("id");
            String thumbnail = obj.getString("thumbnail");
            String name = obj.getString("name");
            int unitPrice = obj.getInt("unitPrice");

            this.id = id;
            this.thumbnail = thumbnail;
            this.name = name;
            this.unitPrice = unitPrice;

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

//    public int getCartId() {
//        return cartId;
//    }
//
//    public void setCartId(int cartId) {
//        this.cartId = cartId;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
}
