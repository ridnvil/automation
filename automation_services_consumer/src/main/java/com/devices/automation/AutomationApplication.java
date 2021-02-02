package com.devices.automation;

import com.devices.automation.config.Config;
import com.devices.automation.services.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutomationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomationApplication.class, args);
		String topic = "";
		for (String ar: args){
			topic = ar;
		}

		Config.KAFKATOPIC = "9b94ca1b0401";

		Consumer consumer = new Consumer();
		consumer.consume();
	}
}
