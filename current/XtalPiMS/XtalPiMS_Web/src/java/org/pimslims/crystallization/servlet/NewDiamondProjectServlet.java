/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.access.Access;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * @author ejd53
 */
public class NewDiamondProjectServlet extends PIMSServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		WorkingBean wb = new WorkingBean();
		request.setAttribute("error", wb.getError());
		request.setAttribute("message", wb.getMessage());
		request.setAttribute("proposal", wb.getProposalCode());
		request.setAttribute("acronym", wb.getProteinAcronym());
		RequestDispatcher rd = request
				.getRequestDispatcher("/NewDiamondProject.jsp");
		rd.forward(request, response);

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		WorkingBean wb = new WorkingBean();
		wb.setUsername(request.getRemoteUser());
		wb.setProposalCode(request.getParameter("proposal").toLowerCase()
				.trim());
		wb.setProteinAcronym(request.getParameter("acronym").trim());

		WritableVersion version = ModelImpl.getModel().getWritableVersion(
				Access.ADMINISTRATOR);
		try {
			processRequest(version, wb);
			if ("".equals(wb.getError())) {
				version.commit();
			} else {
				version.abort();
			}
		} catch (AbortedException e) {
			wb.setError("Could not create project due to a database problem.");
		} catch (ConstraintException e) {
			wb.setError("A project called " + wb.getProposalCode() + "."
					+ wb.getProteinAcronym() + "already exists");
		} finally {
			if (!version.isCompleted()) {
				version.abort();
				wb.setError("Could not create project due to a database problem.");
			}
		}
		if (wb.getError().equals("")) {
			wb.setMessage("Project " + wb.getProposalCode() + "."
					+ wb.getProteinAcronym() + " created.");
			wb.setProposalCode("");
			wb.setProteinAcronym("");
		} else {
			wb.setMessage("");
		}

		request.setAttribute("error", wb.getError());
		request.setAttribute("message", wb.getMessage());
		request.setAttribute("proposal", wb.getProposalCode());
		request.setAttribute("acronym", wb.getProteinAcronym());
		RequestDispatcher rd = request
				.getRequestDispatcher("/NewDiamondProject.jsp");
		rd.forward(request, response);

	}

	@Override
	public String getServletInfo() {
		return "Handles creation of Diamond projects (LN by proposal code and protein acronym)";
	}

	protected void processRequest(WritableVersion version, WorkingBean wb)
			throws ConstraintException {

		String lnName = wb.getProposalCode() + "." + wb.getProteinAcronym();
		if ("".equals(wb.getProteinAcronym())
				|| "".equals(wb.getProposalCode())) {
			wb.setError("Both proposal code and protein acronym are required.");
		} else if (!wb.getProposalCode().matches("^[a-z]{2}[0-9]+$")) {
			wb.setError("Proposal code is two letters followed by numbers, e.g., 'mx4025'.");
		} else {
			boolean proposalExists = false;
			boolean userIsOnProposal = false;
			boolean projectExists = false;
			UserGroup proposalGroup = version.findFirst(UserGroup.class,
					UserGroup.PROP_NAME, wb.getProposalCode());
			if (null != proposalGroup) {
				proposalExists = true;
				Set<User> members = proposalGroup.getMemberUsers();
				Iterator<User> i = members.iterator();
				while (i.hasNext()) {
					User user = (User) i.next();
					if (user.getName().equals(wb.getUsername())) {
						userIsOnProposal = true;
						break;
					}
				}
				LabNotebook existingProject = version.findFirst(
						LabNotebook.class, LabNotebook.PROP_NAME, lnName);
				if (null != existingProject) {
					projectExists = true;
				}
			}
			if (!proposalExists || !userIsOnProposal) {
				wb.setError("Either proposal " + wb.getProposalCode()
						+ " does not exist, or you are not on it.");
			} else if (projectExists) {
				wb.setError("A project called " + wb.getProposalCode() + "."
						+ wb.getProteinAcronym() + " already exists");
			} else {
				UserGroup singleUser = version.findFirst(UserGroup.class,
						UserGroup.PROP_NAME, wb.getUsername());
				if (null == singleUser) {
					singleUser = new UserGroup(version, wb.getUsername());
					User user = version.findFirst(User.class, User.PROP_NAME,
							wb.getUsername());
					singleUser.addMemberUser(user);
				}
				LabNotebook ln = new LabNotebook(version, lnName);
				Permission createPermission = new Permission(version, "create",
						ln, singleUser);
				Permission readPermission = new Permission(version, "read", ln,
						singleUser);
				Permission updatePermission = new Permission(version, "update",
						ln, singleUser);
				Permission deletePermission = new Permission(version, "delete",
						ln, singleUser);
				singleUser.addPermission(createPermission);
				singleUser.addPermission(readPermission);
				singleUser.addPermission(updatePermission);
				singleUser.addPermission(deletePermission);
			}
		}
	}

	// for tests
	protected WorkingBean getWorkingBean() {
		return new WorkingBean();
	}

	protected class WorkingBean {

		private String error = "";
		private String message = "";
		private String proteinAcronym = "";
		private String proposalCode = "";
		private String username = "";

		/**
		 * @return the error
		 */
		public String getError() {
			return error;
		}

		/**
		 * @param error
		 *            the error to set
		 */
		public void setError(String error) {
			this.error = error;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message
		 *            the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		/**
		 * @return the proteinAcronym
		 */
		public String getProteinAcronym() {
			return proteinAcronym;
		}

		/**
		 * @param proteinAcronym
		 *            the proteinAcronym to set
		 */
		public void setProteinAcronym(String proteinAcronym) {
			this.proteinAcronym = proteinAcronym;
		}

		/**
		 * @return the proposalCode
		 */
		public String getProposalCode() {
			return proposalCode;
		}

		/**
		 * @param proposalCode
		 *            the proposalCode to set
		 */
		public void setProposalCode(String proposalCode) {
			this.proposalCode = proposalCode;
		}

		/**
		 * @return the username
		 */
		public String getUsername() {
			return username;
		}

		/**
		 * @param username
		 *            the username to set
		 */
		public void setUsername(String username) {
			this.username = username;
		}

	}

}
