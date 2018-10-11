package com.epiclabs.texascodeem;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import com.epiclabs.texascodeem.*;

@RestController
@SpringBootApplication
public class TexasCodeEmApplication {

	@Autowired
	private GameController gameController;
	
	@CrossOrigin
	@RequestMapping(value = "/api/generateUserID", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> generateUserID(@RequestBody Map<String, Object> body) {
		return gameController.generateUserID(body);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TexasCodeEmApplication.class, args);
	}
}
