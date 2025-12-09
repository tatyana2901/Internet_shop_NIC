package Internet_shop_NIC.DTO;

public class JWTResponseDTO {

    private String token;

    public JWTResponseDTO() {
    }

    public JWTResponseDTO(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "JWTResponseDTO{" +
                "token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
