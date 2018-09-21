public class Player {

    private String name;
    private Card[] holeCards = new Card[2];

    public Player(String name) {
        this.name = name;
    }

    public void setCard(int i, Card card) {
        if (i >= 1 && i <= 2) {
            holeCards[i] = card;
        }
        return;
    }

    public Card getCard(int i) {
        if (i >= 1 && i <= 2) {
            return holeCards[i];
        }
    }

}