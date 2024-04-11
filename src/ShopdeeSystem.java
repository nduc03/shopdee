import java.util.*;

public final class ShopdeeSystem {
    private static final double SHIPPER_FEE = 0.01;
    private static final double PROFIT = 0.09;
    private final static double SHOP_PORTION = 1.0 - SHIPPER_FEE - PROFIT;

    private static Hashtable<String, Customer> customers;
    private static Hashtable<String, Shipper> shippers;
    private static HashSet<Order> shippingOrder;
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

    public List<Order> getCustomerOrder(Customer customer) {
        if (customer == null) return Collections.emptyList();

        return shippingOrder.stream()
                .filter(order -> order.getCustomer().equals(customer))
                .toList();
    }

    public boolean createOrder(Customer customer) {
        if (customer == null) return false;
        Date now = new Date();
        List<ItemStock> cart = customer.releaseCart();
        Order order = new Order(customer, now, cart);
        shippingOrder.add(order);
        profit += order.getTotalPrice() * PROFIT;
        return true;
    }

    public boolean shipperTakesOrder(Shipper shipper, Order order) {
        if (shipper == null || order == null) return false;
        if (shipper.getOrder() == null || order.getShipper() == null) return false;

        shipper.setOrder(order);
        order.setShipper(shipper);
        return true;
    }

    public void shipperFinishesOrder(Shipper shipper) {
        if (shipper == null) return;
        Order order = shipper.getOrder();
        if (order == null) return;

        shipper.setOrder(null);
        shipper.addBalance(order.getTotalPrice() * SHIPPER_FEE);
        order.shipped();
    }

    public void userConfirmOrder(Customer customer, Order order) {
        if (customer == null || order == null) return;
        if (!order.getShipped()) return;
        shippingOrder.remove(order);
        if (order.getCustomer().equals(customer)) {
            for (ItemStock itemStock : order.getContent()) {
                itemStock.getShop().getShopOwner().addBalance(itemStock.getPrice() * SHOP_PORTION);
            }
        }
    }

    public boolean registerCustomer(String username, String password) {
        if (username == null || password == null) return false;
        if (customers.containsKey(username)) {
            return false;
        }
        customers.put(username, new Customer(/*TODO: Placeholder*/));
        return true;
    }

    public boolean registerShipper(String username, String password) {
        if (username == null || password == null) return false;
        if (shippers.containsKey(username)) {
            return false;
        }
        shippers.put(username, new Shipper(/*TODO: Placeholder*/));
        return true;
    }

    public Optional<User> authorizeCustomer(String username, String password) {
        return null;
    }

    public Optional<User> authorizeShipper(String username, String password) {
        return null;
    }
}
