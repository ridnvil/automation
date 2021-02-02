package com.bank.produces.automationproducer;

import com.bank.produces.automationproducer.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutomationProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomationProducerApplication.class, args);

		Config.TOPICDEVICES.add("6f8acaf20403");
		Config.TOPICDEVICES.add("8445ca230403");
		Config.TOPICDEVICES.add("9b79caa50401");
	}

}
