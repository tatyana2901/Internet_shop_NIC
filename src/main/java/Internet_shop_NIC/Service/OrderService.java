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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final YandexEmailService emailService;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final UserService userService;
    private final FromCartItemToOrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final OrderConfirmationToEmailResponseMapper orderConfirmMapper;

    @Autowired
    public OrderService(YandexEmailService emailService,
                        CartRepository cartRepository,
                        CartService cartService,
                        UserService userService,
                        FromCartItemToOrderItemMapper orderItemMapper,
                        OrderRepository orderRepository, ProductService productService, OrderConfirmationToEmailResponseMapper orderConfirmMapper) {
        this.emailService = emailService;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.userService = userService;
        this.orderItemMapper = orderItemMapper;
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderConfirmMapper = orderConfirmMapper;
    }

    @Transactional
    public void createOrder(UsDetails usDetails) {
        Long userId = userService.getUserId(usDetails);
        if (!userService.ifUserExists(userId)) {
            throw new UserNotExistException("Пользователь с id " + userId + " не найден.");
        }
        Users users = userService.getUser(usDetails);

        List<CartItem> cartItemsNotInStock = cartRepository.findCartItemsNotInStock(userId);
        if (!cartItemsNotInStock.isEmpty()) {
            throw new OutOfStockProductException("Не хватает товара для оформления заказа. Уменьшите количество товара в соответствие с доступным остатком.");
        }

        List<CartItem> cartItems = cartService.getAllUserCartItems(userId);
        if (cartItems.isEmpty()) {
            throw new CartIsEmptyException("Корзина пуста. Добавьте товары в корзину перед оформлением заказа");
        }

        Map<Long, CartItem> mappedCartItemsToProductIds = cartService.mapCartItemsToProductIds(cartItems);
        List<Product> productsByUserCartItems = cartService.getProductsByUserCartItems(mappedCartItemsToProductIds);

        List<OrderItem> orderItems = productsToOrderItems(productsByUserCartItems, mappedCartItemsToProductIds);


        Orders orders = new Orders();
        Double orderTotalPrice = getOrderTotalPrice(orderItems);
        orders.setTotalPrice(orderTotalPrice);
        orders.setOrderItems(orderItems);
        orders.setUser(users);
        orders.setCreatedAt(LocalDateTime.now());


        List<Product> decreasedQuantityProducts = decreaseProductQuantity(productsByUserCartItems, mappedCartItemsToProductIds);
        productService.updateProducts(decreasedQuantityProducts);
        cartService.deleteAllUserCartItems(userId);
        orderRepository.save(orders);

        List<OrderConfirmationToEmailResponse> orderConfirmItems = productsToOrderConfirmItems(productsByUserCartItems, mappedCartItemsToProductIds);
        String userEmail = users.getEmail();
        Long ordersNumber = orders.getId();
        emailService.sendOrderConfirmation(userEmail, orderTotalPrice, ordersNumber, orderConfirmItems);
    }

    private Double getOrderTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

    private List<OrderItem> productsToOrderItems(List<Product> productsByUserCartItems,
                                                 Map<Long, CartItem> mappedCartItemsToProductIds) {
        return productsByUserCartItems.stream()
                .map(product -> {
                    CartItem cartItem = getCartItemByProduct(mappedCartItemsToProductIds, product);
                    return orderItemMapper.ToOrderItem(product, cartItem);
                })
                .collect(Collectors.toList());
    }

    private List<Product> decreaseProductQuantity(List<Product> productsByUserCartItems,
                                                  Map<Long, CartItem> mappedCartItemsToProductIds) {

        productsByUserCartItems.forEach(product -> {
            CartItem cartItem = getCartItemByProduct(mappedCartItemsToProductIds, product);
            int cartItemQuantity = cartItem.getQuantity();
            int decreasedProductQuantity = product.getStockQuantity() - cartItemQuantity;
            product.setStockQuantity(decreasedProductQuantity);
        });
        return productsByUserCartItems;
    }

    private List<OrderConfirmationToEmailResponse> productsToOrderConfirmItems(List<Product> productsByUserCartItems,
                                                                               Map<Long, CartItem> mappedCartItemsToProductIds) {
        return productsByUserCartItems.stream()
                .map(product -> {
                    CartItem cartItem = getCartItemByProduct(mappedCartItemsToProductIds, product);
                    return orderConfirmMapper.toOrderConfirmationToEmailResponse(product, cartItem);
                })
                .collect(Collectors.toList());

    }

    private CartItem getCartItemByProduct(Map<Long, CartItem> mappedCartItemsToProductIds, Product product) {
        Long productId = product.getId();
        return mappedCartItemsToProductIds.get(productId);
    }
}
