import java.util.Scanner;

public class Game {

    private static final int PLAYER_SIZE = 2;
    private Player[] players;
	private Deck deck;
    private Board board;

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
        players[0].setPutInPot(smallBlind);

        //establish bb
        System.out.println(players[1].getName() + " is Big Blind. \n");
        players[1].setStack(players[1].getStack() - bigBlind);
        pot += bigBlind;
        players[1].setPutInPot(bigBlind);

        preFlopAction:
        while(players[0].getStack() != players[1].getStack()) {
            System.out.println(players[0].getName() + "'s action. Pot = " + pot + " Stack = " + players[0].getStack());

            String buttonDecision;
            sbDecision:
            while (true) {
                System.out.println(players[0].getPutInPot());
                double buttonCallAmount = pot - 2 * players[0].getPutInPot();
                System.out.println("Enter your decision: ");
                System.out.println("[c] - call ("+ buttonCallAmount +")");
                System.out.println("[r] - raise");
                System.out.println("[f] - fold");
                System.out.println("");

                buttonDecision = s.nextLine();
                switch(buttonDecision) {
                    case "c": call(players[0], buttonCallAmount);
                              break sbDecision;
                    case "r": raise(players[0]);
                              break sbDecision;
                    case "f": fold(players[0]);
                              return;
                    default: System.out.println("Invalid option.");
                            continue sbDecision;
                }
            }

            if (buttonDecision.equals("c") && pot != 2.0) {
                return;
            }

            
            bbDecision:
            while (true) {
                System.out.println(players[1].getName() + "'s action. Pot = " + pot + " Stack = " + players[1].getStack());
                double callAmount = pot - 2 * players[1].getPutInPot();
                System.out.println("Enter your decision: ");
                if (pot == 2.0) {
                    System.out.println("[x] - check");
                    System.out.println("[r] - raise");
                } else {
                    System.out.println("[c] - call (" + callAmount + ")");
                    System.out.println("[r] - raise");
                    System.out.println("[f] - fold \n");
                }

               // s.nextLine();
                String bbDecision = s.nextLine();
                switch(bbDecision) {
                    case "x": return;
                    case "c": call(players[1], callAmount);
                              break bbDecision;
                    case "r": raise(players[1]);
                              break bbDecision;
                    case "f": fold(players[1]);
                              return;
                    default: System.out.println("Invalid option.");
                            continue bbDecision;
                }
            }
        }
    }

    private void getFlopAction() {
        board = new Board();
        board.addCard(0, deck.deal());
        board.addCard(1, deck.deal());
        board.addCard(2, deck.deal());

        System.out.println("\nThe flop is " + board.getCard(0).toString() + " " + board.getCard(1).toString() + " " + board.getCard(2).toString());
    }

    private void raise(Player p) {
        while (true) {
            System.out.println("Enter total raise size: ");
            double raise = s.nextDouble();
            if (! (raise >= 2 * pot)) {
                System.out.println("Invalid raise size!");
                continue;
            } 
            pot += raise - p.getPutInPot();
            p.setStack(p.getStack() - raise + p.getPutInPot());
            p.setPutInPot(raise);
            return;
        }
    }

    private void call(Player p, double amount) {
        p.setStack(p.getStack() - amount);
        pot += amount;
        p.setPutInPot(p.getPutInPot() + amount);
    }

    private void fold(Player p) {
        p.setInHand(false);
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
          players[i] = new Player("Player" + i);
      }

      Game game = new Game(players);
      game.start();
    }
}