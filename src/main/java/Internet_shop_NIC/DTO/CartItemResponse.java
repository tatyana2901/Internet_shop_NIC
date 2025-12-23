package Internet_shop_NIC.DTO;

public class CartItemResponse {

    private Long productId;
    private String image_url;
    private String name;
    private int quantity; //проверить количество товара на складе - вдруг уже раскупили
    private double price; //скидочная цена если есть

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return quantity * price;
    }

    public CartItemResponse(Long productId, String image_url, String name, int quantity, double price) {
        this.productId = productId;
        this.image_url = image_url;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public CartItemResponse() {
    }
}
