package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.RegistrationRequest;
import Internet_shop_NIC.Entity.Users;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-15T16:48:03+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 1.8.0_452 (Amazon.com Inc.)"
)
@Component
public class RegistrationRequestMapperImpl extends RegistrationRequestMapper {

    @Override
    public Users toUser(RegistrationRequest registrationRequest) {
        if ( registrationRequest == null ) {
            return null;
        }

        Users users = new Users();

        users.setFirst_name( registrationRequest.getFirst_name() );
        users.setLast_name( registrationRequest.getLast_name() );
        users.setEmail( registrationRequest.getEmail() );

        users.setCreatedAt( java.time.LocalDateTime.now() );
        users.setRole( "ROLE_USER" );

        setPassword( registrationRequest, users );

        return users;
    }
}
