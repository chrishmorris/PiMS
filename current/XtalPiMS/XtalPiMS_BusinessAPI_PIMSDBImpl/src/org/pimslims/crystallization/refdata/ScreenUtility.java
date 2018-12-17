package org.pimslims.crystallization.refdata;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.ScreenType;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.ConditionDAO;
import org.pimslims.crystallization.dao.ScreenDAO;
import org.pimslims.crystallization.dao.view.ConditionViewDAO;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.holder.RefHolderSource;
import org.pimslims.model.holder.RefSamplePosition;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.sample.RefSample;
import org.pimslims.search.Conditions;
import org.pimslims.search.Searcher;

public class ScreenUtility {
	private final WritableVersion wv;

	public ScreenUtility(final WritableVersion wv) {
		this.wv = wv;
	}

	// TODO see http://cselnx4.dl.ac.uk:8080/jira/browse/XP-60

	public RefHolder loadFile(final Reader reader) throws JDOMException,
			IOException, ConstraintException, AccessException,
			BusinessException {
		final SAXBuilder builder = new SAXBuilder();
		final Document doc = builder.build(reader);
		return this.processScreen(doc.getRootElement());

	}

	private RefHolder processScreen(final Element rootElement)
			throws ConstraintException, AccessException, BusinessException {
		// get screen
		final String screenName = rootElement.getChildText("screenName");
		final RefHolder screen = getOrCreateScreen(screenName);
		// manufacturers
		final String screenSupplierName = rootElement
				.getChildText("screenSupplier");
		if (screenSupplierName != null
				&& screenSupplierName.trim().length() > 0) {
			final Organisation org = getOrCreateOrganisation(screenSupplierName);
			if (screen.getRefHolderSources().size() > 0) {
				for (final RefHolderSource source : screen
						.getRefHolderSources()) {
					source.delete();
				}
			}
			final RefHolderSource newSource = new RefHolderSource(wv,
					screenName, screen, org);
			final String screenCatNum = rootElement
					.getChildText("screenCatNum");
			if (screenCatNum != null && screenCatNum.trim().length() > 0) {
				newSource.setCatalogNum(screenCatNum);
			}
		}
		screen.setHolderCategories(ScreenDAO.getScreenHolderCategory(wv,
				ScreenType.Matrix));
		// details
		final String screenDetails = rootElement.getChildText("screenDetails");
		final String screenReference = rootElement
				.getChildText("screenReference");
		screen.setDetails(screenDetails + "\n" + screenReference);
		// Conditions
		// remove old conditions
		for (final RefSamplePosition rp : screen.getRefSamplePositions()) {
			rp.delete();
		}
		wv.flush();

		// add new conditions
		final Collection<Element> conditions = ((Element) rootElement
				.getChildren("solutions").iterator().next())
				.getChildren("solution");
		for (final Element conditionElement : conditions) {
			addConditionToScreen(screen, screenSupplierName, conditionElement);
		}
		vertifyConditionView(screen);
		wv.flush();
		return screen;
	}

	private void vertifyConditionView(final RefHolder screen)
			throws BusinessException {
		final ConditionViewDAO conditionViewDAO = new ConditionViewDAO(wv);
		final BusinessCriteria criteria = new BusinessCriteria(conditionViewDAO);
		criteria.add(BusinessExpression.Equals(ConditionView.PROP_LOCAL_NAME,
				screen.getName(), true));
		final Collection<ConditionView> views = conditionViewDAO
				.findViews(criteria);
		assert views.size() > 0;

	}

	private void addConditionToScreen(final RefHolder screen,
			final String screenSupplierName, final Element conditionElement)
			throws ConstraintException, BusinessException, AccessException {
		// general condition info
		final String wellString = conditionElement.getChildText("localNum");
		final WellPosition well = new WellPosition(wellString);
		final String solutionId = conditionElement.getChildText("solutionId");
		final String pHfinal = conditionElement.getChildText("pHfinal");
		final Condition condition = new Condition();
		final RefSample pCondition = wv.findFirst(RefSample.class,
				RefSample.PROP_NAME, screen.getName() + "-" + wellString);
		if (pCondition != null) {
			pCondition.delete();
			wv.flush();
		}
		condition.setLocalName(screen.getName() + "-" + wellString);
		condition.setManufacturerCode(solutionId);
		condition.setManufacturerName(screenSupplierName);
		if (pHfinal != null && pHfinal.length() > 0) {
			condition.setFinalpH(new Float(pHfinal));
		}
		// components
		final List<ComponentQuantity> components = getComponents(((Element) conditionElement
				.getChildren("components").iterator().next())
				.getChildren("component"));
		condition.setComponents(components);
		// Find or create condition
		final ConditionDAO conditionDAO = new ConditionDAO(wv);
		final RefSample rs = conditionDAO.createPO(condition);

		// Create the RefSamplePosition
		final Map<String, Object> attr = new HashMap<String, Object>();
		attr.put(RefSamplePosition.PROP_COLPOSITION, well.getColumn());
		attr.put(RefSamplePosition.PROP_REFHOLDER, screen);
		attr.put(RefSamplePosition.PROP_REFSAMPLE, rs);
		attr.put(RefSamplePosition.PROP_ROWPOSITION, well.getRow());
		attr.put(RefSamplePosition.PROP_SUBPOSITION, well.getSubPosition());
		new RefSamplePosition(wv, attr);

	}

	List<ComponentQuantity> getComponents(final List<Element> componentElements) {
		final List<ComponentQuantity> components = new LinkedList<ComponentQuantity>();
		for (final Element componentElement : componentElements) {
			final ComponentQuantity cq = new ComponentQuantity();
			cq.setComponentType(componentElement.getAttributeValue("type"));
			final String concentration = componentElement.getAttributeValue(
					"concentration").replace("%", "");
			cq.setQuantity(new Double(concentration));
			final String concentrationUnit = componentElement
					.getAttributeValue("concentrationUnits");
			cq.processUnit(concentrationUnit);

			final String alias = componentElement.getAttributeValue("name");
			final String pH = componentElement.getAttributeValue("pH");
			// TODO check for alias, chemicals have many names
			final Component component = new Component();
			component.setChemicalName(getChemicalName(alias, wv));
			if (pH != null && pH.length() > 0) {
				component.setPH(new Float(pH));
			}
			cq.setComponent(component);
			components.add(cq);
		}

		return components;
	}

	public static String getChemicalName(String alias, ReadableVersion version) {
		final Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put(Substance.PROP_SYNONYMS,
				Conditions.listContains(alias));
		Searcher s = new Searcher(version);
		Collection<ModelObject> results = s.search(criteria,
				version.getMetaClass(Molecule.class));
		if (!results.isEmpty()) {
			if (1 < results.size()) {
				throw new IllegalStateException(
						"More than one molecule matches name: " + alias);
			}
			Molecule molecule = (Molecule) results.iterator().next();
			return molecule.getName();
		}
		return alias;
	}

	private Organisation getOrCreateOrganisation(final String screenSupplierName)
			throws ConstraintException {
		Organisation org = wv.findFirst(Organisation.class,
				Organisation.PROP_NAME, screenSupplierName);
		if (org == null) {
			org = new Organisation(wv, screenSupplierName);
		}
		return org;
	}

	private RefHolder getOrCreateScreen(final String screenName)
			throws ConstraintException {
		RefHolder screen = wv.findFirst(RefHolder.class, RefHolder.PROP_NAME,
				screenName);
		if (screen == null) {
			screen = new RefHolder(wv, screenName);
			System.out.println("Creating new screen:" + screenName);
		} else {
			System.out.println("Update exsiting screen:" + screenName);
		}
		return screen;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: directory ");
			return;
		}
		File directory = new File(args[0]);
		if (!directory.isDirectory()) {
			System.err.println("Not a directory: "
					+ directory.getAbsolutePath());
			System.exit(1);
		}
		File[] files = directory.listFiles();

		AbstractModel model = ModelImpl.getModel();
		try {
			for (File file : files) {
				if (!file.getName().endsWith(".xml")) {
					continue;
				}
				final WritableVersion version = model
						.getWritableVersion(AbstractModel.SUPERUSER);
				try {
					final java.io.Reader reader2 = new java.io.FileReader(file);
					new ScreenUtility(version).loadFile(reader2);
					version.commit();
					System.out.println("Loaded: " + file.getName());
				} catch (final IOException e) {
					e.printStackTrace();
					System.err.println("Error reading: "
							+ file.getAbsolutePath());
				} catch (ConstraintException e) {
					e.printStackTrace();
					System.err.println("Error processing: "
							+ file.getAbsolutePath());
				} catch (JDOMException e) {
					e.printStackTrace();
					System.err.println("Error in: " + file.getAbsolutePath());
				} catch (BusinessException e) {
					e.printStackTrace();
					System.err.println("Error processing: "
							+ file.getAbsolutePath());
				}
			}
			System.out.println("Loaded all screens");
		} catch (AbortedException e) {
			// should not happen
			throw new RuntimeException(e);
		} catch (AccessException e) {
			// should not happen
			throw new RuntimeException(e);
		}
		System.exit(0);
	}
}
