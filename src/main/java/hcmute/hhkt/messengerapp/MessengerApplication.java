package hcmute.hhkt.messengerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//disable security
//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

@SpringBootApplication
@EnableScheduling
public class MessengerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessengerApplication.class, args);
	}

}
