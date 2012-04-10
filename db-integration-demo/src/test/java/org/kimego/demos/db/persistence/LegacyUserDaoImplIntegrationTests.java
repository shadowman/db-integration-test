package org.kimego.demos.db.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.NoResultException;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kimego.demos.db.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context-legacy.xml")
public class LegacyUserDaoImplIntegrationTests {

	private static final Long USER_ID = 100L;

	private static final long NON_EXISTENT_USER_ID = 0;
	
	@Autowired
	private ApplicationContext context;

	@Autowired
	DataSource db;
	
	private IUserDao repo;

	@Before
	public void TestsInitialization() throws DatabaseUnitException, SQLException, IOException {
		prepareDatabase();
		initializeUnitAndMocks();
	}

	private void initializeUnitAndMocks() {
		repo = context.getBean(IUserDao.class);
	}
	
	@After
	public void TestsFinalization() throws DatabaseUnitException, SQLException, IOException {
		cleanDatabase();
	}
	
	private void prepareDatabase() throws DatabaseUnitException, SQLException, IOException {
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet());
	}
	
	private void cleanDatabase() throws DatabaseUnitException, SQLException, IOException {
		DatabaseOperation.DELETE_ALL.execute(getConnection(), getDataSet());
	}
	
	private IDatabaseConnection getConnection() throws SQLException {
		Connection dsConnection = db.getConnection();
        IDatabaseConnection connection = new DatabaseConnection(dsConnection, "PUBLIC");  
        DatabaseConfig config = connection.getConfig();  
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());  
        return connection;  
	}

	private IDataSet getDataSet() throws DataSetException, IOException {
        return new FlatXmlDataSet(ClassLoader.getSystemResourceAsStream("sample-data.xml"));  
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
	public void UserDaoImplSaveStoresNewEntityInDatabase() throws DataSetException, SQLException {
		User user = new User();
		user.setUsername("jane");
		user.setPassword("shine");
		
		repo.save(user);
		
        QueryDataSet databaseSet = new QueryDataSet(getConnection());  
        databaseSet.addTable("users", "select * from users where username='jane'");  
        ITable actualTable = databaseSet.getTables()[0]; 
        Assert.assertEquals(1, actualTable.getRowCount());
	}
}
