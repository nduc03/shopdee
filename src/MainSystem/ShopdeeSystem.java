package MainSystem;

import Item.Cart;
import Item.Item;
import Item.ItemStock;
import Order.Order;
import Order.OrderContent;
import Order.OrderState;
import Shop.Shop;
import User.*;
import Utils.Address;

import java.util.*;

public final class ShopdeeSystem {
    private static final double SHIPPER_FEE = 5000.0;
    private static final double PROFIT = 0.09;
    private static final double SHOP_PORTION = 1.0 - PROFIT;
    private static Hashtable<String, User> users;
    private static double profit = 0.0;
    private static List<Order> orders;

//    private static Hashtable<String, Shipper> shippers;

    private ShopdeeSystem() {
        users = new Hashtable<>();
        orders = new ArrayList<>();
//        shippers = new Hashtable<>();
        // TODO: Đọc file -> khởi tạo mọi thứ từ file
    }

    private static final class SingletonHolder {
        private static final ShopdeeSystem instance = new ShopdeeSystem();
    }

    public static ShopdeeSystem getInstance() {
        return SingletonHolder.instance;
    }

    public List<ItemStock> findProducts(String productName) {
        ArrayList<ItemStock> result = new ArrayList<>();
        for (ItemStock itemStock : getAllItemStocks()) {
            if (itemStock.getItem().getName().contains(productName)) {
                result.add(itemStock);
            }
        }
        return result;
    }

    public Optional<ItemStock> getProductById(int id) {
        for (ItemStock itemStock : getAllItemStocks()) {
            if (itemStock.getId() == id) return Optional.of(itemStock);
        }
        return Optional.empty();
    }

    public List<Shop> findShops(String shopName) {
        ArrayList<Shop> result = new ArrayList<>();
        for (Shop shop : getAllShops()) {
            if (shop.getName().contains(shopName)) {
                result.add(shop);
            }
        }
        return result;
    }

    public List<ItemStock> getAllItemStocks() {
        List<ItemStock> res = new ArrayList<>();
        for (Shop shop : getAllShops()) {
            res.addAll(shop.getStock());
        }
        return res;
    }

    public List<Shop> getAllShops() {
        List<Shop> res = new ArrayList<>();
        for (User user : users.values()) {
            if (user.getRole() != UserRole.Customer) continue;
            Customer customer = (Customer) user;
            Shop shop = customer.getOwnedShop();
            if (shop != null)
                res.add(shop);
        }
        return res;
    }

    public void shopAcceptOrder(Shop shop, List<Integer> orderIds) {
        for (Integer orderId : orderIds) {
            orders.stream()
                    .filter(order -> orderId == order.getId())
                    .forEach(shop::acceptOrder);
        }
    }

    public List<Order> getOrdersReadyToShip(Shipper shipper) {
        return orders.stream()
                .filter(order ->
                        order.getOrderState().equals(OrderState.SHOP_ACCEPTED) || order.getOrderState().equals(OrderState.AT_WAREHOUSE)
                )
                .filter(order -> order.getLocation().city() == shipper.getAddress().city())
                .toList();
    }

    public List<Order> getShippingOrders(Shipper shipper) {
        return orders.stream()
                .filter(order -> order.getOrderState() == OrderState.SHIPPING)
                .filter(order -> order.getShipper() == shipper)
                .toList();
    }

    public List<Order> getOrdersByShop(Shop shop) {
        return orders.stream().filter(order -> order.getShop().equals(shop)).toList();
    }

    public List<Order> getCustomerOrders(Customer customer) {
        if (customer == null) return Collections.emptyList();

        return orders.stream()
                .filter(order -> order.getCustomer().equals(customer))
                .toList();
    }

    public SystemResponse createOrder(Customer customer) {
        if (customer == null) return new SystemResponse(false, "Invalid customer when creating order.");
//        Date now = new Date();
        Cart cart;
        try {
            cart = customer.buy(); // get all items from cart then delete user cart
        } catch (Error e) {
            return new SystemResponse(false, "Not enough balance to make order.");
        }

        if (cart.isEmpty()) return new SystemResponse(false, "Empty cart.");

        // list all shop from all items in the cart
        HashSet<Shop> allShopFromCart = new HashSet<>();
        cart.getItems().forEach(cartItem -> allShopFromCart.add(cartItem.getItemStock().getShop()));

        // create a list of orders corresponding to the shop
//        List<Order> orderList = new ArrayList<>();
        allShopFromCart.forEach(shop -> {
            OrderContent orderContent = OrderContent.filterFromCustomerCart(shop, cart);
            orders.add(new Order(customer, new Date(), orderContent));
        });

        // reduce the quantity in the shop when create order
        cart.getItems().forEach(cartItem -> {
            ItemStock itemStock = cartItem.getItemStock();
            itemStock.setQuantity(itemStock.getQuantity() - cartItem.getQuantity());
        });

//        orders.addAll(orderList);
        profit += cart.getTotalPrice() * PROFIT;
        return new SystemResponse(true, "Order successfully created.");
    }

    public boolean shipperTakesOrder(Shipper shipper, int orderId) {
        if (shipper == null) return false;

        Order order = orders.stream()
                .filter(o -> o.getId() == orderId)
                .findFirst().orElse(null);
        if (order != null && order.getLocation().city() == shipper.getAddress().city()) {
            shipper.takesOrder(order);
            return true;
        }
        return false;
    }

    public boolean shipperFinishesOrder(Shipper shipper, int orderId) {
        if (shipper == null) return false;
        Order order = orders.stream()
                .filter(o -> o.getId() == orderId)
                .findFirst().orElse(null);
        if (order == null) return false;

        double payAmount = SHIPPER_FEE;
        shipper.finishesOrder(order, payAmount);
        profit -= payAmount;
        return true;
    }

    public void userConfirmOrder(Customer customer, Order order) {
        if (customer == null || order == null) return;
        if (customer.confirmOrder(order)) {
            // If the confirmation succeed, pay money to shop
            Shop shop = order.getShop();
            shop.increaseRevenue(order.getContent().getTotalPrice() * SHOP_PORTION);
        } else {
            // TODO: process the invalid order that cannot confirm
        }
    }

    public void userConfirmOrder(Customer customer, int orderId) {
        if (customer == null) return;

        Order order = orders.stream()
                .filter(o -> o.getId() == orderId)
                .findFirst().orElse(null);

        userConfirmOrder(customer, order);
    }

    public boolean registerCustomer(String username, String password, String name, String phone, Address address) {
        if (username == null || password == null) return false;
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new Customer(username, password, name, phone, address));
        return true;
    }

    public boolean registerShipper(String username, String password, String name, String phone, Address address) {
        if (username == null || password == null) return false;
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new Shipper(username, password, name, phone, address));
        return true;
    }

    public Optional<User> authorizeUser(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        User customer = users.get(username);
        if (customer == null) return Optional.empty();
        if (customer.getPassword().equals(password)) return Optional.of(customer);
        return Optional.empty();
    }

//    public Optional<User> authorizeShipper(String username, String password) {
//        if (username == null || password == null) return Optional.empty();
//        Shipper shipper = shippers.get(username);
//        if (shipper == null) return Optional.empty();
//        if (shipper.getPassword().equals(password)) return Optional.of(shipper);
//        return Optional.empty();
//    }

    public void shopAddItem(Shop shop, String itemName, double price, int quantity) {
        shop.addItem(new ItemStock(new Item(itemName), price, quantity, shop));
    }

    public boolean existsUser(String username) {
        return users.containsKey(username);
    }

//    public boolean existsShipper(String username) {
//        return shippers.containsKey(username);
//    }
}
