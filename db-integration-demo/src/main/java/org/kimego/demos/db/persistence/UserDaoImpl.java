package org.kimego.demos.db.persistence;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.kimego.demos.db.domain.User;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class UserDaoImpl implements IUserDao {
	
	private EntityManager entities;
	
	public UserDaoImpl() {
	}
	
	public UserDaoImpl(EntityManagerFactory factory) {
		this.setEntities(factory.createEntityManager());
	}
	
	public User findUserById(long uid) {
		TypedQuery<User> query = getEntities().createQuery("select u from User u where u.id = :id", User.class);
		query.setParameter("id", uid);
		return query.getSingleResult();
	}

	public Collection<User> findUsersByName(String partialName) {
		return null;
	}

	public void save(User user) {
		getEntities().persist(user);
	}

	public EntityManager getEntities() {
		return entities;
	}

	@PersistenceContext
	public void setEntities(EntityManager entities) {
		this.entities = entities;
	}
}
