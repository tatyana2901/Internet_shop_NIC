package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CategoryResponse;
import Internet_shop_NIC.Entity.Category;
import Internet_shop_NIC.Mapper.CategoryResponseMapper;
import Internet_shop_NIC.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryResponseMapper categoryResponseMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryResponseMapper categoryResponseMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryResponseMapper = categoryResponseMapper;
    }


    public List<CategoryResponse> getRootCategories() {
        List<Category> categories = categoryRepository.findByParentsIsEmpty();
        return categories.stream()
                .map(categoryResponseMapper::toCategoryResponse)
                .collect(Collectors.toList());


    }

    @Transactional // работает и без нее, но непонятно, почему
    public List<CategoryResponse> getSubCategories(Long parentId) {
        if (parentId > 0) {
            Optional<Category> categoryOptional = categoryRepository.findById(parentId);
            return categoryOptional.map(category -> category.getChildren().stream()
                    .map(categoryResponseMapper::toCategoryResponse).
                    collect(Collectors.toList())).orElseGet(ArrayList::new);
        }
        throw new IllegalArgumentException("parentId is incorrect");
    }



}
