package com.epiclabs.texascodeem.api;
import java.util.Random;

public class Utility {
	
	public static String makeID() {
		Random rand = new Random();
		String ID = "";
		for (int i = 0; i < 4; i++) {
			ID += rand.nextInt(10);
		}
		return ID;
	}
}
