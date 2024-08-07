package dev.be.oneday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnedayApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnedayApplication.class, args);
	}

}
