package pt.com.hugodias.converters.application;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
	  public static void main(String [] args) {
		  Locale.setDefault(Locale.US);
	        SpringApplication.run(BatchConfiguration.class, args);
	  }
	}