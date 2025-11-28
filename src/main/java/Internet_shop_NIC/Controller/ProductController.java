package Internet_shop_NIC.Controller;

import Internet_shop_NIC.DTO.CategoryDTO;
import Internet_shop_NIC.DTO.ProductCatalogDTO;
import Internet_shop_NIC.DTO.ProductListingDTO;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Repository.ProductRepository;
import Internet_shop_NIC.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    //в каталоге
    @GetMapping("/categories/{categoryId}/direct_products")
    public List<ProductCatalogDTO> getProductsByCategory(@PathVariable("categoryId") Long categoryId) {
        return productService.getDirectProductsByCategory(categoryId);
    }

    //в листинге
    @GetMapping("/categories/{categoryId}/products")
    public List<ProductListingDTO> getAllProductsByCategory(@PathVariable("categoryId") Long categoryId,
                                                            @RequestParam(defaultValue = "price-asc") String sort) {
        return productService.getSortedProductsByCategoryAndSubCat(categoryId, sort);
    }
    //товар по категории


}
