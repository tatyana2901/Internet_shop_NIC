package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.CartItemResponse;
import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Entity.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class CartItemResponseMapper {

    public abstract CartItemResponse toCartItemResponse(Product product, CartItem cartItem);

    @AfterMapping
    protected void setPrice(Product product, @MappingTarget CartItemResponse cartItemResponse) {
        cartItemResponse.setPrice(product.getDiscountedPrice());
    }

    @AfterMapping
    protected void setAvailableQuantity(Product product, CartItem cartItem, @MappingTarget CartItemResponse cartItemResponse) {

        int stockQuantity = product.getStockQuantity();
        int cartProductsQuantity = cartItem.getQuantity();

        cartItemResponse.setAvailableStock(Math.min(stockQuantity, cartProductsQuantity));
    }

}
