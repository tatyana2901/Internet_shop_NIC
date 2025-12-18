package Internet_shop_NIC.Controller;

import Internet_shop_NIC.DTO.CurrentUserResponse;
import Internet_shop_NIC.Mapper.CurrentUserResponseMapper;
import Internet_shop_NIC.Security.UsDetails;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final CurrentUserResponseMapper currentUserResponseMapper;

    @Autowired
    public ProfileController(CurrentUserResponseMapper currentUserResponseMapper) {
        this.currentUserResponseMapper = currentUserResponseMapper;
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "BearerAuth") //Swagger
    public CurrentUserResponse getCurrentUser(@AuthenticationPrincipal
                                              @Parameter(hidden = true)
                                              UsDetails usDetails) //Swagger
    {
        return currentUserResponseMapper.toCurrentUserDTO(usDetails);
    }

}
