package Internet_shop_NIC.Controller;


import Internet_shop_NIC.DTO.CategoryDTO;
import Internet_shop_NIC.Entity.Category;
import Internet_shop_NIC.Repository.CategoryRepository;
import Internet_shop_NIC.Repository.UserRepository;
import Internet_shop_NIC.Service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
 //   private UserRepository r;
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService/*, UserRepository r*/) {
        this.categoryService = categoryService;
       // this.r = r;
    }


    @GetMapping()
    public List<CategoryDTO> getRootCategories() {
       // System.out.println(r.findAll());
        return categoryService.getRootCategories();
    }


    @GetMapping("/{id}")
    public List<CategoryDTO> getSubCategories(@PathVariable("id") Long id) {
        return categoryService.getSubCategories(id);
    }
}

