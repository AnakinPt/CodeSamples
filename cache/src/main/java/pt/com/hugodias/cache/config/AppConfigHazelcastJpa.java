package pt.com.hugodias.cache.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import pt.com.hugodias.cache.dao.User;
import pt.com.hugodias.cache.dao.UserDaoService;
import pt.com.hugodias.cache.dao.UserDaoServiceHazelcast;

@Configuration
@EnableCaching
@ComponentScan({ "pt.com.hugodias.cache.*" })
public class AppConfigHazelcastJpa {
	
	@Bean
	public HazelcastInstance getInstance(){
		ClientConfig config = new ClientConfig();
		config.getGroupConfig().setName("dev");
		config.getGroupConfig().setPassword("dev");
		config.getNetworkConfig().addAddress("127.0.0.1:5701");
		HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
		return client;
	}
	
	@Bean(name = "usersMap")
	public IMap<Long,User> usersMap() {
		return getInstance().getMap("users");
	}

	@Bean(name="userDaoService")
	public UserDaoService userDaoService(){
		return new UserDaoServiceHazelcast();
	}
}
