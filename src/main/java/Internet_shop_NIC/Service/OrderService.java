package Internet_shop_NIC.Service;

import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Entity.OrderItem;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Exception.CartIsEmptyException;
import Internet_shop_NIC.Exception.OutOfStockProductException;
import Internet_shop_NIC.Mapper.FromCartItemToOrderItemMapper;
import Internet_shop_NIC.Repository.CartRepository;
import Internet_shop_NIC.Security.UsDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class OrderService {


    private final CartRepository cartRepository;
    private final CartService cartService;
    private final UserService userService;
    private final FromCartItemToOrderItemMapper orderItemMapper;

    @Autowired
    public OrderService(CartRepository cartRepository, CartService cartService, UserService userService, FromCartItemToOrderItemMapper orderItemMapper) {
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.userService = userService;
        this.orderItemMapper = orderItemMapper;
    }

    //@Transactional
    public void createOrder(UsDetails usDetails) {
        Long userId = userService.getUserId(usDetails);
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


        List<OrderItem> orderItems = productsByUserCartItems.stream().map(new Function<Product, OrderItem>() {
            @Override
            public OrderItem apply(Product product) {
                Long productId = product.getId();
                CartItem cartItem = mappedCartItemsToProductIds.get(productId);
                OrderItem orderItem = orderItemMapper.ToOrderItem(product, cartItem);

                //orderItem->orders
                //orders->users
                //orders->добавить orderItems
                //save (orders)

                return orderItem;
            }
        }).collect(Collectors.toList());
        //mapper fromCartItemToOrderItemMapper


    }
    //выполнить как транзакцию
    //получить все товары из корзины

    //выбросить исключение если на складе остатков меньше чем в корзине
    //проверить остатки
    //очистить корзину
    //уменьшить остатки на складе

    //отправка письма на почту
}
