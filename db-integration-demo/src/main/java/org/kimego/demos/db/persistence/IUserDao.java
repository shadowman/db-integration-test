package org.kimego.demos.db.persistence;

import java.util.Collection;

import org.kimego.demos.db.domain.User;

public interface IUserDao {
	User findUserById(long uid);
	Collection<User> findUsersByName(String partialName);
	void save(User user);
}
