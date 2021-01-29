package com.epiclabs.texascodeem;

import com.epiclabs.texascodeem.api.Player;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurnController {

    private static TurnController instance;

    private int currentPlayerTurn;

    private TurnController() {
        currentPlayerTurn = -1;
    }

    public static TurnController getInstance() {
        if (instance == null) {
            instance = new TurnController();
        }

        return instance;
    }

    public int whoseTurn() {
        return currentPlayerTurn;
    }

    public int setTurn() {
        List<Player> players = PlayerController.getInstance().getPlayers();
        int numberOfPlayers = PlayerController.getInstance().getNumberOfPlayers();

        if (numberOfPlayers == 2 || numberOfPlayers == 3) {
            currentPlayerTurn = players.get(0).getIdInt();
        } else {
            currentPlayerTurn = players.get(3).getIdInt();
        }

        return currentPlayerTurn;
    }

    public int setTurnToUser(int userId) {
        currentPlayerTurn = userId;

        return currentPlayerTurn;
    }

    public boolean incrementTurn() {
        List<Player> players = PlayerController.getInstance().getPlayers();
        Player player;
        int userId;

        for (int i = 0; i < players.size(); i++) {
            player = players.get(i);
            userId = player.getIdInt();

            if (userId == whoseTurn()) {
                if (i == players.size() - 1) {
                    // Loop through to make sure the next player is in the hand
                    for (int j = 0; j < players.size(); j++) {
                        Player nextPlayer = players.get(j);

                        if (nextPlayer.inHand() && j != players.size() - 1) {
                            setTurnToUser(nextPlayer.getIdInt());
                            return false;
                        }
                    }
                } else {
                    // Loop through to make sure the next player is in the hand
                    for (int j = i + 1; true; j++) {
                        if (j == players.size()) {
                            return false;
                        }

                        Player nextPlayer = players.get(j);

                        if (nextPlayer.inHand()) {
                            setTurnToUser(nextPlayer.getIdInt());
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

    public Map<String, Object> acceptTurn(Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        int userId = Integer.parseInt(body.get("userId").toString());

        if (userId != whoseTurn()) {
            response.put("status", HttpStatus.METHOD_NOT_ALLOWED);
            response.put("message", "It is not your turn.");
            return response;
        }

        String action = body.get("action").toString();

        if (action.equals("check")) {
            // Do nothing
        } else if (action.equals("call")) {
            int remainingStack = PlayerController.getInstance().subtractStack(userId, BoardController.getInstance().getCurrentBet());

            // This will add only what the player had left to the pot
            if (remainingStack < 0) {
                BoardController.getInstance().incrementPot(BoardController.getInstance().getCurrentBet() + remainingStack);
            } else {
                BoardController.getInstance().incrementPot(BoardController.getInstance().getCurrentBet());
            }
        } else if (action.equals("raise")) {
            int raiseToTotal = Integer.parseInt(body.get("raiseSize").toString());
            BoardController.getInstance().incrementPot(BoardController.getInstance().getCurrentBet() + raiseToTotal);
            BoardController.getInstance().setCurrentBet(raiseToTotal);
            BoardController.getInstance().setClosingAction(userId);
        } else if (action.equals("fold")) {
            PlayerController.getInstance().setInHand(userId, false);
        } else {
            response.put("status", HttpStatus.NOT_ACCEPTABLE);
            response.put("message", "Expected action to equal call, raise, check or fold");
            return response;
        }

        incrementTurn();

        response.put("status", HttpStatus.OK);
        return response;
    }
}
