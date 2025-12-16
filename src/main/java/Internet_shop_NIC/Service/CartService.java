package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.AddToCartRequest;
import Internet_shop_NIC.Repository.CartRepository;
import Internet_shop_NIC.Security.UsDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void addProductToCart( AddToCartRequest addToCartRequest,
                                  UsDetails usDetails){

        int quantity = addToCartRequest.getQuantity();
        Long productId = addToCartRequest.getProductId();
        Long userId = usDetails.getUser().getId();

        if ()

    }


}
