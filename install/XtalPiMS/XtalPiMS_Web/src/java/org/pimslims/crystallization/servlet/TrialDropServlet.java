package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.ComponentView;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.crystallization.view.SampleView;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;

/**
 * Servlet implementation class for Servlet: TrialDropServlet
 * 
 */
public class TrialDropServlet extends
		org.pimslims.crystallization.servlet.XtalPIMSServlet {
	static final long serialVersionUID = 1L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void processRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		final CommonRequestParams crp = CommonRequestParams
				.parseRequest(request);
		String barcode = request.getParameter(ScoreView.PROP_BARCODE);
		if (null != barcode) {
			barcode = barcode.trim();
		}
		final String well = request.getParameter(TrialDropView.PROP_WELL);
		final String synchrotronStr = request
				.getParameter(TrialDropView.PROP_SYNCHROTRON);
		final String localScreenName = request
				.getParameter(ConditionView.PROP_LOCAL_NAME);
		final String localScreenNumber = request
				.getParameter(ConditionView.PROP_LOCAL_NUMBER);
		final String manufacturerName = request
				.getParameter(ConditionView.PROP_MANUFACTURER_NAME);
		final String manufacturerScreenName = request
				.getParameter(ConditionView.PROP_MANUFACTURER_SCREEN_NAME);
		final String manufacturerCode = request
				.getParameter(ConditionView.PROP_MANUFACTURER_CODE);
		final String manufacturerCatCode = request
				.getParameter(ConditionView.PROP_MANUFACTURER_CAT_CODE);
		final String volatileConditionStr = request
				.getParameter(ConditionView.PROP_VOLATILE_CONDITION);
		final String minFinalpHStr = request.getParameter("min"
				+ ConditionView.PROP_FINAL_PH);
		final String maxFinalpHStr = request.getParameter("max"
				+ ConditionView.PROP_FINAL_PH);
		final String saltConditionStr = request
				.getParameter(ConditionView.PROP_SALT_CONDITION);
		final String componentName = request
				.getParameter(ComponentView.PROP_NAME);
		final String componentType = request
				.getParameter(ComponentView.PROP_TYPE);
		final String volatileComponentStr = request
				.getParameter(ComponentView.PROP_VOLATILE_COMPONENT);
		final String casNumber = request
				.getParameter(ComponentView.PROP_CAS_NUMBER);
		final String minPHStr = request.getParameter("min"
				+ ComponentView.PROP_PH);
		final String maxPHStr = request.getParameter("max"
				+ ComponentView.PROP_PH);
		final String sampleName = request.getParameter("sampleName");
		final String sampleId = request.getParameter("sampleId");
		final String description = request.getParameter("sampleDescription");
		final String samplePHStr = request.getParameter("samplePH");
		final String molecularWeight = request
				.getParameter(SampleView.PROP_MOLECULAR_WEIGHT);
		final String numSubUnitsStr = request
				.getParameter(SampleView.PROP_NUM_SUB_UNITS);
		final String batchReference = request
				.getParameter(SampleView.PROP_BATCH_REFERENCE);
		final String origin = request.getParameter(SampleView.PROP_ORIGIN);
		final String type = request.getParameter(SampleView.PROP_TYPE);
		final String cellularLocation = request
				.getParameter(SampleView.PROP_CELLULAR_LOCATION);
		final String concentrationStr = request
				.getParameter(SampleView.PROP_CONCENTRATION);
		;
		final String minCreateDate = request.getParameter("minCreateDate");
		final String maxCreateDate = request.getParameter("maxCreateDate");
		final String giNumberStr = request
				.getParameter(SampleView.PROP_GI_NUMBER);
		final String constructName = request
				.getParameter(SampleView.PROP_CONSTRUCT_NAME);
		final String constructIdStr = request
				.getParameter(SampleView.PROP_CONSTRUCT_ID);
		final String targetName = request
				.getParameter(SampleView.PROP_TARGET_NAME);
		final String targetIdStr = request
				.getParameter(SampleView.PROP_TARGET_ID);
		final String owner = request.getParameter(SampleView.PROP_OWNER);
		final String groupName = request.getParameter(SampleView.PROP_GROUP);
		final String imageDate = request.getParameter(ImageView.PROP_DATE);
		final String inspectionName = request
				.getParameter(ImageView.PROP_INSPECTION_NAME);
		final String instrument = request
				.getParameter(ImageView.PROP_INSTRUMENT);
		final String temperature = request
				.getParameter(ImageView.PROP_TEMPERATURE);
		final String imageType = request.getParameter("imageType");
		final String scoreDate = request.getParameter("scoreDate");
		final String scoreDescription = request
				.getParameter("scoreDescription");
		final String colour = request.getParameter("scoreColour");
		final String annotator = request.getParameter("annotator");
		final String softwareVersion = request.getParameter("version");
		final String scoreType = request.getParameter("scoreType");
		String subPosition = request.getParameter("subPosition");
		if (subPosition == null || subPosition.length() == 0) {
			subPosition = "1";
		}
		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);

			final TrialService trialService = dataStorage.getTrialService();
			final BusinessCriteria criteria = new BusinessCriteria(trialService);
			if ((barcode != null) && (!barcode.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						TrialDropView.PROP_BARCODE, barcode, true));
				crp.getSessionBookmark().put(TrialDropView.PROP_BARCODE,
						barcode);
			}
			if ((well != null) && (!well.equals(""))) {
				criteria.add(BusinessExpression.Equals(TrialDropView.PROP_WELL,
						well, true));
				crp.getSessionBookmark().put(TrialDropView.PROP_WELL, well);
				if (well.contains(".")) {
					final WellPosition position = new WellPosition(well);
					subPosition = "" + position.getSubPosition();
				}
			}
			if ((synchrotronStr != null) && (!synchrotronStr.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						TrialDropView.PROP_SYNCHROTRON, synchrotronStr, true));
				crp.getSessionBookmark().put(TrialDropView.PROP_SYNCHROTRON,
						synchrotronStr);
			}
			if ((localScreenName != null) && (!localScreenName.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ConditionView.PROP_LOCAL_NAME, localScreenName, true));
				crp.getSessionBookmark().put(ConditionView.PROP_LOCAL_NAME,
						localScreenName);
			}
			if ((localScreenNumber != null) && (!localScreenNumber.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ConditionView.PROP_LOCAL_NUMBER, localScreenNumber,
						true));
				crp.getSessionBookmark().put(ConditionView.PROP_LOCAL_NUMBER,
						localScreenNumber);
			}
			if ((manufacturerName != null) && (!manufacturerName.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ConditionView.PROP_MANUFACTURER_NAME, manufacturerName,
						true));
				crp.getSessionBookmark().put(
						ConditionView.PROP_MANUFACTURER_NAME, manufacturerName);
			}
			if ((manufacturerScreenName != null)
					&& (!manufacturerScreenName.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ConditionView.PROP_MANUFACTURER_SCREEN_NAME,
						manufacturerScreenName, true));
				crp.getSessionBookmark().put(
						ConditionView.PROP_MANUFACTURER_SCREEN_NAME,
						manufacturerScreenName);
			}
			if ((manufacturerCode != null) && (!manufacturerCode.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ConditionView.PROP_MANUFACTURER_CODE, manufacturerCode,
						true));
				crp.getSessionBookmark().put(
						ConditionView.PROP_MANUFACTURER_CODE, manufacturerCode);
			}
			if ((manufacturerCatCode != null)
					&& (!manufacturerCatCode.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ConditionView.PROP_MANUFACTURER_CAT_CODE,
						manufacturerCatCode, true));
				crp.getSessionBookmark().put(
						ConditionView.PROP_MANUFACTURER_CAT_CODE,
						manufacturerCatCode);
			}
			if ((volatileConditionStr != null)
					&& (!volatileConditionStr.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ConditionView.PROP_VOLATILE_CONDITION,
						volatileConditionStr, true));
				crp.getSessionBookmark().put(
						ConditionView.PROP_VOLATILE_CONDITION,
						volatileConditionStr);
			}
			if ((saltConditionStr != null) && (!saltConditionStr.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ConditionView.PROP_SALT_CONDITION, saltConditionStr,
						true));
				crp.getSessionBookmark().put(ConditionView.PROP_SALT_CONDITION,
						saltConditionStr);
			}
			if ((componentName != null) && (!componentName.equals(""))) {
				criteria.add(BusinessExpression.Equals(ComponentView.PROP_NAME,
						componentName, true));
				crp.getSessionBookmark().put(ComponentView.PROP_NAME,
						componentName);
			}
			if ((componentType != null) && (!componentType.equals(""))) {
				criteria.add(BusinessExpression.Equals(ComponentView.PROP_TYPE,
						componentType, true));
				crp.getSessionBookmark().put(ComponentView.PROP_TYPE,
						componentType);
			}
			if ((volatileComponentStr != null)
					&& (!volatileComponentStr.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ComponentView.PROP_VOLATILE_COMPONENT,
						volatileComponentStr, true));
				crp.getSessionBookmark().put(
						ComponentView.PROP_VOLATILE_COMPONENT,
						volatileComponentStr);
			}
			if ((casNumber != null) && (!casNumber.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ComponentView.PROP_CAS_NUMBER, casNumber, true));
				crp.getSessionBookmark().put(ComponentView.PROP_CAS_NUMBER,
						casNumber);
			}
			if ((sampleName != null) && (!sampleName.equals(""))) {
				criteria.add(BusinessExpression.Equals(SampleView.PROP_NAME,
						sampleName, true));
				crp.getSessionBookmark().put(SampleView.PROP_NAME, sampleName);
			}
			if ((sampleId != null) && (!sampleId.equals(""))) {
				criteria.add(BusinessExpression.Equals(SampleView.PROP_ID,
						sampleId, true));
				crp.getSessionBookmark().put(SampleView.PROP_ID, sampleId);
			}
			if ((description != null) && (!description.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						SampleView.PROP_DESCRIPTION, description, true));
				crp.getSessionBookmark().put(SampleView.PROP_DESCRIPTION,
						description);
			}
			if ((batchReference != null) && (!batchReference.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						SampleView.PROP_BATCH_REFERENCE, batchReference, true));
				crp.getSessionBookmark().put(SampleView.PROP_BATCH_REFERENCE,
						batchReference);
			}
			if ((origin != null) && (!origin.equals(""))) {
				criteria.add(BusinessExpression.Equals(SampleView.PROP_ORIGIN,
						origin, true));
				crp.getSessionBookmark().put(SampleView.PROP_ORIGIN, origin);
			}
			if ((type != null) && (!type.equals(""))) {
				criteria.add(BusinessExpression.Equals(SampleView.PROP_TYPE,
						type, true));
				crp.getSessionBookmark().put(SampleView.PROP_TYPE, type);
			}
			if ((cellularLocation != null) && (!cellularLocation.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						SampleView.PROP_CELLULAR_LOCATION, cellularLocation,
						true));
				crp.getSessionBookmark().put(SampleView.PROP_CELLULAR_LOCATION,
						cellularLocation);
			}
			if ((giNumberStr != null) && (!giNumberStr.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						SampleView.PROP_GI_NUMBER, giNumberStr, true));
				crp.getSessionBookmark().put(SampleView.PROP_GI_NUMBER,
						giNumberStr);
			}
			if ((constructName != null) && (!constructName.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						SampleView.PROP_CONSTRUCT_NAME, constructName, true));
				crp.getSessionBookmark().put(SampleView.PROP_CONSTRUCT_NAME,
						constructName);
			}
			if ((constructIdStr != null) && (!constructIdStr.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						SampleView.PROP_CONSTRUCT_ID, constructIdStr, true));
				crp.getSessionBookmark().put(SampleView.PROP_CONSTRUCT_ID,
						constructIdStr);
			}
			if ((targetName != null) && (!targetName.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						SampleView.PROP_TARGET_NAME, targetName, true));
				crp.getSessionBookmark().put(SampleView.PROP_TARGET_NAME,
						targetName);
			}
			if ((targetIdStr != null) && (!targetIdStr.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						SampleView.PROP_TARGET_ID, targetIdStr, true));
				crp.getSessionBookmark().put(SampleView.PROP_TARGET_ID,
						targetIdStr);
			}
			if ((inspectionName != null) && (!inspectionName.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ImageView.PROP_INSPECTION_NAME, inspectionName, true));
				crp.getSessionBookmark().put(ImageView.PROP_INSPECTION_NAME,
						inspectionName);
			}
			if ((instrument != null) && (!instrument.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ImageView.PROP_INSTRUMENT, instrument, true));
				crp.getSessionBookmark().put(ImageView.PROP_INSTRUMENT,
						instrument);
			}
			if ((temperature != null) && (!temperature.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ImageView.PROP_TEMPERATURE, temperature, true));
				crp.getSessionBookmark().put(ImageView.PROP_TEMPERATURE,
						temperature);
			}
			if ((groupName != null) && (!groupName.equals(""))) {
				criteria.add(BusinessExpression.Equals(SampleView.PROP_GROUP,
						groupName, true));
				crp.getSessionBookmark().put(SampleView.PROP_GROUP, groupName);
			}
			if ((owner != null) && (!owner.equals(""))) {
				criteria.add(BusinessExpression.Equals(SampleView.PROP_OWNER,
						owner, true));
				crp.getSessionBookmark().put(SampleView.PROP_OWNER, owner);
			}

			if ((scoreDescription != null) && (!scoreDescription.equals(""))) {
				criteria.add(BusinessExpression.Equals("scoreDescription",
						scoreDescription, true));
				crp.getSessionBookmark().put("scoreDescription",
						scoreDescription);
			}

			/*
			 * Do something with these ranges...
			 */
			/*
			 * minFinalpHStr = request.getParameter("min" +
			 * ConditionView.PROP_FINAL_PH); minPHStr =
			 * request.getParameter(ComponentView.PROP_PH); samplePHStr =
			 * request.getParameter("samplePH"); molecularWeight =
			 * request.getParameter(SampleView.PROP_MOLECULAR_WEIGHT);
			 * numSubUnitsStr =
			 * request.getParameter(SampleView.PROP_NUM_SUB_UNITS);
			 * concentrationStr =
			 * request.getParameter(SampleView.PROP_CONCENTRATION);;
			 * minCreateDate = request.getParameter("minCreateDate");
			 * maxCreateDate = request.getParameter("maxCreateDate"); imageDate
			 * = request.getParameter(ImageView.PROP_DATE);
			 */

			final JSONObject object = new JSONObject();
			if (request.getParameter("count") != null) {
				final int screenCount = trialService.findViewCount(criteria);

				object.put("count", screenCount);
			} else {
				criteria.setMaxResults(crp.getMaxResults());
				criteria.setFirstResult(crp.getFirstResult());
				if (crp.isAscending()) {
					criteria.addOrder(BusinessOrder.ASC(crp.getSort()));
				} else {
					criteria.addOrder(BusinessOrder.DESC(crp.getSort()));
				}

				/*
				 * trialService.setCompositeImageURLStub(ImageURL
				 * .getUrl_compositeimages()); trialService
				 * .setSliceImageURLStub(ImageURL.getUrl_sliceimages());
				 * trialService.setZoomedImageURLStub(ImageURL
				 * .getUrl_zoomedimages());
				 */

				final Collection<TrialDropView> trialDrops = trialService
						.findViews(criteria);
				// JMD - Default zero records returned
				int recordsReturned = 0;

				final JSONArray array = new JSONArray();
				if (trialDrops != null) {

					final Iterator<TrialDropView> it = trialDrops.iterator();
					while (it.hasNext()) {
						final TrialDropView trialDrop = it.next();
						if (subPosition.equalsIgnoreCase("-1")
								|| trialDrop.getWell().endsWith(
										"." + subPosition)) {
							array.add(trialDrop.toJSON());
							// System.out.println("Found trialDrop at
							// "+trialDrop.getWell()+" with
							// "+trialDrop.getImages().size()+" images");
						} else {
							// System.out.println("Found trialDrop,not match
							// subPosition("+subPosition+"), at
							// "+trialDrop.getWell()+" with
							// "+trialDrop.getImages().size()+" images");
						}
					}

					// JMD recordsReturned set here to avoid potential NPE
					recordsReturned = trialDrops.size();

				}

				object.put("records", array);
				System.out.println("records" + array.toArray().length);

				/*
				 * JMD - given that this is outside the if (trialDrops != null)
				 * block above, this is a potential NPE
				 * 
				 * object.put("recordsReturned", trialDrops.size());
				 */
				object.put("recordsReturned", recordsReturned);

			}

			this.setContentTypeJson(response);
			this.setNoCache(response);
			response.getWriter().print(object);
		} catch (final BusinessException ex) {
			throw new ServletException(ex);
		}

		// JMD - Added generic catch-all
		catch (final Throwable t) {
			t.printStackTrace(); // TODO no, return error indication to user
		}

		finally {
			// Only store for the non-count request!
			if (null == request.getParameter("count")) {
				crp.storeSessionBookmark(request.getSession(),
						"trialDropsDataTable");
			}
			closeResources(dataStorage);
		}

	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the
	// code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	@Override
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		processRequest(request, response);
	}
}
