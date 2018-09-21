public class Player {

    private String name;
    private Card card1;
    private Card card2;

    public Player(String name) {
        this.name = name;
    }

    public void setCard(int i, Card card) {
        if (i == 1) {
            card1 = card;
        } else if (i == 2) {
            card2 = card;
        }
        return;
    }

    public Card getCard(int i) {
        if (i == 1) {
            return card1;
        } else if (i == 2) {
            return card2;
        }
    }

}