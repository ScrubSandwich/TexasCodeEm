package com.epiclabs.texascodeem.api;

import com.epiclabs.texascodeem.GameController;

public class Player {

    private final String name;
    private final String id;
    private int stackSize;
    private int position;
    private int seatNumber;
    private boolean inHand;
    private Card[] cards;

    public Player(String name, String id) {
        this(name, id, -1);
    }

    public Player(String name, String id, int desiredSeatNumber) {
        this.name = name;
        this.id = id;
        this.stackSize = Values.DEFAULT_STACK_SIZE;
        this.position = -1;
        this.seatNumber = GameController.getSeatOfNewPlayer(desiredSeatNumber);
        this.cards = new Card[Values.NUMBER_OF_CARDS];
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getIdInt() {
        return Integer.parseInt(id);
    }
  
    public boolean setCards(Card[] cards) {
        if (Values.NUMBER_OF_CARDS != cards.length) {
            return false;
        }

        for (int i = 0; i < Values.NUMBER_OF_CARDS; i++) {
            Card card = cards[i];
            this.cards[i] = new Card(card.getSuit(), card.getNumber());
        }

        return true;
    }

    public Card[] getCards() {
        return this.cards;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setStackSize(int stackSize) {
        stackSize = stackSize;
    }

    public void incrementStack(int size) {
        stackSize += size;
    }

    public void decrementStack(int size) {
        stackSize -= size;
    }

    public void setInHand(boolean inHand) {
        this.inHand = inHand;
    }

    public boolean inHand() {
        return inHand;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int incrementPosition() {
        if (++position >= 9) {
            position = 0;
        }

        return position;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int incrementSeatNumber() {
        if (++seatNumber >= Values.NUMBER_OF_SEATS) {
            seatNumber = 0;
        }

        return seatNumber;
    }
}
