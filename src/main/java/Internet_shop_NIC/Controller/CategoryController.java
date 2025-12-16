package Internet_shop_NIC.Controller;


import Internet_shop_NIC.DTO.CategoryResponse;
import Internet_shop_NIC.Service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping()
    public List<CategoryResponse> getRootCategories() {
        return categoryService.getRootCategories();
    }


    @GetMapping("/{id}")
    public List<CategoryResponse> getSubCategories(@PathVariable("id") Long id) {
        return categoryService.getSubCategories(id);
    }
}

