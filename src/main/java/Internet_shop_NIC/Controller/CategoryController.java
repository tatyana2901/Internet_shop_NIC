package Internet_shop_NIC.Controller;


import Internet_shop_NIC.DTO.CategoryResponse;
import Internet_shop_NIC.DTO.CategoryAddingRequest;
import Internet_shop_NIC.Service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> addNewCategory(@RequestBody CategoryAddingRequest categoryAddingRequest) {
        categoryService.addNewCategory(categoryAddingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

