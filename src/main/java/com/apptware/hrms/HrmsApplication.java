package com.apptware.hrms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class HrmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrmsApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigure() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping("/**") // Applies to all paths
						.allowedOrigins("https://employee-registeration-form.vercel.app", "https://hrms-poc-employee-management.vercel.app","http://localhost:5173","http://localhost:5001")
						.allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*");
			}
		};
	}
}

