/*
 * PlateServlet.java Created on 19 October 2007, 14:54
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.view.ImageViewDAO;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.schedule.ScheduledTask;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * @author Bill Lin
 * @version
 */
public class InspectionUpdate extends PIMSServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7643062815567251311L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 */
	protected void processRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		final ReadableVersion rv = this.getReadableVersion(request, response);
		try {
			// ---------------------------------------------------------------------
			// inpsection
			final String inspectionName = request.getParameter("name");
			final ScheduledTask inspection = rv.findFirst(ScheduledTask.class,
					ScheduledTask.PROP_NAME, inspectionName);
			request.setAttribute("inspection", inspection);

			// plate
			final Holder holder = rv.get(Holder.class, inspection.getHolder()
					.getDbId());
			request.setAttribute("holder", holder);

			// Imagers
			final Collection<Instrument> imagers = rv.getAll(Instrument.class,
					0, 1000);
			request.setAttribute("imagers", imagers);
			if (null != inspection.getInstrument()) {
				request.setAttribute("currentImagerId", inspection
						.getInstrument().getDbId());
			}
			// ---------------------------------------------------------------------
			// images
			final CrystalType holderType = (CrystalType) holder.getHolderType();
			// for size
			final int maxRow = holderType.getMaxRow();
			final int maxCol = holderType.getMaxColumn();
			// note that this assumes that the reservoir is the last subposition
			final int maxSub = holderType.getMaxSubPosition() - 1;

			final String[] rows = new String[maxRow];
			final String[] cols = new String[maxCol];
			System.arraycopy(HolderFactory.ROWS, 0, rows, 0, maxRow);
			System.arraycopy(HolderFactory.COLUMNS, 0, cols, 0, maxCol);
			request.setAttribute("rows", rows);
			request.setAttribute("cols", cols);
			final List<Integer> subs = new LinkedList<Integer>();
			for (int i = 0; i < maxSub; i++) {
				// or if i!=holderType.getResSubPosition()
				subs.add(i);
			}
			request.setAttribute("subs", subs);

			final ImageViewDAO imageDAO = new ImageViewDAO(rv);
			final BusinessCriteria imageCriteria = new BusinessCriteria(
					imageDAO);
			imageCriteria.add(BusinessExpression.Equals(ImageView.PROP_BARCODE,
					holder.getName(), true));
			imageCriteria
					.add(BusinessExpression.Equals(
							ImageView.PROP_INSPECTION_NAME,
							inspection.getName(), true));
			final Collection<ImageView> imageViews = imageDAO
					.findViews(imageCriteria);
			final List<ImageView>[] imageViewList = getImageViewList(
					imageViews, holderType);
			request.setAttribute("imageViewList", imageViewList);

			// ----------------------------------------------------------------------------
			// Finished & forward
			//
			final RequestDispatcher rd = request
					.getRequestDispatcher("/InspectionUpdate.jsp");
			rd.forward(request, response);

			rv.commit();
		} catch (final AbortedException e) {
			throw new ServletException(e);
		} catch (final ConstraintException e) {
			throw new ServletException(e);
		} catch (final BusinessException e) {
			throw new ServletException(e);
		} finally {
			if (!rv.isCompleted()) {
				rv.abort();
			}
		}
	}

	private List<ImageView>[] getImageViewList(
			final Collection<ImageView> imageViews, final HolderType holderType) {
		final int maxCol = holderType.getMaxColumn();
		final int maxRow = holderType.getMaxRow();
		final int maxSub = holderType.getMaxSubPosition() - 1;
		final List<ImageView>[] imageViewList = new LinkedList[maxSub];
		for (int i = 0; i < maxSub; i++) {
			imageViewList[i] = new LinkedList<ImageView>();
		}
		final Map<WellPosition, ImageView> wellWithImages = new HashMap<WellPosition, ImageView>();
		for (final ImageView imageView : imageViews) {
			final WellPosition well = new WellPosition(imageView.getWell());

			wellWithImages.put(well, imageView);
		}
		for (int sub = 0; sub < maxSub; sub++) {
			// or if i!=holderType.getResSubPosition()
			for (int row = 0; row < maxRow; row++) {
				for (int col = 0; col < maxCol; col++) {
					final WellPosition well = new WellPosition(row + 1,
							col + 1, sub + 1);
					ImageView imageview = wellWithImages.get(well);
					if (imageview == null) {
						imageview = new ImageView();
						imageview.setWell(well.toString());
					}
					imageViewList[sub].add(imageview);
				}
			}

		}
		return imageViewList;
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
	// the + sign on the left to edit the code.">
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
		processPostRequest(request, response);
	}

	private void processPostRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		final WritableVersion wv = getWritableVersion(request, response);

		try {
			final String oldInspectionName = request.getParameter("name");
			final ScheduledTask inspection = wv.findFirst(ScheduledTask.class,
					ScheduledTask.PROP_NAME, oldInspectionName);
			final String inspectionName = request
					.getParameter("inspectionName");
			inspection.setName(inspectionName);

			final String plateID = request.getParameter("holderID");
			final Holder holder = wv.get(Holder.class, new Long(plateID));
			// name
			// Screen
			final Instrument imager = wv.get(Instrument.class,
					new Long(request.getParameter("imagerID")));
			inspection.setInstrument(imager);

			inspection.setDetails(request.getParameter("description"));

			wv.commit();
			this.redirect(response,
					request.getContextPath()
							+ "/EditPlate/org.pimslims.model.holder.Holder:"
							+ holder.getName() + "#inspections");
		} catch (final AbortedException e) {
			throw new ServletException(e);
		} catch (final ConstraintException e) {
			throw new ServletException(e);
		} finally {
			if (!wv.isCompleted()) {
				wv.abort();
			}
		}
	}

	/**
	 * Returns a short description of the servlet.
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}
	// </editor-fold>
}
