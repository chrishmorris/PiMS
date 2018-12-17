/**
 * pims-backup org.pimslims.dbexport FilterDataPerUser.java
 *
 * @author alan
 * @date Aug 18, 2009
 *
 *       Protein Information Management System
 * @version: 2.2
 *
 *           Copyright (c) 2009 alan The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.dbexport;

import java.io.File;
import java.io.FileNotFoundException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.accessControl.User;

/**
 * Delete unused reference data
 * 
 */
public class DeleteUnused {

    private static ModelImpl model;
    private static String propertyFile;

    /**
     *
     * Constructor for FilterDataPerUserHQL
     * @param args
     */
    public static void main(final String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide a Properties file that defines the connection to the database.");

            // For development purposes
            propertyFile = "./temp_Properties";

			return;

        } else {
            propertyFile = args[0];
        }

        // Load the properties file
        try {
            DeleteUnused.initModel(propertyFile);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            System.exit(1);
        }

        // Get a writable connection to the DB
        final WritableVersion version =
            DeleteUnused.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

        try {

        Session session = version.getSession();

        String hqlQueryDelete =
 "delete from AccessObject notebook where "
					+ "0 = (select count (*) from LabBookEntry as page where notebook=page.access)";
			doDelete(session, hqlQueryDelete);

			doDelete(
					session,
					"delete from UserGroup as group where 0=(select count(*) from Permission p where group=p.userGroup)");

			doDelete(
					session,
					"delete from User as user where  user.userGroups is empty "
							+ " and 0=(select count(*) from UserGroup g where user = g.header)");

			doDelete(
					session,
					"delete from UserGroup as group where 0=(select count(*) from Permission p where group=p.userGroup)");
			doDelete(
					session,
					"delete from Person as person where 0=(select count(*) from User u where person=u.person)");
			doDelete(
					session,
					"delete from Protocol as protocol where protocol.access is null and  0=(select count(*) from Experiment e where protocol=e.protocol)");

			doDelete(
					session,
					"delete from Database as database where  0=(select count(*) from ExternalDbLink link where database=link.dbName)");
			doDelete(
					session,
					"delete from ExperimentType as type where 0=(select count(*) from Experiment e where type=e.experimentType)");
			doDelete(
					session,
					"delete from Organism as organism where 0=(select count(*) from Target t where organism=t.species) "
							+ " and 0=(select count(*) from AbstractComponent ac where organism=ac.naturalSource) ");
			// TODO Milestone, WorkflowItem
			doDelete(
					session,
					"delete from RefSample as recipe where 0=(select count(*) from Sample s where recipe=s.refSample) "
			// for 5.0 onwards:
			// " and 0=(select count(*) from RefInputSample ris where recipe=ris.refSample) "
			);
			doDelete(
					session,
					"delete from TargetStatus as status where 0=(select count(*) from Milestone m where status=m.status)");

			doDelete(
					session,
					"delete from SampleCategory  as category    "
							+ " where 0=(select count(*) from AbstractSample s where category member of s.sampleCategories)"
							+ " and 0=(select count(*) from RefInputSample ris where category=ris.sampleCategory)"
							+ " and 0=(select count(*) from RefOutputSample ros where category=ros.sampleCategory)");

			// don't need to delete from SampleComponent, that will cascade from
			// delete of samples and recipes

			// delete unused AbstractComponents. This has some subclasses.

			final String UNUSED_ABSTRACT_COMPONENT = "  0=(select count(*) from SampleComponent as sc where m=sc.refComponent)";
			doDelete(session, "delete from Host as m where "
					+ UNUSED_ABSTRACT_COMPONENT);
			// These next three are not strictly necessary, since they are
			// subclasses of Molecule
			// but doing it first will provide clearer messages
			doDelete(session, "delete from Extension as m where  "
					+ UNUSED_ABSTRACT_COMPONENT);
			doDelete(session,
 "delete from Construct as m where "
					+ UNUSED_ABSTRACT_COMPONENT);
			doDelete(session,
 "delete from Primer as m where "
					+ UNUSED_ABSTRACT_COMPONENT);
			doDelete(
					session,
					"delete from Molecule as m where m.nucTargets is empty and m.protTargets is empty and "
							+ UNUSED_ABSTRACT_COMPONENT);

			doDelete(
					session,
					"delete from ComponentCategory as category where 0=(select count(*) from AbstractComponent ac where category member of ac.categories)");

			// TODO HazardPhrase, HolderCategory, HolderType, RefSamplePosition

			doDelete(
					session,
					"delete from HazardPhrase as h where 0=(select count(*) from AbstractSample s where h member of s.hazardPhrases)");
			// TODO reconsider this
			doDelete(session, "delete from Organisation ");

        session.getTransaction().commit();
        session.close();

        } finally {
            if (!version.isCompleted()) {version.abort();}
        }
    }

	/**
	 * @param session
	 * @param hqlQueryDelete
	 */
	private static void doDelete(Session session, String hqlQueryDelete) {
		Query queryDelete = session.createQuery(hqlQueryDelete).setCacheable(
				false);
		int count = queryDelete.executeUpdate();
		if (0 != count) {
			System.out
					.println("Rows deleted: " + count + "\n" + hqlQueryDelete);
		}
	}

    /**
     * @param string
     * @throws FileNotFoundException
     */
    private static void initModel(final String propertyFileName) throws FileNotFoundException {
        //start from propertyFile if provided
        System.out.println("loading DB connection info from: " + propertyFileName);
        final File properties = new java.io.File(propertyFileName);
        if (!properties.exists()) {
            System.out.println("file does NOT exist:" + properties.getAbsolutePath());
        } else if (!properties.isFile()) {
            System.out.println("please give a file NOT a directory: " + propertyFileName);
        }
        model = (ModelImpl) org.pimslims.dao.ModelImpl.getModel(properties);
        //System.out.println(model.getProperty("db.url"));
    }

}
