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
	private static int currentBoard = Values.HAND_OVER;
	private static Card[] boardCards = new Card[Values.NUMBER_OF_BOARD_CARDS];
	private static int closingAction = -1;
	private static int currentBet = 0;
	private static int pot = 0;

	public Map<String, Object> isReady(Map<String, Object> body) {
		Map<String, Object> response = new HashMap<>();

		List<Player> players = PlayerController.getPlayersList();
        int userId = Integer.parseInt(body.get("userId").toString());
		boolean isReady = players.size() >= Values.NUMBER_OF_PLAYERS;

		if (isReady) {
		    currentBoard = Values.PREFLOP;
		    if (Utility.isValidUserId(userId)) {
		        if (!PlayerController.hasCards(userId)) {
                    setCurrentPlayerTurn();
                    dealCardsToPlayerAndUpdateResponse(response, userId);
                    PlayerController.setInHand(userId, true);
                    setPot();
                    printGameInfo();
                } else {
                    response.put("message", "User already has cards");
                }
            } else {
                response.put("message", "Invalid user ID");
            }
		}

		response.put("status", HttpStatus.OK);
		response.put("isReady", isReady);
		response.put("players", PlayerController.getPlayers());

		return (response);
	}

	private void dealCardsToPlayerAndUpdateResponse(Map<String, Object> response, int id) {
        Map<String, Object> cardsMap = new HashMap<>();
        Card[] cards = new Card[Values.NUMBER_OF_CARDS];

        // Deal out the cards for this player
        for (int i = 0; i < Values.NUMBER_OF_CARDS; i++) {
            cards[i] = deck.deal();
        }

        // Add the player's cards into the response
        for (int i = 0; i < Values.NUMBER_OF_CARDS; i++) {
            cardsMap.put("card" + i, cards[i].toString());
        }

        PlayerController.addCards(id, cards);
        response.put("cards", cardsMap);
    }

	public Map<String, Object> whoseTurn(Map<String, Object> body) {
		Map<String, Object> response = new HashMap<>();
		String userId = body.get("userId").toString();
		//List<Player> players = PlayerController.getPlayersList();

		response.put("status", HttpStatus.OK);
		response.put("players", PlayerController.getPlayers());

		return response;
	}

	public Map<String, Object> acceptTurn(Map<String, Object> body) throws Exception {
		Map<String, Object> response = new HashMap<>();
		int userId = Integer.parseInt(body.get("userId").toString());
		boolean roundComplete = false;

		if (userId != currentPlayerTurn) {
			response.put("status", HttpStatus.METHOD_NOT_ALLOWED);
			response.put("message", "It is not your turn.");
			return response;
		}

		String action;
		try {
            action = body.get("action").toString();
        } catch (Exception ex){
            response.put("status", HttpStatus.NO_CONTENT);
            response.put("message", "You need to supply an action");
            return response;
        }

		if (action.equals("check")) {

		    if (closingAction == userId) {
		        roundComplete = true;
            } else {
                if (currentBet > 0) {
                    response.put("message", "You cannot check if you are not the one who bet");
                    response.put("status", HttpStatus.BAD_REQUEST);
                }
            }

            System.out.println(PlayerController.getName(userId) + " has checked ");
            System.out.println("========================");
		} else if (action.equals("bet")) {
            if (currentBet > 0) {
                response.put("message", "You cannot bet with a current bet out. You can raise, however");
                response.put("status", HttpStatus.BAD_REQUEST);
            }

            int amount = Integer.parseInt(body.get("amount").toString());
            currentBet = amount;
            closingAction = userId;
            pot += amount;
            PlayerController.setMoneyInPot(userId, amount);

            System.out.println(PlayerController.getName(userId) + " has bet " + amount);
            System.out.println("========================");
        } else if (action.equals("call")) {

			int remainingStack = PlayerController.subtractStack(userId, currentBet);

			// This will add only what the player had left to the pot
			if (remainingStack < 0) {
				pot += (currentBet + remainingStack);
			} else {
				pot += currentBet - PlayerController.getMoneyInPot(userId);
				PlayerController.setMoneyInPot(userId, currentBet);
			}

            if (closingAction == userId) {
                roundComplete = true;
            }

            System.out.println(PlayerController.getName(userId) + " has called " + currentBet);
            System.out.println("========================");

		} else if (action.equals("raise")) {

			int raiseToTotal = Integer.parseInt(body.get("raiseSize").toString());
			pot += currentBet + raiseToTotal;
			currentBet = raiseToTotal;
			closingAction = userId;

            System.out.println(PlayerController.getName(userId) + " has raised to " + raiseToTotal);
            System.out.println("========================");

		} else if (action.equals("fold")) {
			PlayerController.setInHand(userId, false);
			if (closingAction == userId) {
			    roundComplete = true;
            }

            System.out.println(PlayerController.getName(userId) + " has folded");
            System.out.println("========================");
		} else {
			response.put("status", HttpStatus.NOT_ACCEPTABLE);
			response.put("message", "Expected action to equal bet, call, raise, check or fold");
			return response;
		}

		if (!roundComplete) {
            boolean handOver = incrementPlayerTurn();
        } else {
            currentBet = 0;
            incrementStreet();
            boolean handOver = incrementPlayerTurn();
        }
        printGameInfo();

		response.put("status", HttpStatus.OK);
        response.put("street", getStreet());
		return response;
	}

	public Map<String, Object> generateUserID(Map<String, Object> body) {
		Map<String, Object> response = new HashMap<>();

		String username = body.get("username").toString();
		String id = Utility.makeID();
		Player newPlayer = new Player(username, id);
		if (currentBoard == Values.HAND_OVER) { newPlayer.setInHand(true); }
		PlayerController.addPlayer(newPlayer);

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
                        Player nextPlayer = players.get(j);

                        if (nextPlayer.inHand()) {
                            currentPlayerTurn = nextPlayer.getIdInt();
                            return false;
                        }

                        if (j == players.size() - 1) {
                            j = 0;
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

	public static boolean incrementStreet() {
		if (++currentBoard > Values.RIVER) {
		    currentBoard = Values.HAND_OVER;
		    return false;
		}

		dealBoard(currentBoard);

		return true;
	}

	private static void dealBoard(int board) {
	    if (board == Values.FLOP) {
	        Card c = deck.deal();
            System.out.println(c.toString());
            boardCards[0] = c;
            c = deck.deal();
            System.out.println(c.toString());
            boardCards[1] = c;
            c = deck.deal();
            System.out.println(c.toString());
            boardCards[2] = c;
        } else if (board == Values.TURN) {
            boardCards[3] = deck.deal();
        } else if (board == Values.RIVER) {
            boardCards[4] = deck.deal();
        } else {
            // Do nothing
        }
    }

	private static String getCurrentPlayersTurnName() {
        List<Player> players = PlayerController.getPlayersList();
        Player player;

        for (int i = 0; i < players.size(); i++) {
            player = players.get(i);
            if (player.getIdInt() == currentPlayerTurn) { return player.getName(); }
        }

        return "No one's turn";
    }

    private static String getStreet() {
	    switch (currentBoard) {
            case Values.PREFLOP:
	            return "Preflop";
            case Values.FLOP:
                return "Flop";
            case Values.TURN:
                return "Turn";
            case Values.RIVER:
                return "River";
            default:
                return "Hand Over";
        }
    }

    private static String getPlayersInHand() {
        List<Player> players = PlayerController.getPlayersList();
        String response = "";

        for (int i = 0; i < players.size(); i++) {
            if (!players.get(i).inHand()) { continue; }

            response += players.get(i).getName();
            if (i != players.size() - 1) { response += ", "; }
        }

        return response;
    }

	private void printGameInfo() {
        System.out.println("Current Player Turn: " + getCurrentPlayersTurnName());
        System.out.println("Current Street: " + getStreet());
        System.out.println("Current Bet: " + currentBet);
        System.out.println("Current Pot: " + pot);
        System.out.println("Players is hand: " + getPlayersInHand());

        if (currentBoard > 0) {
            System.out.print("Cards: ");
            System.out.print(boardCards[0] + " ");
            System.out.print(boardCards[1] + " ");
            System.out.print(boardCards[2] + " ");
            System.out.print(boardCards[3] + " ");
            System.out.println(boardCards[4]);
        }
        System.out.println("========================");
    }

    private void setPot() {
        List<Player> players = PlayerController.getPlayersList();
        int numberOfPlayers = players.size();

        if (numberOfPlayers == 2) {
            Player p1 = players.get(0);
            Player p2 = players.get(1);

            // Make sure the player has not already put the SB in
            if (p1.getMoneyInPot() != Values.SB) {
                p1.decrementStack(Values.SB);
                p1.setMoneyInPot(Values.SB);
                pot += Values.SB;
            }

            // Make sure the player has not already put the BB in
            if (p2.getMoneyInPot() != Values.BB) {
                p2.decrementStack(Values.BB);
                p2.setMoneyInPot(Values.BB);
                pot += Values.BB;
                closingAction = p2.getIdInt();
            }
        }

        if (numberOfPlayers > 2) {
            Player p1 = players.get(1);
            Player p2 = players.get(2);

            // Make sure the player has not already put the SB in
            if (p1.getMoneyInPot() != Values.SB) {
                p1.decrementStack(Values.SB);
                p1.setMoneyInPot(Values.SB);
                pot += Values.SB;
            }

            // Make sure the player has not already put the BB in
            if (p2.getMoneyInPot() != Values.BB) {
                p2.decrementStack(Values.BB);
                p2.setMoneyInPot(Values.BB);
                pot += Values.BB;
                closingAction = p2.getIdInt();
            }
        }

        currentBet = Values.BB;
    }
}
