package de.unistuttgart.iste.pe2ws20.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@OpenAPIDefinition(info = @Info(title = " PE II WS2021 REST API", version = "1.4",
		description = "The REST API Documentation. Methods to " +
				"create, read, update and delete each entity has been implemented."))

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// enable cross-origin resource sharing (CORS)
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// allow CORS requests for all resources and HTTP methods from the frontend origin
				registry.addMapping("/**")
						.allowedMethods("OPTIONS", "HEAD", "GET", "PUT", "POST", "DELETE");
					//	.allowedOrigins("http://localhost:8000");
			}
		};
	}
}
