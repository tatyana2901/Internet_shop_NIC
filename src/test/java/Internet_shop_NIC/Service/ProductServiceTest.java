package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.ProductCatalogResponse;
import Internet_shop_NIC.DTO.ProductListingResponse;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Exception.ProductNotFoundException;
import Internet_shop_NIC.Mapper.ProductListingResponseMapper;
import Internet_shop_NIC.Mapper.ProductListingResponseMapperImpl;
import Internet_shop_NIC.Repository.ProductRepository;
import Internet_shop_NIC.Security.UsDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Spy
    private ProductListingResponseMapper productListingResponseMapper = new ProductListingResponseMapperImpl();
    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;
    private Product product3;
    private UsDetails usDetails;
    private Users user;

    @BeforeEach
    void setUp() {
        user = new Users();
        usDetails = new UsDetails(user);
        productService = new ProductService(productRepository, productListingResponseMapper);

        product1 = new Product();
        product1.setId(1L);
        product1.setName("Product A");
        product1.setBasePrice(100.0);
        product1.setDiscountPercent(10);
        product1.setStockQuantity(10);
        product1.setCreatedAt(LocalDateTime.now());

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Product B");
        product2.setBasePrice(50.0);
        product2.setDiscountPercent(0);
        product2.setStockQuantity(5);
        product2.setCreatedAt(LocalDateTime.now());

        product3 = new Product();
        product3.setId(3L);
        product3.setName("Product C");
        product3.setBasePrice(200.0);
        product3.setDiscountPercent(20);
        product3.setStockQuantity(15);
        product3.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getSortedProductsByCategoryAndSubCat_ShouldReturnASCSortedProducts() {
        Long categoryId = 1L;
        List<Product> products = Arrays.asList(product2, product1, product3);

        when(productRepository.findProductsByCategoryAndSubcategorySortedOnBasePriceASC(categoryId))
                .thenReturn(products);

        List<ProductListingResponse> result = productService.getSortedProductsByCategoryAndSubCat(
                categoryId, "price-asc", usDetails);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(50.0, result.get(0).getBasePrice());
        assertEquals(100.0, result.get(1).getBasePrice());
        assertEquals(200.0, result.get(2).getBasePrice());

        assertEquals("Product B", result.get(0).getName());
        assertEquals("Product A", result.get(1).getName());
        assertEquals("Product C", result.get(2).getName());

        verify(productRepository).findProductsByCategoryAndSubcategorySortedOnBasePriceASC(categoryId);
    }

    @Test
    void getSortedProductsByCategoryAndSubCat_ShouldReturnDESCSortedProducts() {
        Long categoryId = 1L;
        List<Product> products = Arrays.asList(product3, product1, product2);

        when(productRepository.findProductsByCategoryAndSubcategorySortedOnBasePriceDESC(categoryId))
                .thenReturn(products);

        List<ProductListingResponse> result = productService.getSortedProductsByCategoryAndSubCat(
                categoryId, "price-desc", usDetails);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(200.0, result.get(0).getBasePrice());
        assertEquals(100.0, result.get(1).getBasePrice());
        assertEquals(50.0, result.get(2).getBasePrice());

        assertEquals("Product C", result.get(0).getName());
        assertEquals("Product A", result.get(1).getName());
        assertEquals("Product B", result.get(2).getName());

        verify(productRepository).findProductsByCategoryAndSubcategorySortedOnBasePriceDESC(categoryId);
    }

    @Test
    void getSortedProductsByCategoryAndSubCat_ShouldThrowIllegalArgumentExceptionWhenInvalidCategoryId() {
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getSortedProductsByCategoryAndSubCat(null, "price-asc", usDetails);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            productService.getSortedProductsByCategoryAndSubCat(0L, "price-asc", usDetails);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            productService.getSortedProductsByCategoryAndSubCat(-1L, "price-asc", usDetails);
        });

        verify(productRepository, never()).findProductsByCategoryAndSubcategorySortedOnBasePriceASC(any());
        verify(productRepository, never()).findProductsByCategoryAndSubcategorySortedOnBasePriceDESC(any());
    }

    @Test
    void getSortedProductsByCategoryAndSubCat_ShouldThrowProductNotFoundExceptionWhenEmptyList() {
        Long categoryId = 1L;

        when(productRepository.findProductsByCategoryAndSubcategorySortedOnBasePriceASC(categoryId))
                .thenReturn(Collections.emptyList());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getSortedProductsByCategoryAndSubCat(categoryId, "price-asc", usDetails);
        });

        assertTrue(exception.getMessage().contains("No product by category and subcategory"));

        verify(productRepository).findProductsByCategoryAndSubcategorySortedOnBasePriceASC(categoryId);
    }

    @Test
    void getDirectProductsByCategory_ShouldReturnProductCatalogResponses() {
        Long categoryId = 1L;
        List<Product> products = Arrays.asList(product1, product2, product3);

        when(productRepository.findAllByCategoriesId(categoryId)).thenReturn(products);

        List<ProductCatalogResponse> result = productService.getDirectProductsByCategory(categoryId);

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals("Product A", result.get(0).getName());
        assertEquals(1L, result.get(0).getId());

        assertEquals("Product B", result.get(1).getName());
        assertEquals(2L, result.get(1).getId());

        assertEquals("Product C", result.get(2).getName());
        assertEquals(3L, result.get(2).getId());

        verify(productRepository).findAllByCategoriesId(categoryId);
    }

    @Test
    void getDirectProductsByCategory_ShouldThrowProductNotFoundExceptionWhenEmptyList() {
        Long categoryId = 1L;

        when(productRepository.findAllByCategoriesId(categoryId)).thenReturn(Collections.emptyList());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getDirectProductsByCategory(categoryId);
        });

        assertTrue(exception.getMessage().contains("No direct product by category"));

        verify(productRepository).findAllByCategoriesId(categoryId);
    }

    @Test
    void updateProducts_ShouldUpdateProducts() {
        List<Product> productsToUpdate = Arrays.asList(product1, product2, product3);

        productService.updateProducts(productsToUpdate);

        verify(productRepository).saveAll(productsToUpdate);
    }

}
