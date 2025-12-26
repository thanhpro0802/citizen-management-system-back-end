package com.citizen.management.citizen_management_system_back_end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CitizenManagementSystemBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(CitizenManagementSystemBackEndApplication.class, args);
	}

}
