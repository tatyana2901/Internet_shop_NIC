package Internet_shop_NIC.Repository;


import Internet_shop_NIC.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

   // List<Product> findAllByCategoryId(Long id);

    List<Product> findByCategoriesId(Long id);
}
