import java.util.ArrayList;
import java.util.List;

public class ItemStock {
    private final Item item;
    private int quantity;
    private final List<Review> reviews;
    private final Shop shop;

    public ItemStock(Item item, int quantity, Shop shop) {
        this.item = item;
        this.quantity = quantity;
        this.shop = shop;
        reviews = new ArrayList<>();
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Shop getShop() {
        return shop;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void removeReview(int reviewId) {
        reviews.stream()
                .filter(review -> review.getId() == reviewId)
                .findAny()
                .ifPresent(this.reviews::remove);
    }

    public void modifyReview(int reviewId, double newRating, String newComment) {
        reviews.stream()
                .filter(review -> review.getId() == reviewId)
                .findAny()
                .ifPresent(review -> {
                    review.setRating(newRating);
                    review.setComment(newComment);
                });
    }

    public void minusWith(ItemStock itemStock) {
        if (itemStock.getItem().getId() != this.item.getId()) {
            throw new IllegalArgumentException("Item mismatch");
        }
        this.quantity -= itemStock.getQuantity();
    }
}
