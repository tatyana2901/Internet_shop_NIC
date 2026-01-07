package Internet_shop_NIC;

import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Repository.CartRepository;
import Internet_shop_NIC.Repository.UserRepository;
import Internet_shop_NIC.Security.UsDetails;
import Internet_shop_NIC.Service.OrderService;
import Internet_shop_NIC.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InternetShopNicApplication implements CommandLineRunner {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(InternetShopNicApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

       /*orderService.createOrder(4L);*/
    }
}
