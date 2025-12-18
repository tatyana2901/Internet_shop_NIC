package Internet_shop_NIC.DTO;


public class ProductListingResponse {
    private Long id;
    private String name;
    private String description;
    private String image_url;
    private String availability;
    private double base_price;
    private Double discountedPrice;

    public ProductListingResponse(Long id, String name, String description, String image_url, double base_price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.base_price = base_price;
    }

    public String getDescription() {
        return description;
    }

    public String getAvailability() {
        return availability;
    }

    public String getImage_url() {
        return image_url;
    }

    public double getBase_price() {
        return base_price;
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
                ", image_url='" + image_url + '\'' +
                ", availability='" + availability + '\'' +
                ", base_price=" + base_price +
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
