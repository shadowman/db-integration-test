package org.kimego.demos.db.persistence;

import javax.persistence.NoResultException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kimego.demos.db.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context.xml")
public class UserDaoImplIntegrationTests {

	private static final Long USER_ID = 100L;

	private static final long NON_EXISTENT_USER_ID = 0;
	
	@Autowired
	private ApplicationContext context;
	
	private IUserDao repo;

	@Before
	public void TestsInitialization() {
		repo = context.getBean(IUserDao.class);
	}
	
	
	@Test
	public void UserDaoImplFindByIdReturnsUserEntityWithThatIdIfItWasFound() {
		User user = repo.findUserById(USER_ID);
		
		Assert.assertNotNull(user);
	}
	
	@Test(expected=NoResultException.class)
	public void UserDaoImplFindByIdThrowsNoResultExceptionIfAnEntityWithThatIdWasNotFound() {
		User user = repo.findUserById(NON_EXISTENT_USER_ID);
		
		Assert.assertNull(user);
	}
	
	@Test
	public void UserDaoImplSaveStoresNewEntityInDatabase() {
		User user = new User();
		user.setUsername("user");
		user.setPassword("secret");
		
		repo.save(user);
		
		Assert.assertNotNull(repo.findUserById(user.getId()));
	}
}
