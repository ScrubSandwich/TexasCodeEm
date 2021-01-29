package com.epiclabs.texascodeem;

import java.lang.String;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RestController
@SpringBootApplication
public class TexasCodeEmApplication {

	@CrossOrigin
	@RequestMapping(value = "/api/initializeGame", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initializeGame() {
		return GameController.getInstance().initializeGame();
	}

	@CrossOrigin
	@RequestMapping(value = "/api/generateUserId", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> generateUserID(@RequestBody Map<String, Object> body) {
		return GameController.getInstance().generateUserID(body);
	}

	@CrossOrigin
	@RequestMapping(value = "api/isReady", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> isReady(@RequestBody Map<String, Object> body) {
		return GameController.getInstance().isReady(body);
	}

	@CrossOrigin
	@RequestMapping(value = "api/whoseTurn", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> whoseTurn() {
		return GameController.getInstance().whoseTurn();
	}

	@CrossOrigin
	@RequestMapping(value = "api/acceptTurn", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> acceptTurn(@RequestBody Map<String, Object> body) {
		return GameController.getInstance().acceptTurn(body);
	}

	@CrossOrigin
	@RequestMapping(value = "/api/getPlayers", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getPlayers() {
		return GameController.getInstance().getPlayers();
	}

	@CrossOrigin
	@RequestMapping(value = "/api/getBoard", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getBoard() {
		return GameController.getInstance().getBoard();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TexasCodeEmApplication.class, args);
	}
}
