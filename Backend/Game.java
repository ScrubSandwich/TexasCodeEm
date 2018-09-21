import java.util.Scanner;

public class Game {

    private static final int PLAYER_SIZE = 2;
    private Player[] players;
	private Deck deck;

    private boolean running = true;
    private Scanner s;

    private double pot;
    private static final double smallBlind = 1.0;
    private static final double bigBlind = 2.0;

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

    private void dealToPlayers() {
		for (int i = 0; i < PLAYER_SIZE; i++) {
            players[i].setCard(1, deck.deal());
            players[i].setCard(2, deck.deal());

            //System.out.println("Player" + i + "'s Cards: " + players[i].getCard(1).toString());
            //System.out.println("Player" + i + "'s Cards: " + players[i].getCard(2).toString());
		}
    }

    private void getPreFlopAction() {
        for (int i = 0; i < PLAYER_SIZE; i++) {
            
        }
    }

    private void getFlopAction() {

    }

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