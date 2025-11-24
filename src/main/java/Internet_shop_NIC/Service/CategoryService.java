package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CategoryDTO;
import Internet_shop_NIC.Entity.Category;
import Internet_shop_NIC.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<CategoryDTO> getRootCategories() {
        List<Category> categories = categoryRepository.findByParentsIsEmpty();
        return categories.stream()
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());


    }

    @Transactional // работает и без нее, но непонятно, почему
    public List<CategoryDTO> getSubCategories(Long parentId) {
        if (parentId > 0) {
            Optional<Category> categoryOptional = categoryRepository.findById(parentId);
            return categoryOptional.map(category -> category.getChildren().stream()
                    .map(this::toCategoryDTO)
                    .toList()).orElseGet(ArrayList::new);
        }
        throw new IllegalArgumentException("parentId is incorrect");
    }

    private CategoryDTO toCategoryDTO(Category category) {
        if (category != null) {
            return new CategoryDTO(category.getCategoryId(), category.getName());
        } else throw new IllegalArgumentException("category is null");
    }


   /* public Set<Product> getProductsByCategoryAndSubCat(Long categoryId) {
        //РЕШИТЬ С СОРТИРОВКОЙ _ КОМПААТОР????
        Set<Product> products = new HashSet<>();

        if (categoryId > 0) {
            Optional<Category> optCat = categoryRepository.findById(categoryId);

            if (optCat.isPresent()) {
                Category category = optCat.get();
                if (category.getProducts() != null) {
                    products.addAll(new HashSet<>(category.getProducts()));
                }
                if (category.getChildren() != null){
                    for (Category child : category.getChildren()) {
                        getProductsByCategoryAndSubCat(child.getCategory_id());
                    }

                }


            }

        }

    }*/

}
