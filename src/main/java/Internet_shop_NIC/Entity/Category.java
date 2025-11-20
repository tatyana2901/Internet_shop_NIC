package Internet_shop_NIC.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long category_id;
    @Column
    private String name;
    @Column
    private LocalDateTime created_at;

    @ManyToMany
    @JoinTable(
            name = "category_parent",
            joinColumns = @JoinColumn(name = "child_id", referencedColumnName = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id", referencedColumnName = "category_id"))
    private List<Category> parents;

    @ManyToMany(mappedBy = "parents")
    private List<Category> children;

    @ManyToMany(mappedBy = "categories")
    private List<Product> products;

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getParents() {
        return parents;
    }

    public void setParents(List<Category> parents) {
        this.parents = parents;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


}
