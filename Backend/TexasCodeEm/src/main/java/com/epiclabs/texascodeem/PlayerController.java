package com.epiclabs.texascodeem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epiclabs.texascodeem.api.Card;
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

            playersObject.add(playersMap);
        }

        response.put("players", playersObject);
        return response;

    }

    public static void addPlayer(Player p) {
        players.add(p);
    }

    public static boolean removePlayer(Player p) {
        return (players.remove(p));
    }

    public static boolean addCards(int playerId, Card[] cards) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            if (Integer.parseInt(player.getId()) == playerId) {
                player.setCards()
            }
        }
    }
}
