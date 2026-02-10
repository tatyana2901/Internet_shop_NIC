package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.OrderConfirmationToEmailResponse;
import Internet_shop_NIC.Entity.*;
import Internet_shop_NIC.Exception.CartIsEmptyException;
import Internet_shop_NIC.Exception.OutOfStockProductException;
import Internet_shop_NIC.Exception.UserNotExistException;
import Internet_shop_NIC.Mapper.FromCartItemToOrderItemMapper;
import Internet_shop_NIC.Mapper.OrderConfirmationToEmailResponseMapper;
import Internet_shop_NIC.Repository.CartRepository;
import Internet_shop_NIC.Repository.OrderRepository;
import Internet_shop_NIC.Security.UsDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private YandexEmailService emailService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private FromCartItemToOrderItemMapper orderItemMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private OrderConfirmationToEmailResponseMapper orderConfirmMapper;

    @InjectMocks
    private OrderService orderService;

    private UsDetails usDetails;
    private Users user;
    private CartItem cartItem;
    private Product product;
    private OrderItem orderItem;
    private OrderConfirmationToEmailResponse orderConfirmResponse;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1L);
        user.setEmail("test@example.com");

        usDetails = new UsDetails(user);
        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setUserId(1L);
        cartItem.setProductId(1L);
        cartItem.setQuantity(2);

        product = new Product();
        product.setId(1L);
        product.setStockQuantity(10);
        product.setBasePrice(100.0);

        orderItem = new OrderItem();
        orderItem.setQuantity(2);
        orderItem.setPrice(100.0);
    }

    @Test
    void createOrder_ShouldCreateOrderSuccessfully() {
        Long userId = 1L;
        List<CartItem> cartItems = Arrays.asList(cartItem);
        Map<Long, CartItem> cartItemsMap = new HashMap<>();
        cartItemsMap.put(1L, cartItem);
        List<Product> products = Arrays.asList(product);

        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(2);
        orderItem.setPrice(100.0);

        OrderConfirmationToEmailResponse emailResponse =
                new OrderConfirmationToEmailResponse("Test Product", 100.0, 2);

        when(userService.getUserId(usDetails)).thenReturn(userId);
        when(userService.ifUserExists(userId)).thenReturn(true);
        when(userService.getUser(usDetails)).thenReturn(user);
        when(cartRepository.findCartItemsNotInStock(userId)).thenReturn(Collections.emptyList());
        when(cartService.getAllUserCartItems(userId)).thenReturn(cartItems);
        when(cartService.mapCartItemsToProductIds(cartItems)).thenReturn(cartItemsMap);
        when(cartService.getProductsByUserCartItems(cartItemsMap)).thenReturn(products);
        when(orderItemMapper.ToOrderItem(product, cartItem)).thenReturn(orderItem);
        when(orderConfirmMapper.toOrderConfirmationToEmailResponse(product, cartItem))
                .thenReturn(emailResponse);

        when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> {
            Orders order = invocation.getArgument(0);
            order.setId(100L);
            return order;
        });

        orderService.createOrder(usDetails);

        verify(orderRepository).save(any(Orders.class));
        verify(orderConfirmMapper).toOrderConfirmationToEmailResponse(
                eq(product),
                eq(cartItem)
        );
        verify(emailService).sendOrderConfirmation(
                eq("test@example.com"),
                eq(200.0),
                eq(100L),
                anyList()
        );
        verify(productService).updateProducts(products);
        verify(cartService).deleteAllUserCartItems(userId);
    }

    @Test
    void createOrder_ShouldThrowUserNotExistExceptionWhenUserNotExist() {
        Long userId = 1L;

        when(userService.getUserId(usDetails)).thenReturn(userId);
        when(userService.ifUserExists(userId)).thenReturn(false);

        UserNotExistException exception = assertThrows(UserNotExistException.class, () -> {
            orderService.createOrder(usDetails);
        });

        assertTrue(exception.getMessage().contains("Пользователь с id " + userId + " не найден"));
        verify(userService).getUserId(usDetails);
        verify(userService).ifUserExists(userId);
        verify(userService, never()).getUser(any());
        verify(cartRepository, never()).findCartItemsNotInStock(any());
    }

    @Test
    @DisplayName("Создание заказа при отсутствии товара на складе выбрасывает исключение")
    void createOrder_ShouldThrowOutOfStockProductExceptionWhenNoProductInStock() {
        Long userId = 1L;
        CartItem outOfStockItem = new CartItem();
        outOfStockItem.setId(2L);
        outOfStockItem.setQuantity(5);
        List<CartItem> outOfStockItems = Arrays.asList(outOfStockItem);

        when(userService.getUserId(usDetails)).thenReturn(userId);
        when(userService.ifUserExists(userId)).thenReturn(true);
        when(userService.getUser(usDetails)).thenReturn(user);
        when(cartRepository.findCartItemsNotInStock(userId)).thenReturn(outOfStockItems);

        OutOfStockProductException exception = assertThrows(OutOfStockProductException.class, () -> {
            orderService.createOrder(usDetails);
        });

        assertTrue(exception.getMessage().contains("Не хватает товара для оформления заказа"));
        verify(userService).getUserId(usDetails);
        verify(userService).ifUserExists(userId);
        verify(userService).getUser(usDetails);
        verify(cartRepository).findCartItemsNotInStock(userId);
        verify(cartService, never()).getAllUserCartItems(any());
    }

    @Test
    void createOrderShouldThrowCartIsEmptyExceptionWhenEmptyCart() {
        Long userId = 1L;

        when(userService.getUserId(usDetails)).thenReturn(userId);
        when(userService.ifUserExists(userId)).thenReturn(true);
        when(userService.getUser(usDetails)).thenReturn(user);
        when(cartRepository.findCartItemsNotInStock(userId)).thenReturn(Collections.emptyList());
        when(cartService.getAllUserCartItems(userId)).thenReturn(Collections.emptyList());

        CartIsEmptyException exception = assertThrows(CartIsEmptyException.class, () -> {
            orderService.createOrder(usDetails);
        });

        assertTrue(exception.getMessage().contains("Корзина пуста"));

        verify(userService).getUserId(usDetails);
        verify(userService).ifUserExists(userId);
        verify(userService).getUser(usDetails);
        verify(cartRepository).findCartItemsNotInStock(userId);
        verify(cartService).getAllUserCartItems(userId);
        verify(cartService, never()).mapCartItemsToProductIds(any());
    }

    @Test
    void createOrder_ShouldSendEmailWithCorrectData() {
        Long userId = 1L;
        List<CartItem> cartItems = Arrays.asList(cartItem);
        Map<Long, CartItem> mappedCartItems = new HashMap<>();
        mappedCartItems.put(product.getId(), cartItem);
        List<Product> products = Arrays.asList(product);

        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(2);
        orderItem.setPrice(100.0);

        OrderConfirmationToEmailResponse orderConfirmResponse =
                new OrderConfirmationToEmailResponse("Test Product", 100.0, 2);

        when(userService.getUserId(usDetails)).thenReturn(userId);
        when(userService.ifUserExists(userId)).thenReturn(true);
        when(userService.getUser(usDetails)).thenReturn(user);
        when(cartRepository.findCartItemsNotInStock(userId)).thenReturn(Collections.emptyList());
        when(cartService.getAllUserCartItems(userId)).thenReturn(cartItems);
        when(cartService.mapCartItemsToProductIds(cartItems)).thenReturn(mappedCartItems);
        when(cartService.getProductsByUserCartItems(mappedCartItems)).thenReturn(products);
        when(orderItemMapper.ToOrderItem(product, cartItem)).thenReturn(orderItem);
        when(orderConfirmMapper.toOrderConfirmationToEmailResponse(product, cartItem))
                .thenReturn(orderConfirmResponse);

        when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> {
            Orders order = invocation.getArgument(0);
            order.setId(200L);
            return order;
        });

        orderService.createOrder(usDetails);

        verify(orderConfirmMapper).toOrderConfirmationToEmailResponse(
                eq(product),
                eq(cartItem)
        );
        verify(emailService).sendOrderConfirmation(
                eq("test@example.com"),
                eq(200.0),
                eq(200L),
                any(Iterable.class)
        );
        verify(productService).updateProducts(products);
        verify(cartService).deleteAllUserCartItems(userId);
    }



}