package Internet_shop_NIC.DTO;

import java.util.ArrayList;
import java.util.List;

public class CartPageResponse {
    private CurrentUserResponse currentUserResponse;      // "Иванов А."
    private List<CartItemResponse> items = new ArrayList<>();     // товары с галочкой
    private Integer totalItems = 0;            // общее количество
    private Double totalPrice = 0.0;

    public CartPageResponse(CurrentUserResponse currentUserResponse) {
        this.currentUserResponse = currentUserResponse;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CurrentUserResponse getCurrentUserResponse() {
        return currentUserResponse;
    }

    public void setCurrentUserResponse(CurrentUserResponse currentUserResponse) {
        this.currentUserResponse = currentUserResponse;
    }
}
