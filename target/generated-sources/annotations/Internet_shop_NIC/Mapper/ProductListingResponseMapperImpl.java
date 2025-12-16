package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.ProductListingResponse;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Security.UsDetails;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-16T12:34:58+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 1.8.0_462 (Amazon.com Inc.)"
)
@Component
public class ProductListingResponseMapperImpl extends ProductListingResponseMapper {

    @Override
    public ProductListingResponse toProductListingResponse(Product product, UsDetails usDetails) {
        if ( product == null && usDetails == null ) {
            return null;
        }

        String name = null;
        String description = null;
        String image_url = null;
        double base_price = 0.0d;
        if ( product != null ) {
            name = product.getName();
            description = product.getDescription();
            image_url = product.getImage_url();
            base_price = product.getBase_price();
        }

        ProductListingResponse productListingResponse = new ProductListingResponse( name, description, image_url, base_price );

        setAvailability( product, productListingResponse );
        setDiscountedPrice( product, productListingResponse, usDetails );

        return productListingResponse;
    }
}
