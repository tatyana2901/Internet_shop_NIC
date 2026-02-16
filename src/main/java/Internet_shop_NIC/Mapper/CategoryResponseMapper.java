package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.CategoryResponse;
import Internet_shop_NIC.Entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryResponseMapper {

    CategoryResponse toCategoryResponse(Category category);
}
