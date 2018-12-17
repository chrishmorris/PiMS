/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for ViewPlates.jsp
 */
PIMS.xtal.initViewGroup = function () {
	var url, processGroupData, toggleFilter, ftPlates, ftConstructs, ftSamples;

	// Get construct info
	processGroupData = function (transport) {
		var data = PIMS.xtal.parseJson(transport.responseText);
		if (data.records.length > 0) {
			PIMS.xtal.getElement("group_name").innerHTML = data.records[0].groupName;
			PIMS.xtal.getElement("group_head").innerHTML = data.records[0].groupHead;
			PIMS.xtal.getElement("group_organisation").innerHTML = data.records[0].organisation;
			PIMS.xtal.getElement("p_group").value = data.records[0].groupName;
			PIMS.xtal.getElement("ct_group").value = data.records[0].groupName;
			PIMS.xtal.getElement("s_group").value = data.records[0].groupName;
		} else {
			PIMS.xtal.getElement('content').innerHTML = "<h3>You do not have permission to see this group</h3>";
		}
	};
	url = contextPath + "/GroupsServlet?startIndex=0&results=1&sort=groupName&dir=asc&groupName=" + PIMS.xtal.groupName;
	PIMS.xtal.asyncRequest('GET', url, {success: processGroupData, scope: this}, "");

	// Bind filter toggle listeners
	PIMS.xtal.addListener('show_hide_plate_filter', 'click', PIMS.xtal.toggleFilter);
	PIMS.xtal.addListener('show_hide_construct_filter', 'click', PIMS.xtal.toggleFilter);
	PIMS.xtal.addListener('show_hide_sample_filter', 'click', PIMS.xtal.toggleFilter);

	PIMS.xtal.Imagers.init();

	ftPlates = PIMS.xtal.initFilterPlates({
		preset: {
			sb: 'sb_g_p',
			group: PIMS.xtal.groupName
		},
		sbMustMatch: {
			group: PIMS.xtal.groupName
		},
		ignore: ['group']
	});
	ftConstructs = PIMS.xtal.initFilterConstructs({
		preset: {
			sb: 'sb_g_ct',
			group: PIMS.xtal.groupName
		},
		sbMustMatch: {
			group: PIMS.xtal.groupName
		},
		ignore: ['group']
	});
	ftSamples = PIMS.xtal.initFilterSamples({
		preset: {
			sb: 'sb_g_s',
			group: PIMS.xtal.groupName
		},
		sbMustMatch: {
			group: PIMS.xtal.groupName
		},
		ignore: ['group']
	});

	ftPlates.initialize();
	ftConstructs.initialize();
	ftSamples.initialize();

	PIMS.xtal.addListener("p_clear", "click", function () {
		PIMS.xtal.getElement("p_group").value = PIMS.xtal.groupName;
	});
	PIMS.xtal.addListener("ct_clear", "click", function () {
		PIMS.xtal.getElement("ct_group").value = PIMS.xtal.groupName;
	});
	PIMS.xtal.addListener("s_clear", "click", function () {
		PIMS.xtal.getElement("s_group").value = PIMS.xtal.groupName;
	});

	PIMS.xtal.getElement("p_group").readOnly = 'readonly';
	PIMS.xtal.getElement("ct_group").readOnly = 'readonly';
	PIMS.xtal.getElement("s_group").readOnly = 'readonly';

	PIMS.xtal.initializeHistory();
};
