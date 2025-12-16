package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.CurrentUserResponse;
import Internet_shop_NIC.Security.UsDetails;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-16T18:50:37+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 1.8.0_452 (Amazon.com Inc.)"
)
@Component
public class CurrentUserResponseMapperImpl extends CurrentUserResponseMapper {

    @Override
    public CurrentUserResponse toCurrentUserDTO(UsDetails usDetails) {
        if ( usDetails == null ) {
            return null;
        }

        CurrentUserResponse currentUserResponse = new CurrentUserResponse();

        setCurrentUser( usDetails, currentUserResponse );

        return currentUserResponse;
    }
}
