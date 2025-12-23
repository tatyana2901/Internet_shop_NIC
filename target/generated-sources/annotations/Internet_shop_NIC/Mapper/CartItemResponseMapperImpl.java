package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.CartItemResponse;
import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Entity.Product;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-23T15:48:06+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 1.8.0_452 (Amazon.com Inc.)"
)
@Component
public class CartItemResponseMapperImpl extends CartItemResponseMapper {

    @Override
    public CartItemResponse toCartItemResponse(Product product, CartItem cartItem) {
        if ( product == null && cartItem == null ) {
            return null;
        }

        CartItemResponse cartItemResponse = new CartItemResponse();

        if ( product != null ) {
            cartItemResponse.setImage_url( product.getImage_url() );
            cartItemResponse.setName( product.getName() );
        }
        if ( cartItem != null ) {
            cartItemResponse.setProductId( cartItem.getProductId() );
            cartItemResponse.setQuantity( cartItem.getQuantity() );
        }

        setPrice( product, cartItemResponse );
        setQuantity( product, cartItem, cartItemResponse );

        return cartItemResponse;
    }
}
