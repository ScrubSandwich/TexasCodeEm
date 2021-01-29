package com.epiclabs.texascodeem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.epiclabs.texascodeem.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GameController {

	private static GameController instance;

	private GameController() {

	}

	public static GameController getInstance() {
		if (instance == null) {
			instance = new GameController();
		}
		return instance;
	}

	public Map<String, Object> initializeGame() {
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.OK);

		Deck.getInstance().shuffle();

		return response;
	}

	public Map<String, Object> generateUserID(Map<String, Object> body) {
		Map<String, Object> response = new HashMap<>();

		String username = body.get("username").toString();
		String desiredSeatPosition = "-1";
		String id = Utility.createID();

		PlayerController.getInstance().addPlayer(new Player(username, id, getSeatOfNewPlayer(Integer.parseInt(desiredSeatPosition))));

		response.put("status", HttpStatus.OK);
		response.put("userID", id);
		return response;
	}

	public Map<String, Object> isReady(Map<String, Object> body) {
		Map<String, Object> response = new HashMap<>();

		boolean isReady = isReady();

		if (isReady) {
			TurnController.getInstance().setTurn();

			Map<String, Object> cardsMap = new HashMap<>();
			Card[] cards = new Card[Values.NUMBER_OF_CARDS];
			int userId = Integer.parseInt(body.get("userId").toString());

			for (int i = 0; i < Values.NUMBER_OF_CARDS; i++) {
				cards[i] = Deck.getInstance().deal();
			}

			// Add the player's cards into the response
			for (int i = 0; i < Values.NUMBER_OF_CARDS; i++) {
				cardsMap.put("card" + i, cards[i].toString());
			}
			response.put("cards", cardsMap);

			// Add the cards to the player object
			PlayerController.getInstance().addCards(userId, cards);
			PlayerController.getInstance().setInHand(userId, true);

			// Set position to default
			try {
				PlayerController.getInstance().setPosition(userId);
			} catch (Exception e) {
				response.put("Error setting position to default", e.getMessage());
			}
		}

		response.put("status", HttpStatus.OK);
		response.put("isReady", isReady);
		response.put("players", PlayerController.getInstance().getPlayers());

		return (response);
	}

	public boolean isReady() {
		return PlayerController.getInstance().getNumberOfPlayers() >= Values.NUMBER_OF_PLAYERS;
	}

	public Map<String, Object> whoseTurn() {
		Map<String, Object> response = new HashMap<>();

		response.put("status", HttpStatus.OK);
		response.put("turn", TurnController.getInstance().whoseTurn());
		response.put("players", PlayerController.getInstance().getPlayers());

		return response;
	}

	public Map<String, Object> acceptTurn(Map<String, Object> body) {
		return TurnController.getInstance().acceptTurn(body);
	}

	// Returns true if the hand is over. False if hand is still going
	public static boolean incrementPlayerTurn() {
		return TurnController.getInstance().incrementTurn();
	}

	public static int getCurrentPlayerTurn() {
		return TurnController.getInstance().whoseTurn();
	}

	public int getPot() {
		return BoardController.getInstance().getPot();
	}

	// desiredSeatPosition == -1 if it is not specified
	public static int getSeatOfNewPlayer(int desiredSeatPosition) {
		boolean[] takenSeats = new boolean[Values.NUMBER_OF_SEATS];

		// If seat location is not specified
		if (desiredSeatPosition < 0) {
			List<Player> players = PlayerController.getInstance().getPlayers();

			for (int i = 0; i < players.size(); i++) {
				takenSeats[ players.get(i).getSeatNumber() ] = true;
			}

			// Seat for new player will be the first open one
			for (int i = 0 ; i < takenSeats.length; i++) {
				if (!takenSeats[i]) { return i; }
			}

			// All seats are taken
			return -1;
		}

		if (!takenSeats[desiredSeatPosition]) { return desiredSeatPosition; }

		return -1; //TODO throw new Exception("");
	}

	public Map<String, Object> getPlayers() {
		List<Player> players = PlayerController.getInstance().getPlayers();

		Map<String, Object> response = new HashMap<>();
		List< Map<String, Object> > playersObject = new ArrayList<>();

		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			Map<String, Object> playersMap = new HashMap<>();

			playersMap.put("name", player.getName());
			playersMap.put("id", player.getId());
			playersMap.put("stackSize", player.getStackSize());
			playersMap.put("seatNumber", player.getSeatNumber());
			playersMap.put("position", PositionController.getInstance().getPosition(Integer.parseInt(player.getId())));
			playersMap.put("inHand", player.inHand());

			boolean turn = Integer.parseInt(player.getId()) == GameController.getCurrentPlayerTurn();
			playersMap.put("turn", turn);

			playersObject.add(playersMap);
		}

		response.put("players", playersObject);
		response.put("status", HttpStatus.OK);

		return response;
	}

	public Map<String, Object> getBoard() {
		Card[] board = BoardController.getInstance().getBoard();
		Map<String, Object> response = new HashMap<>();

		for (int i = 0; i < board.length; i++) {
			Card[] flop = {board[0], board[1], board[2]};
			response.put("Flop", flop);
			response.put("Turn", board[3]);
			response.put("River", board[4]);
		}

		response.put("status", HttpStatus.OK);

		return response;

	}
}
