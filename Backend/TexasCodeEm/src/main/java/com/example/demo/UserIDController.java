package com.example.demo;

import java.util.Random;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserIDController {
	
	private Random rand;
	private String ID;
	
	@RequestMapping("")
	public String getUserID() {
		ID = "";
		rand = new Random();
		
		for (int i = 0; i < 4; i++) {
			ID += rand.nextInt(10);
		}
		return ID;
	}
}