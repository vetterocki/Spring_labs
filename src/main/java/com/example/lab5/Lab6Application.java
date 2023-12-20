package com.example.lab5;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(title = "Forum API", version = "1.0",
        license = @License(name = "Apache-2.0 license",
            url = "https://www.apache.org/licenses/LICENSE-2.0"),
        description = """
            An API for the application implementing features of user`s forum."""
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "test server")
    }
)
public class Lab6Application {

  public static void main(String[] args) {
    SpringApplication.run(Lab6Application.class, args);
  }

}
