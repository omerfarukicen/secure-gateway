package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.Arrays;

import static org.springframework.boot.SpringApplication.run;


@SpringBootApplication
@ComponentScan("tr.com")
@Slf4j
//@EnableRedisWebSession
public class App {
    public static void main(String[] args) {
        log.info("Starting Project");
        Environment env = run(App.class, args).getEnvironment();
        log.info("""
                          APPLICATION INFO
                        ----------------------------------------------------------
                            Application '%s' is running! 
                            Port: '%s':
                            Profile(s): %s
                        ----------------------------------------------------------
                        """.formatted(
                        env.getProperty("spring.application.name"),
                        env.getProperty("server.port"),
                        Arrays.stream(env.getActiveProfiles()).findFirst().get()
                )
        );
    }

}
