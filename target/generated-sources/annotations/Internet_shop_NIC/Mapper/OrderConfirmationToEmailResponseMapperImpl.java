package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.OrderConfirmationToEmailResponse;
import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Entity.Product;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-27T12:49:25+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 1.8.0_462 (Amazon.com Inc.)"
)
@Component
public class OrderConfirmationToEmailResponseMapperImpl extends OrderConfirmationToEmailResponseMapper {

    @Override
    public OrderConfirmationToEmailResponse toOrderConfirmationToEmailResponse(Product product, CartItem cartItem) {
        if ( product == null && cartItem == null ) {
            return null;
        }

        OrderConfirmationToEmailResponse orderConfirmationToEmailResponse = new OrderConfirmationToEmailResponse();

        if ( product != null ) {
            orderConfirmationToEmailResponse.setName( product.getName() );
        }
        if ( cartItem != null ) {
            orderConfirmationToEmailResponse.setQuantity( cartItem.getQuantity() );
        }

        setPrice( product, orderConfirmationToEmailResponse );

        return orderConfirmationToEmailResponse;
    }
}
