/**
 * Rhombix_Impl org.pimslims.rhombix TrialServiceImpl.java
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
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.SchedulePlan;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;

/**
 * TrialServiceImpl
 * 
 */
public class TrialServiceImpl implements TrialService {

    /**
     * PLATE_TABLE String
     */
    private static final String PLATE_TABLE = "plate";

    /**
     * PLATE_TYPE_TABLE String
     */
    private static final String PLATE_TYPE_TABLE = "plate_type";

    private final RhombixConnection connection;

    private final Map<Long, Sample> sampleCache;

    /**
     * Constructor for TrialServiceImpl
     * 
     * @param connection
     * @param dataStorage
     */
    public TrialServiceImpl(RhombixConnection connection, RhombixDataStorageImpl dataStorage) {
        this.connection = connection;
        this.sampleCache = dataStorage.getSampleCache();
    }

    /**
     * TrialServiceImpl.create TODO there is a mismatch in models here. Rhombix records plate dimensions, and
     * xtalPiMS does not. xtalPiMS records number of subpositions, etc, and Rhombix does not.
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#create(org.pimslims.business.crystallization.model.PlateType)
     */
    @Override
    public PlateType create(PlateType plateType) {
        // we need this for testing
        RhombixDataStorageImpl.insert(PLATE_TYPE_TABLE, "type_name", this.connection, plateType.getName());

        try {
            for (int row = 1; row <= plateType.getRows(); row++) {
                for (int column = 1; column <= plateType.getColumns(); column++) {
                    String insert =
                        " insert into well \r\n"
                            + "   (site_id, well_id,   plate_type_id, well_name, row_num, column_num, create_user_id)\r\n"
                            + "   values (1, well_seq.nextval,   plate_type_seq.currval, ?, ?, ?, 1 )";

                    RhombixPreparedStatement statement = this.connection.prepareStatement(insert);
                    statement.setString(1, new WellPosition(row, column).toStringNoSubPosition());
                    statement.setInt(2, row - 1);
                    statement.setInt(3, column - 1);
                    statement.executeUpdate();
                    statement.close();
                }

            }
            return this.findPlateType(plateType.getName());
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * TrialServiceImpl.createTrialPlate
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#createTrialPlate(java.lang.String,
     *      org.pimslims.business.crystallization.model.PlateType)
     */
    @Override
    public TrialPlate createTrialPlate(String barcode, PlateType plateType) throws BusinessException {
        // we need this for testing
        try {
            Long typeId = insertTrialPlate(barcode, plateType);

            TrialPlate ret = this.findTrialPlate(barcode);
            assert ret.getPlateType().getId().equals(typeId);
            this.createDrops(ret);
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    private Long insertTrialPlate(String barcode, PlateType plateType) throws SQLException {
        Long typeId = plateType.getId();

        RhombixPreparedStatement statement =
            this.connection.prepareStatement(" insert \r\n"
                + "              into plate (site_id, plate_id, plate_type_id,  barcode)\r\n"
                + "                values (1, plate_seq.nextval, ?, ? )\r\n");
        statement.setLong(1, typeId);
        statement.setString(2, barcode);
        statement.executeUpdate();
        statement.close();
        statement =
            this.connection.prepareStatement(" insert \r\n"
                + "              into experiment (site_id, experiment_id, plate_id )\r\n"
                + "                values (1, experiment_seq.nextval, plate_seq.currval )");
        statement.executeUpdate();
        statement.close();
        return typeId;
    }

    /**
     * TrialServiceImpl.findAdditionalImagingRequests
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findAdditionalImagingRequests()
     */
    @Override
    public Map<String, Calendar> findAdditionalImagingRequests() throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.findAllPlateTypes
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findAllPlateTypes()
     */
    @Override
    public Collection<PlateType> findAllPlateTypes() throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.findAllTrialPlates
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findAllTrialPlates(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<TrialPlate> findAllTrialPlates(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.findPlateRetrievals
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findPlateRetrievals()
     */
    @Override
    public Map<String, Calendar> findPlateRetrievals() throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.findPlateType
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findPlateType(long)
     */
    @Override
    public PlateType findPlateType(long id) throws BusinessException {
        try {
            RhombixPreparedStatement statement =
                this.connection.prepareStatement("select * from " + PLATE_TYPE_TABLE + " where "
                    + PLATE_TYPE_TABLE + "_id=?");
            statement.setLong(1, id);
            PlateType ret = makePlateTypeBean(statement);
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    private static PlateType doFindPlateType(RhombixConnection connection2, long id) throws SQLException {
        RhombixPreparedStatement statement =
            connection2.prepareStatement("select * from " + PLATE_TYPE_TABLE + " where " + PLATE_TYPE_TABLE
                + "_id=?");
        statement.setLong(1, id);
        PlateType ret = makePlateTypeBean(statement);
        statement.close();
        return ret;
    }

    /**
     * TrialServiceImpl.findPlateType
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findPlateType(java.lang.String)
     */
    @Override
    public PlateType findPlateType(String name) throws BusinessException {
        try {
            RhombixPreparedStatement statement =
                this.connection.prepareStatement("select * from " + PLATE_TYPE_TABLE + " where type_name=?");
            statement.setString(1, name);
            PlateType ret = makePlateTypeBean(statement);
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    private static PlateType makePlateTypeBean(RhombixPreparedStatement statement) throws SQLException {
        ResultSet result = statement.executeQuery();
        boolean found = result.next();
        if (!found) {
            return null;
        }

        PlateType ret = new PlateType();
        ret.setId(result.getLong(PLATE_TYPE_TABLE + "_id"));

        ret.setName(result.getString("type_name"));
        found = result.next();
        assert !found : "More than one result for: " + statement.toString();
        result.close();
        return ret;
    }

    /**
     * TrialServiceImpl.findTrialDrop
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findTrialDrop(java.lang.String,
     *      org.pimslims.business.crystallization.model.WellPosition) Note that this is less effecient than
     *      TrialServiceImpl.addTrialDrops
     */
    @Override
    public TrialDrop findTrialDrop(String barcode, WellPosition wellPosition) throws BusinessException {
        try {
            return doFindTrialDrop(this.connection, barcode, wellPosition, this.sampleCache);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    static TrialDrop doFindTrialDrop(RhombixConnection rhombixConnection, String barcode,
        WellPosition wellPosition, Map<Long, Sample> cache) throws SQLException {
        RhombixPreparedStatement statement =
            rhombixConnection
                .prepareStatement("select DROP_SITE_DETAIL.* from "
                    + "DROP_SITE_DETAIL, GRID, EXPERIMENT, PLATE "
                    + " WHERE PLATE.PLATE_ID=EXPERIMENT.PLATE_ID AND EXPERIMENT.EXPERIMENT_ID=DROP_SITE_DETAIL.EXPERIMENT_ID "
                    + " AND DROP_SITE_DETAIL.GRID_ID=GRID.GRID_ID " + " and BARCODE=?"
                    + " and START_WELL_ROW<=? AND ?<=END_WELL_ROW "
                    + " and START_WELL_COLUMN<=? AND ?<=END_WELL_COLUMN " + " and LU_COMPARTMENT_ID=?");
        statement.setString(1, barcode);
        statement.setInt(2, wellPosition.getRow() - 1);
        statement.setInt(3, wellPosition.getRow() - 1);
        statement.setInt(4, wellPosition.getColumn() - 1);
        statement.setInt(5, wellPosition.getColumn() - 1);
        statement.setInt(6, wellPosition.getSubPosition());
        ResultSet result = statement.executeQuery();
        boolean found = result.next();
        TrialDrop ret = null;
        if (found) {
            ret = makeTrialDropBean(rhombixConnection, result, wellPosition, cache);
        }
        assert !result.next() : "More than one result for: " + statement.toString();
        result.close();
        statement.close();
        return ret;
    }

    /**
     * TrialServiceImpl.addTrialDrops
     * 
     * @param cache
     * 
     * @param ret
     * @throws SQLException
     */
    private static void addTrialDrops(RhombixConnection connection2, TrialPlate plate, Map<Long, Sample> cache)
        throws SQLException {
        // should we do: long experiment_id = ((TrialPlateImpl) plate).getRhombixExperimentId(); 
        RhombixPreparedStatement statement =
            connection2
                .prepareStatement("select DROP_SITE_DETAIL.*, START_WELL_ROW, END_WELL_ROW, START_WELL_COLUMN, END_WELL_COLUMN  from "
                    + "DROP_SITE_DETAIL, GRID, EXPERIMENT, PLATE "
                    + " WHERE PLATE.PLATE_ID=EXPERIMENT.PLATE_ID AND EXPERIMENT.EXPERIMENT_ID=DROP_SITE_DETAIL.EXPERIMENT_ID "
                    + " AND DROP_SITE_DETAIL.GRID_ID=GRID.GRID_ID " + " and BARCODE=?");
        statement.setString(1, plate.getBarcode());
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            int subPosition = result.getInt("LU_COMPARTMENT_ID");
            // each entry in GRID represents a rectangle on the plate
            int start_row = result.getInt("START_WELL_ROW");
            int end_row = result.getInt("END_WELL_ROW");
            int start_column = result.getInt("START_WELL_COLUMN");
            int end_column = result.getInt("END_WELL_COLUMN");
            //TODO must check bounds of this iteration with real data
            for (int row = start_row; row <= end_row; row++) {
                for (int column = start_column; column <= end_column; column++) {
                    WellPosition position = new WellPosition(row + 1, column + 1, subPosition);
                    TrialDrop drop = makeTrialDropBean(connection2, result, position, cache);
                    plate.addTrialDrop(drop);
                }
            }
        }
        result.close();
        statement.close();

    }

    private static TrialDrop makeTrialDropBean(RhombixConnection connection, ResultSet result,
        WellPosition position, Map<Long, Sample> cache) throws SQLException {

        TrialDrop ret = new TrialDrop();
        ret.setId(result.getLong("DROP_SITE_DETAIL_ID"));
        ret.setWellPosition(position);
        ret.setAdditiveScreen(true);
        //TODO get additive details here
        // could use EXPERIMENT_ID

        long sampleId = result.getLong("MACROMOLECULE_VERSION_ID");
        if (0 != sampleId) {
            Sample sample = SampleServiceImpl.doFind(connection, sampleId, cache);
            float ratio = result.getFloat("DROP_RATIO_PROTEIN");
            float volume = result.getFloat("TOTAL_DROP_VOLUME");
            double amount = volume * ratio; //TODO check the units, I have assumed nL
            SampleQuantity quantity = new SampleQuantity(sample, amount * Math.pow(10, -9), "L");
            ret.addSample(quantity);
        }
        return ret;
    }

    /**
     * TrialServiceImpl.findTrialDrop
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findTrialDrop(org.pimslims.business.crystallization.model.TrialPlate,
     *      org.pimslims.business.crystallization.model.WellPosition)
     */
    @Override
    public TrialDrop findTrialDrop(TrialPlate plate, WellPosition wellPosition) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }
    /**
     * TrialServiceImpl.findTrialDrops
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findTrialDrops(org.pimslims.business.core.model.Sample,
     *      org.pimslims.business.crystallization.model.TrialPlate,
     *      org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<TrialDrop> findTrialDrops(Sample sample, TrialPlate plate, BusinessCriteria criteria)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.findTrialPlate
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findTrialPlate(long)
     */
    @Override
    public TrialPlate findTrialPlate(long id) throws BusinessException {
        try {
            return doFindTrialPlate(this.connection, id, this.sampleCache);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * TrialServiceImpl.doFindTrialPlate
     * 
     * @param connection2
     * @param id
     * @param cache
     * @return
     * @throws SQLException
     * @throws BusinessException
     */
    static TrialPlate doFindTrialPlate(RhombixConnection connection2, long id, Map<Long, Sample> cache)
        throws SQLException, BusinessException {
        RhombixPreparedStatement statement =
            connection2.prepareStatement("select " + PLATE_TABLE + ".*, "
                + "EXPERIMENT.EXPERIMENT_ID, EXPERIMENT.SCREEN_ID " + " from " + PLATE_TABLE
                + ", EXPERIMENT " + " where EXPERIMENT.PLATE_ID=" + PLATE_TABLE + "." + PLATE_TABLE + "_ID"
                + " and EXPERIMENT.PLATE_ID=?");
        statement.setLong(1, id);
        ResultSet result = statement.executeQuery();
        TrialPlateImpl ret = makePlateBean(connection2, result, cache);
        assert !result.next();
        result.close();
        statement.close();

        return ret;
    }

    /**
     * TrialServiceImpl.doFindTrialPlate
     * 
     * @param connection2
     * @param id
     * @return
     * @throws SQLException
     * @throws BusinessException
     */
    static long doFindTrialPlateId(RhombixConnection connection2, String barcode) throws SQLException {
        RhombixPreparedStatement statement =
            connection2.prepareStatement("select " + PLATE_TABLE + "_ID" + " from " + PLATE_TABLE
                + " where barcode=?");
        statement.setString(1, barcode);
        ResultSet result = statement.executeQuery();
        result.next();
        long ret = result.getLong(1);
        assert !result.next();
        result.close();
        statement.close();

        return ret;
    }

    /**
     * TrialServiceImpl.findTrialPlate
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findTrialPlate(java.lang.String)
     */
    @Override
    public TrialPlate findTrialPlate(String barcode) throws BusinessException {
        try {
            RhombixPreparedStatement statement =
                this.connection.prepareStatement("select " + PLATE_TABLE + ".*,"
                    + " EXPERIMENT.EXPERIMENT_ID, EXPERIMENT.SCREEN_ID " + " from " + PLATE_TABLE
                    + ", EXPERIMENT " + " where barcode=? and EXPERIMENT.PLATE_ID=" + PLATE_TABLE + "."
                    + PLATE_TABLE + "_ID");
            statement.setString(1, barcode);
            ResultSet result = statement.executeQuery();
            TrialPlateImpl ret = makePlateBean(this.connection, result, this.sampleCache);

            assert !result.next() : "More than one result for barcode: " + barcode;
            result.close();
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    private static TrialPlateImpl makePlateBean(RhombixConnection connection2, ResultSet result,
        Map<Long, Sample> cache) throws SQLException {
        TrialPlateImpl ret = null;
        if (result.next()) {
            long typeId = result.getLong("plate_type_id");
            PlateType type = doFindPlateType(connection2, typeId);
            assert null != type : "No such plate type: " + typeId;
            ret = new TrialPlateImpl(type);
            ret.setId(result.getLong("" + PLATE_TABLE + "_id"));
            Calendar createDate = Calendar.getInstance();
            createDate.setTimeInMillis(result.getDate("create_datetime").getTime());
            ret.setCreateDate(createDate);

            ret.setBarcode(result.getString("barcode"));
            ret.setDescription(result.getString("description"));
            ret.setRhombixExperimentId(result.getLong("EXPERIMENT_ID"));

            long ownerId = result.getLong("CREATE_USER_ID");
            if (0L != ownerId) {
                Person owner = PersonServiceImpl.getPerson(connection2, ownerId);
                ret.setOwner(owner);
            }

            //TODO ret.setScreen
            long screenId = result.getLong("SCREEN_ID");
            if (0L != screenId) {
                Screen screen = ScreenServiceImpl.getScreen(connection2, screenId);
                ret.setScreen(screen);
            }
        }
        addTrialDrops(connection2, ret, cache);
        //ret.setOwner(owner);
        return ret;
    }

    /**
     * TrialServiceImpl.findTrialPlateByPartialBarcode
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findTrialPlateByPartialBarcode(java.lang.String,
     *      org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<TrialPlate> findTrialPlateByPartialBarcode(String barcode, BusinessCriteria criteria)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.removeAdditionalImaging
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#removeAdditionalImaging(java.lang.String)
     */
    @Override
    public void removeAdditionalImaging(String barcode) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.removePlateRetrievalRequest
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#removePlateRetrievalRequest(java.lang.String)
     */
    @Override
    public void removePlateRetrievalRequest(String barcode) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.requestAdditionalImaging
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#requestAdditionalImaging(java.lang.String,
     *      java.util.Calendar)
     */
    @Override
    public void requestAdditionalImaging(String barcode, Calendar date) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.requestPlateRetrieval
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#requestPlateRetrieval(java.lang.String,
     *      java.util.Calendar)
     */
    @Override
    public void requestPlateRetrieval(String barcode, Calendar dueDate) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.saveTrialDrop This is used for testing only. It is not the most efficient way to
     * insert into the Rhombix database
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#saveTrialDrop(org.pimslims.business.crystallization.model.TrialDrop)
     */
    @Override
    public TrialDrop saveTrialDrop(TrialDrop trialDrop) throws BusinessException {
        TrialDrop drop = this.findTrialDrop(trialDrop.getPlate().getBarcode(), trialDrop.getWellPosition());
        if (null != drop) {
            return drop;
        }
        // TODO macro_molecule_version_id

        try {
            insertDrop(trialDrop);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
        return this.findTrialDrop(trialDrop.getPlate().getBarcode(), trialDrop.getWellPosition());
    }

    private void insertDrop(TrialDrop trialDrop) throws SQLException {
        String sql =
            "insert "
                + " into GRID (SITE_ID, GRID_ID, START_WELL_ROW, END_WELL_ROW, START_WELL_COLUMN, END_WELL_COLUMN ) "
                + " values (1, GRID_SEQ.nextval, ?,?,?,?)\r\n";

        RhombixPreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setInt(1, trialDrop.getWellPosition().getRow() - 1);
        statement.setInt(2, trialDrop.getWellPosition().getRow() - 1);
        statement.setInt(3, trialDrop.getWellPosition().getColumn() - 1);
        statement.setInt(4, trialDrop.getWellPosition().getColumn() - 1);
        statement.executeUpdate();
        statement.close();

        long sampleId = 0;
        List<SampleQuantity> quantities = trialDrop.getSamples();
        if (!quantities.isEmpty()) {
            assert 1 == quantities.size();
            sampleId = quantities.iterator().next().getSample().getId();
        }

        statement =
            this.connection
                .prepareStatement("insert into DROP_SITE_DETAIL "
                    + "(site_id, DROP_SITE_DETAIL_ID, GRID_ID, LU_COMPARTMENT_ID, EXPERIMENT_ID, MACROMOLECULE_VERSION_ID)"
                    + "values (1, DROP_SITE_DETAIL_SEQ.nextval, GRID_SEQ.currval, ?, ?, ?)");

        statement.setInt(1, trialDrop.getWellPosition().getSubPosition());
        statement.setLong(2, ((TrialPlateImpl) trialDrop.getPlate()).getRhombixExperimentId());
        statement.setLong(3, sampleId);
        statement.executeUpdate();
        statement.close();

        RhombixPreparedStatement q =
            connection.prepareStatement("select DROP_SITE_DETAIL_SEQ.currval from dual");
        ResultSet result = q.executeQuery();
        result.next();
        trialDrop.setId(result.getLong(1));
        q.close();
    }

    /**
     * TrialServiceImpl.saveTrialPlate
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#saveTrialPlate(org.pimslims.business.crystallization.model.TrialPlate)
     */
    @Override
    public TrialPlate saveTrialPlate(TrialPlate trialPlate) throws BusinessException {
        try {
            insertTrialPlate(trialPlate.getBarcode(), trialPlate.getPlateType());
            TrialPlate ret = this.findTrialPlate(trialPlate.getBarcode());
            createDrops(ret);
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    private void createDrops(TrialPlate trialPlate) throws BusinessException {
        try {
            if (trialPlate.getTrialDrops().isEmpty()) {
                trialPlate.buildAllTrialDrops();
            }
            for (final TrialDrop trialDrop : trialPlate.getTrialDrops()) {
                this.insertDrop(trialDrop);
            }
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * TrialServiceImpl.updateTrialDrop
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#updateTrialDrop(org.pimslims.business.crystallization.model.TrialDrop)
     */
    @Override
    public void updateTrialDrop(TrialDrop trialDrop) throws BusinessException {
        long sampleId = 0;
        if (!trialDrop.getSamples().isEmpty()) {
            assert 1 == trialDrop.getSamples().size();
            SampleQuantity quantity = trialDrop.getSamples().iterator().next();
            //TODO quantity.getQuantity()
            Sample sample = quantity.getSample();
            sampleId = sample.getId();
        }

        try {
            RhombixPreparedStatement statement =
                this.connection.prepareStatement("update DROP_SITE_DETAIL "
                    + "set MACROMOLECULE_VERSION_ID=?" + "where DROP_SITE_DETAIL_ID=?");
            statement.setLong(2, trialDrop.getId());
            statement.setLong(1, sampleId);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new BusinessException(e);
        }

    }

    /**
     * TrialServiceImpl.updateTrialPlate
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#updateTrialPlate(org.pimslims.business.crystallization.model.TrialPlate)
     */
    @Override
    public void updateTrialPlate(TrialPlate trialPlate) throws BusinessException {

        try {
            RhombixPreparedStatement statement =
                this.connection.prepareStatement("update " + PLATE_TABLE + " set (CREATE_USER_ID)"
                    + "= (  ?)" + " where " + PLATE_TABLE + "_ID" + "=?");

            if (null == trialPlate.getOwner()) {
                statement.setNull(1, java.sql.Types.BIGINT);
            } else {
                long id = trialPlate.getOwner().getId();
                statement.setLong(1, id);
            }

            statement.setLong(2, trialPlate.getId());
            statement.executeUpdate();
            statement.close();

            statement =
                this.connection.prepareStatement("update EXPERIMENT set (SCREEN_ID)" + "= (  ?)" + " where "
                    + "EXPERIMENT_ID" + "=?");

            if (null == trialPlate.getScreen()) {
                statement.setNull(1, java.sql.Types.BIGINT);
            } else {
                long id = trialPlate.getScreen().getId();
                statement.setLong(1, id);
            }

            statement.setLong(2, ((TrialPlateImpl) trialPlate).getRhombixExperimentId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new BusinessException(e);
        }

    }

    /**
     * TrialServiceImpl.getDataStorage
     * 
     * @see org.pimslims.business.BaseService#getDataStorage()
     */
    @Override
    public DataStorage getDataStorage() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.findViewCount
     * 
     * @see org.pimslims.business.ViewService#findViewCount(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Integer findViewCount(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.findViews
     * 
     * @see org.pimslims.business.ViewService#findViews(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<TrialDropView> findViews(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.create
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#create(org.pimslims.business.crystallization.model.PlateType,
     *      org.pimslims.business.crystallization.model.SchedulePlan)
     */
    @Override
    public PlateType create(PlateType plateType, SchedulePlan defaultSchedulePlan) {
        // TODO use the schedule plan
        return this.create(plateType);
    }

    /**
     * TrialServiceImpl.convertPropertyName
     * 
     * @see org.pimslims.business.criteria.PropertyNameConvertor#convertPropertyName(java.lang.String)
     */
    @Override
    public String convertPropertyName(String property) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

}
