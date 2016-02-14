package pt.com.hugodias.cache.dao;

import javax.cache.annotation.CacheResult;

public interface UserDaoService {
	@CacheResult(cacheName="userCache")
	User findById(Long id);
}
