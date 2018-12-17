/**
 * xtalPiMSImpl org.pimslims.crystallization.dao.view ConditionViewDAO.java
 * 
 * @author bl67
 * @date 22 May 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.dao.view;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.PropertyNameConvertor;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.view.ComponentQuantityView;
import org.pimslims.business.crystallization.view.ComponentView;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.sample.RefSample;

/**
 * ConditionViewDAO
 * 
 */
public class ConditionViewDAO extends AbstractSQLViewDAO<ConditionView> implements
    PropertyNameConvertor<ConditionView> {

    /**
     * Constructor for ConditionViewDAO
     */
    public ConditionViewDAO(final ReadableVersion version) {
        super(version);
    }

    @Override
    public String convertPropertyName(final String propertyName) throws BusinessException {
        if (propertyName == null) {
            throw new IllegalArgumentException("Null filter name");
        }
        if (propertyName.equals(ConditionView.PROP_LOCAL_NAME)) {
            return "screen.name";
        } else if (propertyName.equals(ConditionView.PROP_ID)) {
            return "condition.ABSTRACTSAMPLEID";
        } else if (propertyName.equals(ConditionView.PROP_FINAL_PH)) {
            return "condition_.ph";
        } else if (propertyName.equals(ConditionView.PROP_MANUFACTURER_CAT_CODE)) {
            return "source.catalogNum";
        } else if (propertyName.equals(ConditionView.PROP_MANUFACTURER_CODE)) {
            return "source.productName";
        } else if (propertyName.equals(ConditionView.PROP_MANUFACTURER_NAME)) {
            return "org.name";
        } else if (propertyName.equals(ConditionView.PROP_MANUFACTURER_SCREEN_NAME)) {
            return "source.productGroupName";
        } else if (propertyName.equals(ConditionView.PROP_LOCAL_NUMBER)) {
            return "condition_.name";
        } else if (propertyName.equals(ConditionView.PROP_COMPONENTS)) {
            return "refcomonent.name";
        } else if (propertyName.equals(ComponentView.PROP_NAME)) {
            return "refcomonent.name";
        } else if (propertyName.equals(ConditionView.PROP_SALT_CONDITION)) {
            return "condition.issaltcrystal";
        } else if (propertyName.equals(ConditionView.PROP_VOLATILE_CONDITION)) {
            return "molecule.isVolatile";
        } else if (propertyName.equals(ConditionView.PROP_WELL)) {
            throw new UnsupportedOperationException(propertyName + " Not supported.");
        }
        throw new BusinessException("Unable to find matching property in " + this.getClass());
    }

    @Override
    String getCountableName() {
        return "distinct condition.ABSTRACTSAMPLEID";
    }

    @Override
    Class<? extends ModelObject> getRootClass() {
        return RefSample.class;
    }

    @Override
    public String getViewSql(final BusinessCriteria criteria) {
        final String selectSql =
            "select condition.ABSTRACTSAMPLEID as id,"
                + "screen.name as screenname,"
                + "condition_.ph as conditionph,"
                + "org.name as orgname,  "
                + "source.catalogNum as catalogNum,  "
                + "source.productName as productName,  "
                + "source.productGroupName as productGroupName,  "
                + "refcomonent.name as componentname,  "
                + "component.concentration as concentration,  "
                + "component.concentrationUnit as concentrationUnit,  "
                + "component_.DETAILS as componentType,  "
                + "molecule.isVolatile as isVolatile,  "
                + "molecule.casNum as casNum,  "
                + "component.ph as componentph,  "
                + "refsampleposition.rowPosition as row,  "
                + "refsampleposition.colPosition as col,  "
                + "condition.issaltcrystal as issaltcrystal "
                + "from SAM_REFSAMPLE condition  "
                + "join CORE_LABBOOKENTRY condition__ on condition.ABSTRACTSAMPLEID=condition__.DBID "
                + "inner join SAM_ABSTRACTSAMPLE condition_ on condition.ABSTRACTSAMPLEID=condition_.LabBookEntryID  "
                // TODO should this next one be a left join?
                + "join SAM_SAMPLECOMPONENT component on condition.ABSTRACTSAMPLEID=component.ABSTRACTSAMPLEID  "
                + "join CORE_LABBOOKENTRY component_ on component.LabBookEntryID=component_.DBID  "
                + "join MOLE_ABSTRACTCOMPONENT refcomonent on component.REFCOMPONENTID=refcomonent.LabBookEntryID  "
                + "join MOLE_MOLECULE molecule on refcomonent.LabBookEntryID=molecule.substanceid  "
                + "join HOLD_REFSAMPLEPOSITION refsampleposition on condition.ABSTRACTSAMPLEID=refsampleposition.REFSAMPLEID  "
                + "join HOLD_ABSTRACTHOLDER screen on refsampleposition.RefHOLDERID=screen.LabBookEntryID  "
                + "left join SAM_REFSAMPLESOURCE source on condition.ABSTRACTSAMPLEID=source.REFSAMPLEID  "
                + "left join PEOP_ORGANISATION org on source.SUPPLIERID=org.LabBookEntryID ";
        return buildViewQuerySQL(criteria, selectSql, null, "condition__", RefSample.class);
    }

    @Override
    public Collection<ConditionView> runSearch(final org.hibernate.SQLQuery hqlQuery) {
        final List<ConditionView> views = new LinkedList<ConditionView>();
        final SQLQuery q = hqlQuery;
        /*
         * q.addScalar("id", StandardBasicTypes.LONG); q.addScalar("screenname",
         * StandardBasicTypes.STRING); q.addScalar("conditionph",
         * StandardBasicTypes.FLOAT); q.addScalar("orgname",
         * StandardBasicTypes.STRING); q.addScalar("catalogNum",
         * StandardBasicTypes.STRING); q.addScalar("productName",
         * StandardBasicTypes.STRING); q.addScalar("productGroupName",
         * StandardBasicTypes.STRING); q.addScalar("componentname",
         * StandardBasicTypes.STRING); q.addScalar("concentration",
         * StandardBasicTypes.FLOAT); q.addScalar("concentrationUnit",
         * StandardBasicTypes.STRING); q.addScalar("componentType",
         * StandardBasicTypes.STRING); q.addScalar("isVolatile",
         * StandardBasicTypes.BOOLEAN); q.addScalar("casNum",
         * StandardBasicTypes.STRING); q.addScalar("componentph",
         * StandardBasicTypes.FLOAT); q.addScalar("row",
         * StandardBasicTypes.INTEGER); q.addScalar("col",
         * StandardBasicTypes.INTEGER); q.addScalar("issaltcrystal",
         * StandardBasicTypes.BOOLEAN);
         */
        final List<?> results = hqlQuery.list();
        for (final Object object : results) {
            final Object result[] = (Object[]) object;

            final ConditionView view = new ConditionView();
            Object id = result[0];
            view.setConditionId(AbstractSQLViewDAO.getLong(id));
            view.setLocalName((String) result[1]);
            if (view.getLocalName() == null) {
                continue;
            }
            Object number = result[2];
            if (number != null) {
                view.setFinalpH(getDouble(number));
            }
            view.setManufacturerName((String) result[3]);
            view.setManufacturerCatCode((String) result[4]);
            view.setManufacturerCode((String) result[5]);
            view.setManufacturerScreenName((String) result[6]);

            final ComponentQuantityView component = new ComponentQuantityView();
            component.setComponentName((String) result[7]);
            if (result[8] != null) {
                String unit = (String) result[9];
                if (unit != null) {
                    if ("m3/m3".equalsIgnoreCase(unit)) {
                        unit = "% v/v";
                    } else if ("kg/m3".equalsIgnoreCase(unit)) {
                        unit = "% w/v";
                    } else if ("kg/kg".equalsIgnoreCase(unit)) {
                        unit = "% w/w";
                    } else {
                        unit = " " + unit;
                    }
                } else {
                    unit = "";
                }
                component.setQuantity(result[8] + unit);
            }
            component.setComponentType((String) result[10]);
            if (result[11] != null) {
                component.setVolatileComponent((Boolean) result[11]);
            }
            component.setCasNumber((String) result[12]);
            if (result[13] != null) {
                component.setComponentPH(AbstractSQLViewDAO.getFloat(result[13]));
                if (!(AbstractSQLViewDAO.getFloat(result[13])).equals(7.0f))
                    component.setComponentName((String) result[7] + " pH="
                        + String.format("%.2f", AbstractSQLViewDAO.getFloat(result[13])));
            }
            final Integer row = (Integer) result[14];
            final Integer col = (Integer) result[15];
            if (result[16] != null) {
                view.setSaltCondition((Boolean) result[16]);
            }

            view.setWell((new WellPosition(row, col, 1)).toStringNoSubPosition());
            view.setLocalNumber(view.getWell());
            if (views.contains(view)) {
                final ConditionView existingCondition = views.get(views.indexOf(view));
                existingCondition.addComponent(component);
            } else {
                view.addComponent(component);
                views.add(view);
            }
        }
        return views;
    }
}
