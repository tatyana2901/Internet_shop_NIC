package Internet_shop_NIC.Repository;

import Internet_shop_NIC.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByUserId(Long userId);

    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);



}
