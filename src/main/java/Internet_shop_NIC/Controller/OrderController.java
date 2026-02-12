package Internet_shop_NIC.Controller;

import Internet_shop_NIC.Security.UsDetails;
import Internet_shop_NIC.Service.OrderService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<Void> createOrder(@AuthenticationPrincipal
                                            @Parameter(hidden = true)
                                            UsDetails usDetails) {
        orderService.createOrder(usDetails);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
