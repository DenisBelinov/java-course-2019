package bg.sofia.uni.fmi.mjt.shopping.portal;

import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.NoOfferFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.OfferAlreadySubmittedException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.ProductNotFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.offer.Offer;

import java.time.LocalDate;
import java.util.*;

import static java.util.Objects.isNull;

public class ShoppingDirectoryImpl implements ShoppingDirectory {
    private static final int ACCEPTABLE_DAYS_COUNT = 30;

    private Map<String, Set<Offer>> offers = new HashMap<>();

    public Map<String, Set<Offer>> getOffers() {
        return offers;
    } //used for testing

    @Override
    public Collection<Offer> findAllOffers(String productName) throws ProductNotFoundException {
        if (isNull(productName)) {
            throw new IllegalArgumentException("Null productName passed to findAllOffers.");
        }

        Set<Offer> offersForProduct = offers.get(productName.toLowerCase());

        if (isNull(offersForProduct)) {
            throw new ProductNotFoundException(String.format("No product with name %s", productName));
        }

        return filterOutOlderThan30Days(offersForProduct);
    }

    @Override
    public Offer findBestOffer(String productName) throws ProductNotFoundException, NoOfferFoundException {
        if (isNull(productName)) {
            throw new IllegalArgumentException("Null productName passed to findBestOffer.");
        }

        Collection<Offer> offers = findAllOffers(productName);

        if (offers.isEmpty()) {
            throw new NoOfferFoundException(String.format("No offers for %s", productName));
        }

        return offers.iterator().next();
    }

    @Override
    public Collection<PriceStatistic> collectProductStatistics(String productName) throws ProductNotFoundException {
        if (isNull(productName)) {
            throw new IllegalArgumentException("Null productName passed to collectProductStatistics.");
        }

        Set<Offer> offersForProduct = offers.get(productName.toLowerCase());
        if (isNull(offersForProduct)) {
            throw new ProductNotFoundException(String.format("No product with name %s", productName));
        }

        List<PriceStatistic> result = getStatisticsFromOffers(offersForProduct);
        Collections.sort(result, Collections.reverseOrder());

        return result;
    }

    @Override
    public void submitOffer(Offer offer) throws OfferAlreadySubmittedException {
        if (isNull(offer)) {
            throw new IllegalArgumentException("Null Offer passed to submitOffer.");
        }

        Set<Offer> offersForProduct = offers.get(offer.getProductName().toLowerCase());

        if (isNull(offersForProduct)) {
            // we have no other offers for this product
            Set<Offer> newOffers = new TreeSet<>();
            newOffers.add(offer);

            offers.put(offer.getProductName().toLowerCase(), newOffers);
        } else if (!offersForProduct.add(offer)) {
            throw new OfferAlreadySubmittedException("Identical offer already exists in the Directory.");
        }
    }

    private List<Offer> filterOutOlderThan30Days(Set<Offer> offers) {
        LocalDate lastAcceptableDate = LocalDate.now().minusDays(ACCEPTABLE_DAYS_COUNT - 1);

        List<Offer> result = new ArrayList<>();
        for (Offer offer : offers) {
            if (offer.getDate().isBefore(lastAcceptableDate)) {
                continue;
            }
            result.add(offer);
        }

        return result;
    }

    /**
     * Iterates over the offers and generates the statistics.
     *
     * @param offers for a specific product
     * @return List of the statistics for the set of offers
     */
    private List<PriceStatistic> getStatisticsFromOffers(Set<Offer> offers) {
        Map<LocalDate, PriceStatistic> statisticsMap = new LinkedHashMap<>();

        // populate the Map with the offers from the product
        for (Offer offer : offers) {
            LocalDate offerDate = offer.getDate();
            PriceStatistic statisticEntry = statisticsMap.get(offerDate);

            if (isNull(statisticEntry)) {
                // we have no statistics for this date yet
                PriceStatistic stat = new PriceStatistic(offerDate);
                stat.addPrice(offer.getTotalPrice());

                statisticsMap.put(offerDate, stat);
            } else {
                statisticEntry.addPrice(offer.getTotalPrice());
            }
        }

        return new ArrayList<>(statisticsMap.values());
    }
}
