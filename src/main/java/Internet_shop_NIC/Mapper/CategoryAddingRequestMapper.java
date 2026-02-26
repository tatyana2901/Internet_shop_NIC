package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.CategoryAddingRequest;
import Internet_shop_NIC.Entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CategoryAddingRequestMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    public abstract Category toCategory(CategoryAddingRequest categoryAddingRequest);

}
