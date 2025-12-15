package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.ProductCatalog;
import Internet_shop_NIC.DTO.ProductListing;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Repository.ProductRepository;
import Internet_shop_NIC.Security.UsDetails;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<ProductListing> getSortedProductsByCategoryAndSubCat(Long categoryId, String sort, UsDetails usDetails) {
        if (categoryId != null && categoryId > 0 && sort != null) {
            List<Product> products;
            if (sort.equals("price-desc")) {
                products = productRepository.findProductsByCategoryAndSubcategorySortedOnBasePriceDESC(categoryId);
            } else {
                products = productRepository.findProductsByCategoryAndSubcategorySortedOnBasePriceASC(categoryId);
            }

            List<ProductListing> dtoProducts = products
                    .stream()
                    .map(p -> toProductDTO(p, usDetails))
                    .collect(Collectors.toList());
            return dtoProducts;

        }
        throw new IllegalArgumentException("categoryId is incorrect");
    }
    //метод количества товаров  категории


    public List<ProductCatalog> getDirectProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findAllByCategoriesId(categoryId);
        System.out.println(products);
        return products
                .stream()
                .map(p -> new ProductCatalog(p.getName()))
                .collect(Collectors.toList());
    }


    private ProductListing toProductDTO(Product product, UsDetails usDetails) {
        if (product != null) {
            ProductListing productListing = new ProductListing(product.getName(),
                    product.getDescription(),
                    product.getImage_url(),
                    product.getBase_price());

            int amount = product.getStock_quantity();
            if (amount > 5) {
                productListing.setAvailability("В наличии");
            } else if (amount > 0) {
                productListing.setAvailability("Мало");
            } else productListing.setAvailability("Нет в наличии");

            if (usDetails != null && product.getDiscount_percent() != null) {
                Double discountedPrice = product.getBase_price() * (1 - product.getDiscount_percent() / 100.00);
                productListing.setDiscountedPrice(discountedPrice);
            }

            return productListing;
        } else throw new IllegalArgumentException("product is null");
    }


}
