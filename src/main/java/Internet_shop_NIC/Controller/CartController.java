package Internet_shop_NIC.Controller;

import Internet_shop_NIC.DTO.CartItemUpdateRequest;
import Internet_shop_NIC.DTO.CartPageResponse;
import Internet_shop_NIC.DTO.TotalAmountOfProductsInCartResponse;
import Internet_shop_NIC.Security.UsDetails;
import Internet_shop_NIC.Service.CartService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PatchMapping
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<Void> updateCartItem(@RequestBody CartItemUpdateRequest cartItemUpdateRequest,
                                               @AuthenticationPrincipal
                                               @Parameter(hidden = true)
                                               UsDetails usDetails) {
        cartService.updateCartItemQuantity(cartItemUpdateRequest, usDetails);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/count")
    @SecurityRequirement(name = "BearerAuth") //Swagger
    public TotalAmountOfProductsInCartResponse getTotalAmountProductsByUserId(@AuthenticationPrincipal
                                                                              @Parameter(hidden = true)
                                                                              UsDetails usDetails) {
        return cartService.getTotalAmountOfProductsInCart(usDetails);
    }

    /*@GetMapping("/")
    public CartPageResponse getCartPage(@AuthenticationPrincipal @Parameter(hidden = true)UsDetails usDetails)
                                                                               {
    }*/



}
