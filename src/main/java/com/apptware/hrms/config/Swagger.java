package com.apptware.hrms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {

  @Bean
  public OpenAPI customConfig() {
    return new OpenAPI()
        .info(new Info().title("HRMS API's").description("By Apptware"))
        .servers(List.of(new Server().url("http://localhost:8080").description("local"),
            new Server().url("https://hrms-au5y.onrender.com").description("prod")));
  }
}
