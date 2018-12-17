/**
 * Rhombix_Impl org.pimslims.rhombix ScreenServiceImpl.java
 * 
 * @author cm65
 * @date 13 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessCriterion;
import org.pimslims.business.criteria.EqualsExpression;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.ScreenType;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.view.ScreenView;
import org.pimslims.business.exception.BusinessException;

/**
 * ScreenServiceImpl
 * 
 */
public class ScreenServiceImpl implements ScreenService {

    /**
     * SELECT_CONCENTRATION_UNIT String
     */
    private static final String SELECT_CONCENTRATION_UNIT =
        "select lu_chem_conc_units_id from lu_chem_conc_units where abbr=?";

    /**
     * SELECT_CONDITIONS String
     * 
     * Returns one row for each chemical component in each well, with the molar concentration. Also returns
     * one row per well, with a concentration of 100%. This is the name of the condition, e.g.
     * "MemGold Tube 03".
     */
    private static final String SELECT_CONDITIONS =
        "select row_num, column_num, "
            // + " chemical_conc.lu_chem_conc_units_id, "  
            + " chemical.name,  chemical_conc.concentration "
            + ", abbr "
            + " from screen\r\n"
            + "    join final_well_contents on final_well_contents.screen_id=screen.screen_id\r\n"
            + "    join chemical_conc on final_well_contents.chemical_conc_id= chemical_conc.chemical_conc_id\r\n"

            + "    join lu_chem_conc_units on lu_chem_conc_units.lu_chem_conc_units_id = chemical_conc.lu_chem_conc_units_id\r\n"
            + "    join chemical on chemical.chemical_id=chemical_conc.chemical_id\r\n"
            + "    where screen.screen_id=?\r\n" + "    order by row_num, column_num";

    private static final String INSERT_CHEMICAL =
        "insert into chemical " + "(chemical_id, name)" + "values (chemical_seq.nextVal, ?)";

    // note that this only works with a new chemical. That's sufficient for testing.
    private static final String INSERT_CONCENTRATION =
        "insert into chemical_conc "
            + "(chemical_conc_id, chemical_id, concentration, lu_chem_conc_units_id)"
            + "values (chemical_conc_seq.nextVal, chemical_seq.currVal, ?, (" + SELECT_CONCENTRATION_UNIT
            + "))";

    String INSERT_WELL_CONTENTS =
        "insert into final_well_contents "
            + "(final_well_contents_id, row_num, column_num, screen_id, chemical_conc_id)"
            + "values (final_well_contents_seq.nextVal, ?,?,screen_seq.currVal,chemical_conc_seq.currVal)";

    /**
     * _ID String
     */
    private static final String _ID = "_ID";

    private static final String SCREEN_TABLE = "SCREEN";

    private final RhombixConnection connection;

    /**
     * Constructor for ScreenServiceImpl
     * 
     * @param rhombixConnection
     */
    public ScreenServiceImpl(RhombixConnection rhombixConnection) {
        this.connection = rhombixConnection;
    }

    /**
     * ScreenServiceImpl.close
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#close(org.pimslims.business.crystallization.model.Screen)
     */
    @Override
    public void close(Screen screen) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.create
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#create(org.pimslims.business.crystallization.model.Screen)
     */
    @Override
    public void create(Screen screen) throws BusinessException {
        try {
            RhombixPreparedStatement statement =
                this.connection.prepareStatement("insert into " + SCREEN_TABLE + " (" + SCREEN_TABLE + _ID
                    + ", name, description,is_custom,is_additive" + ") values (" + SCREEN_TABLE
                    + "_seq.nextval,?,?,?,?)");
            statement.setString(1, screen.getName());
            statement.setString(2, screen.getManufacturerName());
            statement.setBoolean(3, ScreenType.Optimisation.equals(screen.getScreenType()));
            statement.setBoolean(4, ScreenType.Additive.equals(screen.getScreenType()));
            statement.executeUpdate();
            statement.close();
            screen.setId(PlateInspectionServiceImpl.getCurrentValue(this.connection, SCREEN_TABLE));

            Map<WellPosition, Condition> conditions = screen.getConditionPositions();
            for (Iterator iterator = conditions.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<WellPosition, Condition> entry = (Entry<WellPosition, Condition>) iterator.next();
                Condition condition = entry.getValue();
                WellPosition well = entry.getKey();
                // the line below is how Rhombix represents the condition
                insertComponent(well, new ComponentQuantity(100, "%"), condition.getLocalName());
                List<ComponentQuantity> quantities = condition.getComponents();
                for (Iterator iterator2 = quantities.iterator(); iterator2.hasNext();) {
                    ComponentQuantity quantity = (ComponentQuantity) iterator2.next();
                    String name = quantity.getComponent().getChemicalName();
                    insertComponent(well, quantity, name);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertComponent(WellPosition well, ComponentQuantity quantity, String name)
        throws SQLException {
        insertChemical(name);
        insertConcentration(quantity.getUnits(), quantity.getQuantity());
        insertWellContents(well.getRow(), well.getColumn());
    }

    /**
     * ScreenServiceImpl.insertWellContents
     * 
     * @param row
     * @param column
     * @throws SQLException
     */
    private void insertWellContents(int row, int column) throws SQLException {
        RhombixPreparedStatement statement = this.connection.prepareStatement(INSERT_WELL_CONTENTS);
        statement.setInt(1, row - 1);
        statement.setInt(2, column - 1);
        statement.executeUpdate();
        statement.close();
    }

    /**
     * ScreenServiceImpl.insertConcentration
     * 
     * @param units
     * @param quantity
     * @throws SQLException
     */
    private void insertConcentration(String units, double quantity) throws SQLException {
        /*{ // This bit is not needed in real life, but needed for tests
            RhombixPreparedStatement statement =
                this.connection
                    .prepareStatement("insert into lu_chem_conc_units (lu_chem_conc_units_id, abbr, name) values (2, '%', 'per cent')");
            statement.executeUpdate();
            statement.close();
        } */
        RhombixPreparedStatement statement = this.connection.prepareStatement(INSERT_CONCENTRATION);
        statement.setDouble(1, quantity);
        statement.setString(2, units);
        statement.executeUpdate();
        statement.close();

    }

    /**
     * ScreenServiceImpl.insertChemical This should really be findOrInsert - but this method is sufficient for
     * testing
     * 
     * @param name
     * @throws SQLException
     */
    private void insertChemical(String name) throws SQLException {
        RhombixPreparedStatement statement = this.connection.prepareStatement(INSERT_CHEMICAL);
        statement.setString(1, name);
        statement.executeUpdate();
        statement.close();

    }

    /**
     * ScreenServiceImpl.create
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#create(java.lang.String,
     *      java.util.Map)
     */
    @Override
    public Screen create(String screenName, Map<WellPosition, Condition> conditionPositions)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.find
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#find(long)
     */
    @Override
    public Screen find(long id) throws BusinessException {
        try {
            return getScreen(this.connection, id);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * ScreenServiceImpl.getScreen
     * 
     * @param connection2
     * @param ownerId
     * @return
     * @throws SQLException
     */
    static Screen getScreen(RhombixConnection rhombixConnection, long id) throws SQLException {

        RhombixPreparedStatement statement =
            rhombixConnection.prepareStatement("select * from SCREEN where SCREEN_ID=?");
        statement.setLong(1, id);
        ResultSet result = statement.executeQuery();
        boolean found = result.next();
        if (!found) {
            statement.close();
            return null;
        }
        Screen ret = makeScreenBean(rhombixConnection, result);
        ret.setConditionPositions(getConditionPositions(ret.getId(), rhombixConnection, ret.getName()));

        //Calendar createDate = Calendar.getInstance();
        //createDate.setTimeInMillis(result.getDate("create_datetime").getTime());
        assert !result.next() : "More than one result for: " + statement.toString();
        result.close();
        statement.close();
        return ret;
    }

    /**
     * ScreenServiceImpl.findAll
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findAll(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<Screen> findAll(BusinessCriteria criteria) throws BusinessException {
        try {
            RhombixPreparedStatement statement =
                this.connection.prepareStatement("select *   from " + SCREEN_TABLE);
            ResultSet result = statement.executeQuery();
            Collection<Screen> ret = new ArrayList();
            while (result.next()) {
                Screen bean = makeScreenBean(connection, result);
                ret.add(bean);
            }
            result.close();
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * ScreenServiceImpl.findAllCount
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findAllCount()
     */
    @Override
    public Integer findAllCount() throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.findByChemicalName
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findByChemicalName(java.lang.String,
     *      org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<Component> findByChemicalName(String chemicalName, BusinessCriteria criteria)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.findByCondition
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findByCondition(org.pimslims.business.crystallization.model.Condition,
     *      org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<Screen> findByCondition(Condition condition, BusinessCriteria criteria)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.findByManufacturer
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findByManufacturer(java.lang.String,
     *      org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<Screen> findByManufacturer(String manufacturer, BusinessCriteria criteria)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.findByName
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findByName(java.lang.String)
     */
    @Override
    public Screen findByName(String name) throws BusinessException {
        try {
            RhombixPreparedStatement statement =
                this.connection.prepareStatement("select " + SCREEN_TABLE + ".*  " + " from " + SCREEN_TABLE
                    + " where name=?  ");
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                return null;
            }
            Screen ret = makeScreenBean(this.connection, result);
            ret.setConditionPositions(getConditionPositions(ret.getId(), this.connection, name));
            assert !result.next() : "More than one result for screen: " + name;
            result.close();
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * ScreenServiceImpl.makeScreenBean
     * 
     * @param rhombixConnection
     * @param result
     * @return
     * @throws SQLException
     */
    private static Screen makeScreenBean(RhombixConnection rhombixConnection, ResultSet result)
        throws SQLException {
        Screen ret = new Screen();

        ret.setId(result.getLong(SCREEN_TABLE + _ID));
        ret.setName(result.getString("name"));
        ret.setManufacturerName(result.getString("description"));
        ret.setScreenType(ScreenType.Matrix);
        if (result.getBoolean("IS_ADDITIVE")) {
            ret.setScreenType(ScreenType.Additive);
        }
        if (result.getBoolean("IS_CUSTOM")) {
            ret.setScreenType(ScreenType.Optimisation);
        }
        //Calendar createDate = Calendar.getInstance();
        //createDate.setTimeInMillis(result.getDate("create_datetime").getTime());           

        ret.setName(result.getString("name"));
        return ret;
    }

    /**
     * ScreenServiceImpl.getConditionPositions
     * 
     * @param screenName
     * 
     * @param rhombixConnection
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    private static Map<WellPosition, Condition> getConditionPositions(Long screenId,
        RhombixConnection connection, String screenName) throws SQLException {
        Map<WellPosition, Condition> ret = new HashMap();
        RhombixPreparedStatement statement = connection.prepareStatement(SELECT_CONDITIONS);
        statement.setLong(1, screenId);
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            //System.out.println("lu_chem_conc_units_id: " + result.getLong("lu_chem_conc_units_id"));
            ComponentQuantity quantity = new ComponentQuantity();
            Component component = new Component();
            String name = result.getString("name");
            assert null != name;
            component.setChemicalName(name);
            quantity.setComponent(component);
            String unit = result.getString("abbr");
            double concentration = result.getDouble("concentration");
            quantity.setDisplyUnit(unit);
            quantity.setUnits(unit);
            if ("%".equals(unit)) {
                quantity.setUnits("m3/m3"); // TODO  mol/mol or m3/m3 or kg/kg
                /* Keith Corkum, Rhombix, writes "It's been a long time since I've looked at that code,
                 *  by I believe it is a percentage based on volume."
                 * */
            }
            quantity.setQuantity(concentration);
            WellPosition well =
                new WellPosition(1 + result.getInt("row_num"), 1 + result.getInt("column_num"));
            Condition condition = ret.get(well);
            if (null == condition) {
                condition = new Condition();
                // make default name for condition
                condition.setLocalName(screenName + ":" + well.toString());
                ret.put(well, condition);
            }
            if (100d == concentration && "%".equals(unit)) {
                // this is how Rhombix represents the condition as a whole
                condition.setLocalName(component.getChemicalName());
            } else {
                condition.addComponent(quantity);
            }
        }
        result.close();
        statement.close();
        return ret;
    }

    /**
     * ScreenServiceImpl.findByPlate
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findByPlate(org.pimslims.business.crystallization.model.TrialPlate)
     */
    @Override
    public Screen findByPlate(TrialPlate plate) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.findByPlate
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findByPlate(java.lang.String)
     */
    @Override
    public Screen findByPlate(String barcode) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.findByPlateAndWell
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findByPlateAndWell(org.pimslims.business.crystallization.model.TrialPlate,
     *      org.pimslims.business.crystallization.model.WellPosition)
     */
    @Override
    public Condition findByPlateAndWell(TrialPlate plate, WellPosition wellPosition) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.findByPlateAndWell
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findByPlateAndWell(java.lang.String,
     *      org.pimslims.business.crystallization.model.WellPosition)
     */
    @Override
    public Condition findByPlateAndWell(String barcode, WellPosition wellPosition) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.findBySimilarChemicalName
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findBySimilarChemicalName(java.lang.String,
     *      org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<Component> findBySimilarChemicalName(String chemicalName, BusinessCriteria criteria)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.findManufacturerNames
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findManufacturerNames()
     */
    @Override
    public Collection<String> findManufacturerNames() throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.findScreenTypes
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#findScreenTypes()
     */
    @Override
    public Collection<String> findScreenTypes() throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.getConditions
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#getConditions(org.pimslims.business.crystallization.model.Screen,
     *      org.pimslims.business.criteria.BusinessCriteria)
     */
    @Deprecated
    // never used
    public Collection<Condition> getConditions(Screen screen, BusinessCriteria criteria)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.update
     * 
     * @see org.pimslims.business.crystallization.service.ScreenService#update(org.pimslims.business.crystallization.model.Screen)
     */
    @Override
    public void update(Screen screen) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.getDataStorage
     * 
     * @see org.pimslims.business.BaseService#getDataStorage()
     */
    @Override
    public DataStorage getDataStorage() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScreenServiceImpl.findViewCount
     * 
     * @see org.pimslims.business.ViewService#findViewCount(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Integer findViewCount(BusinessCriteria criteria) throws BusinessException {
        String name = getNameToMatch(criteria);
        Screen screen = this.findByName(name);
        return null == screen ? 0 : 1;
    }

    /**
     * ScreenServiceImpl.findViews
     * 
     * @see org.pimslims.business.ViewService#findViews(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<ScreenView> findViews(BusinessCriteria search) throws BusinessException {
        Collection<ScreenView> ret = new ArrayList();
        String name = getNameToMatch(search);

        Screen screen = this.findByName(name);
        ScreenView view = new ScreenView();
        view.setName(name);
        view.setManufacturerName(screen.getManufacturerName());
        view.setScreenType(screen.getScreenType().name() + " Screen");
        ret.add(view);
        return ret;
    }

    private String getNameToMatch(BusinessCriteria search) {
        List<BusinessCriterion> criteria = search.getCriteria();
        assert 1 == criteria.size() : "Not implemented yet";
        BusinessCriterion criterion = criteria.iterator().next();
        assert criterion instanceof EqualsExpression : "Not implemented yet";
        EqualsExpression expression = (EqualsExpression) criterion;
        assert "name".equals(expression.getProperty());
        String name = (String) expression.getValue();
        return name;
    }

    /**
     * ScreenServiceImpl.convertPropertyName
     * 
     * @see org.pimslims.business.criteria.PropertyNameConvertor#convertPropertyName(java.lang.String)
     */
    @Override
    public String convertPropertyName(String property) throws BusinessException {
        return property;
    }

}
