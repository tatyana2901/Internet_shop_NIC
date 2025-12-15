package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CategoryDTO;
import Internet_shop_NIC.Entity.Category;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Mapper.CategoryMapper;
import Internet_shop_NIC.Repository.CategoryRepository;
import Internet_shop_NIC.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    public List<CategoryDTO> getRootCategories() {
        List<Category> categories = categoryRepository.findByParentsIsEmpty();
        return categories.stream()
                .map(categoryMapper::toCategoryDTO)
                .collect(Collectors.toList());


    }

    @Transactional // работает и без нее, но непонятно, почему
    public List<CategoryDTO> getSubCategories(Long parentId) {
        if (parentId > 0) {
            Optional<Category> categoryOptional = categoryRepository.findById(parentId);
            return categoryOptional.map(category -> category.getChildren().stream()
                    .map(categoryMapper::toCategoryDTO).
                    collect(Collectors.toList())).orElseGet(ArrayList::new);
        }
        throw new IllegalArgumentException("parentId is incorrect");
    }



}
