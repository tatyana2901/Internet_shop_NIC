package Internet_shop_NIC.DTO;

import java.util.List;

public class CartPageResponse {
    private String currentUser;      // "Иванов А."
    private List<CartItemResponse> items;     // товары с галочкой
    private Long totalItems;              // общее количество
    private Long totalPrice;


}
