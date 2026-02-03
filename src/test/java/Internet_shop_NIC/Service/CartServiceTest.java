package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CartItemUpdateRequest;
import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Exception.OutOfStockProductException;
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
import java.util.Optional;

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
    void getTotalAmountOfProductsInCart() {
    }

    @Test
    void getAllUserCartItems() {
    }

    @Test
    void mapCartItemsToProductIds() {
    }

    @Test
    void getProductsByUserCartItems() {
    }

    @Test
    void getCartPageByUserId() {
    }

    @Test
    void deleteAllUserCartItems() {
    }
}