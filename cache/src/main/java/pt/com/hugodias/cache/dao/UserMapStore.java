package pt.com.hugodias.cache.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.MapStore;

public class UserMapStore implements MapStore<Long, User> {

	private final Connection con;

	private static final Logger log = LoggerFactory.getLogger(UserMapStore.class);

	
	public UserMapStore() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dummy", "dummy", "dummy");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public User load(Long key) {
		try {
			log.debug("Getting value from database for "+key);
			ResultSet resultSet = con.createStatement()
					.executeQuery(String.format("select firstname, lastname from user where id =%d", key));
			try {
				if (!resultSet.next())
					return null;
				String firstName = resultSet.getString(1);
				String lastName = resultSet.getString(2);
				return new User(key, firstName, lastName);
			} finally {
				resultSet.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<Long, User> loadAll(Collection<Long> keys) {
		return null;
	}

	@Override
	public Iterable<Long> loadAllKeys() {
		Set<Long> result = new HashSet<Long>();
		try {
			ResultSet resultSet = con.createStatement()
					.executeQuery("select id from user");
			try {
				while (resultSet.next()) {
					Long id = resultSet.getLong(1);
					result.add(id);
				}
			} finally {
				resultSet.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
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
	public void store(Long arg0, User arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeAll(Map<Long, User> arg0) {
		// TODO Auto-generated method stub

	}

}
