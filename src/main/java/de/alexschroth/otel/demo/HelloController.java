package de.alexschroth.otel.demo;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

	record Greeting(String message) {
	}

	@GetMapping
	Greeting hello(@RequestParam Optional<String> name) {
		final var user = name.orElse("unknown user");
		LOGGER.info("Sending a warm welcome to \"{}\"", user);

		return new Greeting("Hello, " + user + "!");
	}
}
