package Internet_shop_NIC.DTO;


public class CategoryResponse {
    private long id;
    private String name;


    public CategoryResponse() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
