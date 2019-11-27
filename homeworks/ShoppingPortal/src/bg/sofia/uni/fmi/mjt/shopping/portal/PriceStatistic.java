package bg.sofia.uni.fmi.mjt.shopping.portal;

import java.time.LocalDate;

public class PriceStatistic implements Comparable<PriceStatistic> {
    private LocalDate date;
    private double lowestPrice;

    private int offersCount;
    private double totalOffersPrice;

    public PriceStatistic(LocalDate date) {
        this.date = date;

        this.lowestPrice = Double.MAX_VALUE;
        this.offersCount = 0;
        this.totalOffersPrice = 0;
    }

    public void addPrice(double price) {
        if (price < lowestPrice) {
            lowestPrice = price;
        }

        totalOffersPrice += price;
        offersCount += 1;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }

    public double getAveragePrice() {
        if (offersCount == 0) {
            return 0;
        }

        return totalOffersPrice / offersCount;
    }

    @Override
    public int compareTo(PriceStatistic o) {
        return date.compareTo(o.getDate());
    }
}
