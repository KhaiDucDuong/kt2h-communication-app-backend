package hcmute.hhkt.messengerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//disable security
//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

@SpringBootApplication
public class ComicLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComicLibraryApplication.class, args);
	}

}
