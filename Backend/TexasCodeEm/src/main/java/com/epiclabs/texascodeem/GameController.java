package com.epiclabs.texascodeem;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.epiclabs.texascodeem.api.Utility;

@Service
public class GameController {
	
	public Map<String, Object> generateUserID(@RequestParam Map<String, Object> body) {
		Map<String, Object> response = new HashMap<String, Object>();
		//String user = body.get("username").toString();
		
		String ID = Utility.makeID();
		response.put("status", HttpStatus.OK);
		response.put("userID", ID);
		return response;
	}
	
}
