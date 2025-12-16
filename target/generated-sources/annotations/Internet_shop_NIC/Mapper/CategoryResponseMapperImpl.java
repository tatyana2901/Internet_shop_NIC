package Internet_shop_NIC.Mapper;

import Internet_shop_NIC.DTO.CategoryResponse;
import Internet_shop_NIC.Entity.Category;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-16T12:34:58+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 1.8.0_462 (Amazon.com Inc.)"
)
@Component
public class CategoryResponseMapperImpl implements CategoryResponseMapper {

    @Override
    public CategoryResponse toCategoryResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse categoryResponse = new CategoryResponse();

        if ( category.getId() != null ) {
            categoryResponse.setId( category.getId() );
        }
        categoryResponse.setName( category.getName() );

        return categoryResponse;
    }
}
