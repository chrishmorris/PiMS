package uk.ac.ox.oppf;

import org.hibernate.cfg.Configuration;

import uk.ac.ox.oppf.optic.model.dao._RootDAO;
import junit.framework.TestCase;

public class StartUpTest extends TestCase {
	
	public void test() throws ClassNotFoundException {
		Class.forName("uk.ac.ox.oppf.optic.HibernateUtils");
		/* 30 */     _RootDAO.setDefaultConfigurationFileName("optic-hibernate.cfg.xml");
		/* 31 */     /*  65 */     Configuration config = new Configuration();
			/*  81 */       config.configure("optic-hibernate.cfg.xml");

			
			config.buildSessionFactory();

	}

}
