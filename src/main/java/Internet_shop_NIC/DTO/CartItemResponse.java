package Internet_shop_NIC.DTO;

public class CartItemResponse {

    private Long productId;
    private String imageUrl;
    private String name;
    private int quantity; //проверить количество товара на складе - вдруг уже раскупили
    private int availableStock;
    private double price; //скидочная цена если есть

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public double getTotalPrice() {
        return availableStock * price;
    }

    public CartItemResponse(Long productId, String imageUrl, String name, int quantity, double price) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public CartItemResponse() {
    }
}
