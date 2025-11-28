package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.ProductCatalogDTO;
import Internet_shop_NIC.DTO.ProductListingDTO;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductListingDTO> getSortedProductsByCategoryAndSubCat(Long categoryId, String sort) {
        if (categoryId != null && categoryId > 0 && sort != null) {
            List<Product> products;
            if (sort.equals("price-desc")) {
                products = productRepository.findProductsByCategoryAndSubcategorySortedOnBasePriceDESC(categoryId);
            } else {
                products = productRepository.findProductsByCategoryAndSubcategorySortedOnBasePriceASC(categoryId);
            }

            List<ProductListingDTO> dtoProducts = products
                    .stream()
                    .map(this::toProductDTO)
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


    private ProductListingDTO toProductDTO(Product product) {
        if (product != null) {
            ProductListingDTO productListingDTO = new ProductListingDTO(product.getName(),
                    product.getDescription(),
                    product.getImage_url(),
                    product.getBase_price(),
                    product.getDiscount_percent());

            int amount = product.getStock_quantity();
            if (amount > 5) {
                productListingDTO.setAvailability("В наличии");
            } else if (amount > 0) {
                productListingDTO.setAvailability("Мало");
            } else productListingDTO.setAvailability("Нет в наличии");
            return productListingDTO;
        } else throw new IllegalArgumentException("product is null");
    }


}
