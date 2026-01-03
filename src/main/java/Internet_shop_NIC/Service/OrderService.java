package Internet_shop_NIC.Service;

import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Exception.CartIsEmptyException;
import Internet_shop_NIC.Exception.OutOfStockProductException;
import Internet_shop_NIC.Repository.CartRepository;
import Internet_shop_NIC.Security.UsDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {


    private final CartRepository cartRepository;
    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public OrderService(CartRepository cartRepository, OrderService orderService, CartService cartService, UserService userService) {
        this.cartRepository = cartRepository;
        this.orderService = orderService;
        this.cartService = cartService;
        this.userService = userService;
    }

    //@Transactional
    public void makeOrder(UsDetails usDetails) {
        Long id = userService.getUserId(usDetails);
        List<CartItem> cartItemsNotInStock = cartRepository.findCartItemsNotInStock(id);
        if (!cartItemsNotInStock.isEmpty()) {
            throw new OutOfStockProductException("Не хватает товара для оформления заказа. Уменьшите количество товара в соответствие с фактическим наличием.");
        }

        List<CartItem> cartItems = cartRepository.findAllByUserId(id);

        if (cartItems.isEmpty()) {
            throw new CartIsEmptyException("Корзина пуста. Добавьте товары в корзину перед оформление заказа");
        }

//ИНИЦИАЛИЗИРОВАТЬ В СУЩНОСТЯХ ВСЕ СПИСКИ!!!!!!


    }
    //выполнить как транзакцию
    //получить все товары из корзины

    //выбросить исключение если на складе остатков меньше чем в корзине
    //проверить остатки
    //очистить корзину
    //уменьшить остатки на складе

    //отправка письма на почту
}
