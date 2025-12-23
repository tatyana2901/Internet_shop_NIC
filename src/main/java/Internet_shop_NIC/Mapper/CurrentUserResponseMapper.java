package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.CurrentUserResponse;
import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Security.UsDetails;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class CurrentUserResponseMapper {

    public abstract CurrentUserResponse toCurrentUserResponse(UsDetails usDetails);


    @AfterMapping
    protected void setCurrentUser(UsDetails usDetails, @MappingTarget CurrentUserResponse currentUserResponse) {
        Users user = usDetails.getUser();
        String lastname = user.getLast_name();
        String firstNameLetter = user.getFirst_name().substring(0, 1);
        currentUserResponse.setCurrentUser(lastname + " " + firstNameLetter + ".");
    }

}
