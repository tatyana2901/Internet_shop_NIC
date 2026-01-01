package Internet_shop_NIC.Repository;

import Internet_shop_NIC.DTO.TotalAmountOfProductsInCartResponse;
import Internet_shop_NIC.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByUserId(Long userId);

    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT new Internet_shop_NIC.DTO.TotalAmountOfProductsInCartResponse(SUM(c.quantity)) FROM CartItem c WHERE c.userId = :userId")
    TotalAmountOfProductsInCartResponse totalAmountOfProductsInCart(@Param("userId") Long userId);

    @Query("SELECT ci FROM CartItem ci LEFT JOIN Product p ON ci.productId = p.id WHERE ci.userId = :userId AND (ci.quantity>p.stockQuantity or p.stockQuantity IS NULL)")
    List<CartItem> findCartItemsNotInStock(@Param("userId") Long userId);

}

//select ci.id, stock_quantity from cart_item ci left join product p on product_id = p.id where user_id = 4 and ci.quantity>p.stock_quantity or p.stock_quantity is null;