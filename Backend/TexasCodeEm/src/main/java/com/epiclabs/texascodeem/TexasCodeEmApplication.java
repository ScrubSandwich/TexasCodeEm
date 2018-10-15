package com.epiclabs.texascodeem;

import java.lang.String;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class TexasCodeEmApplication {

	@Autowired
	private GameController gameController;

	@Autowired
	private PlayerController playerController;
	
	@CrossOrigin
	@RequestMapping(value = "/api/generateUserId", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> generateUserID(@RequestBody Map<String, Object> body) {
		return gameController.generateUserID(body);
	}

	@CrossOrigin
	@RequestMapping(value = "/api/getPlayers", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getPlayers() {
		return playerController.getPlayers();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TexasCodeEmApplication.class, args);
	}
}