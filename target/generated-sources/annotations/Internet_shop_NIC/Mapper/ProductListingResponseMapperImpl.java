package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.ProductListingResponse;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Security.UsDetails;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-30T16:21:13+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 1.8.0_452 (Amazon.com Inc.)"
)
@Component
public class ProductListingResponseMapperImpl extends ProductListingResponseMapper {

    @Override
    public ProductListingResponse toProductListingResponse(Product product, UsDetails usDetails) {
        if ( product == null && usDetails == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String description = null;
        String imageUrl = null;
        double basePrice = 0.0d;
        if ( product != null ) {
            id = product.getId();
            name = product.getName();
            description = product.getDescription();
            imageUrl = product.getImageUrl();
            basePrice = product.getBasePrice();
        }

        ProductListingResponse productListingResponse = new ProductListingResponse( id, name, description, imageUrl, basePrice );

        productListingResponse.setDiscountedPrice( 0.0 );

        setAvailability( product, productListingResponse );
        setDiscountedPrice( product, productListingResponse, usDetails );

        return productListingResponse;
    }
}
