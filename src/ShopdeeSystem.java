import java.util.*;

public final class ShopdeeSystem {
    private static Hashtable<String, Customer> customers;
    private static Hashtable<String, Shipper> shippers;
    private static HashSet<Payment> holdingPayments;
    private static HashSet<Order> shippingOrder;

    private ShopdeeSystem() {
        // TODO: Đọc file -> khởi tạo mọi thứ từ file
        holdingPayments = new HashSet<>();
        shippingOrder = new HashSet<>();
    }

    private static final class ShopdeeSystemHolder {
        private static final ShopdeeSystem instance = new ShopdeeSystem();
    }

    public static ShopdeeSystem getInstance() {
        return ShopdeeSystemHolder.instance;
    }

    /**
     * Should be invoked every fixed amount of time.
     * Check the holdingPayments and shipping status, remove or send if appropriate
     */
    public void onPeriodicCheck() {
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
        cart.forEach(itemStock -> holdingPayments.add(new Payment(
                itemStock.getPrice(),
                now,
                customer,
                itemStock.getShop().getShopOwner(),
                order
        )));
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
        order.shipped(shipper);
    }

    public void userConfirmOrder(Customer customer, Order order) {
        if (customer == null || order == null) return;
        shippingOrder.remove(order);
        holdingPayments.stream()
                .filter(payment -> payment.getOrder().equals(order))
                .forEach(payment -> {
                    payment.getReceiver().addBilledPayment(payment);
                    holdingPayments.remove(payment);
                });
    }

    public boolean registerCustomer(String username, String password) {
        if (customers.containsKey(username)) {
            return false;
        }
        customers.put(username, new Customer(/*TODO: Placeholder*/));
        return true;
    }

    public Optional<User> authorize(String username, String password) {
        return null;
    }

    public Optional<ItemStock> searchItem(String itemName) {
        return null;
    }
}
