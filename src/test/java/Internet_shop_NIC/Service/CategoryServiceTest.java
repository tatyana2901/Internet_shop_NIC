package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CategoryAddingRequest;
import Internet_shop_NIC.DTO.CategoryResponse;
import Internet_shop_NIC.Entity.Category;
import Internet_shop_NIC.Exception.CategoryAlreadyExistsException;
import Internet_shop_NIC.Exception.EmptyCategoryNameException;
import Internet_shop_NIC.Exception.NoRootCategoryException;
import Internet_shop_NIC.Mapper.CategoryAddingRequestMapper;
import Internet_shop_NIC.Mapper.CategoryAddingRequestMapperImpl;
import Internet_shop_NIC.Mapper.CategoryResponseMapper;
import Internet_shop_NIC.Mapper.CategoryResponseMapperImpl;
import Internet_shop_NIC.Repository.CategoryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Spy
    private CategoryResponseMapper categoryResponseMapper = new CategoryResponseMapperImpl();
    @Mock
    private CategoryAddingRequestMapper categoryAddingRequestMapper;


    @InjectMocks
    private CategoryService categoryService;

    private CategoryAddingRequest categoryAddingRequest;
    private Category newCategory;
    private Category parentCategory;
    private Category childCategory;

    void setUpRequest() {
        categoryAddingRequest = new CategoryAddingRequest();
        categoryAddingRequest.setName("Test Category");
        categoryAddingRequest.setParentsId(new ArrayList<>());
        categoryAddingRequest.setChildrenId(new ArrayList<>());
    }

    void setUpCategory() {
        newCategory = new Category();
        newCategory.setName("Test Category");
        newCategory.setParents(new ArrayList<>());
    }

    void setUpParentCategory() {
        parentCategory = new Category();
        parentCategory.setId(1L);
        parentCategory.setName("Parent Category");
        parentCategory.setParents(new ArrayList<>());
    }

    void setUpChildCategory() {
        childCategory = new Category();
        childCategory.setId(2L);
        childCategory.setName("Child Category");
        childCategory.setParents(new ArrayList<>());
    }

    @Test
    void getRootCategories_ShouldReturnListOfCategoryResponse() {
        List<Category> categories = new ArrayList<>();

        Category category1 = new Category();
        category1.setId(1L);
        Category category2 = new Category();
        category2.setId(2L);
        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findByParentsIsEmpty()).thenReturn(categories);

        CategoryResponse categoryResponse1 = new CategoryResponse();
        categoryResponse1.setId(1L);
        CategoryResponse categoryResponse2 = new CategoryResponse();
        categoryResponse2.setId(2L);

        List<CategoryResponse> result = categoryService.getRootCategories();

        assertEquals(2, result.size());
        assertEquals(categoryResponse1.getId(), result.get(0).getId());
        assertEquals(categoryResponse2.getId(), result.get(1).getId());

        verify(categoryRepository, times(1)).findByParentsIsEmpty();
        verify(categoryResponseMapper, times(1)).toCategoryResponse(categories.get(0));
        verify(categoryResponseMapper, times(1)).toCategoryResponse(categories.get(1));
    }

    @Test
    void getRootCategories_ShouldThrowNoRootCategoryException_WhenParentsListIsEmpty() {
        when(categoryRepository.findByParentsIsEmpty()).thenReturn(new ArrayList<>());
        assertThrows(NoRootCategoryException.class, () -> {
            categoryService.getRootCategories();
        });
    }


    @Test
    void getSubCategories_ShouldReturnMappedSubCategories() {
        Category testCategory = new Category();
        testCategory.setId(1L);
        Category childCategory = new Category();
        childCategory.setId(2L);

        List<Category> children = new ArrayList<>();
        children.add(childCategory);
        testCategory.setChildren(children);

        CategoryResponse testResponse = new CategoryResponse();
        testResponse.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryResponseMapper.toCategoryResponse(childCategory)).thenReturn(testResponse);

        List<CategoryResponse> result = categoryService.getSubCategories(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testResponse, result.get(0));
        verify(categoryRepository).findById(1L);
        verify(categoryResponseMapper).toCategoryResponse(childCategory);

    }

    @Test
    void getSubCategories_ShouldReturnEmptyList_WhenNoChildren() {
        Category testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setChildren(new ArrayList<>());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        List<CategoryResponse> result = categoryService.getSubCategories(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getSubCategories_ShouldThrows_WhenInvalidParentId() {
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.getSubCategories(0L);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.getSubCategories(-1L);
        });
    }

    @Test
    void addNewCategory_ShouldThrowCategoryAlreadyExistsException_WhenCategoryNameAlreadyExists() {
        setUpRequest();
        setUpCategory();

        when(categoryRepository.existsByName("Test Category")).thenReturn(true);

        assertThrows(
                CategoryAlreadyExistsException.class,
                () -> categoryService.addNewCategory(categoryAddingRequest)
        );
    }

    @Test
    void addNewCategory_ShouldThrowEmptyCategoryNameException_WhenNameIsNull() {
        setUpRequest();
        categoryAddingRequest.setName(null);

        assertThrows(
                EmptyCategoryNameException.class,
                () -> categoryService.addNewCategory(categoryAddingRequest)
        );

    }


    @Test
    void addNewCategory_ShouldThrowCategoryAlreadyExistsException_WhenNameIsNotUnique() {
        setUpRequest();
        setUpCategory();

        when(categoryRepository.existsByName("Test Category")).thenReturn(true);
        assertThrows(
                CategoryAlreadyExistsException.class,
                () -> categoryService.addNewCategory(categoryAddingRequest)
        );
    }

    @Test
    void addNewCategory_ShouldAddBothParentsAndChildren_WhenBothProvided() {
        setUpRequest();
        setUpCategory();
        setUpParentCategory();
        setUpChildCategory();

        categoryAddingRequest.setParentsId(Collections.singletonList(1L));
        categoryAddingRequest.setChildrenId(Collections.singletonList(2L));

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(childCategory));

        when(categoryRepository.existsByName("Test Category")).thenReturn(false);
        when(categoryAddingRequestMapper.toCategory(categoryAddingRequest)).thenReturn(newCategory);

        categoryService.addNewCategory(categoryAddingRequest);

        assertEquals(1, newCategory.getParents().size(), "Должна быть одна родительская категория");
        assertEquals(parentCategory, newCategory.getParents().get(0), "Родительская категория должна совпадать");

        assertTrue(childCategory.getParents().contains(newCategory),
                "Новая категория должна быть добавлена к родителям дочерней категории");

    }
}