package pt.com.hugodias.cache.dao;

import javax.annotation.Resource;

import com.hazelcast.core.IMap;

public class UserDaoServiceHazelcast implements UserDaoService {

	@Resource(name = "usersMap")
	private IMap<Long,User> users;
	
	
	@Override
	public User findById(Long id) {
		return users.get(id);
	}

	@Override
	public User save(User user) {
		users.put(user.getId(), user);
		return user;
	}

}
