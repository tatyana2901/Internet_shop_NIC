package Internet_shop_NIC.Controller;

import Internet_shop_NIC.DTO.CategoryDTO;
import Internet_shop_NIC.DTO.ProductCatalogDTO;
import Internet_shop_NIC.DTO.ProductListingDTO;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Repository.ProductRepository;
import Internet_shop_NIC.Security.UsDetails;
import Internet_shop_NIC.Service.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @SecurityRequirement(name = "BearerAuth") //Swagger
    public List<ProductListingDTO> getAllProductsByCategory(@PathVariable("categoryId") Long categoryId,
                                                            @RequestParam(defaultValue = "price-asc") String sort,
                                                            @Parameter(hidden = true) //Swagger
                                                            @AuthenticationPrincipal UsDetails usDetails) {

        return productService.getSortedProductsByCategoryAndSubCat(categoryId, sort, usDetails);
    }
    //товар по категории


}
