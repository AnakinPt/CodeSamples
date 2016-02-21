package pt.com.hugodias.cache.dao;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.MapStore;

public class UserMapStoreJpa implements MapStore<Long, User> {

	private static final Logger log = LoggerFactory.getLogger(UserMapStoreJpa.class);

	@Resource(name="userDaoJpa")
	private UserDaoService userDaoServiceJpa;
	
	public UserMapStoreJpa() {
	}

	@Override
	public User load(Long key) {
		log.debug("load key "+key);
		return userDaoServiceJpa.findById(key);
	}

	@Override
	public Map<Long, User> loadAll(Collection<Long> keys) {
		log.debug("loadAll");
		return null;
	}

	@Override
	public Iterable<Long> loadAllKeys() {
		log.debug("loadAllKeys");
		return null;
	}

	@Override
	public void delete(Long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Collection<Long> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(Long key, User user) {
		log.debug("store "+key);
		userDaoServiceJpa.save(user);

	}

	@Override
	public void storeAll(Map<Long, User> arg0) {
		// TODO Auto-generated method stub

	}

	public UserDaoService getUserDaoServiceJpa() {
		return userDaoServiceJpa;
	}

	public void setUserDaoServiceJpa(UserDaoService userDaoServiceJpa) {
		this.userDaoServiceJpa = userDaoServiceJpa;
	}

}
