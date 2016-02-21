package pt.com.hugodias.cache.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoServiceImpl implements UserDaoService {
	private final Connection con;

	public UserDaoServiceImpl() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dummy", "dummy", "dummy");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Cacheable(value = "userCache", key = "#id")
	public User findById(Long id) {
		System.out.println("find user service...");
		try {
			ResultSet resultSet = con.createStatement()
					.executeQuery(String.format("select firstname, lastname from user where id =%d", id));
			try {
				if (!resultSet.next())
					return null;
				String firstName = resultSet.getString(1);
				String lastName = resultSet.getString(2);
				return new User(id, firstName, lastName);
			} finally {
				resultSet.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void slowQuery(long seconds) {
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		return null;
	}
}
