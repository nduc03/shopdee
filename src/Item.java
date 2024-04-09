import java.util.List;
import java.util.Objects;

public class Item {
    private final int id;
    private String name;
    private double price;
    private String description;
    private List<ItemCategory> categories;

    private static int currentId = 1;

    public Item(String name, double price, String description, List<ItemCategory> category) {
        this.id = currentId++;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categories = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ItemCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ItemCategory> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
