package com.epiclabs.texascodeem.api;

public class Deck {

    private Card[] deck;
    private int cardsUsed;
    private final int DECK_SIZE = 52;
    private int currentCard;

    public Deck(){
        deck = new Card[52];
        cardsUsed = 0;
        currentCard = 0;

        //Assign each card into the deck in order
        for (int suit = 0; suit < 4; suit++){
            for (int number = 1; number < 14; number++){
                deck[currentCard] = new Card(suit, number);
                currentCard++;
            }
        }
        currentCard = 0;
    }

    public void shuffle(){
        for (int i = 0; i < DECK_SIZE; i++) {
            int rand = (int)(Math.random()*(i + 1));
            Card temp = deck[i];
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        cardsUsed = 0;
    }

    public Card deal(){
        Card toDeal = deck[currentCard];
        currentCard++;
        return toDeal;
    }
}
