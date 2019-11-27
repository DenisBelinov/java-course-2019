package bg.sofia.uni.fmi.mjt.shopping.portal.offer;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class PremiumOfferTest {

    @Test
    public void testDiscountDecimalPoint() {
        double discount = 10.12678;
        PremiumOffer po = new PremiumOffer("test", LocalDate.now(), "test desc", 1, 2, discount);

        Assert.assertEquals(po.getDiscount(), 10.13, 0.001);
    }
}
