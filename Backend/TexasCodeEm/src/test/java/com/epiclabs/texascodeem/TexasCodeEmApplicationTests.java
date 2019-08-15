package com.epiclabs.texascodeem;

import com.epiclabs.texascodeem.api.Values;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TexasCodeEmApplicationTests {

	@Autowired
	TexasCodeEmApplication application;

	@Autowired
	GameController gameController;

	@Test
	public void onePlayerDoesNotMakeGameReady() {
		Map<String, Object> body = new HashMap<>();
		body.put("username", "test");

		Map<String, Object> response = application.generateUserID(body);

		assertEquals(false, gameController.isReady());
	}

	@Test
	public void minimumNumberOfPlayersMakesGameReady() {
		Map<String, Object> body = new HashMap<>();
		body.put("username", "test");

		assertEquals(false, gameController.isReady());

		for (int i = 0; i < Values.NUMBER_OF_PLAYERS; i++) {
			application.generateUserID(body);
		}

		assertEquals(true, gameController.isReady());
	}

	@Test
	public void incrementTurnTwoPlayers() {
		Map<String, Object> body1 = new HashMap<>();
		Map<String, Object> body2 = new HashMap<>();

		body1.put("username", "player1");
		body2.put("username", "player2");

		Map<String, Object> response1 = application.generateUserID(body1);
		Map<String, Object> response2 = application.generateUserID(body2);

		String player1Id = (String) response1.get("userID");
		String player2Id = (String) response2.get("userID");

		Map<String, Object> bodyForReady1 = new HashMap<>();
		Map<String, Object> bodyForReady2 = new HashMap<>();

		bodyForReady1.put("userId", player1Id);
		bodyForReady2.put("userId", player2Id);

		gameController.isReady(bodyForReady1);
		gameController.isReady(bodyForReady2);

		assertEquals(Integer.parseInt(player1Id), gameController.getCurrentPlayerTurn());

		GameController.incrementPlayerTurn();
		assertEquals(Integer.parseInt(player2Id), gameController.getCurrentPlayerTurn());

		GameController.incrementPlayerTurn();
		assertEquals(Integer.parseInt(player1Id), gameController.getCurrentPlayerTurn());

		GameController.incrementPlayerTurn();
		assertEquals(Integer.parseInt(player2Id), gameController.getCurrentPlayerTurn());
	}

	@Test
	public void incremenetTurnThreePlayers() {
		Map<String, Object> body1 = new HashMap<>();
		Map<String, Object> body2 = new HashMap<>();
		Map<String, Object> body3 = new HashMap<>();

		body1.put("username", "player1");
		body2.put("username", "player2");
		body3.put("username", "player3");

		Map<String, Object> response1 = application.generateUserID(body1);
		Map<String, Object> response2 = application.generateUserID(body2);
		Map<String, Object> response3 = application.generateUserID(body3);

		String player1Id = (String) response1.get("userID");
		String player2Id = (String) response2.get("userID");
		String player3Id = (String) response3.get("userID");

		Map<String, Object> bodyForReady1 = new HashMap<>();
		Map<String, Object> bodyForReady2 = new HashMap<>();
		Map<String, Object> bodyForReady3 = new HashMap<>();

		bodyForReady1.put("userId", player1Id);
		bodyForReady2.put("userId", player2Id);
		bodyForReady3.put("userId", player3Id);

		gameController.isReady(bodyForReady1);
		gameController.isReady(bodyForReady2);
		gameController.isReady(bodyForReady3);

		assertEquals(Integer.parseInt(player1Id), gameController.getCurrentPlayerTurn());

		GameController.incrementPlayerTurn();
		assertEquals(Integer.parseInt(player2Id), gameController.getCurrentPlayerTurn());

		GameController.incrementPlayerTurn();
		assertEquals(Integer.parseInt(player3Id), gameController.getCurrentPlayerTurn());

		GameController.incrementPlayerTurn();
		assertEquals(Integer.parseInt(player1Id), gameController.getCurrentPlayerTurn());
	}

}
