package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CartPageResponse;
import Internet_shop_NIC.DTO.CategoryResponse;
import Internet_shop_NIC.Entity.Category;
import Internet_shop_NIC.Mapper.CategoryResponseMapper;
import Internet_shop_NIC.Mapper.CategoryResponseMapperImpl;
import Internet_shop_NIC.Repository.CategoryRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Spy
    private CategoryResponseMapper categoryResponseMapper = new CategoryResponseMapperImpl();

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getRootCategories_shouldReturnListOfCategoryResponse() {
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

      //  when(categoryResponseMapper.toCategoryResponse(category1)).thenReturn(categoryResponse1);
     //   when(categoryResponseMapper.toCategoryResponse(category2)).thenReturn(categoryResponse2);

        List<CategoryResponse> result = categoryService.getRootCategories();

        assertEquals(2, result.size());
        assertEquals(categoryResponse1.getId(), result.get(0).getId());
        assertEquals(categoryResponse2.getId(), result.get(1).getId());

        verify(categoryRepository, times(1)).findByParentsIsEmpty();
        verify(categoryResponseMapper, times(1)).toCategoryResponse(categories.get(0));
        verify(categoryResponseMapper, times(1)).toCategoryResponse(categories.get(1));
    }

    @Test
    void getRootCategories_shouldReturnEmptyList_WhenNoCategories() {
        when(categoryRepository.findByParentsIsEmpty()).thenReturn(new ArrayList<>());

        List<CategoryResponse> result = categoryService.getRootCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository).findByParentsIsEmpty();
    }

    @Test
    void getSubCategories_shouldReturnMappedSubCategories() {
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
    void getSubCategories_shouldReturnEmptyList_WhenNoChildren() {
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
    void getSubCategories_shouldThrows_WhenInvalidParentId() {
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.getSubCategories(0L);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.getSubCategories(-1L);
        });
    }

}