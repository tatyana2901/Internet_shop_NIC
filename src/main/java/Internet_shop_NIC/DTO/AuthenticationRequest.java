package Internet_shop_NIC.DTO;

import javax.validation.constraints.NotEmpty;

public class AuthenticationRequest {

    @NotEmpty(message = "Введите email!")
    private String email;
    @NotEmpty(message = "Введите пароль!")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
