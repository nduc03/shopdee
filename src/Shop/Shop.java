package Shop;

import Item.ItemStock;
import User.Customer;
import Utils.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Shop {
    private final int id;
    private final String name;
    private final List<ItemStock> stock;
    private double revenue;
    private final Address address;

    private static int currentId = 0;

    public Shop(String name, Address address) {
        id = ++currentId;
        this.name = name;
        this.revenue = 0.0;
        this.address = address;
        stock = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ItemStock> getStock() {
        return stock;
    }

    public int getId() {
        return id;
    }

    public double getRevenue() {
        return revenue;
    }

    public Address getAddress() {
        return address;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void withdraw(Customer customer, double amount) {
        if (customer.getOwnedShop().equals(this)) { // check customer to ensure only shop owner can withdraw
            revenue -= amount;
            customer.addBalance(amount);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shop shop)) return false;
        return id == shop.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
