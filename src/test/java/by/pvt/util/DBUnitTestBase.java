package by.pvt.util;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Callable;

public abstract class DBUnitTestBase extends DBTestCase {
    private static final Properties properties = new Properties();
    private static final SessionFactory sessionFactory;
    protected Session session;

    static {
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sessionFactory = new Configuration()
                .configure("by/pvt/pojo/hibernate_test.cfg.xml")
                .buildSessionFactory();
    }

    public DBUnitTestBase(String name) {
        super(name);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, properties.getProperty("db.driver"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, properties.getProperty("db.url"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, properties.getProperty("db.username"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, properties.getProperty("db.password"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, "");
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(this.getClass().getResourceAsStream(getResourceName()));
    }

    protected abstract String getResourceName();

    @Override
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.CLEAN_INSERT;
    }

    @Before
    public void setUp() {
        session = sessionFactory.openSession();
    }

    @After
    public void destroy() {
        session.close();
    }

    protected  <V> V executeInTransaction(Callable<V> command) throws Exception {
        Transaction transaction = session.beginTransaction();
        V result = null;
        try {
            result = command.call();
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
        return result;
    }
}
