import java.util.Scanner;

public class Game {

    private static final int PLAYER_SIZE = 2;
    private Player[] players;
	private Deck deck;

    private boolean running = true;
    private Scanner s;

    private double pot;
    private static final double smallBlind = 0.5;
    private static final double bigBlind = 1.0;

    public Game(Player[] p) {
        this.players = new Player[p.length];
        s = new Scanner(System.in);

        for (int i = 0; i < PLAYER_SIZE; i++) {
            this.players[i] = p[i];
        }

		deck = new Deck();
        deck.shuffle();
    }

    public void start() {
        while (running) {
            dealToPlayers();
            moveButton();
            getPreFlopAction();
            getFlopAction();
            running = false;
        }
    }

    // give each player 2 cards and print their hand
    private void dealToPlayers() {
		for (int i = 0; i < PLAYER_SIZE; i++) {
            players[i].setCard(1, deck.deal());
            players[i].setCard(2, deck.deal());

            System.out.println("Player" + i + "'s Hand: " + players[i].getCard(1).toString() + players[i].getCard(2).toString());
		}
        System.out.print("\n");
    }

    // allow first the small blind (button), then big blind to either raise, call, or fold.
    private void getPreFlopAction() {
        s = new Scanner(System.in);
        pot = 0.0;
        //establish sb
        System.out.println(players[0].getName() + " is Small Blind.");
        players[0].setStack(players[0].getStack() - smallBlind);
        pot += smallBlind;

        //establish bb
        System.out.println(players[1].getName() + " is Big Blind. \n");
        players[1].setStack(players[1].getStack() - bigBlind);
        pot += bigBlind;

        System.out.println(players[0].getName() + "'s action. Pot = " + pot + " Stack = " + players[0].getStack());

        sbDecision:
        while (true) {
            System.out.println("Enter your decision: ");
            System.out.println("[c] - call (0.5)");
            System.out.println("[r] - raise");
            System.out.println("[f] - fold");
            System.out.println("");

            String buttonDecision = s.nextLine();
            switch(buttonDecision) {
                case "c": players[0].setStack(players[0].getStack() - 0.5);
                          pot += 0.5;
                          break sbDecision;
                case "r": raise:
                        while (true) {
                            System.out.println("Enter total raise size: ");
                            double raise = s.nextDouble();
                            if (! (raise >= 2 * bigBlind)) {
                                System.out.println("Invalid raise size!");
                                continue raise;
                            } 
                            pot += raise - smallBlind;
                            break sbDecision;
                        }
                case "f": players[0].setInHand(false);
                        return;
                default: System.out.println("Invalid option.");
                         continue sbDecision;
            }
        }

        System.out.println(players[1].getName() + "'s action. Pot = " + pot + " Stack = " + players[1].getStack());

        bbDecision:
        while (true) {
            double callAmount = pot - 1.0;
            System.out.println("Enter your decision: ");
            if (pot == 2.0) {
                System.out.println("[x] - check");
                System.out.println("[r] - raise \n");
            } else {
                System.out.println("[c] - call (" + callAmount + ")");
                System.out.println("[r] - raise");
                System.out.println("[f] - fold \n");
            }

            String bbDecision = s.nextLine();
            switch(bbDecision) {
                case "x": return;
                case "c": players[1].setStack(players[1].getStack() - callAmount);
                          pot += callAmount;
                          break bbDecision;
                case "r": raise:
                        while (true) {
                            System.out.println("Enter total raise size: ");
                            double raise = s.nextDouble();
                            if (! (raise >= 2 * pot)) {
                                System.out.println("Invalid raise size!");
                                continue raise;
                            } 
                            pot += raise - bigBlind;
                            break bbDecision;
                        }
                case "f": players[1].setInHand(false);
                        return;
                default: System.out.println("Invalid option.");
                         continue bbDecision;
            }
        }
    }

    private void getFlopAction() {
        System.out.println("The flop is " + deck.deal().toString() + " " + deck.deal().toString() + " " + deck.deal().toString());
    }

    // switches the order of players; the first player in the array is the button
    private void moveButton() {
        Player temp = players[0];
        players[0] = players[1];
        players[1] = temp;
    }

    public static void main(String[] args) {
      Player[] players = new Player[PLAYER_SIZE];

      for (int i = 0; i < PLAYER_SIZE; i++) {
          players[i] = new Player("Player" + (i + 1));
      }

      Game game = new Game(players);
      game.start();
    }
}