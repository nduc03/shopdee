package MainSystem;

import Item.Cart;
import Item.CartItem;
import Item.ItemStock;
import Order.Order;
import Order.ShippingOrder;
import Order.ShoppingOrder;
import Shop.Shop;
import User.Customer;
import User.Shipper;
import User.User;
import Utils.Address;

import java.util.*;

public final class ShopdeeSystem {
    private static final double SHIPPER_FEE = 0.01;
    private static final double PROFIT = 0.09;
    private final static double SHOP_PORTION = 1.0 - SHIPPER_FEE - PROFIT;

    private static Hashtable<String, Customer> customers;
    private static Hashtable<String, Shipper> shippers;
    private static HashSet<ShoppingOrder> shoppingOrder;
    private static HashSet<ShippingOrder> shippingOrder;
    private static double profit = 0.0;

    private ShopdeeSystem() {
        customers = new Hashtable<>();
        shippers = new Hashtable<>();
        shippingOrder = new HashSet<>();
        // TODO: Đọc file -> khởi tạo mọi thứ từ file
    }

    private static final class ShopdeeSystemHolder {
        private static final ShopdeeSystem instance = new ShopdeeSystem();
    }

    public static ShopdeeSystem getInstance() {
        return ShopdeeSystemHolder.instance;
    }

    public List<Address> findAddresses(String address) {
        return null;
    }

    public List<ItemStock> findProducts(String productName) {
        return null;
    }

    public List<Shop> findShops(String shopName) {
        return null;
    }

    public void shopAcceptOrder(List<Integer> orderIds) {
        List<ShoppingOrder> orders = new ArrayList<>();
        for (Integer orderId : orderIds) {
            orders.addAll(shoppingOrder.stream().filter(order -> orderId == order.getId()).toList());
        }
        for (ShoppingOrder order : orders) {
            shippingOrder.add(new ShippingOrder(order.getCustomer(), order.getOrderedDate(), order.getContent()));
        }
    }

    public List<Order> getCustomerOrders(Customer customer) {
        if (customer == null) return Collections.emptyList();

        List<Order> res = new ArrayList<>(shoppingOrder);
        res.addAll(shippingOrder);
        return res.stream()
                .filter(order -> order.getCustomer().equals(customer))
                .toList();
    }

    public SystemResponse createOrder(Customer customer) {
        if (customer == null) return new SystemResponse(false, "Invalid customer when creating order");
        Date now = new Date();
        Cart cart = customer.releaseCart();
        ShoppingOrder order = new ShoppingOrder(customer, now, cart);
        if (customer.getBalance() < order.getContent().getTotalPrice())
            return new SystemResponse(false, "Not enough balance to make order");
        // reduce the quantity in the shop when create order
        cart.getItems().forEach(cartItem -> {
            ItemStock itemStock = cartItem.getItemStock();
            itemStock.setQuantity(itemStock.getQuantity() - cartItem.getQuantity());
        });
        customer.decreaseBalance(order.getContent().getTotalPrice());
        shoppingOrder.add(order);
        profit += order.getContent().getTotalPrice() * PROFIT;
        return new SystemResponse(true, "Order successfully created");
    }

    public boolean shipperTakesOrder(Shipper shipper, int shippingOrderId) {
        if (shipper == null) return false;

        ShippingOrder order = shippingOrder.stream()
                .filter(o -> o.getId() == shippingOrderId)
                .findFirst().orElse(null);
        if (order == null) return false;
        order.setShipper(shipper);
        return true;
    }

    public void shipperFinishesOrder(Shipper shipper, int shippingOrderId) {
        if (shipper == null) return;
        ShippingOrder order = shippingOrder.stream()
                .filter(o -> o.getId() == shippingOrderId)
                .findFirst().orElse(null);
        if (order == null) return;
        if (!order.getShipper().equals(shipper)) return;

        shipper.addBalance(order.getContent().getTotalPrice() * SHIPPER_FEE);
        order.shipped();
    }

    public void userConfirmOrder(Customer customer, ShippingOrder order) {
        if (customer == null || order == null) return;
        if (!order.isShipped()) return;
        shippingOrder.remove(order);
        if (order.getCustomer().equals(customer)) {
            // Pay money to shop
            for (CartItem cartItem : order.getContent().getItems()) {
                Shop shop = cartItem.getItemStock().getShop();
                shop.setRevenue(shop.getRevenue() + order.getContent().getTotalPrice());
            }
        }
    }

    public boolean registerCustomer(String username, String password, String name, String phone, Address address) {
        if (username == null || password == null) return false;
        if (customers.containsKey(username)) {
            return false;
        }
        customers.put(username, new Customer(username, password, name, phone, address));
        return true;
    }

    public boolean registerShipper(String username, String password, String name, String phone, Address address) {
        if (username == null || password == null) return false;
        if (shippers.containsKey(username)) {
            return false;
        }
        shippers.put(username, new Shipper(username, password, name, phone, address));
        return true;
    }

    public Optional<User> authorizeCustomer(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        Customer customer = customers.get(username);
        if (customer == null) return Optional.empty();
        if (customer.getPassword().equals(password)) return Optional.of(customer);
        return Optional.empty();
    }

    public Optional<User> authorizeShipper(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        Shipper shipper = shippers.get(username);
        if (shipper == null) return Optional.empty();
        if (shipper.getPassword().equals(password)) return Optional.of(shipper);
        return Optional.empty();
    }
}
