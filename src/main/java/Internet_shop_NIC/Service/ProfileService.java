package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.CurrentUserDTO;
import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Security.UsDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    public ProfileService() {
    }

    public CurrentUserDTO toCurrentUserDTO(UserDetails userDetails) {

        if (userDetails != null) {
            UsDetails ud = (UsDetails) userDetails;
            Users user = ud.getUser();
            String lastname = user.getLast_name();
            String firstNameLetter = user.getFirst_name().substring(0, 1);
            return new CurrentUserDTO(lastname + " " + firstNameLetter + ".");
        }

        return null;

    }

}
