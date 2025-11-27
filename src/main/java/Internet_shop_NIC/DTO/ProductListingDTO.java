package Internet_shop_NIC.DTO;


public class ProductListingDTO {

    private String name;
    private String description;
    private String image_url;
    private String availability;
    private double base_price;
    private int discount_percent;


    public ProductListingDTO(String name, String description, String image_url, double base_price, int discount_percent) {
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.base_price = base_price;
        this.discount_percent = discount_percent;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "name='" + name + '\'' +
                ", image_url='" + image_url + '\'' +
                ", availability='" + availability + '\'' +
                ", base_price=" + base_price +
                ", discount_percent=" + discount_percent +
                '}';
    }
}
