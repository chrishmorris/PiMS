/*
 * PlateServlet.java Created on 19 October 2007, 14:54
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.crystallization.Image;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.schedule.ScheduledTask;
import org.pimslims.servlet.ListFiles;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.util.File;

/**
 * 
 * @author Ian Berry
 * @version
 */
public class InspectionImageUpload extends PIMSServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7643062815567251311L;

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
			final String inspectionName = request
					.getParameter("inspectionName");
			final ScheduledTask inspection = wv.findFirst(ScheduledTask.class,
					ScheduledTask.PROP_NAME, inspectionName);
			wv.setDefaultOwner(inspection.getAccess());
			String tab = "";
			final HolderType holderType = (CrystalType) inspection.getHolder()
					.getHolderType();
			// TODO for (int i : type.getSubpositions())
			for (int sub = 1; sub < holderType.getMaxSubPosition(); sub++) {
				// get info from file items
				final java.util.Collection<FileItem> items = ListFiles
						.getFileItems(request);
				String rowcol = "";
				String imageUrl = "";
				String fileName = "";
				java.io.InputStream uploadedStream = null;
				for (final FileItem item : items) {

					if (item.isFormField()) {
						// well
						if (item.getFieldName().equals("imageRowCol" + sub)) {
							rowcol = item.getString();
						}
						// url
						if (item.getFieldName().equals("imageUrl" + sub)) {
							imageUrl = item.getString();
						}
						continue;
					}
					uploadedStream = item.getInputStream();
					fileName = item.getName();
				}
				if (rowcol.length() < 1) {
					continue;
				}
				// get old image
				final WellPosition well = new WellPosition(rowcol + "." + sub);
				Image image = getImageByWell(inspection, well);

				if (imageUrl != null && imageUrl.length() > 0) {
					if (imageUrl.equalsIgnoreCase("none")) {
						if (image != null) {
							image.delete();
						}
					} else {
						if (imageUrl.startsWith("http")) {
							imageUrl = imageUrl.substring(4);
						}
						if (image == null) {
							image = createImage(wv, inspection, well, "http",
									imageUrl);
						} else {
							image.setFilePath("http");
							image.setFileName(imageUrl);
						}
					}
				} else {
					// add image by upload
					final File file = wv.createFile(uploadedStream, fileName,
							inspection);

					if (image == null) {
						image = createImage(wv, inspection, well,
								request.getContextPath() + "/ViewFile/",
								file.getHook() + "/" + file.getName());
					} else {
						image.setFilePath(request.getContextPath()
								+ "/ViewFile/");
						image.setFileName(file.getHook() + "/" + file.getName());
					}
				}

				tab = "images" + sub;
			}
			wv.commit();
			this.redirect(response, request.getContextPath()
					+ "/Update/Inspection?name=" + inspectionName + "#" + tab);
		} catch (final AbortedException e) {
			throw new ServletException(e);
		} catch (final ConstraintException e) {
			throw new ServletException(e);
		} catch (final FileUploadException e) {
			throw new ServletException(e);
		} catch (final AccessException e) {
			throw new ServletException(e);
		} finally {
			if (!wv.isCompleted()) {
				wv.abort();
			}
		}

	}

	private Image createImage(final WritableVersion wv,
			final ScheduledTask inspection, final WellPosition well,
			final String path, final String name) throws ConstraintException {
		final Holder holder = inspection.getHolder();
		final Sample sample = getSampleByWell(holder, well);
		final Image image = new Image(wv, path, name);
		image.setScheduledTask(inspection);
		image.setSample(sample);
		return image;
	}

	private Sample getSampleByWell(final Holder holder, final WellPosition well) {
		for (final Sample sample : holder.getSamples()) {
			if (sample.getRowPosition() == well.getRow()) {
				if (sample.getColPosition() == well.getColumn()) {
					if (sample.getSubPosition() == well.getSubPosition()) {
						return sample;
					}
				}
			}
		}
		return null;
	}

	private Image getImageByWell(final ScheduledTask inspection,
			final WellPosition well) {
		for (final Image image : inspection.getImages()) {
			if (image.getSample().getRowPosition() == well.getRow()) {
				if (image.getSample().getColPosition() == well.getColumn()) {
					if (image.getSample().getSubPosition() == well
							.getSubPosition()) {
						return image;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns a short description of the servlet.
	 */
	@Override
	public String getServletInfo() {
		return "inspection image uploader";
	}
	// </editor-fold>
}
