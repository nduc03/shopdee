package MainSystem;

import Item.Cart;
import Item.CartItem;
import Item.Item;
import Item.ItemStock;
import Order.Order;
import Order.OrderContent;
import Order.OrderState;
import Shop.Shop;
import User.Customer;
import User.Shipper;
import User.User;
import User.UserRole;
import Utils.Address;
import Utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class SystemManager {
    private static final double SHIPPER_FEE = 5000.0;
    private static final double PROFIT = 0.09;
    private static final double SHOP_PORTION = 1.0 - PROFIT;
    private static final Path dataPath = Paths.get("data.json");
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Hashtable<String, User> users;
    private double profit;
    private List<Order> orders;

    private SystemManager() {
        if (Files.exists(dataPath)) {
            try {
                String content = Files.readAllLines(dataPath).get(0);
                SystemDataHolder data = mapper.readValue(content, SystemDataHolder.class);
                users = data.users();
                profit = data.profit();
                orders = data.orders();
            } catch (Exception e) {
                System.out.println(e.getMessage());
//                e.printStackTrace();
                if (!Utils.promptInput("System message: Error reading data! Create new data? (y/n) ").equalsIgnoreCase("y")) {
                    System.exit(1);
                }
                users = new Hashtable<>();
                profit = 0.0;
                orders = new ArrayList<>();
            }
        } else {
            System.out.println("System message: Data file not found. Creating new data.");
            users = new Hashtable<>();
            profit = 0.0;
            orders = new ArrayList<>();
        }
    }

    private static final class SingletonHolder {
        private static final SystemManager instance = new SystemManager();
    }

    public static SystemManager getInstance() {
        return SingletonHolder.instance;
    }

    public void saveData() {
        try {
            SystemDataHolder data = new SystemDataHolder(users, profit, orders);
            String content = mapper.writeValueAsString(data);
            Files.writeString(dataPath, content);
        } catch (IOException e) {
            System.out.println("System message: Error opening file!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("System message: Error saving data!");
        }
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

    public void shopAcceptOrders(Shop shop, List<Integer> orderIds) {
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

//    public List<Order> getOrdersByShop(Shop shop) {
//        return orders.stream().filter(order -> order.getShop().equals(shop)).toList();
//    }

    public List<Order> getShopOrdersReadyToTake(Shop shop) {
        return orders.stream()
                .filter(order -> order.getShop().equals(shop) && order.getOrderState().equals(OrderState.CREATED))
                .toList();
    }

    public List<Order> getCustomerOrders(Customer customer) {
        if (customer == null) return Collections.emptyList();

        return orders.stream()
                .filter(order -> order.getCustomer().equals(customer))
                .toList();
    }

    public SystemResponse createOrder(Customer customer) {
        if (customer == null) return new SystemResponse(false, "Invalid customer when creating order.");

        Cart cart;
        try {
            cart = customer.buy(); // get all items from cart then delete user cart
        } catch (Error e) {
            return new SystemResponse(false, "Not enough balance to make order.");
        }

        if (cart.isEmpty()) return new SystemResponse(false, "Empty cart.");

        double customerPaidAmount = cart.getTotalPrice();

        // list all shop from all items in the cart
        HashSet<Shop> allShopFromCart = new HashSet<>();
        cart.getItems().forEach(cartItem -> allShopFromCart.add(cartItem.getItemStock().getShop()));

        // create a temp list of orders corresponding to the shop then later add to the order list
        List<Order> tempOrders = new ArrayList<>();
        allShopFromCart.forEach(shop -> {
            OrderContent orderContent = OrderContent.filterFromCustomerCart(shop, cart);
            tempOrders.add(new Order(customer, new Date(), orderContent));
        });

        // reduce the quantity in the shop when create order
        for (CartItem cartItem : cart.getItems()) {
            ItemStock itemStock = cartItem.getItemStock();
            if (!itemStock.buy(cartItem.getQuantity())) {
                customer.refund(customerPaidAmount);
                return new SystemResponse(false, "Fail to buy because: Not enough quantity for item " + itemStock.getItem().getName());
            }
        }

        // only add tempOrders to the real order list when all validation are successful
        orders.addAll(tempOrders);
        profit += customerPaidAmount * PROFIT;
        return new SystemResponse(true, "Order successfully created.");
    }

    public boolean shipperTakesOrder(Shipper shipper, int orderId) {
        if (shipper == null) return false;

        Order order = orders.stream()
                .filter(o -> o.getId() == orderId)
                .findFirst().orElse(null);
        if (order != null && order.getLocation().city() == shipper.getAddress().city()) {
            try {
                shipper.takesOrder(order);
            } catch (IllegalArgumentException e) {
                return false;
            }
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
        try {
            shipper.finishesOrder(order, payAmount);
        } catch (IllegalArgumentException e) {
            return false;
        }
        profit -= payAmount;
        return true;
    }

    public boolean userConfirmOrder(Customer customer, Order order) {
        if (customer == null || order == null) return false;
        if (customer.confirmOrder(order)) {
            // If the confirmation succeed, pay money to shop
            Shop shop = order.getShop();
            shop.increaseRevenue(order.getTotalPrice() * SHOP_PORTION);
            return true;
        }
        // when confirmation failed (most likely happen due to confirming while not being delivered yet)
        // ...
        return false;
    }

    public boolean userConfirmOrder(Customer customer, int orderId) {
        if (customer == null) return false;

        Order order = orders.stream()
                .filter(o -> o.getId() == orderId)
                .findFirst().orElse(null);

        return userConfirmOrder(customer, order);
    }

    public boolean registerCustomer(String username, String password, String name, String phone, Address address) {
        if (username == null || password == null) return false;
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new Customer(username, password, name, phone, address));
        saveData();
        return true;
    }

    public boolean registerShipper(String username, String password, String name, String phone, Address address) {
        if (username == null || password == null) return false;
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new Shipper(username, password, name, phone, address));
        saveData();
        return true;
    }

    public Optional<User> authorizeUser(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        User customer = users.get(username);
        if (customer == null) return Optional.empty();
        if (customer.getPassword().equals(password)) return Optional.of(customer);
        return Optional.empty();
    }

    public void shopAddItem(Shop shop, String itemName, double price, int quantity) {
        shop.addItem(new ItemStock(new Item(itemName), price, quantity, shop));
    }

    public boolean existsUser(String username) {
        return users.containsKey(username);
    }
}
