package com.epiclabs.texascodeem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epiclabs.texascodeem.api.Card;
import com.epiclabs.texascodeem.api.Player;

import org.springframework.stereotype.Service;

@Service
public class PlayerController {

    private static PlayerController instance;

    private List<Player> players = new ArrayList<>();

    private PlayerController() {

    }

    public static PlayerController getInstance() {
        if (instance == null) {
            instance = new PlayerController();
        }

        return instance;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean addPlayer(Player p) {
        return players.add(new Player(p.getName(), p.getId()));
    }

    public boolean removePlayer(Player p) {
        return players.remove(p);
    }

    public boolean addCards(int playerId, Card[] cards) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            if (Integer.parseInt(player.getId()) == playerId) {
                player.setCards(cards);
                return true;
            }
        }

        return false;
    }

    // Returns remaining stack size; Negative if more than what is left is subtracted
    public int subtractStack(int userId, int amount) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            if (player.getIdInt() == userId) {
                int stack = player.getStackSize();
                return stack - amount;
            }
        }

        return Integer.MIN_VALUE;
    }

    public void setInHand(int userId, boolean inHand) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            if (player.getIdInt() == userId) {
                player.setInHand(inHand);
                return;
            }
        }
    }

    public void setPosition(int userId) throws Exception {
        PositionController.getInstance().findPosition(userId);
    }

    public int getNumberOfPlayers() {
        return this.players.size();
    }
}
