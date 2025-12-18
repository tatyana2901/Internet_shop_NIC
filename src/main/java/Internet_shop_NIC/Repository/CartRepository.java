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




}
