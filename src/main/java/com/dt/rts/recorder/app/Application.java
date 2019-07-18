package com.dt.rts.recorder.app;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.dt.rts.recorder")
@EntityScan("com.dt.rts.recorder.entity")
@EnableJpaRepositories("com.dt.rts.recorder.repo")
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        
        for(String arg:args) {
            if(arg.indexOf("virtualize") > -1) {
            	Constants.VIRTUALIZE = true;
            }
        	System.out.println(arg);
        }
    }
    
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }
}
