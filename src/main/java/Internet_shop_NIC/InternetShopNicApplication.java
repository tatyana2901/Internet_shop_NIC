package Internet_shop_NIC;

import Internet_shop_NIC.Repository.UserRepository;
import Internet_shop_NIC.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class InternetShopNicApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternetShopNicApplication.class, args);
    }


}
