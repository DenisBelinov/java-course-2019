package bg.sofia.uni.fmi.mjt.shopping.portal.exceptions;

public class OfferAlreadySubmittedException extends Exception {
    public OfferAlreadySubmittedException(String errorMessage) {
        super(errorMessage);
    }
}
