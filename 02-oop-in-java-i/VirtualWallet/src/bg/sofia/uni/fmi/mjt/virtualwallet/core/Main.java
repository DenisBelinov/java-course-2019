package bg.sofia.uni.fmi.mjt.virtualwallet.core;

import bg.sofia.uni.fmi.mjt.virtualwallet.core.card.Card;
import bg.sofia.uni.fmi.mjt.virtualwallet.core.card.StandardCard;

public class Main {

    public static void main(String[] args) {
        Card[] cards = new Card[5];

        cards[0] = new StandardCard("asd");
        cards[1] = new StandardCard("bsd");

        System.out.println("asd");
    }
}
