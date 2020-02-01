package pbouda.nativeimage.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@EnableWebFlux
@SpringBootApplication
@EnableReactiveMongoRepositories
public class Application implements WebFluxConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

