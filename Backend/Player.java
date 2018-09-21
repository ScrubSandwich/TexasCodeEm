public class Player {

    private String name;
    private Card card1;
    private Card card2;

    private double stack;
    private double DEFAULT_STACK_SIZE = 50;
    private boolean inHand;

    public Player(String name) {
        this.name = name;
        this.stack = DEFAULT_STACK_SIZE;
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
        return null;
    }

    public void setInHand(boolean newInHand) {
        this.inHand = newInHand;
    }

    public boolean getInHand() {
        return inHand;
    }

    public void setStack(double newStack) {
        this.stack = newStack;
    }

    public double getStack() {
        return stack;
    }

    public String getName(){
        return name;
    }
}