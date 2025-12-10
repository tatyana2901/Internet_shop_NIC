package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.ProductCatalogDTO;
import Internet_shop_NIC.DTO.ProductListingDTO;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductListingDTO> getSortedProductsByCategoryAndSubCat(Long categoryId, String sort, UserDetails userDetails) {
        if (categoryId != null && categoryId > 0 && sort != null) {
            List<Product> products;
            if (sort.equals("price-desc")) {
                products = productRepository.findProductsByCategoryAndSubcategorySortedOnBasePriceDESC(categoryId);
            } else {
                products = productRepository.findProductsByCategoryAndSubcategorySortedOnBasePriceASC(categoryId);
            }

            List<ProductListingDTO> dtoProducts = products
                    .stream()
                    .map(p -> toProductDTO(p, userDetails))
                    .collect(Collectors.toList());
            return dtoProducts;

        }
        throw new IllegalArgumentException("categoryId is incorrect");
    }
    //метод количества товаров  категории


    public List<ProductCatalogDTO> getDirectProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findAllByCategoriesId(categoryId);
        System.out.println(products);
        return products
                .stream()
                .map(p -> new ProductCatalogDTO(p.getName()))
                .collect(Collectors.toList());
    }


    private ProductListingDTO toProductDTO(Product product, UserDetails userDetails) {
        if (product != null) {
            ProductListingDTO productListingDTO = new ProductListingDTO(product.getName(),
                    product.getDescription(),
                    product.getImage_url(),
                    product.getBase_price());

            int amount = product.getStock_quantity();
            if (amount > 5) {
                productListingDTO.setAvailability("В наличии");
            } else if (amount > 0) {
                productListingDTO.setAvailability("Мало");
            } else productListingDTO.setAvailability("Нет в наличии");

            if (userDetails != null && product.getDiscount_percent() != null) {
                Double discountedPrice = product.getBase_price() * (1 - product.getDiscount_percent() / 100.00);
                productListingDTO.setDiscountedPrice(discountedPrice);
            }

            return productListingDTO;
        } else throw new IllegalArgumentException("product is null");
    }


}
