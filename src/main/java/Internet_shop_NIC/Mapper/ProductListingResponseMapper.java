package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.ProductListingResponse;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Security.UsDetails;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class ProductListingResponseMapper {

    @Mapping(target = "discountedPrice", constant = "0.0")
    public abstract ProductListingResponse toProductListingResponse(Product product, UsDetails usDetails);

    @AfterMapping
    protected void setAvailability(Product product, @MappingTarget ProductListingResponse productListingResponse) {
        int amount = product.getStockQuantity();
        if (amount > 5) {
            productListingResponse.setAvailability("В наличии");
        } else if (amount > 0) {
            productListingResponse.setAvailability("Мало");
        } else productListingResponse.setAvailability("Нет в наличии");
    }

    @AfterMapping
    protected void setDiscountedPrice(Product product, @MappingTarget ProductListingResponse productListingResponse, UsDetails usDetails) {
        if (usDetails != null && product.getDiscountPercent() != null) {
            Double discountedPrice = product.getDiscountedPrice();
            productListingResponse.setDiscountedPrice(discountedPrice);
        }
    }

}
