package com.epiclabs.texascodeem.api;

public class Card {

    public static final int SPADE = 0;
    public static final int CLUB = 1;
    public static final int HEART = 2;
    public static final int DIAMOND = 3;

    public static final int JACK = 11;
    public static final int QUEEN = 12;
    public static final int KING = 13;

    private final int suit;
    private final int number;

    public Card(int suit, int number){
        this.suit = suit;
        this.number = number;
    }

    public Card(String suit, String number) {
        this.suit = getSuitNumber(suit);
        this.number = getNumberNumber(number);
    }

    public int getNumber(){
        return this.number;
    }

    public int getSuit(){
        return this.suit;
    }

    public int getNumberNumber(String s) {
        switch (s) {
            case "A":   return 1;
            case "2":   return 2;
            case "3":   return 3;
            case "4":   return 4;
            case "5":   return 5;
            case "6":   return 6;
            case "7":   return 7;
            case "8":   return 8;
            case "9":   return 9;
            case "T":   return 10;
            case "J":   return JACK;
            case "Q":   return QUEEN;
            case "K":   return KING;
        }

        return -1;
    }

    public String getNumberString(){
        switch (number) {
            case 1:     return "A";
            case 2:     return "2";
            case 3:     return "3";
            case 4:     return "4";
            case 5:     return "5";
            case 6:     return "6";
            case 7:     return "7";
            case 8:     return "8";
            case 9:     return "9";
            case 10:    return "T";
            case JACK:  return "J";
            case QUEEN: return "Q";
            case KING:  return "K";
        }

        return "INVALID NUMBER";
    }

    public int getSuitNumber(String s) {
        switch (s) {
            case "S":   return SPADE;
            case "H":   return HEART;
            case "D":   return DIAMOND;
            case "C":   return CLUB;
        }

        return -1;
    }

    public String getSuitString(){
        switch (suit) {
            case SPADE:   return "S";
            case HEART:   return "H";
            case DIAMOND: return "D";
            case CLUB:    return "C";
        }

        return "INVALID SUIT";
    }

    @Override
    public String toString(){
        return (getNumberString() + getSuitString());
    }

}
