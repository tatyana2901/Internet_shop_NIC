package Internet_shop_NIC;

import Internet_shop_NIC.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InternetShopNicApplication implements CommandLineRunner {
    @Autowired
    private final CartRepository repository;

    public InternetShopNicApplication(CartRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(InternetShopNicApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println(repository.findNotInStockCartItemsId(4L));
    }
}
