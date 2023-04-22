package safe.safe;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class SafeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeApplication.class, args);
	}

}
