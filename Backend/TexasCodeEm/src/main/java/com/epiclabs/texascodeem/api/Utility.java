package com.epiclabs.texascodeem.api;

import com.epiclabs.texascodeem.PlayerController;

import java.util.List;
import java.util.Random;

public class Utility {
	
	public static String makeID() {
		Random rand = new Random();
		String ID = "";

		for (int i = 0; i < 4; i++) {
			ID += rand.nextInt(Values.ID_LENGTH);
		}

		return ID;
	}

	public static boolean isValidUserId(int id) {
		List<Player> players = PlayerController.getPlayersList();

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getIdInt() == id) { return true; }
		}

		return false;
	}
}
