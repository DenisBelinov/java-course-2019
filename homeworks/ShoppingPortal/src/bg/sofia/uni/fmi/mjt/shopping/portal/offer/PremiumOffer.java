package bg.sofia.uni.fmi.mjt.shopping.portal.offer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class PremiumOffer extends AbstractOffer {
    private static final double MIN_DISCOUNT = 0;
    private static final double MAX_DISCOUNT = 100;

    private final double discount;

    public double getDiscount() {
        return discount;
    }

    public PremiumOffer(String productName,
                        LocalDate date,
                        String description,
                        double price,
                        double shippingPrice,
                        double discount) {

        super(productName, date, description, price, shippingPrice);

        if (discount < MIN_DISCOUNT || discount > MAX_DISCOUNT) {
            throw new IllegalArgumentException(
                    String.format("Discount %s not in range [%s , %s].", discount, MIN_DISCOUNT, MAX_DISCOUNT));
        }

        this.discount = round(discount, 2);
    }

    @Override
    public double getTotalPrice() {
        double totalPrice = price + shippingPrice;

        return totalPrice - totalPrice * discount / MAX_DISCOUNT;
    }

    // from https://stackoverflow.com/a/2808648/4532765
    private static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException("Invalid places passed to round function.");
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
