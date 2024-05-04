package MainSystem;

import Item.CartItem;
import Item.ItemStock;
import User.*;
import Order.Order;
import Shop.Shop;
import Utils.Utils;
import Utils.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public final class ShopdeeClient {

    private static User currentUser = null;
    private static final ShopdeeSystem system = ShopdeeSystem.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    private ShopdeeClient() {
    }

    public static void displayMenu() {
        while (true) {
            String username;
            String password;
            System.out.println("====== LOGIN MENU ======");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký");
            System.out.println("3. Thoát");
            System.out.println("========================");

            // Nhập lựa chọn từ người dùng
            String choice = Utils.promptInput("Nhập lựa chọn của bạn: ");
            // Xử lý lựa chọn
            switch (choice) {
                case "1":
                    String shipperYesno = Utils.promptInput("Bạn có phải shipper không? (y/n) ");
                    boolean isShipper = shipperYesno.equalsIgnoreCase("y");
                    String role = (isShipper) ? "shipper" : "khách hàng";
                    username = Utils.promptInput(String.format("Nhập tài khoản %s: ", role));
                    password = Utils.promptInput("Nhập password: ");
                    login(isShipper, username, password);
                    if (currentUser == null) {
                        System.out.println("Không tìm thấy tài khoản.");
                    } else if (currentUser.getRole().equals(UserRole.Customer)) {
                        displayCustomerMenu();
                    } else {
                        displayShipperMenu(shippers.get(username));
                    }
                    break;
                case "2":
                    // Gọi hàm để xử lý đăng ký
                    UserRole role = null;
                    System.out.print("Nhập tài khoản: ");
                    username = scanner.nextLine();
                    System.out.print("Nhập password: ");
                    password = scanner.nextLine();
                    if (checkAccount(username, password) == true) {
                        System.out.println("Chọn role của bạn: ");
                        System.out.println("1. Khách hàng");
                        System.out.println("2. Shipper");
                        int select = scanner.nextInt();
                        scanner.nextLine();
                        switch (select) {
                            case 1:
                                role = role.CUSTOMER;
                                displayRegister(role, username, password);
                                break;
                            case 2:
                                role = role.SHIPPER;
                                displayRegister(role, username, password);
                                break;
                            default:
                                System.out.println("INVALID");
                        }
                    }
                    break;
                case "3":
                    System.out.println("Thoát khỏi chương trình...");
                    System.exit(0);
                default:
                    System.out.println("INVALID");
            }
        }
    }

    // Current user will be set to null if account is not found
    private static void login(boolean isShipper, String username, String password) {
        if (isShipper) {
            currentUser = system.authorizeShipper(username, password).orElse(null);
        } else {
            currentUser = system.authorizeCustomer(username, password).orElse(null);
        }
    }

    public static void register(boolean isShipper,
                                String username,
                                String password,
                                String name,
                                String phone,
                                Address address) {

    }

    public static void logout() {

    }

    private static void displayCustomerMenu() {
        // check if current user is Customer, if true c is currentUser as Customer type
        if (!(currentUser instanceof Customer c)) {
            System.out.println("Error! current user is not Customer");
            return;
        }
        while (true) {
            System.out.println("========= MENU =========");
            System.out.println("1. Update Profile");
            System.out.println("2. View your cart");
            System.out.println("3. View orders");
            System.out.println("4. Your shop");
            System.out.println("5. Deposit");
            System.out.println("6. Withdraw");
            System.out.println("7. Log out");
            System.out.println("========================");

            String choice = Utils.promptInput("Enter your choice: ");
            scanner.nextLine();
            switch (choice) {
                case "1":
                    updateProfile();
                    break;
                case "2":
                    viewCart();
                    System.out.println("========= MENU =========");
                    System.out.println("1. Add item");
                    System.out.println("2. Remove item");
                    System.out.println("3. Pay");

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
                            System.out.println("INVALID");
                    }
                case "3":
                    System.out.println("Your orders:");
                    for (Order order : system.getCustomerOrders(c)) {
                        System.out.println(order.toString());
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
        // check if current user is Shipper, if true c is currentUser as Shipper type
        if (!(currentUser instanceof Shipper s)) {
            System.out.println("Error! current user is not Customer");
            return;
        }
        System.out.println("- Balance: " + s.getBalance());
        System.out.println("========= MENU =========");
        System.out.println("1. Receive order");
        System.out.println("2. Confirm order");
        System.out.println("3. View task");
        System.out.println("4. Update Profile");
        System.out.println("5. Withdraw");
        System.out.println("6. Log out");

        System.out.print("Nhập lựa chọn của bạn: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                receiveOrder(s);
                break;
            case 2:
                confirmOrder(s);
                break;
            case 3:
                viewShipperTask(s);
            case 4:
                updateProfile();
                break;
            case 5:
                withdraw();
                break;
            case 6:

                break;
            default:
                System.out.println("INVALID");
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
        System.out.println("- Revenue: " + c.getOwnedShop().getRevenue());
        System.out.println("========= MENU =========");
        System.out.println("1. Change Shop information");
        System.out.println("2. Add/Delete item");
        System.out.println("3. Take Orders");
        System.out.println("4. Transfer money to account");

        String choice = Utils.promptInput("Nhập lựa chọn của bạn: ");

        switch (choice) {
            case "1":
                changeShopInfo(c.getOwnedShop());
                break;
            case "2":
                System.out.println("1. Add item");
                System.out.println("2. Delete item");
                System.out.print("Nhập lựa chọn của bạn: ");
                int select = scanner.nextInt();
                scanner.nextLine();
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
            case "4:":
                c.addBalance(c.getOwnedShop().getRevenue());
                c.getOwnedShop().setRevenue(0);
                System.out.println("Chuyển thành công!");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void addItemToShop(Shop shop) {
        // todo
    }

    private static void deleteItemFromShop(Shop shop) {
        // todo
    }

    private static void acceptOrderByShop(Shop shop) {
        List<Order> ordersByThisShop = system.getOrdersByShop(shop);
        List<Integer> orderIdsByThisShop = ordersByThisShop.stream().mapToInt(order -> order.getId()).boxed().toList();
        for (Order order : system.getOrdersByShop(shop)) {
            System.out.println("List of order by this shop:");
            System.out.println(order.toString());
        }

        List<Integer> orderIds = new ArrayList<>();
        do {
            int id = Utils.promptIntInput("Nhập id đơn hàng bạn chuẩn bị xong: ").orElse(-1);

            if (id <= 0 || !orderIdsByThisShop.contains(id)) System.out.println("Invalid id.");
            else orderIds.add(id);
        } while (Utils.promptInput("Bạn muốn tiếp tục(y/n): ").equalsIgnoreCase("y"));

        system.shopAcceptOrder(shop, orderIds);
    }

    private static void changeShopInfo(Shop shop) {
        System.out.println("1. Change shop name");
        System.out.println("2. Change shop address");
        String choice = Utils.promptInput("Nhập lựa chọn: ");

        switch (choice) {
            case "1":
                System.out.println("Nhập tên shop mới: ");
                String shopName = scanner.next();
                shop.setName(shopName);
                break;
            case "2":
                System.out.println("Nhập địa chỉ shop mới: ");
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

        if (!Utils.promptInput("Bạn có muốn thanh toán không? (y/[n]) ").equalsIgnoreCase("y")) {
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
        System.out.println("Chủ Tài Khoản: CTCP SHOPDEE");
        System.out.println("Ngân hàng: VCB");
        System.out.println("Nội dung chuyển khoản: username");
        System.out.println("===============================");
        return Utils.promptDoubleInput("Số tiền bạn muốn nạp:").orElse(-1.0);
    }

    private static void withdraw() {
        Utils.promptInput("Nhập tên ngân hàng: ");
        Utils.promptInput("Nhập STK: ");
        double amount = Utils.promptDoubleInput("Nhập số tiền muốn rút: ").orElse(-1.0);
        if (amount > currentUser.getBalance() && amount <= 0.0) {
            System.out.println("INVALID");
            return;
        }
        currentUser.decreaseBalance(amount);
        System.out.println("Withdraw successfully");
    }

    private static void updateProfile() {
        if (currentUser == null) {
            System.out.println("User is null! Cannot update profile");
        }
        System.out.println("Your profile:\n" + currentUser.toString());
        System.out.println("1. Change name");
        System.out.println("2. Change phone");
        System.out.println("3. Change address");
        System.out.println("4. Change Password");

        String choice = Utils.promptInput("Nhập lựa chọn của bạn: ");
        switch (choice) {
            case "1":
                System.out.println("Enter new name: ");
                String name = scanner.nextLine();
                currentUser.setName(name);
                break;
            case "2":
                System.out.println("Enter new phone: ");
                String phone = scanner.nextLine();
                currentUser.setName(phone);
                break;
            case "3":
                Address address = askForUpdateAddress().orElse(null);
                if (address != null)
                    currentUser.setAddress(address);
                else System.out.println("Failed to update address.");
                break;
            case "4":
                System.out.println("Enter new password: ");
                String password = scanner.nextLine();
                currentUser.setPassword(password);
            default:
                System.out.println("Invalid choice! Quit updating profile.");
        }
    }

    private static Optional<Address> askForUpdateAddress() {
        Address.City city;
        String choice = Utils.promptInput("Choose your city:\n 1.Ha Noi\n 2.Ho Chi Minh\n 3.Hai Phong\n 4.Can Tho\n 5.Da Nang\n");
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
        String district = Utils.promptInput("Enter district: ");
        String street = Utils.promptInput("Enter street: ");
        String addressLine = Utils.promptInput("Enter address line: ");
        return Optional.of(new Address(addressLine, street, district, city));
    }

    private static void viewCart() {
        Customer c = (Customer) currentUser;
        System.out.println("Your Cart");
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
                System.out.println("Nhap ten san pham: ");
                String itemName = scanner.nextLine();
                searchItem(itemName);
                break;
            case "3":
                System.out.println("Nhap ten Shop: ");
                String shopName = scanner.nextLine();
                searchShop(shopName);
                break;
            default:
                System.out.println("Invalid option. Stop adding item to cart.");
                return;
        }
        while (true) {
            int id = Utils.promptIntInput("Nhap id item ban muon them: ").orElse(-1);
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
            int quantity = Utils.promptIntInput("Nhap quantity item ban muon them: ").orElse(-1);
            if (quantity <= 0) {
                System.out.println("Invalid quantity. Item is not added.");
                if (Utils.promptInput("Continue? (y/[n]) ").equalsIgnoreCase("y")) continue;
                else break;
            }
            c.addToCart(product, quantity);
            if (!Utils.promptInput("Stop adding product? (y/[n]) ").equalsIgnoreCase("y")) {
                break;
            }
        }
    }

    private static void removeItemFromCart() {
        Customer c = (Customer) currentUser;
        while (true) {
            int id = Utils.promptIntInput("Nhap id item ban muon xoa: ").orElse(-1);
            if (id == -1 || c.getCart().existsInCart(id)) {
                System.out.println("Invalid id. Item will not remove.");
                if (Utils.promptInput("Continue? (y/[n]) ").equalsIgnoreCase("y")) continue;
                else break;
            }
            int quantity = Utils.promptIntInput("Nhap quantity item ban muon xoa: ").orElse(-1);
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
        system.findProducts(itemName).forEach(product -> System.out.println(product.toString()));
    }

    private static void searchShop(String shopName) {
        system.findShops(shopName).forEach(shop -> System.out.println(shop.toString()));
    }
}
