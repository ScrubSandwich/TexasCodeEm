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
	private static int closingAction = -1;
	private static int currentBet = 0;
	private static int pot = 0;

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

	public Map<String, Object> acceptTurn(Map<String, Object> body) {
		Map<String, Object> response = new HashMap<>();
		int userId = Integer.parseInt(body.get("userId").toString());

		if (userId != currentPlayerTurn) {
			response.put("status", HttpStatus.METHOD_NOT_ALLOWED);
			response.put("message", "It is not your turn.");
			return response;
		}

		String action = body.get("action").toString();

		if (action.equals("check")) {
			incrementPlayerTurn();
		} else if (action.equals("call")) {
			incrementPlayerTurn();
			int remainingStack = PlayerController.subtractStack(userId, currentBet);

			// This will add only what the player had left to the pot
			if (remainingStack < 0) {
				pot += (currentBet + remainingStack);
			} else {
				pot += currentBet;
			}
		} else if (action.equals("raise")) {
			int raiseToTotal = Integer.parseInt(body.get("raiseSize").toString());
			pot += currentBet + raiseToTotal;
			currentBet = raiseToTotal;
			closingAction = userId;
		} else if (action.equals("fold")) {
			PlayerController.setInHand(userId, false);
		} else {
			response.put("status", HttpStatus.NOT_ACCEPTABLE);
			response.put("message", "Expected action to equal call, raise, check or fold");
			return response;
		}

		response.put("status", HttpStatus.OK);
		return response;
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

	public static void incrementPlayerTurn() {
		List<Player> players = PlayerController.getPlayersList();
		Player player;
		int userId;

		for (int i = 0; i < players.size(); i++) {
			player = players.get(i);
			userId = player.getIdInt();

			if (userId == currentPlayerTurn) {
				if (i == players.size() - 1) {
					currentPlayerTurn = players.get(0).getIdInt();
				} else {
					currentPlayerTurn = players.get(++i).getIdInt();
				}

				break;
			}
		}
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
