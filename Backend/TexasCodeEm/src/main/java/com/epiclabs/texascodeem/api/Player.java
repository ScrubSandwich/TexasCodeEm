package com.epiclabs.texascodeem.api;

public class Player {

    private final String name;
    private final String id;
    private int stackSize;
    private Card[] cards;

    public Player(String name, String id) {
        this.name = name;
        this.id = id;
        this.stackSize = Values.DEFAULT_STACK_SIZE;
        this.cards = new Card[Values.NUMBER_OF_CARDS];
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
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
}
