package Internet_shop_NIC.DTO;


public class ProductListingResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String availability;
    private double basePrice;
    private Double discountedPrice;

    public ProductListingResponse(Long id, String name, String description, String imageUrl, double basePrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.basePrice = basePrice;
    }

    public String getDescription() {
        return description;
    }

    public String getAvailability() {
        return availability;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "name='" + name + '\'' +
                ", image_url='" + imageUrl + '\'' +
                ", availability='" + availability + '\'' +
                ", base_price=" + basePrice +
                ", discount_percent=" + discountedPrice +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}
