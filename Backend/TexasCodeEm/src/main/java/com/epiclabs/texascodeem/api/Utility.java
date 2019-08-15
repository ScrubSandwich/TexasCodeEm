package com.epiclabs.texascodeem.api;

import java.util.Random;

public class Utility {
	
	public static String makeID() {
		Random rand = new Random();
		String ID = "";

		for (int i = 0; i < Values.ID_LENGTH; i++) {
			ID += rand.nextInt(9);
		}

		return ID;
	}
}
