package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CategoryResponse;
import Internet_shop_NIC.DTO.CategoryAddingRequest;
import Internet_shop_NIC.Entity.Category;
import Internet_shop_NIC.Exception.CategoryAlreadyExistsException;
import Internet_shop_NIC.Exception.CategoryNotExistException;
import Internet_shop_NIC.Exception.EmptyCategoryNameException;
import Internet_shop_NIC.Exception.NoRootCategoryException;
import Internet_shop_NIC.Mapper.CategoryAddingRequestMapper;
import Internet_shop_NIC.Mapper.CategoryResponseMapper;
import Internet_shop_NIC.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryResponseMapper categoryResponseMapper;
    private final CategoryAddingRequestMapper categoryAddingRequestMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryResponseMapper categoryResponseMapper, CategoryAddingRequestMapper categoryAddingRequestMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryResponseMapper = categoryResponseMapper;
        this.categoryAddingRequestMapper = categoryAddingRequestMapper;
    }


    public List<CategoryResponse> getRootCategories() {
        List<Category> categories = categoryRepository.findByParentsIsEmpty();
        if (categories.isEmpty()) {
            throw new NoRootCategoryException("Не найдены корневые категории.");
        }
        return categories.stream()
                .map(categoryResponseMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CategoryResponse> getSubCategories(Long parentId) {
        if (parentId > 0) {
            Optional<Category> categoryOptional = categoryRepository.findById(parentId);
            return categoryOptional.map(category -> category.getChildren().stream()
                    .map(categoryResponseMapper::toCategoryResponse).
                    collect(Collectors.toList())).orElseGet(ArrayList::new);
        }
        throw new IllegalArgumentException("parentId is incorrect");
    }

    @Transactional
    public void addNewCategory(CategoryAddingRequest categoryAddingRequest) {
        String newCategoryName = categoryAddingRequest.getName();
        if (newCategoryName == null) {
            throw new EmptyCategoryNameException("Не запоплнено имя новой категории.");
        }
        if (categoryRepository.existsByName(newCategoryName)) {
            throw new CategoryAlreadyExistsException("Категория с таким названием уже существует в системе");
        }

        Category category = categoryAddingRequestMapper.toCategory(categoryAddingRequest);

        List<Long> parentsId = categoryAddingRequest.getParentsId();
        if (!parentsId.isEmpty()) {
            List<Category> parentCategories = getCategoriesFromIds(parentsId);
            category.setParents(parentCategories);
        }

        categoryRepository.save(category);

        List<Long> childrenId = categoryAddingRequest.getChildrenId();
        if (!childrenId.isEmpty()) {
            List<Category> childrenCategories = getCategoriesFromIds(childrenId);

            for (Category childrenCategory : childrenCategories) {
                childrenCategory.getParents().add(category);
            }
        }
    }

    List<Category> getCategoriesFromIds(List<Long> categoryIds) {
        return categoryIds.stream()
                .map(this::getCategoryById)
                .collect(Collectors.toList());
    }

    private Category getCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.orElseThrow(() -> new CategoryNotExistException("Категории с таким id не существует"));
    }


}
