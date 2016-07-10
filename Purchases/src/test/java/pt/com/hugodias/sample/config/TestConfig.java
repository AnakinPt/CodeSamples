package pt.com.hugodias.sample.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pt.com.hugodias.sample.services.DatabaseService;
import pt.com.hugodias.sample.services.PurchaseService;
import pt.com.hugodias.sample.services.TestPurchaseServiceImpl;

@Configuration
public class TestConfig {
	
	@Bean
	public PurchaseService purchaseService(){
		return new TestPurchaseServiceImpl();
	}
	
	@Bean
	public DatabaseService databaseService(){
		return Mockito.mock(DatabaseService.class);
	}
	
}
