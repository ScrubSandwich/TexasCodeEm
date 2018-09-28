public class Board {
    private Card[] board;

    public Board() {
        this.board = new Card[5];
    }

    public void addCard(int index, Card c) {
        this.board[index] = c;
    }

    public Card getCard(int index) {
        return this.board[index];
    }
}