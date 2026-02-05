package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CartItemUpdateRequest;
import Internet_shop_NIC.DTO.CartPageResponse;
import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Exception.OutOfStockProductException;
import Internet_shop_NIC.Exception.ProductNotFoundException;
import Internet_shop_NIC.Mapper.CartItemResponseMapper;
import Internet_shop_NIC.Mapper.CartItemResponseMapperImpl;
import Internet_shop_NIC.Mapper.CurrentUserResponseMapper;
import Internet_shop_NIC.Mapper.CurrentUserResponseMapperImpl;
import Internet_shop_NIC.Repository.CartRepository;
import Internet_shop_NIC.Repository.ProductRepository;
import Internet_shop_NIC.Security.UsDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @Spy
    private CartItemResponseMapper cartItemResponseMapper = new CartItemResponseMapperImpl();
    @Spy
    private CurrentUserResponseMapper currentUserResponseMapper = new CurrentUserResponseMapperImpl();
    @Mock
    private UserService userService;
    @InjectMocks
    private CartService cartService;

    private CartItemUpdateRequest cartItemUpdateRequest;
    private UsDetails usDetails;
    private Users user;
    private Product product;


    @Test
    void updateCartItemQuantity_ShouldDeleteCartItemWhenZeroQuantity() {
        cartItemUpdateRequest = new CartItemUpdateRequest();
        user = new Users();
        user.setId(1L);
        usDetails = new UsDetails(user);
        product = new Product();
        product.setId(100L);
        product.setStockQuantity(10);

        cartItemUpdateRequest.setProductId(100L);
        cartItemUpdateRequest.setQuantity(0);

        cartService.updateCartItemQuantity(cartItemUpdateRequest, usDetails);

        verify(cartRepository).deleteByUserIdAndProductId(1L, 100L);
        verify(cartRepository, never()).findByUserIdAndProductId(anyLong(), anyLong());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void updateCartItemQuantity_ShouldUpdateExistingCartItemQuantity() {
        cartItemUpdateRequest = new CartItemUpdateRequest();
        product = new Product();
        user = new Users();
        user.setId(1L);
        usDetails = new UsDetails(user);
        cartItemUpdateRequest.setProductId(90L);
        cartItemUpdateRequest.setQuantity(3);
        product.setStockQuantity(10);
        when(productRepository.findById(90L)).thenReturn(Optional.of(product));

        CartItem existingCartItem = new CartItem();
        existingCartItem.setId(50L);
        existingCartItem.setUserId(1L);
        existingCartItem.setProductId(90L);
        existingCartItem.setQuantity(1);
        existingCartItem.setCreatedAt(LocalDateTime.now());

        when(cartRepository.findByUserIdAndProductId(1L, 90L))
                .thenReturn(Optional.of(existingCartItem));

        cartService.updateCartItemQuantity(cartItemUpdateRequest, usDetails);

        assertEquals(3, existingCartItem.getQuantity());
        verify(cartRepository).save(existingCartItem);
        verify(cartRepository, never()).deleteByUserIdAndProductId(anyLong(), anyLong());
    }

    @Test
    void updateCartItemQuantity_ShouldThrowsOutOfStockProductExceptionWhenQuantityStockIsNotEnough() {
        CartItemUpdateRequest request = new CartItemUpdateRequest();

        user = new Users();
        user.setId(2L);
        usDetails = new UsDetails(user);

        request.setProductId(100L);
        request.setQuantity(15);

        Product product = new Product();
        product.setId(100L);
        product.setStockQuantity(10);

        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        OutOfStockProductException exception = assertThrows(
                OutOfStockProductException.class,
                () -> cartService.updateCartItemQuantity(request, usDetails)
        );

        assertTrue(exception.getMessage().contains("Недостаточно товара на складе"));
        assertTrue(exception.getMessage().contains("На складе товара: 10"));
        assertTrue(exception.getMessage().contains("вы кладете в корзину: 15"));

        verify(cartRepository, never()).save(any());
        verify(cartRepository, never()).deleteByUserIdAndProductId(anyLong(), anyLong());
        verify(cartRepository, never()).findByUserIdAndProductId(anyLong(), anyLong());
    }

    @Test
    void updateCartItemQuantity_ShouldThrowsProductNotFoundExceptionWhenProductIsNotFound() {
        CartItemUpdateRequest request = new CartItemUpdateRequest();

        user = new Users();
        user.setId(1L);
        usDetails = new UsDetails(user);

        request.setProductId(10L);
        request.setQuantity(12);

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> cartService.updateCartItemQuantity(request, usDetails)
        );
    }

    private CartItem createCartItem(Long id, Long userId, Long productId, int quantity) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setUserId(userId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setCreatedAt(LocalDateTime.now());
        return item;
    }

    @Test
    void mapCartItemsToProductIds_ShouldMapCartItems() {
        CartItem item1 = createCartItem(1L, 10L, 100L, 2);
        CartItem item2 = createCartItem(2L, 10L, 200L, 3);
        CartItem item3 = createCartItem(3L, 10L, 300L, 1);

        List<CartItem> cartItems = new ArrayList<>(Arrays.asList(item1, item2, item3));

        Map<Long, CartItem> result = cartService.mapCartItemsToProductIds(cartItems);

        assertEquals(3, result.size(), "Мапа должна содержать 3 элемента");

        assertEquals(item1.getId(), result.get(100L).getId(), "Элемент с productId=100 должен соответствовать item1");
        assertEquals(item2.getId(), result.get(200L).getId(), "Элемент с productId=200 должен соответствовать item2");
        assertEquals(item3.getId(), result.get(300L).getId(), "Элемент с productId=300 должен соответствовать item3");

        assertTrue(result.keySet().containsAll(Arrays.asList(100L, 200L, 300L)));
    }

 /*   void setUpBeforeGetCartPageByUserIdTest() {


        user = new Users();
        user.setId(1L);
        user.setUsername("testuser");

        usDetails = new UsDetails(user);
        usDetails.setUsername("testuser");


        product1 = new Product();
        product1.setId(100L);
        product1.setName("Product 1");
        product1.setPrice(10.5);
        product1.setStock(5);

        product2 = new Product();
        product2.setId(200L);
        product2.setName("Product 2");
        product2.setPrice(20.0);
        product2.setStock(3);

        cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setUserId(1L);
        cartItem1.setProductId(100L);
        cartItem1.setQuantity(2);

        cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setUserId(1L);
        cartItem2.setProductId(200L);
        cartItem2.setQuantity(1);
    }*/

    @Test
    void getCartPageByUserId_ShouldReturnZeroTotalsWhenEmptyCart() {
        user = new Users();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("test");
        user.setLastName("testTest");
        user.setCreatedAt(LocalDateTime.now());
        user.setRole("Role");

        usDetails = new UsDetails(user);
        when(userService.getUserId(usDetails)).thenReturn(1L);
        when(cartRepository.findAllByUserId(1L)).thenReturn(Collections.emptyList());

        CartPageResponse response = cartService.getCartPageByUserId(usDetails);

        assertNotNull(response);
        assertNotNull(response.getCurrentUserResponse());
        assertEquals("testTest t.", response.getCurrentUserResponse().getCurrentUser());

        assertTrue(response.getItems().isEmpty());
        assertEquals(0, response.getTotalItems());
        assertEquals(0.0, response.getTotalPrice());
    }

  /*  @Test
    void getCartPageByUserId_ShouldReturnCorrectCartPage() {

        when(userService.getUserId(usDetails)).thenReturn(1L);
        when(cartRepository.findByUserId(1L)).thenReturn(Arrays.asList(cartItem1, cartItem2));

        // Маппинг продуктов по ID для запроса
        Map<Long, CartItem> cartItemsMap = Arrays.asList(cartItem1, cartItem2).stream()
                .collect(Collectors.toMap(CartItem::getProductId, ci -> ci));
…        assertEquals(8, response.getTotalItems()); // 5 + 3 (сумма стоков)
        assertEquals(41.0, response.getTotalPrice()); // 21.0 + 20.0
    }


    @Test
    void getCartPageByUserId_singleItemWithZeroStock_calculatesCorrectly() {
        // given
        Product outOfStockProduct = new Product();
        outOfStockProduct.setId(300L);
        outOfStockProduct.setName("Out of Stock");
…        assertEquals(15.0, response.getTotalPrice()); // цена всё равно считается
    }*/
}