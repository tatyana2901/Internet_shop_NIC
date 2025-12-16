package Internet_shop_NIC.Controller;

import Internet_shop_NIC.DTO.AddToCartRequest;
import Internet_shop_NIC.Security.UsDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    //общее количество товаров в корзине - get запрос

    @PostMapping
    public ResponseEntity<Void> addProductToCart(@RequestBody AddToCartRequest addToCartRequest,
                                                 @AuthenticationPrincipal UsDetails usDetails) {



    }

}
