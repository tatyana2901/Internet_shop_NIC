package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.OrderConfirmationToEmailResponse;
import Internet_shop_NIC.Entity.*;
import Internet_shop_NIC.Mapper.FromCartItemToOrderItemMapper;
import Internet_shop_NIC.Mapper.OrderConfirmationToEmailResponseMapper;
import Internet_shop_NIC.Repository.CartRepository;
import Internet_shop_NIC.Repository.OrderRepository;
import Internet_shop_NIC.Security.UsDetails;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        // Arrange
        Long userId = 1L;
        List<CartItem> cartItems = Arrays.asList(cartItem);
        Map<Long, CartItem> cartItemsMap = new HashMap<>();
        cartItemsMap.put(1L, cartItem);
        List<Product> products = Arrays.asList(product);

        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(2);
        orderItem.setPrice(100.0);

        // Создаём корректный объект для мокирования маппера
        OrderConfirmationToEmailResponse emailResponse =
                new OrderConfirmationToEmailResponse("Test Product", 100.0, 2);

        // Настройка моков
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

        // ← КРИТИЧЕСКИ ВАЖНО: эмулируем генерацию ID БД
        when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> {
            Orders order = invocation.getArgument(0);
            order.setId(100L); // устанавливаем ID вручную
            return order;
        });

        // Act
        orderService.createOrder(usDetails);

        // Assert
        // Проверяем сохранение заказа
        verify(orderRepository).save(any(Orders.class));

        // Проверяем вызов маппера (гарантирует правильные данные в письме)
        verify(orderConfirmMapper).toOrderConfirmationToEmailResponse(
                eq(product),
                eq(cartItem)
        );

        // Проверяем отправку письма с правильным ID (не null!)
        verify(emailService).sendOrderConfirmation(
                eq("test@example.com"),
                eq(200.0),   // 2 шт * 100.0 цена
                eq(100L),    // ← теперь не null, а 100L благодаря настройке мока выше
                anyList()    // ← не проверяем содержимое списка напрямую (нет equals)
        );

        // Проверяем остальные действия
        verify(productService).updateProducts(products);
        verify(cartService).deleteAllUserCartItems(userId);
    }
}

  /*  @Test
    @DisplayName("Создание заказа с несуществующим пользователем выбрасывает исключение")
    void createOrder_UserNotExist_ThrowsUserNotExistException() {
        // Arrange
        Long userId = 1L;

        when(userService.getUserId(usDetails)).thenReturn(userId);
        when(userService.ifUserExists(userId)).thenReturn(false);

        // Act & Assert
        UserNotExistException exception = assertThrows(UserNotExistException.class, () -> {
            orderService.createOrder(usDetails);
        });

        assertTrue(exception.getMessage().contains("Пользователь с id " + userId + " не найден"));

        // Verify interactions
        verify(userService).getUserId(usDetails);
        verify(userService).ifUserExists(userId);
        verify(userService, never()).getUser(any());
        verify(cartRepository, never()).findCartItemsNotInStock(any());
    }

    @Test
    @DisplayName("Создание заказа при отсутствии товара на складе выбрасывает исключение")
    void createOrder_OutOfStockProduct_ThrowsOutOfStockProductException() {
        // Arrange
        Long userId = 1L;
        CartItem outOfStockItem = new CartItem();
        outOfStockItem.setId(2L);
        outOfStockItem.setQuantity(5);
        List<CartItem> outOfStockItems = Arrays.asList(outOfStockItem);

        when(userService.getUserId(usDetails)).thenReturn(userId);
        when(userService.ifUserExists(userId)).thenReturn(true);
        when(userService.getUser(usDetails)).thenReturn(testUser);
        when(cartRepository.findCartItemsNotInStock(userId)).thenReturn(outOfStockItems);

        // Act & Assert
        OutOfStockProductException exception = assertThrows(OutOfStockProductException.class, () -> {
            orderService.createOrder(usDetails);
        });

        assertTrue(exception.getMessage().contains("Не хватает товара для оформления заказа"));

        // Verify interactions
        verify(userService).getUserId(usDetails);
        verify(userService).ifUserExists(userId);
        verify(userService).getUser(usDetails);
        verify(cartRepository).findCartItemsNotInStock(userId);
        verify(cartService, never()).getAllUserCartItems(any());
    }

    @Test
    @DisplayName("Создание заказа с пустой корзиной выбрасывает исключение")
    void createOrder_EmptyCart_ThrowsCartIsEmptyException() {
        // Arrange
        Long userId = 1L;

        when(userService.getUserId(usDetails)).thenReturn(userId);
        when(userService.ifUserExists(userId)).thenReturn(true);
        when(userService.getUser(usDetails)).thenReturn(testUser);
        when(cartRepository.findCartItemsNotInStock(userId)).thenReturn(Collections.emptyList());
        when(cartService.getAllUserCartItems(userId)).thenReturn(Collections.emptyList());

        // Act & Assert
        CartIsEmptyException exception = assertThrows(CartIsEmptyException.class, () -> {
            orderService.createOrder(usDetails);
        });

        assertTrue(exception.getMessage().contains("Корзина пуста"));

        // Verify interactions
        verify(userService).getUserId(usDetails);
        verify(userService).ifUserExists(userId);
        verify(userService).getUser(usDetails);
        verify(cartRepository).findCartItemsNotInStock(userId);
        verify(cartService).getAllUserCartItems(userId);
        verify(cartService, never()).mapCartItemsToProductIds(any());
    }

    @Test
    @DisplayName("Проверка уменьшения количества товара на складе")
    void createOrder_DecreasesProductQuantityCorrectly() {
        // Arrange
        Long userId = 1L;
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        Map<Long, CartItem> mappedCartItems = new HashMap<>();
        mappedCartItems.put(testProduct.getId(), testCartItem);
        List<Product> products = Arrays.asList(testProduct);
        List<OrderItem> orderItems = Arrays.asList(testOrderItem);

        Product productWithDecreasedQuantity = new Product();
        productWithDecreasedQuantity.setId(1L);
        productWithDecreasedQuantity.setStockQuantity(8); // 10 - 2

        when(userService.getUserId(usDetails)).thenReturn(userId);
        when(userService.ifUserExists(userId)).thenReturn(true);
        when(userService.getUser(usDetails)).thenReturn(testUser);
        when(cartRepository.findCartItemsNotInStock(userId)).thenReturn(Collections.emptyList());
        when(cartService.getAllUserCartItems(userId)).thenReturn(cartItems);
        when(cartService.mapCartItemsToProductIds(cartItems)).thenReturn(mappedCartItems);
        when(cartService.getProductsByUserCartItems(mappedCartItems)).thenReturn(products);
        when(orderItemMapper.ToOrderItem(testProduct, testCartItem)).thenReturn(testOrderItem);
        when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> {
            Orders order = invocation.getArgument(0);
            order.setId(100L);
            return order;
        });
        when(orderConfirmMapper.toOrderConfirmationToEmailResponse(testProduct, testCartItem))
                .thenReturn(testOrderConfirmResponse);

        // Act
        orderService.createOrder(usDetails);

        // Assert
        ArgumentCaptor<List<Product>> productsCaptor = ArgumentCaptor.forClass(List.class);
        verify(productService).updateProducts(productsCaptor.capture());

        List<Product> updatedProducts = productsCaptor.getValue();
        assertNotNull(updatedProducts);
        assertEquals(1, updatedProducts.size());
        assertEquals(8, updatedProducts.get(0).getStockQuantity());
    }

    @Test
    @DisplayName("Проверка отправки email подтверждения с правильными данными")
    void createOrder_SendsEmailWithCorrectData() {
        // Arrange
        Long userId = 1L;
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        Map<Long, CartItem> mappedCartItems = new HashMap<>();
        mappedCartItems.put(testProduct.getId(), testCartItem);
        List<Product> products = Arrays.asList(testProduct);
        List<OrderItem> orderItems = Arrays.asList(testOrderItem);
        List<OrderConfirmationToEmailResponse> orderConfirmItems = Arrays.asList(testOrderConfirmResponse);
        Orders savedOrder = new Orders();
        savedOrder.setId(200L);

        when(userService.getUserId(usDetails)).thenReturn(userId);
        when(userService.ifUserExists(userId)).thenReturn(true);
        when(userService.getUser(usDetails)).thenReturn(testUser);
        when(cartRepository.findCartItemsNotInStock(userId)).thenReturn(Collections.emptyList());
        when(cartService.getAllUserCartItems(userId)).thenReturn(cartItems);
        when(cartService.mapCartItemsToProductIds(cartItems)).thenReturn(mappedCartItems);
        when(cartService.getProductsByUserCartItems(mappedCartItems)).thenReturn(products);
        when(orderItemMapper.ToOrderItem(testProduct, testCartItem)).thenReturn(testOrderItem);
        when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> {
            Orders order = invocation.getArgument(0);
            order.setId(200L);
            return order;
        });
        when(orderConfirmMapper.toOrderConfirmationToEmailResponse(testProduct, testCartItem))
                .thenReturn(testOrderConfirmResponse);

        // Act
        orderService.createOrder(usDetails);

        // Assert
        verify(emailService).sendOrderConfirmation(
                eq("test@example.com"),
                eq(200.0),
                eq(200L),
                argThat(list -> list.size() == 1 &&
                        list.get(0).getProductName().equals("Test Product") &&
                        list.get(0).getQuantity() == 2)
        );
    }

    @Test
    @DisplayName("Проверка корректного расчета общей стоимости заказа")
    void createOrder_CalculatesTotalPriceCorrectly() {
        // Arrange
        Long userId = 1L;

        // Создаем несколько товаров
        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(100.0);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(50.0);

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setQuantity(2);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setQuantity(3);

        List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2);
        Map<Long, CartItem> mappedCartItems = new HashMap<>();
        mappedCartItems.put(1L, cartItem1);
        mappedCartItems.put(2L, cartItem2);

        List<Product> products = Arrays.asList(product1, product2);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setQuantity(2);
        orderItem1.setTotalPrice(200.0); // 100 * 2

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setQuantity(3);
        orderItem2.setTotalPrice(150.0); // 50 * 3

        when(userService.getUserId(usDetails)).thenReturn(userId);
        when(userService.ifUserExists(userId)).thenReturn(true);
        when(userService.getUser(usDetails)).thenReturn(testUser);
        when(cartRepository.findCartItemsNotInStock(userId)).thenReturn(Collections.emptyList());
        when(cartService.getAllUserCartItems(userId)).thenReturn(cartItems);
        when(cartService.mapCartItemsToProductIds(cartItems)).thenReturn(mappedCartItems);
        when(cartService.getProductsByUserCartItems(mappedCartItems)).thenReturn(products);
        when(orderItemMapper.ToOrderItem(product1, cartItem1)).thenReturn(orderItem1);
        when(orderItemMapper.ToOrderItem(product2, cartItem2)).thenReturn(orderItem2);
        when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> {
            Orders order = invocation.getArgument(0);
            order.setId(300L);
            return order;
        });
        when(orderConfirmMapper.toOrderConfirmationToEmailResponse(any(), any()))
                .thenReturn(testOrderConfirmResponse);

        // Act
        orderService.createOrder(usDetails);

        // Assert
        ArgumentCaptor<Orders> orderCaptor = ArgumentCaptor.forClass(Orders.class);
        verify(orderRepository).save(orderCaptor.capture());

        Orders savedOrder = orderCaptor.getValue();
        assertNotNull(savedOrder);
        assertEquals(350.0, savedOrder.getTotalPrice());
    }*/


