package csct3434.ipo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IpoApplication {

	public static void main(String[] args) {
		SpringApplication.run(IpoApplication.class, args);
	}

}
