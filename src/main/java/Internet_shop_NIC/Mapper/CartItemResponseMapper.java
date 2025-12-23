package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.CartItemResponse;
import Internet_shop_NIC.DTO.CurrentUserResponse;
import Internet_shop_NIC.DTO.ProductListingResponse;
import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Security.UsDetails;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class CartItemResponseMapper {

    public abstract CartItemResponse toCartItemResponse(Product product, CartItem cartItem);

    @AfterMapping
    protected void setPrice(Product product, @MappingTarget CartItemResponse cartItemResponse) {
        cartItemResponse.setPrice(product.getDiscountedPrice());
    }

    @AfterMapping
    protected void setQuantity(Product product, CartItem cartItem, @MappingTarget CartItemResponse cartItemResponse) {

        int stockQuantity = product.getStock_quantity();
        int cartProductsQuantity = cartItem.getQuantity();

        cartItemResponse.setQuantity(Math.min(stockQuantity, cartProductsQuantity));
    }

}
