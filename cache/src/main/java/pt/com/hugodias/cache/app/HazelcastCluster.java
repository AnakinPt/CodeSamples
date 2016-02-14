package pt.com.hugodias.cache.app;

import com.hazelcast.config.*;
import com.hazelcast.config.EvictionConfig.MaxSizePolicy;
import com.hazelcast.config.MapStoreConfig.InitialLoadMode;
import com.hazelcast.core.Hazelcast;

public class HazelcastCluster {
	public static void main(String[] args) {
		Config config = new Config();
		MapConfig mapConfig = new MapConfig();
		mapConfig.setEvictionPolicy(EvictionPolicy.LFU);
		mapConfig.setMinEvictionCheckMillis(0);
		mapConfig.setTimeToLiveSeconds(30);
		
		NearCacheConfig nearCacheConfig = new NearCacheConfig();
		nearCacheConfig.setCacheLocalEntries(true);
		EvictionConfig evictionConfig = new EvictionConfig();
		evictionConfig.setEvictionPolicy(EvictionPolicy.LFU);
		evictionConfig.setMaximumSizePolicy(MaxSizePolicy.ENTRY_COUNT);
		evictionConfig.setSize(50);
		nearCacheConfig.setEvictionConfig(evictionConfig);
		nearCacheConfig.setTimeToLiveSeconds(30);
		mapConfig.setNearCacheConfig(nearCacheConfig );
		MaxSizeConfig maxSizeConfig = new MaxSizeConfig();
		maxSizeConfig.setMaxSizePolicy(com.hazelcast.config.MaxSizeConfig.MaxSizePolicy.PER_NODE);
		maxSizeConfig.setSize(50);
		mapConfig.setName("users");
		mapConfig.setMaxSizeConfig(maxSizeConfig );
		MapStoreConfig mapStoreConfig = new MapStoreConfig();
		mapStoreConfig.setClassName("pt.com.hugodias.cache.dao.UserMapStore");
		mapStoreConfig.setEnabled(true);
		mapStoreConfig.setInitialLoadMode(InitialLoadMode.LAZY);
		mapConfig.setMapStoreConfig(mapStoreConfig );
		config.addMapConfig(mapConfig );
		GroupConfig groupConfig = new GroupConfig();
		groupConfig.setName("dev");
		groupConfig.setPassword("dev");
		config.setGroupConfig(groupConfig);
		Hazelcast.newHazelcastInstance(config);
		
	}

}
