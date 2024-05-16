package Item;

import Shop.Shop;

public class ItemStock {
    private final Item item;
    private double price;
    private int quantity;
    private final Shop shop;

    public ItemStock(Item item, double price, int quantity, Shop shop) {
        this.item = item;
        this.price = price;
        this.quantity = quantity;
        this.shop = shop;
    }

    public Item getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Shop getShop() {
        return shop;
    }

    public void minusWith(ItemStock itemStock) {
        if (itemStock.getItem().getId() != this.item.getId()) {
            throw new IllegalArgumentException("Item mismatch");
        }
        this.quantity -= itemStock.getQuantity();
    }

    @Override
    public String toString() {
        return "ItemStock:" +
                "\nitem name: " + item.getName() +
                "\nprice: " + price +
                "\nquantity: " + quantity +
                "\nshop: " + shop.getName() + '\n';
    }
}
