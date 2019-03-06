package com.epiclabs.texascodeem;

import java.util.HashMap;
import java.util.Map;
import com.epiclabs.texascodeem.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GameController {

	private static Deck deck  = new Deck();

	public Map<String, Object> isReady() {
		Map<String, Object> response = new HashMap<>();

		boolean isReady = PlayerController.getPlayersList().size() >= Values.NUMBER_OF_PLAYERS;

		response.put("status", HttpStatus.OK);
		response.put("isReady", isReady);

		if (isReady) {
			Map<String, Object> cardsMap = new HashMap<>();
			Card[] cards = new Card[Values.NUMBER_OF_CARDS];

			for (int i = 0; i < Values.NUMBER_OF_CARDS; i++) {
				cards[i] = deck.deal();
			}

			for (int i = 0; i < Values.NUMBER_OF_CARDS; i++) {
				cardsMap.put("card" + i, cards[i].toString());
			}

			response.put("cards", cardsMap);
		}

		return (response);
	}

	public Map<String, Object> whoseTurn(Map<String, Object> body) {
		String userId = body.get("userId").toString();
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
