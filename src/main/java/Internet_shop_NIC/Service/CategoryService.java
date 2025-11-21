package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CategoryDTO;
import Internet_shop_NIC.Entity.Category;
import Internet_shop_NIC.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    public List<CategoryDTO> getSubCategories(Long parentId) {
        if (parentId > 0) {
            return categoryRepository.findByParentsId(parentId).stream()
                    .map(this::toCategoryDTO)
                    .collect(Collectors.toList());
        } else throw new IllegalArgumentException("incorrect id");
    }

    private CategoryDTO toCategoryDTO(Category category) {
        if (category != null) {
            return new CategoryDTO(category.getCategory_id(), category.getName());
        } else throw new IllegalArgumentException("category is null");
    }

}
