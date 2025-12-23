package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.ProductCatalogResponse;
import Internet_shop_NIC.DTO.ProductListingResponse;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Mapper.ProductListingResponseMapper;
import Internet_shop_NIC.Repository.ProductRepository;
import Internet_shop_NIC.Security.UsDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductListingResponseMapper productListingResponseMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductListingResponseMapper productListingResponseMapper) {
        this.productRepository = productRepository;
        this.productListingResponseMapper = productListingResponseMapper;
    }





    public List<ProductListingResponse> getSortedProductsByCategoryAndSubCat(Long categoryId, String sort, UsDetails usDetails) {
        if (categoryId != null && categoryId > 0 && sort != null) {
            List<Product> products;
            if (sort.equals("price-desc")) {
                products = productRepository.findProductsByCategoryAndSubcategorySortedOnBasePriceDESC(categoryId);
            } else {
                products = productRepository.findProductsByCategoryAndSubcategorySortedOnBasePriceASC(categoryId);
            }

            List<ProductListingResponse> dtoProducts = products
                    .stream()
                    .map(p -> productListingResponseMapper.toProductListingResponse(p, usDetails))
                    .collect(Collectors.toList());
            return dtoProducts;

        }
        throw new IllegalArgumentException("categoryId is incorrect");
    }
    //метод количества товаров  категории


    public List<ProductCatalogResponse> getDirectProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findAllByCategoriesId(categoryId);
        return products
                .stream()
                .map(p -> new ProductCatalogResponse(p.getName(), p.getId()))
                .collect(Collectors.toList());
    }


}
