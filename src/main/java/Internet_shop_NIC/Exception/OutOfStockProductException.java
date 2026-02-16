package Internet_shop_NIC.Exception;

public class OutOfStockProductException extends RuntimeException {
    public OutOfStockProductException(String message) {
        super(message);
    }
}
