package Internet_shop_NIC.Controller;

import Internet_shop_NIC.DTO.CartItemRequest;
import Internet_shop_NIC.Security.UsDetails;
import Internet_shop_NIC.Service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    //общее количество товаров в корзине - get запрос

    @PatchMapping
    public ResponseEntity<Void> updateCartItem(@RequestBody CartItemRequest cartItemRequest,
                                               @AuthenticationPrincipal UsDetails usDetails) {
        cartService.updateCartItemQuantity(cartItemRequest, usDetails);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
