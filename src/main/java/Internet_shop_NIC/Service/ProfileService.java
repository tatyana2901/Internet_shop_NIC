package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CurrentUserDTO;
import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Security.UsDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {


    public ProfileService() {
    }

    public CurrentUserDTO toCurrentUserDTO(UsDetails usDetails) {
        if (usDetails != null) {
            Users user = usDetails.getUser();
            String lastname = user.getLast_name();
            String firstNameLetter = user.getFirst_name().substring(0, 1);
            return new CurrentUserDTO(lastname + " " + firstNameLetter + ".");
        }
        return null;
    }

}
