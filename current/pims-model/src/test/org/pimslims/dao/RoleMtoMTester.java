/*
 * Created on 09-Dec-2004 @author: Chris Morris
 */
package org.pimslims.dao;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.test.POJOFactory;

/**
 * Tests generated class for Location
 */
public class RoleMtoMTester extends org.pimslims.metamodel.TestAMetaRole {

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.TestAMetaRole#createOtherObject(org.pimslims.metamodel.WritableVersion,
     *      java.util.Map)
     */

    @Override
    protected ModelObject createOtherObject(WritableVersion wv, Map<String, Object> otherAttributes)
        throws AccessException, ConstraintException {

        return wv.create(TargetGroup.class, otherAttributes);
    }

    /**
     * 
     */
    public RoleMtoMTester() {
        super(ModelImpl.getModel(), org.pimslims.model.target.Target.class.getName(),
            org.pimslims.model.target.Target.PROP_TARGETGROUPS, POJOFactory.getAttrTarget(), POJOFactory
                .getAttrTargetGroup(), "Test Target PROP_TARGETPROJECTS association");
    }

    public void testBiDirection() {
        WritableVersion wv = super.model.getWritableVersion(AbstractModel.SUPERUSER);
        String targetHook = null, projectHook = null;
        try {
            Target target = POJOFactory.createTarget(wv);
            TargetGroup project = POJOFactory.createTargetGroup(wv);
            target.addTargetGroup(project);

            assertEquals("added", 1, target.getTargetGroups().size());
            assertEquals("added in reverse", 1, project.getTargets().size());

            targetHook = target.get_Hook();
            projectHook = project.get_Hook();
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = super.model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Target target = (Target) wv.get(targetHook);
            TargetGroup project = (TargetGroup) wv.get(projectHook);

            assertEquals("persistence of target", 1, target.getTargetGroups().size());
            assertEquals("persistence of project", 1, project.getTargets().size());

            // now test remove
            target.removeTargetGroup(project);
            assertEquals("removed", 0, target.getTargetGroups().size());
            assertEquals("removed in reverse", 0, project.getTargets().size());

            wv.delete(target);
            wv.delete(project);

            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    @Override
    protected ModelObject createThisObject(WritableVersion wv, final Map<String, Object> thisAttributes)
        throws AccessException, ConstraintException {
        Map<String, Object> allattributes = new HashMap<String, Object>();
        allattributes.putAll(thisAttributes);
        allattributes.put(Target.PROP_PROTEIN, create(wv, Molecule.class));
        return wv.create(this.thisType.getJavaClass(), allattributes);
    }
}
