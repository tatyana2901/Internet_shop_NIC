package Internet_shop_NIC.DTO;

import java.util.ArrayList;
import java.util.List;

public class CategoryAddingRequest {
    private String name;
    private List<Long> parentsId = new ArrayList<>();
    private List<Long> childrenId = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getParentsId() {
        return parentsId;
    }

    public void setParentsId(List<Long> parentsId) {
        this.parentsId = parentsId;
    }

    public List<Long> getChildrenId() {
        return childrenId;
    }

    public void setChildrenId(List<Long> childrenId) {
        this.childrenId = childrenId;
    }
}
