package gov.ncbj.nomaten.datamanagementbackend;

import gov.ncbj.nomaten.datamanagementbackend.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Import(SwaggerConfiguration.class)
@SpringBootApplication
public class DataManagementBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataManagementBackendApplication.class, args);
	}

}
