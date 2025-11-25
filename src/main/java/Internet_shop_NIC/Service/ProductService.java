package Internet_shop_NIC.Service;

import Internet_shop_NIC.Entity.Category;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

/*
    public Set<Product> getProductsByCategoryAndSubCat(Long categoryId) {
        //РЕШИТЬ С СОРТИРОВКОЙ _ КОМПААТОР????
        Set<Product> products = new HashSet<>();

        if (categoryId > 0) {

            List<Product> rootProducts = productRepository.findByCategoriesId(categoryId);


        }

    }*/

}
