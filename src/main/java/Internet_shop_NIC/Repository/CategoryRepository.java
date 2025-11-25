package Internet_shop_NIC.Repository;


import Internet_shop_NIC.Entity.Category;
import Internet_shop_NIC.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentsIsEmpty();

    Optional<Category> findById(Long id);

}
