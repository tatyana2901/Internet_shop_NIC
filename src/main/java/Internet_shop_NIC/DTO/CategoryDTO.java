package Internet_shop_NIC.DTO;

import Internet_shop_NIC.Entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {
    private long id;
    private String name;
    private List<CategoryDTO> children;

    public CategoryDTO() {
    }

    public CategoryDTO(long id, String name) {
        this.id = id;
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChildren(List<CategoryDTO> children) {
        this.children = children;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CategoryDTO> getChildren() {
        return children;
    }


}
