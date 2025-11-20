package Internet_shop_NIC.Controller;


import Internet_shop_NIC.Entity.Category;
import Internet_shop_NIC.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        System.out.println("РАБОТАЕТ");
        this.categoryRepository = categoryRepository;
    }


    @GetMapping("/test")
    public List<Category> getPeople() {
        System.out.println("ЗАПУСК");
        System.out.println(categoryRepository.findAll());
        return categoryRepository.findAll(); // Jackson конвертирует эти объекты в JSON
    }
    @GetMapping("/")
    public List<Category> getG() {
        System.out.println("ЗАПУСК");
        System.out.println(categoryRepository.findAll());
        return categoryRepository.findAll(); // Jackson конвертирует эти объекты в JSON
    }
}
