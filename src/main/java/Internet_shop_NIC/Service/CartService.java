package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CartItemRequest;
import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Exception.OutOfStockProductException;
import Internet_shop_NIC.Exception.ProductNotFoundException;
import Internet_shop_NIC.Repository.CartRepository;
import Internet_shop_NIC.Repository.ProductRepository;
import Internet_shop_NIC.Security.UsDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void updateCartItemQuantity(CartItemRequest cartItemRequest,
                                       UsDetails usDetails) {

        int quantity = cartItemRequest.getQuantity();
        Long productId = cartItemRequest.getProductId();
        Long userId = usDetails.getUser().getId();
        if (quantity == 0) {
            cartRepository.deleteByUserIdAndProductId(userId, productId);
            return;
        }
        int stockQuantity = getProductQuantityAvailable(productId);
        if (stockQuantity < quantity) {
            throw new OutOfStockProductException("Недостаточно товара на складе. На складе товара: " + stockQuantity + ", вы кладете в корзину: " + quantity);
        }
        CartItem cartItem = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setUserId(userId);
                    newItem.setProductId(productId);
                    newItem.setQuantity(0);
                    newItem.setCreated_at(LocalDateTime.now());
                    return newItem;
                });

        cartItem.setQuantity(quantity);
        cartRepository.save(cartItem);

    }

    private int getProductQuantityAvailable(Long productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Не найден продукт по id " + productId))
                .getStock_quantity();
    }

}

