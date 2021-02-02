package com.bank.database.automationdatabaseservices;

import com.bank.database.automationdatabaseservices.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutomationDatabaseServicesApplication {
	private static final Logger log = LoggerFactory.getLogger(AutomationDatabaseServicesApplication.class);
	public static void main(String[] args) {
		log.info("Automation API By AZ Dev.");
		SpringApplication.run(AutomationDatabaseServicesApplication.class, args);
		Config.userToken.add("dvgqaAMcSz2kO82en2B_ms:APA91bGTKUttiLriw9VA-yNj6gygRIu_lMm53wY8KdE1QhitkWUpI-sti5OzjmzEcD6NOwVBTXGG0pT5H7imxW98Eg7nLuVNSJ2gGhEkvZ8tcFi4V_6B7GIPapOkl3y-NJMadTTR9cI7");
		Config.userToken.add("cqQqQ2QjSc6JiTY87Qd5am:APA91bGbJljIt_ED6-N4zs_EzwReh3CGkCv3PRSvUpJaEJ3lMgdecFWab12B-G2tarqewpSvtXFvIwr3XzOiD7pLwGQddPrZNdWQPDMLw2ZrizxM4DA3xn3ZWl1CqC-Iaz2al0EddVDu");
	}
}
