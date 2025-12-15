package Internet_shop_NIC.Mapper;


import Internet_shop_NIC.DTO.RegistrationRequest;
import Internet_shop_NIC.Entity.Users;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class RegistrationRequestMapper {
    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "role", constant = "ROLE_USER")
    @Mapping(target = "password", ignore = true)
    public abstract Users toUser(RegistrationRequest registrationRequest);

    @AfterMapping
    protected void setPassword(RegistrationRequest dto, @MappingTarget Users user) {
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

}
