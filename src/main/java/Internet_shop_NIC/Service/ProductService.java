package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.ProductCatalogDTO;
import Internet_shop_NIC.DTO.ProductListingDTO;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Repository.ProductRepository;
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

    public List<ProductListingDTO> getSortedProductsByCategoryAndSubCat(Long categoryId) {
        //РЕШИТЬ С СОРТИРОВКОЙ _ КОМПААТОР????
        if (categoryId > 0) {
            List<Product> products = productRepository.findProductsByCategoryAndSubcategory(categoryId);
            List<ProductListingDTO> sortedDtoProducts = products
                    .stream()
                    .sorted((o1, o2) -> (int) (o1.getBase_price() - o2.getBase_price()))
                    .map(this::toProductDTO)
                    .collect(Collectors.toList());
            return sortedDtoProducts;

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
