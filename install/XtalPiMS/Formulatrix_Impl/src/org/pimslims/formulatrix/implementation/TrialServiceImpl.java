/**
 *  TrialServiceImpl.java
 * 
 * @author cm65
 * @date 13 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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

    private final ManufacturerConnection connection;

    private final Map<Long, Sample> sampleCache;

    /**
     * Constructor for TrialServiceImpl
     * 
     * @param connection
     * @param dataStorage
     */
    public TrialServiceImpl(ManufacturerConnection connection, ManufacturerDataStorageImpl dataStorage) {
        this.connection = connection;
        this.sampleCache = dataStorage.getSampleCache();
    }

    /**
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#create(org.pimslims.business.crystallization.model.PlateType)
     */
    @Override
    //TODO 02.1
    public PlateType create(PlateType plateType) {
        // we need this for testing
//        ManufacturerDataStorageImpl.insert(PLATE_TYPE_TABLE, "type_name", this.connection, plateType.getName());
//        ManufacturerDataStorageImpl.insert("Containers", "Name", this.connection, plateType.getName());

        try {
        	String sql="INSERT INTO Containers(Name, NumRows, NumColumns, MaxNumDrops, " +
        				"DefaultWellVolume, DefaultAdditiveVolume, MaxWellVolume, DefaultDropVolume, MaxDropVolume, Type) " +
        			"VALUES (?, ?, ?, ?, " +
        				"1, 1, 1, 1, 1, 1)";
            ManufacturerPreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, plateType.getName());
            stmt.setInt(2, plateType.getRows());
            stmt.setInt(3, plateType.getColumns());
            stmt.setInt(4, plateType.getSubPositions());
            stmt.executeUpdate();
            stmt.close();

            /* I don't think we do this for Formulatrix - just create the plate type, no "reference" wells

            //get the ID of the plate type we just inserted
            String sql2="SELECT IDENT_CURRENT('Containers') AS PlateTypeID";
            ManufacturerPreparedStatement stmt2 = this.connection.prepareStatement(sql2);
            ResultSet result=stmt2.executeQuery();
            result.next();
            int plateTypeID=result.getInt("PlateTypeID");
            stmt2.close();

        	int wellNumber=1;
            for (int row = 1; row <= plateType.getRows(); row++) {
                for (int column = 1; column <= plateType.getColumns(); column++) {
                	WellPosition wp=new WellPosition(row, column);
                    String insert="INSERT INTO Well " +
                    		"(PlateID, WellNumber, RowLetter, ColumnNumber, Volume, WaterVolume, CurrentVolume)" +
                    		"VALUES (?, ?, ?, ?, 1, 1, 1)";
                    ManufacturerPreparedStatement statement = this.connection.prepareStatement(insert);
                    statement.setInt(1, plateTypeID);
                    statement.setInt(2, wellNumber);
                    statement.setString(3, wp.toStringNoSubPosition().substring(0, 1));
                    statement.setInt(4, column);
                    statement.executeUpdate();
                    statement.close();
                    wellNumber++;
                }
            }
            */
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
            Long plateId = insertTrialPlate(barcode, plateType);

            TrialPlate ret = this.findTrialPlate(barcode);
            assert ret.getId().equals(plateId);
            this.createDrops(ret);
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    private Long insertTrialPlate(String barcode, PlateType plateType) throws SQLException {
        Long typeId = plateType.getId();
        Timestamp now=new Timestamp(System.currentTimeMillis());
 
        // Experiments record first
        ManufacturerPreparedStatement statement =
            this.connection.prepareStatement("INSERT INTO Experiment " +
            		"(ContainerID, DatePrepared, Seeding, UserID, ImagingScheduleID, TreeNodeID, SetupTempID, IncubationTempID, PlateRowCount, PlateColumnCount) " +
            		"OUTPUT INSERTED.* " +
            		"VALUES(?, CONVERT(datetime,?), 1, 1, 1, 1,  1, 1, 1, 1) ");
        statement.setLong(1, typeId);
        statement.setTimestamp(2, now);
        ResultSet result=statement.executeQuery();
        result.next();
        long experimentID=result.getLong("ID");
        statement.close();
               
        // Now the Plate record
        statement =
                this.connection.prepareStatement("INSERT INTO Plate " +
                		"(PlateNumber, ExperimentID, Barcode, TreeNodeID, State, IsQueued, CanvasRowPosition, CanvasColumnPosition, DateDispensed) " +
                		"OUTPUT INSERTED.* " +
                		"VALUES (1, ?, ?, 1, 3, 1, 1, 1, ?)");
        statement.setLong(1, experimentID);
        statement.setString(2, barcode);
        statement.setTimestamp(3, now);
        result=statement.executeQuery();
        result.next();
        long plateID=result.getLong("ID");
        statement.close();

        // And lastly, all the Well records for this plate
        int numRows=plateType.getRows();
        int numColumns=plateType.getColumns();
        int wellNumber=1;
        for(int r=1; r<=numRows;r++){
            for(int c=1; c<=numColumns;c++){
            	WellPosition wp=new WellPosition(r, c);
            	String rowLetter=wp.toString().substring(0, 1);
            	statement=this.connection.prepareStatement("INSERT INTO Well " +
            			"(PlateID, WellNumber, RowLetter, ColumnNumber) " +
            			"VALUES (?, ?, ?, ?)");
                statement.setLong(1, plateID);
            	statement.setInt(2, wellNumber);
            	statement.setString(3, rowLetter);
            	statement.setInt(4, c);
            	statement.executeUpdate();
            	statement.close();
            	wellNumber++;
            }
        }
        
 /*
        statement =
                this.connection.prepareStatement("INSERT INTO ExperimentPlate" +
                		"(PlateID,ImagingTasksEnabled)" +
                		"VALUES (?, 1)");
        statement.setLong(1, plateID);
        statement.executeUpdate();
        statement.close();
/*
        statement =
                this.connection.prepareStatement("");
        statement.executeUpdate();
        statement.close();
        return typeId;
        */
        return plateID;
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
            ManufacturerPreparedStatement statement =
                this.connection.prepareStatement("SELECT * FROM Containers WHERE ID=?");
            statement.setLong(1, id);
            PlateType ret = makePlateTypeBean(statement);
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    private static PlateType doFindPlateType(ManufacturerConnection connection2, long id) throws SQLException {
        ManufacturerPreparedStatement statement =
            connection2.prepareStatement("SELECT * FROM Containers WHERE ID=?");
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
            ManufacturerPreparedStatement statement =
                this.connection.prepareStatement("SELECT * FROM Containers WHERE Name=?");
            statement.setString(1, name);
            PlateType ret = makePlateTypeBean(statement);
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    private static PlateType makePlateTypeBean(ManufacturerPreparedStatement statement) throws SQLException {
        ResultSet result = statement.executeQuery();
        boolean found = result.next();
        if (!found) {
            return null;
        }

        PlateType ret = new PlateType();
        ret.setId(result.getLong("ID"));
        ret.setName(result.getString("Name"));
        ret.setRows(result.getInt("NumRows"));
        ret.setColumns(result.getInt("NumColumns"));
        ret.setSubPositions(1+result.getInt("MaxNumDrops"));
        ret.setReservoir(1);
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

    static TrialDrop doFindTrialDrop(ManufacturerConnection mfrConnection, String barcode,
        WellPosition wellPosition, Map<Long, Sample> cache) throws SQLException {

        String rowLetter=wellPosition.toString().substring(0, 1);
        int columnNumber=wellPosition.getColumn();
        int dropNumber=wellPosition.getSubPosition();

    	ManufacturerPreparedStatement statement =
            mfrConnection
            	.prepareStatement("SELECT WellDrop.ID AS DropID FROM " +
            			" Plate, Experiment, Well, WellDrop " +
            			" WHERE Plate.ExperimentID=Experiment.ID AND Well.PlateID=Plate.ID AND WellDrop.WellID=Well.ID" +
		                " AND Plate.Barcode=? AND Well.RowLetter=? AND Well.ColumnNumber=? AND WellDrop.DropNumber=? ");
        
        statement.setString(1, barcode);
        statement.setString(2, rowLetter);
        statement.setInt(3, columnNumber);
        statement.setInt(4, dropNumber);
        ResultSet result = statement.executeQuery();
        boolean found = result.next();
        TrialDrop ret = null;
        if (found) {
            ret = makeTrialDropBean(mfrConnection, result, wellPosition, cache);
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
    private static void addTrialDrops(ManufacturerConnection connection2, TrialPlate plate, Map<Long, Sample> cache)
        throws SQLException {

        ManufacturerPreparedStatement statement =
                connection2
                    .prepareStatement("SELECT " +
                    		"Well.ID AS WellID, Well.RowLetter AS RowLetter, Well.ColumnNumber AS ColumnNumber, " +
                    		"WellDrop.ID AS DropID, WellDrop.DropNumber AS SubPosition "
                        + "FROM Containers, Experiment, Plate, Well, WellDrop "
                        + " WHERE Containers.ID=Experiment.ContainerID AND Experiment.ID=Plate.ExperimentID AND Plate.ID=Well.PlateID AND Well.ID=WellDrop.WellID"
                        + " AND Plate.Barcode=?");
        statement.setString(1, plate.getBarcode());
        ResultSet result = statement.executeQuery();
        while (result.next()) {
        	String row=result.getString("RowLetter");
        	int column=result.getInt("columnNumber");
        	int subPosition=result.getInt("subPosition");
            WellPosition position = new WellPosition(row+String.format("%02d", column)+"."+subPosition);
            TrialDrop drop = makeTrialDropBean(connection2, result, position, cache);
            plate.addTrialDrop(drop);
        }
        result.close();
        statement.close();
    }

    private static TrialDrop makeTrialDropBean(ManufacturerConnection connection, ResultSet result,
        WellPosition position, Map<Long, Sample> cache) throws SQLException {

        TrialDrop ret = new TrialDrop();
        ret.setId(result.getLong("DropID"));
        ret.setWellPosition(position);
// Not for Oulu
//        ret.setAdditiveScreen(true);
        //MAYBE get additive details here
        // could use EXPERIMENT_ID
// Not for Oulu
//        long sampleId = result.getLong("MACROMOLECULE_VERSION_ID");
//        if (0 != sampleId) {
//            Sample sample = SampleServiceImpl.doFind(connection, sampleId, cache);
//            float ratio = result.getFloat("DROP_RATIO_PROTEIN");
//            float volume = result.getFloat("TOTAL_DROP_VOLUME");
//            double amount = volume * ratio; //MAYBE check the units, I have assumed nL
//            SampleQuantity quantity = new SampleQuantity(sample, amount * Math.pow(10, -9), "L");
//            ret.addSample(quantity);
//        }
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
     *      org.pimslims.business.criteria.BusinessCriteria)
     */
    public Collection<TrialDrop> findTrialDrops(Sample sample, BusinessCriteria criteria)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.findTrialDrops
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findTrialDrops(org.pimslims.business.crystallization.model.TrialPlate,
     *      org.pimslims.business.criteria.BusinessCriteria)
     */
    public Collection<TrialDrop> findTrialDrops(TrialPlate plate, BusinessCriteria criteria)
        throws BusinessException {
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
     * TrialServiceImpl.findTrialDrops
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#findTrialDrops(org.pimslims.business.core.model.Sample,
     *      java.util.Collection, org.pimslims.business.criteria.BusinessCriteria)
     */
    public Collection<TrialDrop> findTrialDrops(Sample sample, Collection<Condition> conditions,
        BusinessCriteria criteria) throws BusinessException {
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
     * @param id Plate.ID
     * @param cache
     * @return
     * @throws SQLException
     * @throws BusinessException
     */
    static TrialPlate doFindTrialPlate(ManufacturerConnection connection2, long id, Map<Long, Sample> cache)
        throws SQLException, BusinessException {
        ManufacturerPreparedStatement statement =
                connection2.prepareStatement("SELECT Plate.ID AS PlateID, CONVERT(varchar(25),Plate.DateDispensed,121) AS DispensedDate, " +
            		"Plate.Barcode AS Barcode, Plate.ExperimentID AS ExperimentID, " +
            		"Experiment.ContainerID AS PlateTypeID, Experiment.UserID AS OwnerID " +
            		"FROM Plate " +
            		"JOIN Experiment ON (Plate.ExperimentID = Experiment.ID) " +
            		"WHERE Plate.ID=?"
            );
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
    static long doFindTrialPlateId(ManufacturerConnection connection2, String barcode) throws SQLException {
        ManufacturerPreparedStatement statement =
            connection2.prepareStatement("SELECT ID FROM Plate WHERE Barcode=?");
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
            ManufacturerPreparedStatement statement =
                	this.connection.prepareStatement("SELECT Plate.ID AS PlateID, CONVERT(varchar(25),Plate.DateDispensed,121) AS DispensedDate, " +
                    		"Plate.Barcode AS Barcode, Plate.ExperimentID AS ExperimentID, " +
                    		"Experiment.ContainerID AS PlateTypeID, Experiment.UserID AS OwnerID " +
                    		"FROM Plate " +
                    		"JOIN Experiment ON (Plate.ExperimentID = Experiment.ID) " + 
                			" WHERE Plate.Barcode=?");
            
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

    private static TrialPlateImpl makePlateBean(ManufacturerConnection connection2, ResultSet result,
        Map<Long, Sample> cache) throws SQLException, BusinessException {
        TrialPlateImpl ret = null;
        if (result.next()) {
            long typeId = result.getLong("PlateTypeID");
            PlateType type = doFindPlateType(connection2, typeId);
            assert null != type : "No such plate type: " + typeId;
            ret = new TrialPlateImpl(type);
            ret.setId(result.getLong("PlateID")); 
            ret.setBarcode(result.getString("Barcode"));
            ret.setMfrExperimentId(result.getLong("ExperimentID"));

            try {
                Calendar createDate = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            	Date d = sdf.parse(result.getString("DispensedDate"));
            	createDate.setTime(d);
                ret.setCreateDate(createDate);
            } catch (ParseException e){
            	throw new BusinessException(e);
            }

            long ownerId = result.getLong("OwnerID"); 
            if (0L != ownerId) {
                Person owner = PersonServiceImpl.getPerson(connection2, ownerId);
                ret.setOwner(owner);
            }
            
            //MAYBE ret.setScreen
            /* Not for Formulatrix/Oulu - Ed
            long screenId = result.getLong("SCREEN_ID"); /* FIXME column name 
            if (0L != screenId) {
                Screen screen = ScreenServiceImpl.getScreen(connection2, screenId);
                ret.setScreen(screen);
            }
            */
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
        // MAYBE macro_molecule_version_id

        try {
            insertDrop(trialDrop);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
        return this.findTrialDrop(trialDrop.getPlate().getBarcode(), trialDrop.getWellPosition());
    }

    private void insertDrop(TrialDrop trialDrop) throws SQLException {
        
        //first up, we need the well's database ID. This is tedious.
        WellPosition wp=trialDrop.getWellPosition();
        String rowName=wp.toString().substring(0, 1);
        int columnNumber=wp.getColumn();
        long plateID=trialDrop.getPlate().getId();

        String sql="SELECT ID AS WellID FROM Well WHERE PlateID=? AND RowLetter=? AND ColumnNumber=?";
        
        ManufacturerPreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setLong(1, plateID);
        statement.setString(2, rowName);
        statement.setInt(3, columnNumber);
        ResultSet result=statement.executeQuery();
        result.next();
        long wellID=result.getLong("WellID");
        statement.close();

        //Now, we can insert the drop.
        sql="INSERT INTO WellDrop " +
        		"(WellID, DropNumber, DropWellVolume, DropProteinVolume, Volume)" +
        		"VALUES(?, ?, 0.05, 0.05, 0.1)";
        statement = this.connection.prepareStatement(sql);
        statement.setLong(1, wellID);
        statement.setInt(2, trialDrop.getWellPosition().getSubPosition());
        statement.executeUpdate();
        statement.close();

        
//        long sampleId = 0;
//        List<SampleQuantity> quantities = trialDrop.getSamples();
//        if (!quantities.isEmpty()) {
//            assert 1 == quantities.size();
//            sampleId = quantities.iterator().next().getSample().getId();
//        }
//
//        statement =
//            this.connection
//                .prepareStatement(/* FIXME rewrite */ "insert into DROP_SITE_DETAIL "
//                    + "(site_id, DROP_SITE_DETAIL_ID, GRID_ID, LU_COMPARTMENT_ID, EXPERIMENT_ID, MACROMOLECULE_VERSION_ID)"
//                    + "values (1, DROP_SITE_DETAIL_SEQ.nextval, GRID_SEQ.currval, ?, ?, ?)");
//
//        statement.setInt(1, trialDrop.getWellPosition().getSubPosition());
//        statement.setLong(2, ((TrialPlateImpl) trialDrop.getPlate()).getMfrExperimentId());
//        statement.setLong(3, sampleId);
//        statement.executeUpdate();
//        statement.close();
//
//        ManufacturerPreparedStatement q =
//            connection.prepareStatement(/* FIXME rewrite */ "select DROP_SITE_DETAIL_SEQ.currval from dual");
//        ResultSet result = q.executeQuery();
//        result.next();
//        trialDrop.setId(result.getLong(1));
//        q.close();
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
     * TrialServiceImpl.setCompositeImageURLStub
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#setCompositeImageURLStub(java.lang.String)
     */
    public void setCompositeImageURLStub(String urlStub) {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.setSliceImageURLStub
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#setSliceImageURLStub(java.lang.String)
     */
    public void setSliceImageURLStub(String urlStub) {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.setZoomedImageURLStub
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#setZoomedImageURLStub(java.lang.String)
     */
    public void setZoomedImageURLStub(String urlStub) {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * TrialServiceImpl.updateTrialDrop
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#updateTrialDrop(org.pimslims.business.crystallization.model.TrialDrop)
     */
    @Override
//TODO 02.2
    public void updateTrialDrop(TrialDrop trialDrop) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
//        long sampleId = 0;
//        if (!trialDrop.getSamples().isEmpty()) {
//            assert 1 == trialDrop.getSamples().size();
//            SampleQuantity quantity = trialDrop.getSamples().iterator().next();
//            //MAYBE quantity.getQuantity()
//            Sample sample = quantity.getSample();
//            sampleId = sample.getId();
//        }
//
//        try {
//            ManufacturerPreparedStatement statement =
//                this.connection.prepareStatement(/* FIXME rewrite */  "update DROP_SITE_DETAIL "
//                    + "set MACROMOLECULE_VERSION_ID=?" + "where DROP_SITE_DETAIL_ID=?");
//            statement.setLong(2, trialDrop.getId());
//            statement.setLong(1, sampleId);
//            statement.executeUpdate();
//            statement.close();
//        } catch (SQLException e) {
//            throw new BusinessException(e);
//        }

    }

    /**
     * TrialServiceImpl.updateTrialPlate
     * 
     * @see org.pimslims.business.crystallization.service.TrialService#updateTrialPlate(org.pimslims.business.crystallization.model.TrialPlate)
     */
    @Override
    public void updateTrialPlate(TrialPlate trialPlate) throws BusinessException {

        try {
            ManufacturerPreparedStatement statement =
                    this.connection.prepareStatement("SELECT ExperimentID FROM Plate WHERE ID=?");
            statement.setLong(1, trialPlate.getId());
            ResultSet result=statement.executeQuery();
            result.next();
            long experimentID=result.getLong("ExperimentID");
            statement.close();
            statement=this.connection.prepareStatement("UPDATE Experiment " +
            		" SET UserID=? " +
            		" WHERE ID=?");

            if (null == trialPlate.getOwner()) {
                statement.setNull(1, java.sql.Types.BIGINT);
            } else {
                long id = trialPlate.getOwner().getId();
                statement.setLong(1, id);
            }
            statement.setLong(2, experimentID);
            statement.executeUpdate();
            statement.close();
/* Not for Oulu */
//            statement =
//                this.connection.prepareStatement(/* FIXME rewrite */ "update EXPERIMENT set (SCREEN_ID)" + "= (  ?)" + " where "
//                    + "EXPERIMENT_ID" + "=?");
//
//            if (null == trialPlate.getScreen()) {
//                statement.setNull(1, java.sql.Types.BIGINT);
//            } else {
//                long id = trialPlate.getScreen().getId();
//                statement.setLong(1, id);
//            }
//
//            statement.setLong(2, ((TrialPlateImpl) trialPlate).getMfrExperimentId());
//            statement.executeUpdate();
//            statement.close();
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
        // MAYBE use the schedule plan
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
