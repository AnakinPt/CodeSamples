package pt.com.hugodias.cache.config;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;

@Configuration
@EnableCaching
@ComponentScan({ "pt.com.hugodias.cache.*" })
public class AppConfigHazelcast {

	@Bean
	public CacheManager cacheManager() {
		ClientConfig config = new ClientConfig();
		config.getGroupConfig().setName("dev");
		config.getGroupConfig().setPassword("dev");
		config.getNetworkConfig().addAddress("127.0.0.1:5701");
		HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

		return new HazelcastCacheManager(client);
	}

	@Bean
	public KeyGenerator keyGenerator() {
		return null;
	}

	@Bean(name = "usersMap")
	public Cache usersMap() {
		return cacheManager().getCache("users");
	}

}
