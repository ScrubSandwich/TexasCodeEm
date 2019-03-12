package com.epiclabs.texascodeem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epiclabs.texascodeem.api.Card;
import com.epiclabs.texascodeem.api.Values;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.epiclabs.texascodeem.api.Player;

@Service
public class PlayerController {

    private static List<Player> players = new ArrayList<>();

    public static List<Player> getPlayersList() {
        return players;
    }

    public static Map<String, Object> getPlayers() {
        Map<String, Object> response = new HashMap<>();
        List< Map<String, Object> > playersObject = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Map<String, Object> playersMap = new HashMap<>();

            playersMap.put("name", player.getName());
            playersMap.put("id", player.getId());
            playersMap.put("stackSize", player.getStackSize());

            boolean turn = Integer.parseInt(player.getId()) == GameController.getCurrentPlayerTurn();
            playersMap.put("turn", turn);

            playersObject.add(playersMap);
        }

        response.put("players", playersObject);
        return response;
    }

    public static boolean addPlayer(Player p) {
        return ( players.add(new Player(p.getName(), p.getId())) );
    }

    public static boolean removePlayer(Player p) {
        return (players.remove(p));
    }

    public static boolean addCards(int playerId, Card[] cards) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            if (Integer.parseInt(player.getId()) == playerId) {
                player.setCards(cards);
                return true;
            }
        }

        return false;
    }

    // Returns remaining stack sizee; Negative if more than what is left is subtracted
    public static int subtractStack(int userId, int amount) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            if (player.getIdInt() == userId) {
                int stack = player.getStackSize();
                return stack - amount;
            }
        }

        return Integer.MIN_VALUE;
    }

    public static void setInHand(int userId, boolean inHand) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            if (player.getIdInt() == userId) {
                player.setInHand(false);
                return;
            }
        }
    }

    public static boolean hasCards(int id) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.getIdInt() == id) {
                if (player.hasCards()) { return true; }
            }
        }

        return false;
    }
}
