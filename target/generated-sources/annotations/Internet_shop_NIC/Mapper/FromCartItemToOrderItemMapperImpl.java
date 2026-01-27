package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Entity.OrderItem;
import Internet_shop_NIC.Entity.Product;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-27T12:49:25+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 1.8.0_462 (Amazon.com Inc.)"
)
@Component
public class FromCartItemToOrderItemMapperImpl extends FromCartItemToOrderItemMapper {

    @Override
    public OrderItem ToOrderItem(Product product, CartItem cartItem) {
        if ( product == null && cartItem == null ) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        if ( cartItem != null ) {
            orderItem.setQuantity( cartItem.getQuantity() );
        }
        orderItem.setProduct( product );
        orderItem.setCreatedAt( java.time.LocalDateTime.now() );

        setPrice( product, orderItem );
        setProduct( product, orderItem );

        return orderItem;
    }
}
