package bg.sofia.uni.fmi.mjt.shopping.portal.offer;

import java.time.LocalDate;
import java.util.Objects;

public abstract class AbstractOffer implements Offer {
    protected final String productName;
    protected final LocalDate date;
    protected final String description;

    protected final double price;
    protected final double shippingPrice;

    public AbstractOffer(String productName, LocalDate date, String description, double price, double shippingPrice) {
        this.productName = productName;
        this.date = date;
        this.description = description;
        this.price = price;
        this.shippingPrice = shippingPrice;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double getShippingPrice() {
        return shippingPrice;
    }

    @Override
    public abstract double getTotalPrice();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractOffer)) {
            return false;
        }
        AbstractOffer that = (AbstractOffer) o;
        return Double.compare(that.getTotalPrice(), getTotalPrice()) == 0 &&
                productName.toLowerCase().equals(that.productName.toLowerCase()) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName.toLowerCase(), date, getTotalPrice());
    }

    @Override
    public int compareTo(Offer o) {
        int diff = (int) (getTotalPrice() - o.getTotalPrice());
        if (diff == 0) {
            if (o.equals(this)) {
                return 0;
            }
            // We have two offers for the same product and the same price.
            // It doesn't really matter how they are placed in the TreeMap, so return -1
            return -1;
        }

        return diff;
    }
}
