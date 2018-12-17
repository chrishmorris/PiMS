/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*
 * from http://www.quirksmode.org/js/findpos.html
 * returns the x coordinate of supplied object's top-left corner
 */
function findPosX(obj) {
	var curleft = 0;
	if (obj.offsetParent) {
		while (obj.offsetParent) {
			curleft += obj.offsetLeft;
			obj = obj.offsetParent;
		}
	} else if (obj.x) {
		curleft += obj.x;
	}
	return curleft;
}

/*
 * from http://www.quirksmode.org/js/findpos.html
 * returns the y coordinate of supplied object's top-left corner
 */
function findPosY(obj) {
	var curtop = 0;
	if (obj.offsetParent) {
		while (obj.offsetParent) {
			curtop += obj.offsetTop;
			obj = obj.offsetParent;
		}
	} else if (obj.y) {
		curtop += obj.y;
	}
	return curtop;
}

/*
 * modified from http://www.quirksmode.org/js/findpos.html
 * Sets the submenu position relative to its main menu counterpart
 */
function setSubmenuPos(obj, lyr) {
	var topOffset, leftOffset, newX, newY, x;
	//offsets from top left corner of parent menu item, in em
	topOffset = 1.3; //em
	leftOffset = 0; //em

	newX = findPosX(obj);
	newY = findPosY(obj);
	x = document.getElementById(lyr);
	//	x.style.top  = newY + 'px';
	//	x.style.left = newX + 'px';
	x.style.top = '0px';
	x.style.left = '0px';

	x.style.margin = topOffset + "em 0 0 " + leftOffset + "em";
}

/*
 * Shows the submenu with the id submenu_x
 * Assumes that the parent menu item has id menu_x
 */
function showMenu(menuNum) {
	if (document.getElementById("submenu_" + menuNum)) {
		document.getElementById("submenu_" + menuNum).style.display = "block";
	}
}

/*
 * Hides all submenus
 * Assumes that the submenu has the id submenu_x
 * Assumes that the parent menu item has id menu_x
 * Assumes that parent menu items are numbered sequentially from zero
 */
function hideMenus() {
	var count = 0;
	while (document.getElementById("menu_" + count)) {
		if (document.getElementById("submenu_" + count)) {
			document.getElementById("submenu_" + count).style.display = "none";
		}
		count++;
	}
}

function setUpMenus() {
	var i, obj;
	i = 0;
	while (document.getElementById("menu_" + i)) {
		if (document.getElementById("submenu_" + i)) {
			obj = document.getElementById("menu_" + i);
			setSubmenuPos(obj, "submenu_" + i);
		}
		i++;
	}
	hideMenus();
}
