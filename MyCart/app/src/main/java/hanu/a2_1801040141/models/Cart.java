package hanu.a2_1801040141.models;

public class Cart {
    private int cartId;
    private int productId;
    private String name;
    private String thumbnail;
    private int unitPrice;
    private int quantity;

    public Cart(int cartId, int productId, String name, String thumbnail, int unitPrice, int quantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.name = name;
        this.thumbnail = thumbnail;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public Cart(int productId, String name, String thumbnail, int unitPrice, int quantity) {
        this.productId = productId;
        this.name = name;
        this.thumbnail = thumbnail;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
