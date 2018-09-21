public class Game {

    private static int PLAYER_SIZE = 2;
    private Player[] players;
	private Deck;

    private boolean running = false;

    public void Game(Player[] players) {
        players = new Player[players.length];
		deck = new Deck();
        deck.shuffle();
    }

    public void start() {
        while (running) {
            dealToPlayers();
            getFlopAction();
        }
    }

    private void dealToPlayers() {
		for (int i = 0; i < PLAYER_SIZE; i++) {
            players[i].setCard(1, deck.deal());
            players[i].setCard(2, deck.deal());

            System.out.println("Player" + i + "'s Cards: " + players[i].toString());
		}
    }

    private void getFlopAction() {
        
    }

    public static void main(String[] args) {
      Player[] players = new Player[PLAYER_SIZE];

      for (int i = 0; i < PLAYER_SIZE; i++) {
          players[i] = "Player" + (i + 1);
      }

      Game game = new Game(players);
      game.start();
    }
}