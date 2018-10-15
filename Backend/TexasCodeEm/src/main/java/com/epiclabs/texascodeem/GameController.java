package com.epiclabs.texascodeem;

import java.util.HashMap;
import java.util.Map;

import com.epiclabs.texascodeem.api.Player;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.epiclabs.texascodeem.api.Utility;

@Service
public class GameController {
	
	public Map<String, Object> generateUserID(@RequestParam Map<String, Object> body) {
		Map<String, Object> response = new HashMap<>();

		String username = "usernameTest"; // = body.get("username").toString();
		String id = Utility.makeID();
		PlayerController.addPlayer(new Player(username, id));

		response.put("status", HttpStatus.OK);
		response.put("userID", id);
		return response;
	}
}
