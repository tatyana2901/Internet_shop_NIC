package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.CartItemResponse;
import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Entity.OrderItem;
import Internet_shop_NIC.Entity.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class FromCartItemToOrderItemMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", ignore = true)
    public abstract OrderItem ToOrderItem(Product product, CartItem cartItem);


    @AfterMapping
    protected void setPrice(Product product, @MappingTarget OrderItem orderItem) {
        orderItem.setPrice(product.getDiscountedPrice());
    }

    @AfterMapping
    protected void setProduct(Product product, @MappingTarget OrderItem orderItem) {
        orderItem.setProduct(product);
    }

}
