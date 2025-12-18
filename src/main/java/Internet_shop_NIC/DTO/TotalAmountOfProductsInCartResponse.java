package Internet_shop_NIC.DTO;

public class TotalAmountOfProductsInCartResponse {

   private Long totalAmount;

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public TotalAmountOfProductsInCartResponse(Long totalAmount) {
        this.totalAmount = totalAmount;
    }
}
