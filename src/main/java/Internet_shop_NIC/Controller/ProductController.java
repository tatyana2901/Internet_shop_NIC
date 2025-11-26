package Internet_shop_NIC.Controller;

import Internet_shop_NIC.DTO.CategoryDTO;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductRepository pr;

    @Autowired
    public ProductController(ProductRepository pr) {
        this.pr = pr;
    }

    @GetMapping()
    public String getAllProductsByCategory() {

        System.out.println(pr.findProductsByCategoryAndSubcategory(1L));

        return "";
    }

    //товар по категории


}
