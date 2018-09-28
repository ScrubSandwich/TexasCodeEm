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
            getTurnAction();
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
        players[0].setPutInPot(0.0);
        players[1].setPutInPot(0.0);

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

        while(players[0].getStack() != players[1].getStack()) {
            decision(players[0]);

            if (players[0].getPutInPot() == players[1].getPutInPot() && pot != 2.0) {
                return;
            }

           
            if (pot == 2.0) {
                System.out.println("\n" + players[1].getName() + "'s action. Pot = " + pot + " Stack = " + players[1].getStack());
                System.out.println("Enter your decision: ");
                System.out.println("[x] - check");
                System.out.println("[r] - raise");
                String decision = s.nextLine();
                switch (decision) {
                    case "x": return;
                    case "r": raise(players[1]);
                              break;
                }
            } else {
                decision(players[1]);
            }
        }
    }

    private void getFlopAction() {
        double tempPot = pot;
        board = new Board();
        board.addCard(0, deck.deal());
        board.addCard(1, deck.deal());
        board.addCard(2, deck.deal());

        System.out.println("\nThe flop is " + board.getCard(0).toString() + " " + board.getCard(1).toString() + " " + board.getCard(2).toString());
        decisionWithCheck(players[1], "flop");

        do {
            if (tempPot == pot) {
                decisionWithCheck(players[0], "flop");
            }
            decision(players[1]);
            decision(players[0]);
        } while (players[0].getStack() != players[1].getStack());
    }

    private void getTurnAction() {
        System.out.println("hello");
    }

    private void getRiverAction() {

    }

    private void showDown() {

    }

    private void raise(Player p) {
        while (true) {
            System.out.println("Enter total raise size: ");
            double raise = s.nextDouble();
            s.nextLine();
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
        if (p.equals(players[0])) {
            System.out.println("Player 1 wins " + pot + ".");
            players[1].setStack(players[1].getStack() + pot);
            start();
        } else {
            System.out.println("Player 0 wins " + pot + ".");
            players[1].setStack(players[0].getStack() + pot);
            start();
        }
    }

    private void bet(Player p) {
        while (true) {
            System.out.println("Enter size of bet: ");
            double bet = s.nextDouble();
            s.nextLine();
            if (bet < 1.0) {
                System.out.println("Invalid bet size.");
                continue;
            }
            pot += bet;
            p.setPutInPot(p.getPutInPot() + bet);
            p.setStack(p.getStack() - bet);
            break;
        }
    }

    private void decision(Player p) {
        System.out.println("\n" + p.getName() + "'s action. Pot = " + pot + " Stack = " + p.getStack());
        String decision;
        while (true) {
            double callAmount = pot - 2 * p.getPutInPot();
            System.out.println("Enter your decision: ");
            System.out.println("[c] - call ("+ callAmount +")");
            System.out.println("[r] - raise");
            System.out.println("[f] - fold");
            System.out.println("");

            decision = s.nextLine();
            switch(decision) {
                case "c": call(p, callAmount);
                          return;
                case "r": raise(p);
                          return;
                case "f": fold(p);
                          return;
                default: System.out.println("Invalid option.");
                         continue;
            }
        }
    }

    private void decisionWithCheck(Player p, String street) {
        System.out.println("\n" + p.getName() + "'s action. Pot = " + pot + " Stack = " + p.getStack());
        String decision;
        while (true) {
            System.out.println("Enter your decision: ");
            System.out.println("[x] - check");
            System.out.println("[b] - bet");

            decision = s.nextLine();
            switch(decision) {
                case "b": bet(p);
                          return;
                case "x": if (p.equals(players[1])) { 
                            return;
                          } else if (street.equals("flop")) {
                              getTurnAction();
                          } else if (street.equals("turn")) {
                              getRiverAction();
                          } else if (street.equals("river")) {
                              showDown();
                          }
                default: System.out.println("Invalid option.");
                         continue;
            }
        }
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