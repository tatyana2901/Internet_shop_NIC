package Internet_shop_NIC.Controller;


import Internet_shop_NIC.DTO.CategoryDTO;
import Internet_shop_NIC.Entity.Category;
import Internet_shop_NIC.Repository.CategoryRepository;
import Internet_shop_NIC.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {


    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired



    @GetMapping("/")
    public List<CategoryDTO> getG() {
        System.out.println("ЗАПУСК");

        return categoryService.getSubCategories(); // Jackson конвертирует эти объекты в JSON
    }
}
