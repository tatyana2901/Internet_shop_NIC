package Internet_shop_NIC.Repository;


import Internet_shop_NIC.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // List<Product> findAllByCategoryId(Long id);

    @Query(value = """
            WITH RECURSIVE category_tree AS (
                SELECT id
                FROM category
                WHERE id = :categoryId
            
                UNION ALL
            
                SELECT c.id
                FROM category c
                JOIN category_parent cp ON c.id = cp.child_id
                JOIN category_tree ct ON cp.parent_id = ct.id
            )
            SELECT DISTINCT p.*
            FROM product p
            JOIN product_category pc ON p.id = pc.product_id
            WHERE pc.category_id IN (SELECT id FROM category_tree)
            """, nativeQuery = true)
    List<Product> findProductsByCategoryAndSubcategory(@Param("categoryId") Long id);
}
