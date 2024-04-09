import java.util.Hashtable;
import java.util.List;

public class Shop {
    private String name;
    private List<ItemStock> stock;
    private double profit;
    private Address address;

    public Shop(String name, Address address) {

    }

    public String getName() {
        return name;
    }

    public List<ItemStock> getStock() {
        return stock;
    }

    public double getProfit() {
        return profit;
    }

    public Address getAddress() {
        return address;
    }
}
