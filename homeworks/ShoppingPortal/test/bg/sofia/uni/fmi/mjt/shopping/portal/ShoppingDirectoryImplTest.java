package bg.sofia.uni.fmi.mjt.shopping.portal;

import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.OfferAlreadySubmittedException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.ProductNotFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.offer.Offer;
import bg.sofia.uni.fmi.mjt.shopping.portal.offer.PremiumOffer;
import bg.sofia.uni.fmi.mjt.shopping.portal.offer.RegularOffer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.fail;

public class ShoppingDirectoryImplTest {
    private ShoppingDirectoryImpl sh;


    private Offer prod11 =
            new RegularOffer("product1", LocalDate.now(), "prod1", 1, 2);
    private Offer prod12 =
            new RegularOffer("product1", LocalDate.now(), "prod1", 5, 6);
    private Offer prod13 =
            new PremiumOffer("Product1", LocalDate.now().minusDays(1), "prod1", 5, 6, 0.5);

    private Offer prod2 =
            new RegularOffer("product2", LocalDate.now(), "prod2", 1, 2);


    @Before
    public void init() {
        sh = new ShoppingDirectoryImpl();
    }

    @Test
    public void testSubmitOfferTwoOffersForSameProduct() {
        try {
            sh.submitOffer(prod11);
            sh.submitOffer(prod12);
        } catch (OfferAlreadySubmittedException e) {
            fail();
        }

        Map<String, Set<Offer>> offers = sh.getOffers();
        Assert.assertEquals(offers.get("product1").size(), 2);
    }

    @Test
    public void testSubmitOfferTwoOffersForSameProductWithDifferentCapitalization() {
        try {
            sh.submitOffer(prod11);
            sh.submitOffer(prod13);
        } catch (OfferAlreadySubmittedException e) {
            fail();
        }

        Map<String, Set<Offer>> offers = sh.getOffers();
        Assert.assertEquals(offers.get("product1").size(), 2);
    }

    @Test
    public void testSubmitOfferTwOffersForDifferentProduct() {
        try {
            sh.submitOffer(prod11);
            sh.submitOffer(prod2);
        } catch (OfferAlreadySubmittedException e) {
            fail();
        }

        Map<String, Set<Offer>> offers = sh.getOffers();
        Assert.assertEquals(offers.size(), 2);
    }

    @Test(expected = OfferAlreadySubmittedException.class)
    public void testSubmitOfferSameOffer() throws OfferAlreadySubmittedException {
        try {
            sh.submitOffer(prod11);
        } catch (OfferAlreadySubmittedException e) {
            fail();
        }

        sh.submitOffer(prod11);
    }

    @Test
    public void testFindAllOffers() {
        try {
            sh.submitOffer(prod11);
            sh.submitOffer(prod13);
        } catch (OfferAlreadySubmittedException e) {
            fail();
        }

        Collection<Offer> offers = null;
        try {
            offers = sh.findAllOffers(prod11.getProductName());
        } catch (ProductNotFoundException e) {
            fail();
        }

        Assert.assertEquals(offers.size(), 2);
        // check if the Collection was sorted
        Assert.assertEquals(prod11, offers.iterator().next());
    }

    @Test
    public void testCollectProductStatisticsAveragePrice() {
        try {
            sh.submitOffer(prod11);
            sh.submitOffer(prod12);
        } catch (OfferAlreadySubmittedException e) {
            fail();
        }
        Collection<PriceStatistic> result = null;
        try {
            result = sh.collectProductStatistics(prod11.getProductName());
        } catch (Exception e) {
            fail();
        }
        double expectedAverage = (prod11.getTotalPrice() + prod12.getTotalPrice()) / 2;
        Assert.assertEquals(result.iterator().next().getAveragePrice(), expectedAverage, 0.0001);
    }
}
