package Internet_shop_NIC.DTO;

public class CurrentUserDTO {

    private String currentUser;


    public CurrentUserDTO(String currentUser) {
        this.currentUser = currentUser;
    }

    public CurrentUserDTO() {
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
}
