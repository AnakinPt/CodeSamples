package pt.com.hugodias.cache.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StopWatch;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.IMap;

import pt.com.hugodias.cache.config.AppConfigHazelcast;

public class AppHazelcast {
	private static final Logger log = LoggerFactory.getLogger(AppHazelcast.class);
	
	public static void main(String[] args) {
	     
	    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfigHazelcast.class);
	    Cache users =  (Cache) context.getBean("usersMap");

        StopWatch stopwatch = new StopWatch();
        for (int i = 0; i< 200 ; i++){
            stopwatch.start(String.valueOf(i));
            long userId = 500 + i % 10;
	    	log.debug("Result : {}", users.get(userId).get());
            stopwatch.stop();
            try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// do nothing
			}
        }
        System.out.println(stopwatch.prettyPrint());

	  //shut down the Spring context.
	    ((ConfigurableApplicationContext)context).close();
	  HazelcastClient.shutdownAll();   
	}
}
