package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.OrderConfirmationToEmailResponse;
import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Entity.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class OrderConfirmationToEmailResponseMapper {


    public abstract OrderConfirmationToEmailResponse toOrderConfirmationToEmailResponse(Product product, CartItem cartItem);

    @AfterMapping
    protected void setPrice(Product product, @MappingTarget OrderConfirmationToEmailResponse orderConfirmResponse) {
        orderConfirmResponse.setPrice(product.getDiscountedPrice());
    }

}
