package pt.com.hugodias.sample.game;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration
@Import(value=AppConfig.class)
public class TestConfig {
	@Bean(name="max_row")
	public int max_row(){
		return 10;
	}
	
	@Bean(name="max_column")
	public int max_column(){
		return 10;
	}

	@Bean(name="max_players")
	public int max_players(){
		return 2;
	}

	@Bean(name="suspensionTime")
	@Scope("singleton")
	public int suspensionTime(){
		return 1000;
	}

}
