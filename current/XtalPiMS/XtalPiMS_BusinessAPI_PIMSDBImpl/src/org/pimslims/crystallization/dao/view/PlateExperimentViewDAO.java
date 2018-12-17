/**
 * xtalPiMSImpl org.pimslims.crystallization.dao.view PlateExperimentViewDAO.java
 * 
 * @author bl67
 * @date 2 May 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.dao.view;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.PropertyNameConvertor;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.TrialDropDAO;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.holder.Holder;

/**
 * PlateExperimentViewDAO
 * 
 */
public class PlateExperimentViewDAO extends
		AbstractSQLViewDAO<PlateExperimentView> implements
		ViewDAO<PlateExperimentView>,
		PropertyNameConvertor<PlateExperimentView> {

	/**
	 * Constructor for PlateExperimentViewDAO
	 * 
	 * @param version
	 */
	public PlateExperimentViewDAO(final ReadableVersion version) {
		super(version);
	}

	/**
	 * PlateExperimentViewDAO.convertPropertyName
	 * 
	 * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#convertPropertyName(java.lang.String)
	 */
	@Override
	public String convertPropertyName(final String propertyName)
			throws BusinessException {
		if (propertyName == null) {
			throw new IllegalArgumentException("Null filter name");
		}
		if (propertyName.equals(PlateExperimentView.PROP_BARCODE)) {
			return "plate_1.name";
		} else if (propertyName.equals(PlateExperimentView.PROP_CONSTRUCT_ID)) {
			return "construct.labbookentryid";
		} else if (propertyName.equals(PlateExperimentView.PROP_CONSTRUCT_NAME)) {
			return "construct.commonname";
		} else if (propertyName.equals(PlateExperimentView.PROP_CREATE_DATE)) {
			return "plate.startdate";
		} else if (propertyName.equals(PlateExperimentView.PROP_DESCRIPTION)) {
			return "plate_2.details";
		} else if (propertyName.equals(PlateExperimentView.PROP_DESTROY_DATE)) {
			return "plate.enddate";
		} else if (propertyName.equals(PlateExperimentView.PROP_GROUP)) {
			return "accessobject.name";
		} else if (propertyName.equals(PlateExperimentView.PROP_IMAGER)) {
			return "instrument.name";
		} else if (propertyName
				.equals(PlateExperimentView.PROP_LAST_IMAGE_DATE)) {
			return "lastinpsection.completiontime";
		} else if (propertyName
				.equals(PlateExperimentView.PROP_NUMBER_OF_CRYSTALS)) {
			return "plate.crystalnumber";
		} else if (propertyName.equals(PlateExperimentView.PROP_OWNER)) {
			return "owneruser.name";
		} else if (propertyName.equals(PlateExperimentView.PROP_RUN_BY)) {
			return "\"operator\".name";
		} else if (propertyName.equals(PlateExperimentView.PROP_SAMPLE_ID)) {
			return "proteinsample.labbookentryid";
		} else if (propertyName.equals(PlateExperimentView.PROP_SAMPLE_NAME)) {
			return "proteinsample.name";
		} else if (propertyName.equals(PlateExperimentView.PROP_STATUS)) {
			return "(CASE WHEN plate.enddate IS NULL THEN 'active' ELSE 'destroyed' END)";
		} else if (propertyName.equals(PlateExperimentView.PROP_TEMPERATURE)) {
			return "instrument.temperature";
		} else if (propertyName.equals(PlateExperimentView.PROP_PLATE_TYPE)) {
			return "holdertype.name";
		} else if (propertyName.equals(PlateExperimentView.PROP_SCREEN)) {
			return "reholder.name";
		}
		throw new BusinessException("Unable to find matching property in "
				+ this.getClass());
	}

	/**
	 * PlateExperimentViewDAO.getCountableName
	 * 
	 * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getCountableName()
	 */
	@Override
	String getCountableName() {
		return "plate_1.name";
	}

	/**
	 * PlateExperimentViewDAO.getRootClass
	 * 
	 * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getRootClass()
	 */
	@Override
	Class<Holder> getRootClass() {

		return Holder.class;
	}

	/**
	 * PlateExperimentViewDAO.getViewHQL
	 * 
	 * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getViewHQL(org.pimslims.business.criteria.BusinessCriteria)
	 */
	@Override
	public String getViewSql(final BusinessCriteria criteria) {
		final String selectHQL =
		// "select barcode,createdate,destroydate,description,constructname,constructid,\"owner\",runby,\"group\",status,lastimagedate,imager,temperature,numberofcrystals,platetype,screen,proteinsampleid,proteinsamplename from trialplateview";
		" SELECT plate_1.name AS barcode,"
				+ " plate.startdate AS createdate,"
				+ " plate.enddate AS destroydate,"
				+ " plate_2.details AS description,"
				+ " construct.commonname AS constructname,"
				+ " construct.labbookentryid AS constructid,"
				+ " owneruser.name AS \"owner\","
				+ " \"operator\".name AS runby,"
				+ " accessobject.name AS \"group\", "
				+ " CASE WHEN plate.enddate IS NULL THEN 'active' ELSE 'destroyed'"
				+ " END AS status,"
				+ " lastinpsection.completiontime AS lastimagedate,"
				+ " instrument.name AS imager,"
				+ " instrument.temperature,"
				+ " plate.crystalnumber AS numberofcrystals,"
				+ " holdertype.name AS platetype,"
				+ " reholder.name AS screen,"
				+ " proteinsample.labbookentryid AS proteinsampleid,"
				+ " proteinsample.name AS proteinsamplename,"
				+ " plate_2.accessid "
				+ "  FROM hold_holder plate"
				+ "  JOIN hold_abstractholder plate_1 ON plate.abstractholderid = plate_1.labbookentryid"
				+ "  JOIN core_labbookentry plate_2 ON plate.abstractholderid = plate_2.dbid"
				+ "  JOIN sam_sample sample ON plate.firstsampleid = sample.abstractsampleid"
				+ "  JOIN expe_outputsample outputsamp2_ ON sample.abstractsampleid = outputsamp2_.sampleid"
				+ "  JOIN expe_experiment experiment ON outputsamp2_.experimentid = experiment.labbookentryid"
				+ "  JOIN core_labbookentry experiment1_ ON experiment.labbookentryid = experiment1_.dbid"
				+ "  LEFT JOIN expe_inputsample inputsample ON inputsample.experimentid = experiment.labbookentryid"
				+ " AND inputsample.name='"
				+ TrialDropDAO.PURIFIED_PROTEIN
				+ "' "
				+ "  LEFT JOIN sam_abstractsample proteinsample ON inputsample.sampleid = proteinsample.labbookentryid"
				+ "  LEFT JOIN sche_scheduledtask lastinpsection ON plate.lasttaskid = lastinpsection.labbookentryid"
				+ "  LEFT JOIN targ_researchobjective construct ON experiment.researchobjectiveid = construct.labbookentryid"
				+ "  LEFT JOIN acco_user owneruser ON experiment1_.creatorid = owneruser.systemclassid"
				+ "  LEFT JOIN acco_user \"operator\" ON experiment.operatorid = \"operator\".systemclassid"
				+ "  LEFT JOIN expe_instrument instrument ON lastinpsection.instrumentid = instrument.labbookentryid"
				+ "  LEFT JOIN ref_abstractholdertype holdertype ON plate_1.holdertypeid = holdertype.publicentryid"
				+ "  LEFT JOIN hold_refholderoffset refholdero15_ ON plate.abstractholderid = refholdero15_.holderid"
				+ "  LEFT JOIN hold_abstractholder reholder ON refholdero15_.refholderid = reholder.labbookentryid"
				+ "  LEFT JOIN core_accessobject accessobject ON accessobject.systemclassid = plate_2.accessid";

		// could
		// " LEFT JOIN expe_parameter additiveScreen ON outputsamp2_.experimentid = additiveScreen.experimentid WHERE additiveScreen.name='Additive Screen?'"
		return buildViewQuerySQL(criteria, selectHQL, null, "plate_2",
				Holder.class);
	}

	/**
	 * PlateExperimentViewDAO.runSearch
	 * 
	 * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#runSearch(org.hibernate.Query)
	 */
	@Override
	Collection<PlateExperimentView> runSearch(
			final org.hibernate.SQLQuery sqlQuery) {
		final Collection<PlateExperimentView> plateViews = new LinkedList<PlateExperimentView>();
		final SQLQuery q = sqlQuery;
		/*
		 * q.addScalar("barcode", StandardBasicTypes.STRING);
		 * q.addScalar("createdate", StandardBasicTypes.CALENDAR);
		 * q.addScalar("destroydate", StandardBasicTypes.CALENDAR);
		 * q.addScalar("description", StandardBasicTypes.STRING);
		 * q.addScalar("constructname", StandardBasicTypes.STRING);
		 * q.addScalar("constructid", StandardBasicTypes.LONG);
		 * q.addScalar("owner", StandardBasicTypes.STRING); q.addScalar("runBy",
		 * StandardBasicTypes.STRING); q.addScalar("group",
		 * StandardBasicTypes.STRING); q.addScalar("status",
		 * StandardBasicTypes.STRING); q.addScalar("lastimagedate",
		 * StandardBasicTypes.CALENDAR); q.addScalar("imager",
		 * StandardBasicTypes.STRING); q.addScalar("temperature",
		 * StandardBasicTypes.FLOAT); q.addScalar("numberofcrystals",
		 * StandardBasicTypes.LONG); q.addScalar("platetype",
		 * StandardBasicTypes.STRING); q.addScalar("screen",
		 * StandardBasicTypes.STRING); q.addScalar("proteinsampleid",
		 * StandardBasicTypes.LONG); q.addScalar("proteinsamplename",
		 * StandardBasicTypes.STRING);
		 */
		final List<?> results = sqlQuery.list();
		// int j = 0;
		for (final Object object : results) {
			final Object result[] = (Object[]) object;
			/*
			 * j++; for (final Object element : result) {
			 * System.out.println("view" + j + ":" + element); }
			 */

			final PlateExperimentView plateView = new PlateExperimentView();
			plateView.setBarcode((String) result[0]);
			plateView.setCreateDate(getCalDate(result[1]));
			plateView.setDestroyDate(getCalDate(result[2]));
			plateView.setDescription((String) result[3]);
			plateView.setConstructName((String) result[4]);
			plateView.setConstructId(AbstractSQLViewDAO.getLong(result[5]));
			plateView.setOwner((String) result[6]);
			plateView.setRunBy((String) result[7]);
			// plateView.setSampleName((String) result[8]);
			// plateView.setSampleId(AbstractSQLViewDAO.getLong(result[9]));
			plateView.setGroup((String) result[8]);
			plateView.setStatus((String) result[9]);
			plateView.setLastImageDate(getCalDate(result[10]));
			plateView.setImager((String) result[11]);
			plateView.setTemperature(AbstractSQLViewDAO.getFloat(result[12]));
			Long numCrystals = AbstractSQLViewDAO.getLong(result[13]);
			if (null != numCrystals) {
				plateView.setNumberOfCrystals(numCrystals.intValue());
			}
			plateView.setPlateType((String) result[14]);
			plateView.setScreen((String) result[15]);
			plateView.setSampleId(AbstractSQLViewDAO.getLong(result[16]));
			plateView.setSampleName((String) result[17]);
			// TODO plateView.setAdditiveScreen
			plateViews.add(plateView);
		}
		return plateViews;
	}

}
