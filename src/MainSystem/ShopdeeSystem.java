package MainSystem;

import Item.*;
import Order.*;
import Shop.Shop;
import User.*;
import Utils.Address;

import java.util.*;

public final class ShopdeeSystem {
    private static final double SHIPPER_FEE = 0.01;
    private static final double PROFIT = 0.09;
    private final static double SHOP_PORTION = 1.0 - SHIPPER_FEE - PROFIT;

    private static Hashtable<String, Customer> customers;
    private static Hashtable<String, Shipper> shippers;
    private static double profit = 0.0;
    private static List<Order> orders = new ArrayList<>();

    private ShopdeeSystem() {
        customers = new Hashtable<>();
        shippers = new Hashtable<>();
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
        ArrayList<ItemStock> result = new ArrayList<>();
        for (ItemStock itemStock : getAllItems()) {
            // TODO: find item
        }
        return result;
    }

    public Optional<ItemStock> getProductById(int id) {
        // TODO:
        return Optional.empty();
    }

    public List<Shop> findShops(String shopName) {
        ArrayList<Shop> result = new ArrayList<>();
        for (Shop shop : getAllShops()) {
            // TODO: find item
        }
        return result;
    }

    public List<ItemStock> getAllItems() {
        // TODO: get all item
        return new ArrayList<>();
    }

    public List<Shop> getAllShops() {
        // TODO: get all shop
        return new ArrayList<>();
    }

    public void shopAcceptOrder(Shop shop, List<Integer> orderIds) {
        for (Integer orderId : orderIds) {
            orders.stream()
                    .filter(order -> orderId == order.getId())
                    .forEach(order -> {
                        if (shop.equals(order.getShop())) order.setOrderState(OrderState.SHOP_ACCEPTED);
                    });
        }
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
        if (customer == null) return new SystemResponse(false, "Invalid customer when creating order");
        Date now = new Date();
        Cart cart = customer.releaseCart(); // get all items from cart then delete user cart

        // list all shop from all items in the cart
        HashSet<Shop> allShopFromCart = new HashSet<>();
        cart.getItems().forEach(cartItem -> allShopFromCart.add(cartItem.getItemStock().getShop()));

        // create a list of orders corresponding to the shop
        List<Order> orderList = new ArrayList<>();
        allShopFromCart.forEach(shop -> {
            OrderContent orderContent = OrderContent.filterFromCustomerCart(shop, cart);
            orderList.add(new Order(customer, now, orderContent));
        });

        double totalCustomerPayment = orderList.stream().mapToDouble(order -> order.getContent().getTotalPrice()).sum();

        if (customer.getBalance() < totalCustomerPayment)
            return new SystemResponse(false, "Not enough balance to make order");

        // reduce the quantity in the shop when create order
        cart.getItems().forEach(cartItem -> {
            ItemStock itemStock = cartItem.getItemStock();
            itemStock.setQuantity(itemStock.getQuantity() - cartItem.getQuantity());
        });

        customer.decreaseBalance(totalCustomerPayment);
        orders.addAll(orderList);
        // TODO: maybe save the order index to customer for faster order searching.
        profit += totalCustomerPayment * PROFIT;
        return new SystemResponse(true, "Order successfully created");
    }

    public boolean shipperTakesOrder(Shipper shipper, int shippingOrderId) {
//        if (shipper == null) return false;
//
//        ShippingOrder order = shippingOrder.stream()
//                .filter(o -> o.getId() == shippingOrderId)
//                .findFirst().orElse(null);
//        if (order == null) return false;
//        order.setShipper(shipper);
//        return true;
        return false;
    }

    public void shipperFinishesOrder(Shipper shipper, int shippingOrderId) {
//        if (shipper == null) return;
//        ShippingOrder order = shippingOrder.stream()
//                .filter(o -> o.getId() == shippingOrderId)
//                .findFirst().orElse(null);
//        if (order == null) return;
//        if (!order.getShipper().equals(shipper)) return;
//
//        shipper.addBalance(order.getContent().getTotalPrice() * SHIPPER_FEE);
//        order.shipped();
    }

    public void userConfirmOrder(Customer customer, Order order) {
//        if (customer == null || order == null) return;
//        if (!order.isShipped()) return;
//        shippingOrder.remove(order);
//        if (order.getCustomer().equals(customer)) {
//            // Pay money to shop
//            for (CartItem cartItem : order.getContent().getItems()) {
//                Shop shop = cartItem.getItemStock().getShop();
//                shop.setRevenue(shop.getRevenue() + order.getContent().getTotalPrice());
//            }
//        }
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
