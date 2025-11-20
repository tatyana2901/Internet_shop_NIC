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

  @Transactional
    public List<CategoryDTO> getFullCategoryTree() {
        List<Category> categories = categoryRepository.findByParentsIsEmpty();
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());


    }

    private CategoryDTO convertToDto(Category category) {
        CategoryDTO dto = new CategoryDTO(category.getCategory_id(), category.getName());

        // Если детей нет - останавливаем рекурсию
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            List<CategoryDTO> childDtos = category.getChildren().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            dto.setChildren(childDtos);
        } else {
            dto.setChildren(new ArrayList<>()); // явно указываем пустой список
        }

        return dto;
    }

}
