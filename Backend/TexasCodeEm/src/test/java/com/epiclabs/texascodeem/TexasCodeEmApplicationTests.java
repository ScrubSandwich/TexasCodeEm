package com.epiclabs.texascodeem;

import com.epiclabs.texascodeem.api.Values;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.Any;
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

	@Test
	public void generateUserIdTest() {
		Map<String, Object> body = new HashMap<>();
		body.put("username", "test");
		
		Map<String, Object> response = application.generateUserID(body);

		assertEquals(HttpStatus.OK, response.get("status"));
		assertEquals(Values.ID_LENGTH, response.get("userID").toString().length());
	}

}
