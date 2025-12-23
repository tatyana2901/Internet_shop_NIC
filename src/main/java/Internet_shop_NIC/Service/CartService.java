package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.*;
import Internet_shop_NIC.Entity.CartItem;
import Internet_shop_NIC.Entity.Product;
import Internet_shop_NIC.Exception.OutOfStockProductException;
import Internet_shop_NIC.Exception.ProductNotFoundException;
import Internet_shop_NIC.Mapper.CartItemResponseMapper;
import Internet_shop_NIC.Mapper.CurrentUserResponseMapper;
import Internet_shop_NIC.Repository.CartRepository;
import Internet_shop_NIC.Repository.ProductRepository;
import Internet_shop_NIC.Security.UsDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemResponseMapper cartItemResponseMapper;
    private final CurrentUserResponseMapper currentUserResponseMapper;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, CartItemResponseMapper cartItemResponseMapper, CurrentUserResponseMapper currentUserResponseMapper) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemResponseMapper = cartItemResponseMapper;
        this.currentUserResponseMapper = currentUserResponseMapper;
    }

    @Transactional
    public void updateCartItemQuantity(CartItemUpdateRequest cartItemUpdateRequest,
                                       UsDetails usDetails) {

        int quantity = cartItemUpdateRequest.getQuantity();
        Long productId = cartItemUpdateRequest.getProductId();
        Long userId = usDetails.getUser().getId();
        if (quantity == 0) {
            cartRepository.deleteByUserIdAndProductId(userId, productId);
            return;
        }
        int stockQuantity = getProductQuantityInStock(productId);
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

    private int getProductQuantityInStock(Long productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Не найден продукт по id " + productId))
                .getStock_quantity();
    }

    public TotalAmountOfProductsInCartResponse getTotalAmountOfProductsInCart(UsDetails usDetails) {
        return cartRepository.totalAmountOfProductsInCart(usDetails.getUser().getId());
    }

    public CartPageResponse getCartPageByUserId(UsDetails usDetails) {

        CurrentUserResponse currentUserResponse = currentUserResponseMapper.toCurrentUserResponse(usDetails);
        CartPageResponse cartPageResponse = new CartPageResponse(currentUserResponse);

        Long userId = usDetails.getUser().getId();

        List<CartItem> allUserProducts = cartRepository.findAllByUserId(userId);

        if (!allUserProducts.isEmpty()) {
            Map<Long, CartItem> cartProducts = allUserProducts.stream().
                    collect(Collectors.toMap(CartItem::getProductId, item -> item));

            List<Product> products = productRepository.findAllById(cartProducts.keySet());

            List<CartItemResponse> cartItems = products.stream()
                    .map(product -> {
                        Long productId = product.getId();
                        CartItem cartItem = cartProducts.get(productId);
                        return cartItemResponseMapper.toCartItemResponse(product, cartItem);
                    })
                    .collect(Collectors.toList());

            Integer totalItems = cartItems.stream().mapToInt(CartItemResponse::getQuantity).sum();
            Double totalPrice = cartItems.stream().mapToDouble(CartItemResponse::getTotalPrice).sum();

            cartPageResponse.setItems(cartItems);
            cartPageResponse.setTotalItems(totalItems);
            cartPageResponse.setTotalPrice(totalPrice);

        }
        return cartPageResponse;
    }


}

