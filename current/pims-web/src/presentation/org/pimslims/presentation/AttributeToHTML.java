/*
 * Created on 14.07.2005
 */
package org.pimslims.presentation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.constraint.Constraint;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Utils;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaProperty;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.presentation.create.AttributeValueMap;
import org.pimslims.presentation.create.RoleHooksHolder;
import org.pimslims.presentation.servlet.utils.ValueFormatter;

/**
 * 
 * @author Petr Troshin
 * 
 *         Class to generate html and javascript out of MetaAttribute and
 *         MetaRole Used by Create and ChooseForCreate
 * 
 *         For edits and custom creates, similar functionality is provided by
 *         the PiMS tag library. This class was created before JSPs supported
 *         tags.
 */
public class AttributeToHTML {

	/**
     * 
     */
	public static final String ACTION = "ACTION";

	private boolean required;

	// public boolean isChangeable; Do we need to support this?
	private String javaclassName;

	private Object value;

	private int length, rows;

	private String alias;

	private String name;

	private String exampleValue;

	private Class type;

	private String jsLocal = "", jsGlobal = "";

	private String html = "";

	private MetaRole role;

	private final MetaProperty metaElement;

	private String roleAssociations = "";

	private final String context;

	private Constraint constr;

	private ArrayList enumerationC = null;

	private static final String NB = "&nbsp;";

	private int htmlType; // html type of attribute

	public static final String CLASS_ATTRIBUTE_SEPARATOR = ":";

	// TODO should probably be "&amp;"
	private static final String CLASS_PARAMETER_SEPARATOR = "&";

	private static final Map htmlTypes;
	static {
		htmlTypes = new HashMap();
		AttributeToHTML.htmlTypes.put("checkbox", "1");
		AttributeToHTML.htmlTypes.put("radio", "2");
		AttributeToHTML.htmlTypes.put("textfield", "3");
		AttributeToHTML.htmlTypes.put("filefield", "4");
		AttributeToHTML.htmlTypes.put("menu", "5");
		AttributeToHTML.htmlTypes.put("list", "6");
		AttributeToHTML.htmlTypes.put("textarea", "7");
		AttributeToHTML.htmlTypes.put("hidden", "8");
	}

	private static final String READONLY = " readonly=\"true\" ";

	private final String readonly;

	public static final Boolean SEQ_STRING_IS_PROTEIN = Boolean.TRUE;

	public static final Boolean SEQ_STRING_IS_NUCLEIC_ACID = Boolean.FALSE;

	private final Boolean protein;

	// used in jsps
	public MetaProperty getMetaElement() {
		return this.metaElement;
	}

	// used by custom getter for generic create
	public AttributeToHTML(final MetaRole role, final MetaClass parentClass,
			final RoleHooksHolder param, final ReadableVersion rv,
			final String context, final AttributeValueMap values,
			final Boolean sequenceType) {

		this.protein = sequenceType;
		this.readonly = role.isChangeable() ? "" : AttributeToHTML.READONLY;
		this.context = context;
		final MetaClass metaClass = role.getOtherMetaClass();
		this.javaclassName = metaClass.getMetaClassName();
		// role.getOwnMetaClass();
		this.name = role.getRoleName();
		this.type = role.getClass();
		this.alias = this.name;
		final int card = role.getLow();
		this.required = (card >= 1) ? true : false;
		// role.getHigh();

		if (this.required) {
			this.writeJSLocal();
		}

		this.role = role;
		this.metaElement = role;
		this.value = "";

		this.getHTMLForRole(parentClass, param, rv, values);
		this.prepareRoleAssociations(rv, param);
	}

	/* Public interface for JSP */
	public String getName() {
		assert this.metaElement.getName().equals(this.name);
		return this.name;
	}

	public boolean getRequired() {
		return this.required;
	}

	// used in CreateRoles.jsp
	public String getRoleAssociations() {
		return this.roleAssociations;
	}

	public String getHtml() {
		return this.html;
	}

	/*
	 * public String getExampleValue() { return this.exampleValue; }
	 */

	private void prepareRoleAssociations(final ReadableVersion rv,
			final RoleHooksHolder parameters) {
		this.roleAssociations = "no " + this.role.getAlias() + " set";
		if (parameters != null) {
			final String[] hooks = parameters.getFullHooks(this.name);
			if (hooks != null) {
				this.roleAssociations = "";
				for (int i = 0; i < hooks.length; i++) {
					this.roleAssociations += AttributeToHTML.hooksToLink(rv,
							this.name, hooks[i], this.context) + "<br />";
				}
			}
		}
	}

	/**
	 * To use on the JSP Seems to quotes escape for Javascript, not < for HTML
	 * 
	 * @param text
	 * @return
	 * 
	 * @Deprecated // use StringEscapeUtils.escapeJavaScript or
	 *             StringEscapeUtils.escapeHtml public static String
	 *             getEscapedText(final String text) { return (text != null &&
	 *             text.trim().length() != 0) ? AttributeToHTML.escapeText(text,
	 *             false) : ""; }
	 */

	@Deprecated
	// use a standard utility for this
	private static String escapeText(final String text, final boolean helpText) {
		StringBuffer sb = null;
		if (text != null) {
			boolean escp = false;
			sb = new StringBuffer(text);
			for (int i = 0; i < sb.length(); i++) {
				final char c = sb.charAt(i);
				switch (c) {

				case '"':
					if (escp) {
						escp = false;
					} else {
						sb.replace(i, i + 1, "&quot;");
						i += 5;
					}
					break;

				case '\'':
					if (escp) {
						escp = false;
					} else {
						// sb.replace(i, i+1, "&quot;");
						sb.replace(i, i + 1, "&#39;");
						i += 5;
					}
					break;

				case '/':
					if (helpText) {
						sb.replace(i, i + 1, "\\/");
						i += 1;
					}
					break;

				case '\\':
					escp = true;
					if (!helpText) {
						sb.deleteCharAt(i);
					}
					break;
				default:
					// needs no excaping
					break;
				}
			}
		}
		return sb.toString();
	}

	// TODO we would rather use ModelObjectShortBeans
	public static String hooksToLink(final ReadableVersion rv,
			final String roleName, final String hook, final String context) {
		return "<a href=\"" + context + "/View/" + hook
				+ "\" target=\"_blank\" >" + ServletUtil.getName(rv, hook)
				+ "</a>";
		// Use this to write hooks on the page (may be needed if removing of
		// hooks are permitted in CustomCreate)
		// + writeHidden(roleName, hook);
	}

	protected ArrayList getSubRoles() {
		final Set sub = this.role.getOtherMetaClass().getSubtypes();
		final ArrayList subClasses = new ArrayList(sub.size());
		if (sub != null) {
			// int i = -1;
			for (final Iterator iter = sub.iterator(); iter.hasNext();) {
				final MetaClass subMetaClass = (MetaClass) iter.next();
				// i++;
				subClasses.add(subMetaClass);
			}
		}
		return subClasses;
	}

	protected void getHTMLForRole(final MetaClass parentClass,
			final RoleHooksHolder parameters, final ReadableVersion rv,
			final AttributeValueMap values) {
		// String fieldName = makeFieldName(javaclassName, name);
		// fieldName = fieldName.substring(1, fieldName.length());

		final MetaClass roleObjClass = ServletUtil.getMetaClassForRole(
				parentClass, this.name);
		if (parameters != null) {
			if (!parameters.canAssociateMore(this.role)) {
				this.html += "<!-- !parameters.canAssociateMore(this.role) -->";
			} else {
				this.html += this.getLinks(roleObjClass, parentClass,
						parameters, values);
			}
		} else {
			this.html += this.getLinks(roleObjClass, parentClass, parameters,
					values);
		}

	}

	private String getLinks(final MetaClass roleObjClass,
			final MetaClass parentClass, final RoleHooksHolder hooks,
			final AttributeValueMap values) {

		final StringBuffer sb = new StringBuffer();
		ArrayList subMetaClasses = new ArrayList();
		final Random r = new Random();
		final long unic = r.nextLong();
		if (roleObjClass.isAbstract()) {
			subMetaClasses = this.getSubRoles();
			// TODO This is just a search link - to be unified later.
			// actionshead actions actionsimg actionsbody
			sb.append("<div  id='" + unic + "head" + "'>" // TODO class=
					/*
					 * + "<img  src='" + this.context +
					 * "/images/icons/themes/blue/plus.gif' class='button' onclick='toggleView(\""
					 * + unic + "\", \"" + this.context + "\")' id='" + unic +
					 * "img" + "'>"
					 */
			);

			sb.append(AttributeToHTML.NB + "All" + AttributeToHTML.NB + "types"
					+ AttributeToHTML.NB + "of" + AttributeToHTML.NB + "<b>"
					+ ServletUtil.getPIMSMetaClass(roleObjClass).getAlias()
					+ "</b>" + AttributeToHTML.NB
					+ "<a onclick='return warnChange()' href=\"" + this.context
					+ "/ChooseForCreate/" + parentClass.getMetaClassName()
					+ "/" + this.name
					+ AttributeToHTML.encodedParameterString(hooks, values)
					+ "\"" + " >Search/Add</a><br/>" + "</div>");
			sb.append("<div id='" + unic + "body" + "' >");
		} else {
			subMetaClasses.add(roleObjClass);
		}
		// int count = -1;
		for (final Iterator iter = subMetaClasses.iterator(); iter.hasNext();) {
			// count++;
			final MetaClass metaClass = (MetaClass) iter.next();

			// String subIndx = "";
			String subClassName = "";
			final String subClassFullName = "";
			if (roleObjClass.isAbstract()) {
				// subIndx = count + "*";
				subClassName = ServletUtil.getPIMSMetaClass(metaClass)
						.getAlias();
			}
			// remove this to set subClassFullName =
			// metaClass.getMetaClassName();
			if (ServletUtil.containsRequiredRoles(roleObjClass)) {
				sb.append(this.getSearchLink(parentClass, subClassFullName,
						hooks, values));
			} else {
				// final String link = parentClass.getMetaClassName() + ":" +
				// subIndx + this.name;
				sb.append("<b>" + subClassName + "</b>"
				// + this.getCreateLink(this.context + "/Create/", link,
				// "", hooks, AttributeToHTML.NB + "Set" +
				// AttributeToHTML.NB + "new")
				// + AttributeToHTML.NB + "|" + AttributeToHTML.NB
						+ this.getSearchLink(parentClass, subClassFullName,
								hooks, values) + "<br/>\n");
			}

		}
		if (roleObjClass.isAbstract()) {
			sb.append("</div>");
			/*
			 * sb.append(
			 * "<script type='text/javascript'>if(document.getElementById) {document.getElementById('"
			 * + unic + "body" + "').style.display=\'none\'; }</script>");
			 */
		}

		return sb.toString();
	}

	// ChooseForCreate/org.pimslims.model.target.Target/citations
	// TODO needs support from ChooseForCreate to search through concrete
	// subclasses
	private String getSearchLink(final MetaClass metaClass,
			final String actionMetaClassName, final RoleHooksHolder hooks,
			final AttributeValueMap values) {
		// MetaClass otherMetaClass =
		// metaClass.getMetaRole(name).getOtherMetaClass();

		String metaClassName = metaClass.getMetaClassName();
		if (actionMetaClassName.length() != 0) {
			metaClassName = actionMetaClassName;
		}

		return "<a onclick='return warnChange()' href=\"" + this.context
				+ "/ChooseForCreate/" + metaClassName + "/" + this.name
				+ AttributeToHTML.encodedParameterString(hooks, values) + "\""
				+ " >Search/Add</a>";
	}

	private static String encodedParameterString(final RoleHooksHolder hooks,
			final AttributeValueMap values) {

		final StringBuffer sb = new StringBuffer();
		final StringBuffer sb2 = new StringBuffer();
		if (null != hooks) {
			sb.append(hooks.toDecodedParamString());
		}

		if (sb.length() > 0) {
			sb.append(AttributeToHTML.CLASS_PARAMETER_SEPARATOR);
		}

		sb.append(values.toDecodedParamString());

		try {
			if (sb.length() > 0) {
				sb2.append("?" + AttributeToHTML.ACTION + "=");
				sb2.append(java.net.URLEncoder.encode(sb.toString(), "UTF-8"));
			}
		} catch (final UnsupportedEncodingException e) {
			// should not happen
			throw new RuntimeException(e);
		}
		return sb2.toString();
	}

	/**
     * 
     */
	public AttributeToHTML(final MetaAttribute attribute,
			final String[] values, final String context,
			final Boolean sequenceType) {
		this.readonly = attribute.isChangeable() ? ""
				: AttributeToHTML.READONLY;
		this.metaElement = attribute;
		this.context = context;
		this.protein = sequenceType;
		this.setUp(attribute, values);
	}

	private void setUp(final MetaAttribute attribute, final String[] values) {
		this.name = attribute.getName();
		if (this.alias == null || this.alias.length() == 0) {
			this.alias = this.name;
		}
		// Get set value from values otherwise getDefaultValue
		if (values != null) {
			this.value = values[0];
		} else {
			this.value = attribute.getDefaultValue();
		}
		// If value is not null set exampleValue to ""
		if (this.exampleValue == null) {
			this.exampleValue = "";
		}
		// If there is no value set value = ""
		if (this.value == null) {
			this.value = "";
		}

		this.javaclassName = attribute.getMetaClass().getJavaClass().getName();
		this.type = attribute.getType();

		this.length = attribute.getLength();
		this.required = attribute.isRequired();
		if (this.length == 0) {
			this.rows = 5;
		} else {
			this.rows = this.length / 50;
			if (this.rows > 20) {
				this.rows = 20;
			}
		}
		this.constr = attribute.getConstraint();
		this.setConstraintValues();

		if (attribute.isRequired()) {
			this.writeJSLocal();
		}

		this.getHTMLForAttribute(attribute);
	}

	// <name=org.pimslims.model.target.Target:commonName>
	protected String makeFieldName(final String javaclassName,
			final String attributeName) {
		return "\"" + javaclassName + AttributeToHTML.CLASS_ATTRIBUTE_SEPARATOR
				+ attributeName + "\"";
	}

	// <input type="radio" name="RadioGroup1" value="radio" />
	protected void writeRadio(final int radioNumber, final int checkedNumber,
			String[] names) {
		if (names == null && radioNumber == 2) {
			names = new String[] { "Yes", "No" };

		} else if (names == null && radioNumber == 3) {
			names = new String[] { "Yes", "No", "Not specified" };
		} else {
			throw new AssertionError(
					"You must provide the names for the choice! ");
		}
		for (int i = 0; i < radioNumber; i++) {
			final String checked = (i == checkedNumber) ? "checked=\"checked\""
					: "";
			this.html += "<input onchange='onEdit()' type=\"radio\" class=\"radio\" name="
					+ this.makeFieldName(this.javaclassName, this.name)
					+ " value=\""
					+ names[i]
					+ "\" "
					+ checked
					+ " />"
					+ names[i] + " ";
		}
		this.htmlType = (new Integer(
				(String) AttributeToHTML.htmlTypes.get("radio"))).intValue();
	}

	protected void writeTextField(int maxLength) {

		if (this.length > 0 && this.length <= 80) {
			maxLength = this.length;
		}
		this.html += "<input onchange='onEdit()' name="
				+ this.makeFieldName(this.javaclassName, this.name) + " id="
				+ this.makeFieldName(this.javaclassName, this.name)
				+ " value=\""
				+ StringEscapeUtils.escapeXml(this.value.toString()) + "\""
				+ " type=\"text\"" + " class=\"text\"" + this.readonly
				+ " maxlength=" + "\"" + maxLength + "\"" + " />";
		this.htmlType = (new Integer(
				(String) AttributeToHTML.htmlTypes.get("textfield")))
				.intValue();
	}

	// <textarea name="whyChosen" cols="10" rows="3">dscsc</textarea>
	protected void writeTextArea() {
		this.html += "<textarea onchange='onEdit()' id="
				+ this.makeFieldName(this.javaclassName, this.name) + " name="
				+ this.makeFieldName(this.javaclassName, this.name)
				+ " rows=\"" + this.rows + "\"" + this.readonly + " >"
				+ StringEscapeUtils.escapeXml((String) this.value)
				+ "</textarea>\n";
		this.htmlType = (new Integer(
				(String) AttributeToHTML.htmlTypes.get("textarea"))).intValue();
	}

	/*
	 * public void writeList() { this.htmlType = (new Integer((String)
	 * AttributeToHTML.htmlTypes.get("list"))).intValue(); }
	 */
	/*
	 * public void writeHidden() { this.html += this.value + "<input name=" +
	 * this.makeFieldName(this.javaclassName, this.name) +
	 * " type=\"hidden\" value=\"" + this.value + "\" />\n"; this.htmlType =
	 * (new Integer((String)
	 * AttributeToHTML.htmlTypes.get("hidden"))).intValue(); }
	 */

	private void writeMenu(final String[] values) {
		this.html = "<select onchange='onEdit()' id="
				+ this.makeFieldName(this.javaclassName, this.name) + "  name="
				+ this.makeFieldName(this.javaclassName, this.name) + ">";
		if (!this.required) {
			this.html += "<option value=\"\" selected></option>";
		}
		for (int i = 0; i < values.length; i++) {
			this.html += "<option value=\"" + values[i] + "\">" + values[i]
					+ "</option>";
		}
		this.html += "</select>";
		this.htmlType = (new Integer(
				(String) AttributeToHTML.htmlTypes.get("menu"))).intValue();
	}

	// <input type="checkbox" name="checkbox" value="checkbox" />
	private String writeCheckBox(final int checkBoxNumber) {
		this.htmlType = (new Integer(
				(String) AttributeToHTML.htmlTypes.get("checkbox"))).intValue();
		return "";
	}

	// <input type="file" name="file" /> // REMEMBER FORM ENCTYPE !
	private String writeFileField() {
		this.htmlType = (new Integer(
				(String) AttributeToHTML.htmlTypes.get("filefield")))
				.intValue();
		return "";
	}

	// <input type="reset" value=" ... " onclick="return showCalendar('sel3',
	// '%Y-%m-%d');">
	protected void writeCalendarButton(final String bvalue) {
		/*
		 * html += "&nbsp;<input type=\"reset\" class=\"button\" id=\"button"+
		 * name + "\"" +" value=\"" +bvalue+ "\" />";
		 */
		this.html += "&nbsp;<img src=\"" + this.context
				+ "/images/cal_icon.gif\" id=\"button" + this.name + "\""
				// + " style = \"cursor: pointer; border: 1px solid blue; \" "
				+ " title=\"Click for calendar\" /> ";
	}

	private void writeJSLocal() {
		this.jsLocal += "attachValidation("
				+ this.makeFieldName(this.javaclassName, this.name)
				+ ",{required:true,alias:'" + this.alias + "'});\n";
	}

	// TODO update this for time+date
	private String writeCalendarJS() {
		return "\nCalendar.setup({"
				+ "\n    inputField     :    "
				+ this.makeFieldName(this.javaclassName, this.name)
				+ "," // id
				// of
				// the
				// input
				// field
				+ "\n    ifFormat       :    \"" + Utils.calendar_date_format
				+ "\"," // %m/%d/%Y
				// %I:%M
				// %p
				// format
				// of
				// the
				// input
				// field
				// +"\n showsTime : false," // will display a time selector
				+ "\n    button         :    \"button" + this.name + "\"," // trigger
				// for
				// the
				// calendar
				// (button
				// ID)
				+ "\n    singleClick    :    true," // double-click mode
				+ "\n    step           :    1" // show all years in drop-down
				// boxes (instead of every other
				// year as default)
				+ "\n});";
	}

	// writeHelp
	protected void getHTMLForAttribute(final MetaAttribute attribute) {
		// if(!isChangeable && enumerationC == null) {
		// writeHidden();
		// }
		if (this.enumerationC != null && this.enumerationC.size() != 0) {
			this.writeMenu((String[]) this.enumerationC
					.toArray(new String[this.enumerationC.size()]));
		} else if (this.type == Boolean.class) {
			// set default, note that value could be null
			int radiosel;
			if (this.value == Boolean.TRUE) {
				radiosel = 0; // Yes
			} else if (this.value == Boolean.FALSE) {
				radiosel = 1; // No
			} else {
				radiosel = 2; // Undefined
			}
			System.out.println("radiosel " + radiosel);
			if (this.required) {
				this.writeRadio(2, radiosel == 2 ? 1 : radiosel, null);
			} else {
				this.writeRadio(3, radiosel, null);
			}
		} else if (this.type == java.lang.Float.class) {
			// LATER add javascript validation
			this.writeTextField(Float.toString(Float.MAX_VALUE).length() - 1);
			this.jsLocal += "attachValidation("
					+ this.makeFieldName(this.javaclassName, this.name)
					+ ",{numeric:true, alias:'" + this.alias + "'});\n";
		} else if (this.type == java.lang.String.class) {
			if (this.isTextField()) {
				// will be overriden
				this.writeTextField(1);
			} else {
				this.writeTextArea();
				// TODO Change this!

				if (this.name.indexOf("seqString") >= 0 && this.protein != null) {
					if (this.protein.booleanValue()) {
						this.jsLocal += "attachValidation("
								+ this.makeFieldName(this.javaclassName,
										this.name)
								+ ",{proteinSequence:true,alias:'" + this.alias
								+ "'});\n";
					} else {
						this.jsLocal += "attachValidation("
								+ this.makeFieldName(this.javaclassName,
										this.name)
								+ ",{dnaSequence:true,alias:'" + this.alias
								+ "'});\n";
					}
				}
			}
		} else if (this.type == Calendar.class) {
			this.jsLocal += "attachValidation("
					+ this.makeFieldName(this.javaclassName, this.name)
					+ ",{date:true,alias:'" + this.alias + "'});\n";
			this.jsGlobal += this.writeCalendarJS(); // "%Y-%m-%d"
			// TODO The date format ideally should be the same as passed
			// to writeCalendarJS method
			if (this.value == null || this.value.equals("")) {
				// use now as default time
				this.value = this.getDate(); // Obtain
				// current
				// date
				// value
			}
			this.writeTextField(10); // write a textfield with the date as value
			this.writeCalendarButton("..."); // add calendar to the textfield
			this.html += "<script>document.write('<span title=\"'+getClockChange()+'\">'+getOffsetString(new Date())+'</span>');</script>";
		} else if (this.type == String[].class
				|| this.type == java.util.List.class) {
			if (this.isTextField()) {
				// will be overriden
				this.writeTextField(1);
			} else {
				this.writeTextArea();
			}
		} else if (this.type == Integer.class) {
			this.writeTextField(Integer.toString(Integer.MAX_VALUE).length() - 1);
			this.jsLocal += "attachValidation("
					+ this.makeFieldName(this.javaclassName, this.name)
					+ ",{javaInt:true,alias:'" + this.alias + "'});\n";
		} else if (this.type == Long.class) {
			this.writeTextField(Long.toString(Long.MAX_VALUE).length() - 1);
			this.jsLocal += "attachValidation("
					+ this.makeFieldName(this.javaclassName, this.name)
					+ ",{wholeNumber:true,alias:'" + this.alias + "'});\n";
		} else {
			System.out.println("type " + this.type);
			// other type, not yet handled, display without edit
			throw new AssertionError("Attribute " + this.alias
					+ " has unsupported type: " + this.type);
		}

	}

	/**
	 * Write an input field for dates
	 * 
	 * @param name
	 *            the value for the name element, as String or Timestamp
	 * @param date
	 *            the current value
	 */
	// TODO update for date+time
	private String getDate() {
		return ValueFormatter.formatDate(Calendar.getInstance());
	}

	/**
	 * Write an input field for dates
	 * 
	 * @param date
	 *            the current value
	 * @web function
	 * 
	 *      public static String getDate(Calendar date) { final SimpleDateFormat
	 *      sdf = AttributeToHTML.getDateFormat(); if (date == null) { date =
	 *      Calendar.getInstance(); } return sdf.format(date.getTime()); }
	 */

	/**
	 * Used only for Leeds pages. See pims.tld.
	 * 
	 * @web function AttributeToHTML.getMonthAgoDate
	 * @Deprecated // Leeds code is no longer supported public static String
	 *             getMonthAgoDate() { final Calendar date =
	 *             Calendar.getInstance(); date.roll(Calendar.MONTH, -1); return
	 *             AttributeToHTML.getDate(date); }
	 */

	boolean isTextField() {
		if (this.length > 0 && this.length <= 80) {
			return true;
		}
		return false;
	}

	/**
	 * Set values for the constrained fields
	 * 
	 * @param constr
	 *            instance of ccp.api.metamodel.Constraint class
	 * @param objectAttr
	 *            - hashmap of ModelObject attributes to fill in
	 * @param element
	 *            - the name of the ModelObject attribute to set up
	 * @return the correct value for the ModelObject attribute (element)
	 */
	private boolean setConstraintValues() {
		boolean set = false;
		if (this.constr instanceof org.pimslims.constraint.Constraint.EnumerationConstraint) {
			this.enumerationC = new ArrayList(
					((org.pimslims.constraint.Constraint.EnumerationConstraint) this.constr).allowedValues);
			set = true;
		}
		// if(constr instanceof org.pimslims.constraint.Constraint.) {
		return set;
	}

	/**
	 * @param html
	 *            The html to set.
	 */
	public void setHtml(final String html) {
		this.html = html;
	}

	/**
	 * @return Returns the required.
	 */
	public boolean isRequired() {
		assert this.required == this.metaElement.isRequired();
		return this.required;
	}

	/**
	 * @return Returns the htmlType.
	 */
	public int getHtmlType() {
		return this.htmlType;
	}

	/**
	 * @return Returns the jsGlobal.
	 */
	public String getJsGlobal() {
		return this.jsGlobal;
	}

	/**
	 * @return Returns the jsLocal.
	 */
	public String getJsLocal() {
		return this.jsLocal;
	}

}
