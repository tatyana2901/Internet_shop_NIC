package Internet_shop_NIC.Controller;

import Internet_shop_NIC.DTO.CategoryDTO;
import Internet_shop_NIC.DTO.ProductCatalogDTO;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Repository.ProductRepository;
import Internet_shop_NIC.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    //в каталоге
    @GetMapping("{categoryId}/products")
    public List<ProductCatalogDTO> getProductsByCategory(@PathVariable("categoryId") Long categoryId) {
        return productService.getDirectProductsByCategory(categoryId);
    }

    //в листинге
   /* @GetMapping()
    public String getAllProductsByCategory() {

        System.out.println(productService.getSortedProductsByCategoryAndSubCat(6L));

        return "";
    }
*/
    //товар по категории


}
