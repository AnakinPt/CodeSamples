package pt.com.hugodias.sample.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.config.NetworkConfig;

@Configuration
public class HazelcastConfiguration {

	@Bean
	public Config getConfig(){
		Config config = new Config();
		config.getNetworkConfig().setPort( 5701 );
		config.getNetworkConfig().setPortAutoIncrement( false );
		
		NetworkConfig network = config.getNetworkConfig();
		JoinConfig join = network.getJoin();
		join.getMulticastConfig().setEnabled( false );
		
		MapConfig mapConfig = new MapConfig();
		mapConfig.setName( "purchases" );
		mapConfig.setBackupCount( 2 );
		mapConfig.getMaxSizeConfig().setSize( 10000 );
		mapConfig.setTimeToLiveSeconds( 300 );

		NearCacheConfig nearCacheConfig = new NearCacheConfig();
		nearCacheConfig.setMaxSize( 1000 ).setMaxIdleSeconds( 120 )
		    .setTimeToLiveSeconds( 300 );
		mapConfig.setNearCacheConfig( nearCacheConfig );

		config.addMapConfig( mapConfig );
		return config;
	}

}
