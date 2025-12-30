package Internet_shop_NIC.Controller;

import Internet_shop_NIC.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CartRepository cartRepository;

    @Autowired
    public OrderController(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

  /*  @PostMapping
    public void makeOrder() {



    }*/


}
