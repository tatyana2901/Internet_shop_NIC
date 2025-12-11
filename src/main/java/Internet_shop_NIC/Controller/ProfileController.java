package Internet_shop_NIC.Controller;

import Internet_shop_NIC.DTO.CurrentUserDTO;
import Internet_shop_NIC.Security.UsDetails;
import Internet_shop_NIC.Service.ProfileService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/user")
    @SecurityRequirement(name = "BearerAuth") //Swagger
    public CurrentUserDTO getCurrentUser(@AuthenticationPrincipal
                                         @Parameter(hidden = true) UsDetails usDetails) //Swagger
    {
        return profileService.toCurrentUserDTO(usDetails);
    }

}
