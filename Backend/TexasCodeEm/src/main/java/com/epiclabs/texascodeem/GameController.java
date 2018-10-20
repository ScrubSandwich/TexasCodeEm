package com.epiclabs.texascodeem;

import java.util.HashMap;
import java.util.Map;

import com.epiclabs.texascodeem.api.Card;
import com.epiclabs.texascodeem.api.Deck;
import com.epiclabs.texascodeem.api.Player;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.epiclabs.texascodeem.api.Utility;

@Service
public class GameController {

	private static Deck deck  = new Deck();
	private static int NUMBER_OF_CARDS = 2;
	private static int NUMBER_OF_PLAYERS = 2;

	public Map<String, Object> isReady() {
		Map<String, Object> response = new HashMap<>();

		boolean isReady = PlayerController.getPlayersList().size() >= NUMBER_OF_PLAYERS;

		response.put("status", HttpStatus.OK);
		response.put("isReady", isReady);

		if (isReady) {
			Map<String, Object> cardsMap = new HashMap<>();
			Card[] cards = new Card[NUMBER_OF_CARDS];

			for (int i = 0; i < NUMBER_OF_CARDS; i++) {
				cards[i] = deck.deal();
			}

			for (int i = 0; i < NUMBER_OF_CARDS; i++) {
				cardsMap.put("card" + i, cards[i].toString());
			}

			response.put("cards", cardsMap);
		}

		return (response);
	}

	public Map<String, Object> generateUserID(Map<String, Object> body) {
		Map<String, Object> response = new HashMap<>();

		String username = "usernameTest"; // = body.get("username").toString();
		String id = Utility.makeID();
		PlayerController.addPlayer(new Player(username, id));

		response.put("status", HttpStatus.OK);
		response.put("userID", id);
		return response;
	}

	public static void shuffle() {
		deck.shuffle();
	}
}
