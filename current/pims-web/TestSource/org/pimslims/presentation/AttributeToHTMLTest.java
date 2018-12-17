package org.pimslims.presentation;

import java.util.Collections;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.model.people.Group;
import org.pimslims.model.people.Organisation;
import org.pimslims.presentation.create.AttributeValueMap;
import org.pimslims.presentation.create.RoleHooksHolder;

public class AttributeToHTMLTest extends TestCase {

    private static final String UNIQUE = "ath" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(AttributeToHTMLTest.class);
    }

    private final AbstractModel model;

    public AttributeToHTMLTest(final String arg0) {
        super(arg0);
        this.model = ModelImpl.getModel();
    }

    public void testNullValue() {
        final MetaClass metaClass = this.model.getMetaClass(Organisation.class.getName());
        final MetaAttribute attribute = metaClass.getAttribute(Organisation.PROP_NAME);
        final AttributeToHTML a = new AttributeToHTML(attribute, null, "test", null);
        final String html = a.getHtml();
        final String hook = Organisation.class.getName() + ":" + Organisation.PROP_NAME;
        Assert.assertEquals("<input onchange=\'onEdit()\' name=\"" + hook + "\" id=\"" + hook
            + "\" value=\"\" type=\"text\" class=\"text\" maxlength=\"80\" />", html);
    }

    public void testValue() {
        final MetaClass metaClass = this.model.getMetaClass(Organisation.class.getName());
        final MetaAttribute attribute = metaClass.getAttribute(Organisation.PROP_NAME);
        final AttributeToHTML a = new AttributeToHTML(attribute, new String[] { "test" }, "test", null);
        final String html = a.getHtml();
        final String hook = Organisation.class.getName() + ":" + Organisation.PROP_NAME;
        Assert.assertEquals("<input onchange=\'onEdit()\' name=\"" + hook + "\" id=\"" + hook
            + "\" value=\"test\" type=\"text\" class=\"text\" maxlength=\"80\" />", html);
    }

    //TODO update this for time+date
    public void testDate() {
        final MetaClass metaClass =
            this.model.getMetaClass(org.pimslims.model.target.Milestone.class.getName());
        final MetaAttribute attribute = metaClass.getAttribute(org.pimslims.model.target.Milestone.PROP_DATE);
        final AttributeToHTML a = new AttributeToHTML(attribute, new String[] { "test" }, "test", null);
        final String html = a.getHtml();
        final String hook =
            org.pimslims.model.target.Milestone.class.getName() + ":"
                + org.pimslims.model.target.Milestone.PROP_DATE;
        /* Assert
            .assertEquals(
                "<input onchange='onEdit()' "
                    + "name=\"org.pimslims.model.target.Milestone:date\" "
                    + "id=\"org.pimslims.model.target.Milestone:date\" "
                    + "value=\"test\" type=\"text\" class=\"text\" maxlength=\"10\" />"
                    + "&nbsp;<img src=\"test/images/cal_icon.gif\" id=\"buttondate\" title=\"Click for calendar\" /> "
                    + "<script>document.write('<span title=\"'+getClockChange(new Date())+'\">'+getOffsetString()+'</span>');</script>",
                html); */
    }

    public void testEscape() {
        final MetaClass metaClass = this.model.getMetaClass(Organisation.class.getName());
        final MetaAttribute attribute = metaClass.getAttribute(Organisation.PROP_NAME);
        final AttributeToHTML a = new AttributeToHTML(attribute, new String[] { "aa\"bb" }, "test", null);
        final String html = a.getHtml();
        final String hook = Organisation.class.getName() + ":" + Organisation.PROP_NAME;
        Assert.assertEquals("<input onchange=\'onEdit()\' name=\"" + hook + "\" id=\"" + hook
            + "\" value=\"aa&quot;bb\" type=\"text\" class=\"text\" maxlength=\"80\" />", html);
    }

    public void testGetNoRoleAssociations() throws ConstraintException {
        final MetaClass metaClass = this.model.getMetaClass(Organisation.class.getName());
        final MetaRole role = metaClass.getMetaRole(Organisation.PROP_GROUPS);
        final AttributeValueMap values = new AttributeValueMap(metaClass, Collections.EMPTY_MAP);
        final RoleHooksHolder rhh = new RoleHooksHolder(metaClass);
        final AttributeToHTML a = new AttributeToHTML(role, metaClass, rhh, null, "test", values, null);
        final String ra = a.getRoleAssociations();
        Assert.assertEquals(ra, "no groups set", ra);

    }

    public void testgetRoleAssociations() throws ConstraintException {
        final MetaClass metaClass = this.model.getMetaClass(Organisation.class.getName());
        final MetaRole role = metaClass.getMetaRole(Organisation.PROP_GROUPS);
        final AttributeValueMap values = new AttributeValueMap(metaClass, Collections.EMPTY_MAP);
        final RoleHooksHolder rhh = new RoleHooksHolder(metaClass);
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Organisation organisation = new Organisation(version, AttributeToHTMLTest.UNIQUE);
            final Group group = new Group(version, organisation);
            rhh.parse(Organisation.PROP_GROUPS + "=" + group.get_Hook());
            final AttributeToHTML a =
                new AttributeToHTML(role, metaClass, rhh, version, "test", values, null);
            final String ra = a.getRoleAssociations();
            Assert.assertTrue(ra, ra.contains(group.get_Name()));
        } finally {
            version.abort();
        }
    }

    public void testGetRoleAssociationsEscapes() throws ConstraintException {
        final MetaClass metaClass = this.model.getMetaClass(Organisation.class.getName());
        final MetaRole role = metaClass.getMetaRole(Organisation.PROP_GROUPS);
        final AttributeValueMap values = new AttributeValueMap(metaClass, Collections.EMPTY_MAP);
        final RoleHooksHolder rhh = new RoleHooksHolder(metaClass);
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Organisation organisation = new Organisation(version, AttributeToHTMLTest.UNIQUE);
            final Group group = new Group(version, organisation);
            group.setName(AttributeToHTMLTest.UNIQUE + "<test>");
            rhh.parse(Organisation.PROP_GROUPS + "=" + group.get_Hook());
            final AttributeToHTML a =
                new AttributeToHTML(role, metaClass, rhh, version, "test", values, null);
            final String ra = a.getRoleAssociations();
            Assert.assertFalse(ra, ra.contains("<test"));
        } finally {
            version.abort();
        }
    }

}
