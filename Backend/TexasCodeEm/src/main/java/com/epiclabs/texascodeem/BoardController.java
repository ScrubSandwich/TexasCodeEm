package com.epiclabs.texascodeem;

import com.epiclabs.texascodeem.api.Card;
import com.epiclabs.texascodeem.api.Deck;
import com.epiclabs.texascodeem.api.Values;

public class BoardController {

    private static BoardController instance;

    private static int currentBoardState;
    private static Card[] board;
    private static int closingAction;
    private static int currentBet;
    private static int pot;

    private BoardController() {
        currentBoardState = Values.PREFLOP;
        board = new Card[5];
        closingAction = -1;
        currentBet = 0;
        pot = 0;
    }

    public static BoardController getInstance() {
        if (instance == null) {
            instance = new BoardController();
        }

        return instance;
    }

    public Card[] getBoard() {
        return board;
    }

    public Card[] dealFlop() {
        for (int i = 0; i < 3; i++) {
            board[i] = Deck.getInstance().deal();
        }

        return board;
    }

    public Card[] dealTurn() {
        board[3] = Deck.getInstance().deal();

        return board;
    }

    public Card[] dealRiver() {
        board[4] = Deck.getInstance().deal();

        return board;
    }

    public Card[] clearBoard() {
        board = new Card[5];

        return board;
    }

    private boolean incrementBoard() {
        if (++currentBoardState > Values.RIVER) {
            currentBoardState = Values.PREFLOP;

            return false;
        }

        return true;
    }

    public int setCurrentBet(int bet) {
        currentBet = bet;

        return currentBet;
    }

    public int clearCurrentBet() {
        currentBet = 0;

        return currentBet;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public int setClosingAction(int userId) {
        closingAction = userId;

        return closingAction;
    }

    public int incrementPot(int value) {
        pot += value;

        return pot;
    }

    public int getPot() { return pot; }
}
