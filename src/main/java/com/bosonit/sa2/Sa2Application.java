package com.bosonit.sa2;

import com.bosonit.sa2.application.StorageProperties;
import com.bosonit.sa2.application.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Sa2Application {

	public static String ruta;

	public static void main(String[] args) {
		SpringApplication.run(Sa2Application.class, args);
		if(args.length>0) {
			ruta = args[0];
//			System.out.println("Hola desde main" + ruta);
		}

	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
