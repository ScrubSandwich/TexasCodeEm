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

		boolean isReady = isReady();

		if (isReady) {
			setCurrentPlayerTurn();

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
			PlayerController.setInHand(userId, true);
		}

		response.put("status", HttpStatus.OK);
		response.put("isReady", isReady);
		response.put("players", PlayerController.getPlayers());

		return (response);
	}

	public boolean isReady() {
		List<Player> players = PlayerController.getPlayersList();
		return players.size() >= Values.NUMBER_OF_PLAYERS;
	}

	public Map<String, Object> whoseTurn(Map<String, Object> body) {
		Map<String, Object> response = new HashMap<>();
		String userId = body.get("userId").toString();
		//List<Player> players = PlayerController.getPlayersList();

		response.put("status", HttpStatus.OK);
		response.put("players", PlayerController.getPlayers());

		return response;
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
		    // Do nothing
		} else if (action.equals("call")) {
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

        boolean handOver = incrementPlayerTurn();

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

	private static void setCurrentPlayerTurn() {
		List<Player> players = PlayerController.getPlayersList();
		int len = players.size();

		if (len == 2 || len == 3) {
			currentPlayerTurn = players.get(0).getIdInt();
		} else {
			currentPlayerTurn = players.get(3).getIdInt();
		}

	}

	// Returns true if the hand is over. False if hand is still going
	public static boolean incrementPlayerTurn() {
		List<Player> players = PlayerController.getPlayersList();
		Player player;
		int userId;

		for (int i = 0; i < players.size(); i++) {
			player = players.get(i);
			userId = player.getIdInt();

			if (userId == currentPlayerTurn) {
				if (i == players.size() - 1) {
				    // Loop through to make sure the next player is in the hand
                    for (int j = 0; j < players.size(); j++) {
                        Player nextPlayer = players.get(j);

                        if (nextPlayer.inHand() && j != players.size() - 1) {
                            currentPlayerTurn = nextPlayer.getIdInt();
                            return false;
                        }
                    }
				} else {
                    // Loop through to make sure the next player is in the hand
                    for (int j = i + 1; true; j++) {
//                        if (j == players.size() - 1) {
//                            j = 0;
//                        }

                        Player nextPlayer = players.get(j);

                        if (nextPlayer.inHand()) {
                            currentPlayerTurn = nextPlayer.getIdInt();
                            return false;
                        }
                    }
				}
                // If here is reached, then the hand is over
                return true;
			}
		}

        // Should never reach here
        return true;
	}

	public static int getCurrentPlayerTurn() {
		return currentPlayerTurn;
	}

	public static boolean incrementBoard() {
		if (++currentBoard > Values.RIVER) {
		    currentBoard = Values.PREFLOP;
		    return false;
		}
		return true;
	}

	public static int getBoard() {
		return currentBoard;
	}
}
