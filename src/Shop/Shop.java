package Shop;

import Item.ItemStock;
import Order.Order;
import Order.OrderState;
import User.Customer;
import Utils.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Shop {
    private final int id;
    private String name;
    private final List<ItemStock> stock;
    private double revenue;
    private Address address;

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

    public void setName(String name) {
        this.name = name;
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

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void increaseRevenue(double amount) {
        revenue += amount;
    }

    public void withdraw(Customer customer, double amount) {
        if (customer.getOwnedShop().equals(this)) { // check customer to ensure only shop owner can withdraw
            revenue -= amount;
            customer.addBalance(amount);
        }
    }

    public void addItem(ItemStock itemStock) {
        stock.add(itemStock);
    }

    public boolean removeItem(int itemId) {
        for (int i = 0; i < stock.size(); i++) {
            if (stock.get(i).getItem().getId() == itemId) {
                stock.remove(i);
                return true;
            }
        }
        return false;
    }

//    public Order makeOrder(Customer customer, OrderContent orderContent) {
//        if (customer == null || orderContent == null) throw new IllegalArgumentException("Null argument");
//        if (!orderContent.getShop().equals(this)) throw new IllegalArgumentException("Invalid shop");
//        orderContent.getItems().forEach(cartItem -> {
//            ItemStock itemStock = cartItem.getItemStock();
//            itemStock.setQuantity(itemStock.getQuantity() - cartItem.getQuantity());
//        });
//        return new Order(customer, new Date(), orderContent);
//    }

    public void acceptOrder(Order order) {
        if (order != null && order.getShop() == this && order.getOrderState() == OrderState.CREATED) {
            order.setOrderState(OrderState.SHOP_ACCEPTED);
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
