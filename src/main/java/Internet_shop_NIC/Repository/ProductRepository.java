package Internet_shop_NIC.Repository;


import Internet_shop_NIC.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategoriesId(Long id);

    @Query(value = "WITH RECURSIVE category_tree AS (\n" +
            "    SELECT id\n" +
            "    FROM category\n" +
            "    WHERE id = :categoryId\n" +
            "\n" +
            "    UNION ALL\n" +
            "\n" +
            "    SELECT c.id\n" +
            "    FROM category c\n" +
            "    JOIN category_parent cp ON c.id = cp.child_id\n" +
            "    JOIN category_tree ct ON cp.parent_id = ct.id\n" +
            ")\n" +
            "SELECT DISTINCT p.*\n" +
            "FROM product p\n" +
            "JOIN product_category pc ON p.id = pc.product_id\n" +
            "WHERE pc.category_id IN (SELECT id FROM category_tree)", nativeQuery = true)
    List<Product> findProductsByCategoryAndSubcategory(@Param("categoryId") Long id);


}
