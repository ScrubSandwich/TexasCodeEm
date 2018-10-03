package com.epiclabs.texascodeem;
import java.util.Random;

public class Utility {
	
	private static Random rand;
	
	public static String makeID() {
		rand = new Random();
		String ID = "";
		for (int i = 0; i < 4; i++) {
			ID += rand.nextInt(10);
		}
		return ID;
	}
}
