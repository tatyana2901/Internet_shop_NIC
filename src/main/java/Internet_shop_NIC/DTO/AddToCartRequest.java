package Internet_shop_NIC.DTO;

import javax.validation.constraints.NotNull;

public class AddToCartRequest {
    @NotNull
    private Long productId;
    @NotNull
    private int quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
