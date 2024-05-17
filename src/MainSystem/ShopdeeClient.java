package MainSystem;

import Item.CartItem;
import Item.ItemStock;
import Order.Order;
import Shop.Shop;
import User.Customer;
import User.Shipper;
import User.User;
import User.UserRole;
import Utils.Address;
import Utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ShopdeeClient {

    private static User currentUser = null;
    private static final ShopdeeSystem system = ShopdeeSystem.getInstance();

    private ShopdeeClient() {
    }

    public static void displayMenu() {
        while (true) {
            String username;
            String password;
            System.out.println("====== LOGIN MENU ======");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println("========================");

            String choice = Utils.promptInput("Enter your choice: ");
            switch (choice) {
                case "1":
                    System.out.println("------------Login menu--------------");
                    username = Utils.promptInput("Enter your username: ");
                    password = Utils.promptInput("Enter password: ");
                    login(username, password);
                    if (currentUser == null) {
                        System.out.println("Cannot find account or wrong password.");
                    } else if (currentUser.getRole().equals(UserRole.Customer)) {
                        displayCustomerMenu();
                    } else {
                        displayShipperMenu();
                    }
                    break;
                case "2":
//                    System.out.println("-------Register menu----------");
//                    System.out.println("Choose your role: ");
//                    System.out.println("1. Customer");
//                    System.out.println("2. Shipper");
//                    int roleChoice = Utils.promptIntInput("Option: ").orElse(-1);
//                    if (roleChoice != 1 && roleChoice != 2) {
//                        System.out.println("Invalid choice. Stop register.");
//                        continue;
//                    }
//                    while (true) {
//                        username = Utils.promptInput("Enter your username: ");
//                        if (system.existsUser(username)) {
//                            System.out.println("This username existed. Try again!");
//                        } else break;
//                    }
//                    password = Utils.promptInput("Enter your password: ");
//                    String name = Utils.promptInput("Enter your name: ");
//                    String phone = Utils.promptInput("Enter your phone number: ");
//                    Address address = askForUpdateAddress().orElse(null);
//                    if (address == null) {
//                        System.out.println("Invalid input. Stop register.");
//                        continue;
//                    }
//                    if (roleChoice == 1) {
//                        if (system.registerCustomer(username, password, name, phone, address)) {
//                            System.out.println("Register success!");
//                        } else System.out.println("Register failed!");
//                    } else {
//                        if (system.registerShipper(username, password, name, phone, address)) {
//                            System.out.println("Register success!");
//                        } else System.out.println("Register failed!");
//                    }
                    displayRegisterMenu();
                    break;
                case "3":
                    System.out.println("Exit program...");
                    System.exit(0);
                default:
                    System.out.println("INVALID");
            }
        }
    }

    // Current user will be set to null if account is not found
    private static void login(String username, String password) {
        currentUser = system.authorizeUser(username, password).orElse(null);
    }

    private static void displayRegisterMenu() {
        String username;
        String password;
        System.out.println("-------Register menu----------");
        System.out.println("Choose your role: ");
        System.out.println("1. Customer");
        System.out.println("2. Shipper");
        int roleChoice = Utils.promptIntInput("Option: ").orElse(-1);
        if (roleChoice != 1 && roleChoice != 2) {
            System.out.println("Invalid choice. Stop register.");
            return;
        }
        while (true) {
            username = Utils.promptInput("Enter your username: ");
            if (system.existsUser(username)) {
                System.out.println("This username existed. Try again!");
            } else break;
        }
        password = Utils.promptInput("Enter your password: ");
        String name = Utils.promptInput("Enter your name: ");
        String phone = Utils.promptInput("Enter your phone number: ");
        Address address = askForUpdateAddress().orElse(null);
        if (address == null) {
            System.out.println("Invalid input. Stop register.");
            return;
        }
        if (roleChoice == 1) {
            if (system.registerCustomer(username, password, name, phone, address)) {
                System.out.println("Register success!");
            } else System.out.println("Register failed!");
        } else {
            if (system.registerShipper(username, password, name, phone, address)) {
                System.out.println("Register success!");
            } else System.out.println("Register failed!");
        }
    }

    private static void displayCustomerMenu() {
        // check if current user is Customer, if true c is currentUser as Customer type
        if (!(currentUser instanceof Customer c)) {
            System.out.println("Error! current user is not Customer");
            return;
        }
        while (true) {
            System.out.println("========= Customer menu =========");
            System.out.println("Hello " + currentUser.getName() + "!");
            System.out.println("1. View/Update Profile");
            System.out.println("2. Buy/view your cart");
            System.out.println("3. View orders");
            System.out.println("4. Your shop");
            System.out.println("5. Deposit");
            System.out.println("6. Withdraw");
            System.out.println("7. Log out");
            System.out.println("================================");

            String choice = Utils.promptInput("Enter your choice: ");
            switch (choice) {
                case "1":
                    updateProfile();
                    break;
                case "2":
                    viewCart();
                    System.out.println("========= Buy/Cart menu =========");
                    System.out.println("1. Add item");
                    System.out.println("2. Remove item");
                    System.out.println("3. Pay");
                    System.out.println("4. Exit");

                    String select = Utils.promptInput("Enter your choice: ");
                    switch (select) {
                        case "1":
                            addItemToCart();
                            break;
                        case "2":
                            removeItemFromCart();
                            break;
                        case "3":
                            pay();
                            break;
                        default:
                            if (!select.equals("4")) System.out.println("INVALID");
                    }
                    break;
                case "3":
                    System.out.println("Your orders:");
                    List<Order> customerOrders = system.getCustomerOrders(c);
                    if (customerOrders.isEmpty()) {
                        System.out.println("Your don't have any order.");
                        continue;
                    }
                    for (Order order : customerOrders) {
                        System.out.println(order.toString());
                    }
                    String input =
                            Utils.promptInput("Enter order id to confirm or type 'all' to confirm all or 'exit' to exit: ");
                    if (input.equalsIgnoreCase("exit")) continue;
                    if (input.equalsIgnoreCase("all")) {
                        for (Order order : customerOrders) {
                            system.userConfirmOrder(c, order);
                        }
                    } else {
                        try {
                            int cartId = Integer.parseInt(input);
                            system.userConfirmOrder(c, cartId);
                        } catch (NumberFormatException e) {
                            System.out.println("INVALID");
                        }
                    }
                    break;
                case "4":
                    if (c.getOwnedShop() == null) {
                        createShop();
                    } else {
                        displayShopMenu();
                    }
                    break;
                case "5":
                    double amount = deposit();
                    if (amount >= 0.0) {
                        c.addBalance(amount);
                        System.out.println("Deposit successfully!");
                    } else {
                        System.out.println("Deposit failed!");
                    }
                    break;
                case "6":
                    withdraw();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static void displayShipperMenu() {
        if (!(currentUser instanceof Shipper s)) {
            System.out.println("Error! current user is not Customer");
            return;
        }
        while (true) {
            System.out.println("========= Shipper menu =========");
            System.out.println("Hello " + currentUser.getName() + "!");
            System.out.println("Your balance: " + s.getBalance());
            System.out.println("1. Receive order");
            System.out.println("2. Confirm order");
            System.out.println("3. View task");
            System.out.println("4. View/Update Profile");
            System.out.println("5. Withdraw");
            System.out.println("6. Log out");

            int choice = Utils.promptIntInput("Enter your choice: ").orElse(-1);

            switch (choice) {
                case 1:
                    takesOrder();
                    break;
                case 2:
                    shipperFinishesOrder();
                    break;
                case 3:
                    viewShipperTask();
                    break;
                case 4:
                    updateProfile();
                    break;
                case 5:
                    withdraw();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("INVALID");
            }
        }
    }

    private static void takesOrder() {
        Shipper s = (Shipper) currentUser;
        System.out.println("----------Take order menu---------");

        List<Order> orders = system.getOrdersReadyToShip(s);

        if (orders.isEmpty()) {
            System.out.println("There is no order nearby to ship");
            return;
        }

        for (Order order : orders) {
            System.out.println(order.toString());
        }
        do {
            int id = Utils.promptIntInput("Enter order id you want to take: ").orElse(-1);
            if (!system.shipperTakesOrder(s, id)) System.out.println("Invalid");
        } while (Utils.promptInput("Continue? (y/n) ").equalsIgnoreCase("y"));
    }

    private static void shipperFinishesOrder() {
        Shipper s = (Shipper) currentUser;
        System.out.println("----------Finish order menu-----------");

        List<Order> orders = system.getShippingOrders(s);

        if (orders.isEmpty()) {
            System.out.println("You did not take any order.");
            return;
        }
        for (Order order : orders) {
            System.out.println(order.toString());
        }
        do {
            int id = Utils.promptIntInput("Enter order id you want to finish: ").orElse(-1);
            if (!system.shipperFinishesOrder(s, id)) System.out.println("Invalid");
        } while (Utils.promptInput("Continue? (y/n) ").equalsIgnoreCase("y"));
    }

    private static void viewShipperTask() {
        Shipper s = (Shipper) currentUser;
        System.out.println("------Shipping order--------");
        for (Order order : system.getShippingOrders(s)) {
            System.out.println(order.toString());
        }
    }

    private static void createShop() {
        Customer c = (Customer) currentUser;
        System.out.println("You didn't have a shop before.");
        if (!Utils.promptInput("Do you want create shop? (y/[n]) ").equalsIgnoreCase("y")) {
            return;
        }
        String shopName = Utils.promptInput("Enter your shop name: ");
        System.out.println("Enter your shop address: ");
        Address address = askForUpdateAddress().orElse(null);
        if (address != null) {
            c.setOwnedShop(new Shop(shopName, address));
            System.out.println("Create shop successfully");
        } else {
            System.out.println("Failed to create shop");
        }
    }

    private static void displayShopMenu() {
        Customer c = (Customer) currentUser;
        while (true) {
            System.out.println("========= Shop menu =========");
            System.out.println("Your shop's revenue: " + c.getOwnedShop().getRevenue());
            System.out.println("1. Change Shop information");
            System.out.println("2. Add/Delete item");
            System.out.println("3. Take Orders");
            System.out.println("4. Transfer money to account");
            System.out.println("Other key to exit.");

            String choice = Utils.promptInput("Enter your choice: ");

            switch (choice) {
                case "1":
                    changeShopInfo(c.getOwnedShop());
                    break;
                case "2":
                    System.out.println("1. Add item");
                    System.out.println("2. Delete item");
                    int select = Utils.promptIntInput("Enter your choice: ").orElse(-1);
                    switch (select) {
                        case 1:
                            addItemToShop(c.getOwnedShop());
                            break;
                        case 2:
                            deleteItemFromShop(c.getOwnedShop());
                            break;
                        default:
                            System.out.println("INVALID");
                    }
                    break;
                case "3":
                    acceptOrderByShop(c.getOwnedShop());
                    break;
                case "4":
                    c.addBalance(c.getOwnedShop().getRevenue());
                    c.getOwnedShop().setRevenue(0);
                    System.out.println("Transfer successfully!");
                    break;
                default:
                    System.out.println("Exit shop menu.");
                    return;
            }
        }
    }

    private static void addItemToShop(Shop shop) {
        String itemName = Utils.promptInput("Enter item name: ");
        double price = Utils.promptIntInput("Enter item price: ").orElse(-1);
        if (price <= 0.0) System.out.println("Invalid price. Stop adding.");
        int quantity = Utils.promptIntInput("Enter item quantity: ").orElse(-1);
        if (quantity <= 0) System.out.println("Invalid quantity. Stop adding.");
        system.shopAddItem(shop, itemName, price, quantity);
    }

    private static void deleteItemFromShop(Shop shop) {
        System.out.println("List of shop stock: ");
        for (ItemStock itemStock : shop.getStock()) {
            System.out.println(itemStock.toString());
        }
        int itemId = Utils.promptIntInput("Enter item id to remove: ").orElse(-1);
        if (shop.removeItem(itemId))
            System.out.println("Successfully removed item.");
        else
            System.out.println("Failed to remove item.");
    }

    private static void acceptOrderByShop(Shop shop) {
        List<Order> ordersByThisShop = system.getOrdersByShop(shop);
        if (ordersByThisShop.isEmpty()) {
            System.out.println("Your shop doesn't have any order.");
            return;
        }
        List<Integer> orderIdsByThisShop = ordersByThisShop.stream().mapToInt(order -> order.getId()).boxed().toList();
        for (Order order : system.getOrdersByShop(shop)) {
            System.out.println("List of order by this shop:");
            System.out.println(order.toString());
        }

        List<Integer> orderIds = new ArrayList<>();
        do {
            int id = Utils.promptIntInput("Enter order id you prepared and want to accept: ").orElse(-1);

            if (id <= 0 || !orderIdsByThisShop.contains(id)) System.out.println("Invalid id.");
            else orderIds.add(id);
        } while (Utils.promptInput("Continue? (y/n): ").equalsIgnoreCase("y"));

        system.shopAcceptOrder(shop, orderIds);
    }

    private static void changeShopInfo(Shop shop) {
        System.out.println("1. Change shop name");
        System.out.println("2. Change shop address");
        String choice = Utils.promptInput("Enter option: ");

        switch (choice) {
            case "1":
                String shopName = Utils.promptInput("Enter new shop name: ");
                if (!shopName.isEmpty()) shop.setName(shopName);
                break;
            case "2":
                System.out.println("Enter new shop address: ");
                Address address = askForUpdateAddress().orElse(null);
                if (address != null)
                    shop.setAddress(address);
                else
                    System.out.println("Failed to update address.");
                break;
            default:
                System.out.println("INVALID");
        }
    }

    private static void pay() {
        Customer c = (Customer) currentUser;
        double totalPrice = c.getCart().getTotalPrice();
        System.out.println("Total price: " + totalPrice);

        if (!Utils.promptInput("Are you sure you want to pay? (y/[n]) ").equalsIgnoreCase("y")) {
            return;
        }

        if (c.getBalance() < totalPrice) {
            System.out.println("Your balance is not enough");
            return;
        }

        SystemResponse orderState = system.createOrder(c);

        if (orderState.isSuccess()) {
            System.out.println("Order successfully");
        } else {
            System.out.println("Failed to create order due to: " + orderState.getMessage());
        }
    }

    private static double deposit() {
        System.out.println("STK: 00888888888");
        System.out.println("Name: CTCP SHOPDEE");
        System.out.println("Bank: VCB");
        System.out.println("Transfer message: username");
        System.out.println("===============================");
        return Utils.promptDoubleInput("Enter amount you want to deposit: ").orElse(-1.0);
    }

    private static void withdraw() {
        double amount = Utils.promptDoubleInput("Enter amount you want to withdraw: ").orElse(-1.0);
        if (amount > currentUser.getBalance() && amount <= 0.0) {
            System.out.println("Failed to withdraw");
            return;
        }
        currentUser.withdraw(amount);
        System.out.println("Withdraw successfully");
    }

    private static void updateProfile() {
        if (currentUser == null) {
            System.out.println("User is null! Cannot update profile");
        }
        System.out.println("Your profile:\n" + currentUser.toString());
        System.out.println("Choose an action:");
        System.out.println("1. Change name");
        System.out.println("2. Change phone");
        System.out.println("3. Change address");
        System.out.println("4. Change password");
        System.out.println("Other key to quit.");

        String choice = Utils.promptInput("Enter your option: ");
        try {
            switch (choice) {
                case "1":
                    String name = Utils.promptInput("Enter new name: ");
                    currentUser.setName(name);
                    break;
                case "2":
                    String phone = Utils.promptInput("Enter new phone: ");
                    currentUser.setName(phone);
                    break;
                case "3":
                    Address address = askForUpdateAddress().orElse(null);
                    if (address != null)
                        currentUser.setAddress(address);
                    else System.out.println("Failed to update address.");
                    break;
                case "4":
                    String password = Utils.promptInput("Enter new password: ");
                    currentUser.setPassword(password);
                default:
                    System.out.println("Quit updating profile.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input! Quit updating profile.");
        }
    }

    private static Optional<Address> askForUpdateAddress() {
        Address.City city;
        String choice = Utils.promptInput("Choose your city:\n 1.Ha Noi\n 2.Ho Chi Minh City\n 3.Hai Phong\n 4.Can Tho\n 5.Da Nang\n");
        switch (choice) {
            case "1":
                city = Address.City.HANOI;
                break;
            case "2":
                city = Address.City.HCMC;
                break;
            case "3":
                city = Address.City.HAIPHONG;
                break;
            case "4":
                city = Address.City.CANTHO;
                break;
            case "5":
                city = Address.City.DANANG;
                break;
            default:
                System.out.println("Invalid choice. The address will not be updated.");
                return Optional.empty();
        }
        String addressLine = Utils.promptInput("Enter address line: ");
        return Optional.of(new Address(addressLine, city));
    }

    private static void viewCart() {
        Customer c = (Customer) currentUser;
        if (c.getCart().isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }
        System.out.println("Your Cart: ");
        for (CartItem item : c.getCart().getItems()) {
            System.out.println(item.toString());
        }
    }

    private static void addItemToCart() {
        Customer c = (Customer) currentUser;
        System.out.println("========= Add to cart =========");
        System.out.println("1. View all items");
        System.out.println("2. Search item");
        System.out.println("3. Search shop");
        String choice = Utils.promptInput("Enter your choice: ");
        switch (choice) {
            case "1":
                viewAllItem();
                break;
            case "2":
                String itemName = Utils.promptInput("Enter product name: ");
                searchItem(itemName);
                break;
            case "3":
                String shopName = Utils.promptInput("Enter shop name: ");
                searchShop(shopName);
                break;
            default:
                System.out.println("Invalid option. Stop adding item to cart.");
                return;
        }
        while (true) {
            int id = Utils.promptIntInput("Enter product id you want to add: ").orElse(-1);
            if (id == -1) {
                System.out.println("Invalid id.");
                if (Utils.promptInput("Continue? (y/[n]) ").equalsIgnoreCase("y")) continue;
                else break;
            }
            ItemStock product = system.getProductById(id).orElse(null);
            if (product == null) {
                System.out.println("Cannot find the product. Item will not added");
                if (Utils.promptInput("Continue? (y/[n]) ").equalsIgnoreCase("y")) continue;
                else break;
            }
            int quantity = Utils.promptIntInput("Enter item quantity you want to add: ").orElse(-1);
            if (quantity <= 0) {
                System.out.println("Invalid quantity. Item is not added.");
                if (Utils.promptInput("Continue? (y/[n]) ").equalsIgnoreCase("y")) continue;
                else break;
            }
            if (!c.addToCart(product, quantity)) System.out.println("Add failed!!!");
            if (Utils.promptInput("Stop adding product? (y/[n]) ").equalsIgnoreCase("y")) {
                break;
            }
        }
    }

    private static void removeItemFromCart() {
        Customer c = (Customer) currentUser;
        while (true) {
            int id = Utils.promptIntInput("Enter product id you want to remove: ").orElse(-1);
            if (id == -1 || c.getCart().existsInCart(id)) {
                System.out.println("Invalid id. Item will not remove.");
                if (Utils.promptInput("Continue? (y/[n]) ").equalsIgnoreCase("y")) continue;
                else break;
            }
            int quantity = Utils.promptIntInput("Enter quantity you want to remove: ").orElse(-1);
            if (quantity < 0) {
                System.out.println("Invalid quantity. Item will not remove.");
                if (Utils.promptInput("Continue? (y/[n]) ").equalsIgnoreCase("y")) continue;
                else break;
            }
            c.removeFromCart(id, quantity);
            if (!Utils.promptInput("Stop removing product? (y/[n]) ").equalsIgnoreCase("y")) {
                break;
            }
        }
    }

    private static void viewAllItem() {
        for (Shop shop : system.getAllShops()) {
            System.out.println("- Shop: " + shop.getName());
            for (ItemStock item : shop.getStock()) {
                System.out.println(item.toString());
            }
        }
    }

    private static void searchItem(String itemName) {
        List<ItemStock> result = system.findProducts(itemName);
        if (result.isEmpty()) {
            System.out.println("Currently there is no product on Shopdee.");
            return;
        }
        result.forEach(product -> System.out.println(product.toString()));
    }

    private static void searchShop(String shopName) {
        List<Shop> result = system.findShops(shopName);
        if (result.isEmpty()) {
            System.out.println("Currently there is no shop on Shopdee.");
            return;
        }
        result.forEach(shop -> System.out.println(shop.toString()));
    }
}
