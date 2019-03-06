package com.epiclabs.texascodeem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.epiclabs.texascodeem.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GameController {

	private static Deck deck  = new Deck();
	private static int currentPlayerTurn = -1;
	private static int currentBoard = Values.PREFLOP;

	public Map<String, Object> isReady(Map<String, Object> body) {
		Map<String, Object> response = new HashMap<>();
		List<Player> players = PlayerController.getPlayersList();
		boolean isReady = players.size() >= Values.NUMBER_OF_PLAYERS;

		if (isReady) {
			setCurrentPlayerTurn(Integer.parseInt(players.get(0).getId()));

			Map<String, Object> cardsMap = new HashMap<>();
			Card[] cards = new Card[Values.NUMBER_OF_CARDS];
			int userId = Integer.parseInt(body.get("userId").toString());

			for (int i = 0; i < Values.NUMBER_OF_CARDS; i++) {
				cards[i] = deck.deal();
			}

			// Add the player's cards into the response
			for (int i = 0; i < Values.NUMBER_OF_CARDS; i++) {
				cardsMap.put("card" + i, cards[i].toString());
			}
			response.put("cards", cardsMap);

			// Add the cards to the player object
			PlayerController.addCards(userId, cards);
		}

		response.put("status", HttpStatus.OK);
		response.put("isReady", isReady);
		response.put("players", PlayerController.getPlayers());

		return (response);
	}

	public Map<String, Object> whoseTurn(Map<String, Object> body) {
		Map<String, Object> response = new HashMap<>();
		String userId = body.get("userId").toString();
		List<Player> players = PlayerController.getPlayersList();

		response.put("status", HttpStatus.OK);
		response.put("players", PlayerController.getPlayers());

		return null;
	}

	public Map<String, Object> generateUserID(Map<String, Object> body) {
		Map<String, Object> response = new HashMap<>();

		String username = body.get("username").toString();
		String id = Utility.makeID();
		PlayerController.addPlayer(new Player(username, id));

		response.put("status", HttpStatus.OK);
		response.put("userID", id);
		return response;
	}

	public static void shuffle() {
		deck.shuffle();
	}

	public static void setCurrentPlayerTurn(int userId) {
		currentPlayerTurn = userId;
	}

	public static int getCurrentPlayerTurn() {
		return currentPlayerTurn;
	}

	public static boolean incrementBoard() {
		if (++currentBoard > Values.RIVER) { return false; }
		return true;
	}

	public static int getBoard() {
		return currentBoard;
	}
}
