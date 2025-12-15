package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CurrentUserResponse;
import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Security.UsDetails;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {


    public ProfileService() {
    }

    public CurrentUserResponse toCurrentUserDTO(UsDetails usDetails) {
        if (usDetails != null) {
            Users user = usDetails.getUser();
            String lastname = user.getLast_name();
            String firstNameLetter = user.getFirst_name().substring(0, 1);
            return new CurrentUserResponse(lastname + " " + firstNameLetter + ".");
        }
        return null;
    }

}
