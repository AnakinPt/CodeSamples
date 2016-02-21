package pt.com.hugodias.cache.dao;

public interface UserDaoService {
//	@CacheResult(cacheName="userCache")
	User findById(Long id);
	
	User save(User user);
}
