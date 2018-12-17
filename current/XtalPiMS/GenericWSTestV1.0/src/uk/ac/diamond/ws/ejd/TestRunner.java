package uk.ac.diamond.ws.ejd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axis2.databinding.ADBException;

import uk.ac.diamond.genericws.client.GenericWSV10Stub.AllowedDatabase;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.AllowedFlag;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.AllowedOperator;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.AllowedOrder;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.AllowedTable;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.BuildSQL;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.BuildSQLResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.Confirmation;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.ConstraintTriplet;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.EchoString;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.EchoStringResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetAuthToken;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetAuthTokenResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetFile;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetFileParameters;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetFileResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.LogoutResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.OrderPair;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.QueryParameters;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.Logout;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.LogoutParameters;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.UpdateComments;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.UpdateCommentsParameters;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.UpdateCommentsResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetVisitInfo;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetVisitInfoParameters;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetVisitInfoResponse;

public class TestRunner {
	
	public static String fedId;
	public static String password;
	
	static {
		Properties properties=new Properties();
    	File file=new File("conf/Properties");
    	try {
			InputStream in = new FileInputStream(file);
			properties.load(in);
			TestRunner.fedId=properties.getProperty("diamond.fedid");
			TestRunner.password=properties.getProperty("diamond.password");
		} catch (IOException e) {
			if (e instanceof FileNotFoundException) {
                System.err.println("Could not find connection details at: " + file.getAbsolutePath());
            }
            throw new RuntimeException(e);
        }
	}
	
	static public void main(String args[]){
		
		String fedid = TestRunner.fedId;
		String password = TestRunner.password;

		String trustStore = System.getProperty("javax.net.ssl.trustStore");
        if (trustStore == null) {
            System.out.println("javax.net.ssl.trustStore is not defined");
        } else {
            System.out.println("javax.net.ssl.trustStore = " + trustStore);
        }
		
		RESTTestGenericClient client = new RESTTestGenericClient();
				
//		System.out.println("\n***********************");
//		System.out.println("\n*** GetVersion V1.0 ***");
//		System.out.println("\n***********************");
//		
//		uk.ac.diamond.genericws.client.GenericWSV10Stub.GetVersion versionInput = new uk.ac.diamond.genericws.client.GenericWSV10Stub.GetVersion();
//		versionInput.setGetVersion("xxx");
				
//		uk.ac.diamond.genericws.client.GenericWSV10Stub.GetVersionResponse versionResult = (uk.ac.diamond.genericws.client.GenericWSV10Stub.GetVersionResponse) client.getVersion(versionInput);
//		System.out.println("getVersionResponse: " + versionResult.getGetVersionResponse());
		
//		System.out.println("\n*************************");
//		System.out.println("\n***   EchoString V1.0 ***");
//		System.out.println("\n*************************");
		
//		EchoString echoInput = new EchoString();
//		echoInput.setEchoString("catface");
		
//		 EchoStringResponse reecho = (EchoStringResponse) client.echoString(echoInput);
//		 System.out.println("echoResponse: " + reecho.getEchoStringResponse());
		
		// set credentials object to be used by all queries
		uk.ac.diamond.genericws.client.GenericWSV10Stub.Credentials cred = new uk.ac.diamond.genericws.client.GenericWSV10Stub.Credentials();
		cred.setFedid(fedid);
		cred.setPassword(password);
			
		System.out.println("\n**************************");
		System.out.println("\n***  GetAuthToken V1.0 ***");
		System.out.println("\n**************************");
		
		GetAuthToken getAuthTokenInput = new GetAuthToken();
		getAuthTokenInput.setGetAuthToken(cred);
		
		GetAuthTokenResponse response = client.GetAuthToken(getAuthTokenInput);
        String token = response.getToken();
        System.out.println("token = " +  token);
        
        
        System.out.println("\n***********************");
		System.out.println("\n***  GetVisits V1.0 ***");
		System.out.println("\n***********************");
		
		BuildSQL buildSQLInput1 = new BuildSQL();	
		
		QueryParameters param1 = new QueryParameters();
		param1.setDbname(AllowedDatabase.ispyb);
		AllowedTable table = AllowedTable.blsession;
		param1.setTablename(table);
		param1.setFedid(fedid);
		param1.setToken(token);
		String[] columns1 = {"sessionid", "proposalid", "startdate", "beamlinename", "beamlineoperator", "projectcode", "visit_number"}; 

		param1.setColumnNames(columns1);
		ConstraintTriplet[] triplet1 = new ConstraintTriplet[1];

		triplet1[0] = new ConstraintTriplet();
		triplet1[0].setConstraintColumn("sessionid") ; 
		AllowedOperator op1 = AllowedOperator.value2;
		triplet1[0].setConstraintOperator(op1); 
		triplet1[0].setConstraintValue("-1");

		param1.setConstraints(triplet1);

		param1.setRangestart(0);
		param1.setRangeend(1000);

		OrderPair orderpair = new OrderPair();
		orderpair.setOrderColumn("startdate");
		orderpair.setOrderType(AllowedOrder.desc);
		OrderPair[] pairs = new OrderPair[1];
		pairs[0] = orderpair;
		param1.setOrderpair(pairs);

		buildSQLInput1.setBuildSQL(param1);

		BuildSQLResponse res1 = client.buildSQL(buildSQLInput1);		
		displayRESTResponseObject(res1);
		

/*
		

		System.out.println("\n*************************");
		System.out.println("\n***  GetProteins V1.0 ***");
		System.out.println("\n*************************");
		
		BuildSQL buildSQLInput1a = new BuildSQL();	
		
		QueryParameters param1a = new QueryParameters();
		param1a.setDbname(AllowedDatabase.ispyb);
		AllowedTable table1a = AllowedTable.protein;
		param1a.setTablename(table1a);
		param1a.setFedid(fedid);
		param1a.setToken(token);
		String[] columns1a = {"proteinid", "name", "acronym", "molecularmass", "proteintype", "personid", "bltimestamp", "iscreatedbysamplesheet", "sequence"}; 
		
		param1a.setColumnNames(columns1a);
		ConstraintTriplet[] triplet1a = new ConstraintTriplet[1];

		triplet1a[0] = new ConstraintTriplet();
		triplet1a[0].setConstraintColumn("proteinid") ;
		AllowedOperator op1a = AllowedOperator.value2;
		triplet1a[0].setConstraintOperator(op1a); 
		triplet1a[0].setConstraintValue("-1");

		param1a.setConstraints(triplet1a);
		
		param1a.setRangestart(0);
		param1a.setRangeend(10);

		OrderPair orderpair1a = new OrderPair();
		orderpair1a.setOrderColumn("proteinid");
		orderpair1a.setOrderType(AllowedOrder.desc);
		OrderPair[] pairs1a = new OrderPair[1];
		pairs1a[0] = orderpair1a;
		param1a.setOrderpair(pairs1a);

		buildSQLInput1a.setBuildSQL(param1a);

		BuildSQLResponse res1a = client.buildSQL(buildSQLInput1a);		
		displayRESTResponseObject(res1a);
		
		
		System.out.println("\n**************************");
		System.out.println("\n***  GetBLSamples V1.0 ***");
		System.out.println("\n**************************");
		
		BuildSQL buildSQLInput1b = new BuildSQL();	
		
		QueryParameters param1b = new QueryParameters();
		param1b.setDbname(AllowedDatabase.ispyb);
		AllowedTable table1b = AllowedTable.blsample;
		param1b.setTablename(table1b);
		param1b.setFedid(fedid);
		param1b.setToken(token);
		String[] columns1b = {"blsampleid", "name", "code", "location", "comments", "blsamplestatus", "isinsamplechanger", "crystalid", "containerid"}; 
		
		param1b.setColumnNames(columns1b);
		ConstraintTriplet[] triplet1b = new ConstraintTriplet[1];

		triplet1b[0] = new ConstraintTriplet();
		triplet1b[0].setConstraintColumn("blsampleid") ;
		AllowedOperator op1b = AllowedOperator.value2;
		triplet1b[0].setConstraintOperator(op1b); 
		triplet1b[0].setConstraintValue("-1");

		param1b.setConstraints(triplet1b);
		
		param1b.setRangestart(0);
		param1b.setRangeend(10);

		OrderPair orderpair1b = new OrderPair();
		orderpair1b.setOrderColumn("blsampleid");
		orderpair1b.setOrderType(AllowedOrder.desc);
		OrderPair[] pairs1b = new OrderPair[1];
		pairs1b[0] = orderpair1b;
		param1b.setOrderpair(pairs1b);

		buildSQLInput1b.setBuildSQL(param1b);

		BuildSQLResponse res1b = client.buildSQL(buildSQLInput1b);		
		displayRESTResponseObject(res1b);

		

		
		
		System.out.println("\n*************************");
		System.out.println("\n***  GetCrystals V1.0 ***");
		System.out.println("\n*************************");
		
		BuildSQL buildSQLInput1c = new BuildSQL();	
		
		QueryParameters param1c = new QueryParameters();
		param1c.setDbname(AllowedDatabase.ispyb);
		AllowedTable table1c = AllowedTable.crystal;
		param1c.setTablename(table1c);
		param1c.setFedid(fedid);
		param1c.setToken(token);
		String[] columns1c = {"crystalid", "p.acronym", "proteinid", "diffractionplanid", "crystaluuid", "name", "spacegroup", "morphology", "color", "size_x", "size_y", "size_z", "cell_a", "cell_b", "cell_c", "cell_alpha", "cell_beta", "cell_gamma", "comments"}; 
		
		param1c.setColumnNames(columns1c);
		ConstraintTriplet[] triplet1c = new ConstraintTriplet[1];

		triplet1c[0] = new ConstraintTriplet();
		triplet1c[0].setConstraintColumn("p.proteinid") ;
		triplet1c[0].setConstraintOperator(AllowedOperator.value2); // gt 
		triplet1c[0].setConstraintValue("-1");

		param1c.setConstraints(triplet1c);
		
		param1c.setRangestart(0);
		param1c.setRangeend(10);

		OrderPair orderpair1c = new OrderPair();
		orderpair1c.setOrderColumn("crystalid");
		orderpair1c.setOrderType(AllowedOrder.desc);
		OrderPair[] pairs1c = new OrderPair[1];
		pairs1c[0] = orderpair1c;
		param1c.setOrderpair(pairs1c);

		buildSQLInput1c.setBuildSQL(param1c);

		BuildSQLResponse res1c = client.buildSQL(buildSQLInput1c);		
		displayRESTResponseObject(res1c);
		
		
		System.out.println("\n*************************");
		System.out.println("\n***  GetProposal V1.0 ***");
		System.out.println("\n*************************");
		
		BuildSQL buildSQLInput2 = new BuildSQL();     
        
        QueryParameters param2 = new QueryParameters(); 
        param2.setDbname(AllowedDatabase.ispyb); 
        AllowedTable table2 = AllowedTable.proposal; 
        param2.setTablename(table2); 
        
        param2.setFedid(fedid); 
        param2.setToken(token); 
        String[] columns2 = {"proposalid", "title", "proposalcode", "proposalnumber"};  
 
        param2.setColumnNames(columns2); 
        ConstraintTriplet[] triplet2 = new ConstraintTriplet[1]; 

        triplet2[0] = new ConstraintTriplet(); 
        triplet2[0].setConstraintColumn("proposalid") ;  
        AllowedOperator op2 = AllowedOperator.value1; 
        triplet2[0].setConstraintOperator(op2);  
        triplet2[0].setConstraintValue("21766"); 

        param2.setConstraints(triplet2); 
 
        param2.setRangestart(0); 
        param2.setRangeend(1000); 

        OrderPair orderpair2 = new OrderPair(); 
        orderpair2.setOrderColumn("proposalid"); 
        orderpair2.setOrderType(AllowedOrder.desc); 
        OrderPair[] pairs2 = new OrderPair[1]; 
        pairs2[0] = orderpair2; 
        param2.setOrderpair(pairs2); 

        buildSQLInput2.setBuildSQL(param2); 
 
        BuildSQLResponse res2 = client.buildSQL(buildSQLInput2);
                
        displayRESTResponseObject(res2);
        

        
		System.out.println("\n*************************");
		System.out.println("\n***  GetShipping V1.0 ***");
		System.out.println("\n*************************");
		
		BuildSQL buildSQLInput3 = new BuildSQL();     
        
        QueryParameters param3 = new QueryParameters(); 
        param3.setDbname(AllowedDatabase.ispyb); 
        param3.setTablename(AllowedTable.shipping); 
        
        param3.setFedid(fedid); 
        param3.setToken(token); 
        String[] columns3 = {"shippingid", "proposalid", "shippingname", "shippingstatus", "shippingtype", "safetylevel"};  
 
        param3.setColumnNames(columns3); 
        ConstraintTriplet[] triplet3 = new ConstraintTriplet[1]; 

        triplet3[0] = new ConstraintTriplet(); 
        triplet3[0].setConstraintColumn("shippingid") ;  
        triplet3[0].setConstraintOperator(AllowedOperator.value2);  
        triplet3[0].setConstraintValue("-1"); 

        param3.setConstraints(triplet3); 
 
        param3.setRangestart(0); 
        param3.setRangeend(10); 

        OrderPair orderpair3 = new OrderPair(); 
        orderpair3.setOrderColumn("shippingid"); 
        orderpair3.setOrderType(AllowedOrder.desc); 
        OrderPair[] pairs3 = new OrderPair[1]; 
        pairs3[0] = orderpair3; 
        param3.setOrderpair(pairs3); 

        buildSQLInput3.setBuildSQL(param3); 
 
        BuildSQLResponse res3 = client.buildSQL(buildSQLInput3);
                
        displayRESTResponseObject(res3);

        
		System.out.println("\n**********************");
		System.out.println("\n***  GetDewar V1.0 ***");
		System.out.println("\n**********************");
		
		BuildSQL buildSQLInput4 = new BuildSQL();     
        
        QueryParameters param4 = new QueryParameters(); 
        param4.setDbname(AllowedDatabase.ispyb); 
        param4.setTablename(AllowedTable.dewar); 
        
        param4.setFedid(fedid); 
        param4.setToken(token); 
        String[] columns4 = {"dewarid", "shippingid", "code", "comments", "dewarstatus", "type", "barcode", "storagelocation"};  
 
        param4.setColumnNames(columns4); 
        ConstraintTriplet[] triplet4 = new ConstraintTriplet[1]; 

        triplet4[0] = new ConstraintTriplet(); 
        triplet4[0].setConstraintColumn("dewarid") ;  
        triplet4[0].setConstraintOperator(AllowedOperator.value2);  
        triplet4[0].setConstraintValue("-1"); 

        param4.setConstraints(triplet4); 
 
        param4.setRangestart(0); 
        param4.setRangeend(10); 

        OrderPair orderpair4 = new OrderPair(); 
        orderpair4.setOrderColumn("dewarid"); 
        orderpair4.setOrderType(AllowedOrder.desc); 
        OrderPair[] pairs4 = new OrderPair[1]; 
        pairs4[0] = orderpair4; 
        param4.setOrderpair(pairs4); 

        buildSQLInput4.setBuildSQL(param4); 
 
        BuildSQLResponse res4 = client.buildSQL(buildSQLInput4);
                
        displayRESTResponseObject(res4);

        
		System.out.println("\n**************************");
		System.out.println("\n***  GetContainer V1.0 ***");
		System.out.println("\n**************************");
		
		BuildSQL buildSQLInput5 = new BuildSQL();     
        
        QueryParameters param5 = new QueryParameters(); 
        param5.setDbname(AllowedDatabase.ispyb); 
        param5.setTablename(AllowedTable.container); 
        
        param5.setFedid(fedid); 
        param5.setToken(token); 
        String[] columns5 = {"containerid", "dewarid", "code", "capacity", "containerstatus", "containertype", "samplechangerlocation", "beamlinelocation"};  
 
        param5.setColumnNames(columns5); 
        ConstraintTriplet[] triplet5 = new ConstraintTriplet[1]; 

        triplet5[0] = new ConstraintTriplet(); 
        triplet5[0].setConstraintColumn("containerid") ;  
        triplet5[0].setConstraintOperator(AllowedOperator.value2);  
        triplet5[0].setConstraintValue("-1"); 

        param5.setConstraints(triplet5); 
 
        param5.setRangestart(0); 
        param5.setRangeend(10); 

        OrderPair orderpair5 = new OrderPair(); 
        orderpair5.setOrderColumn("containerid"); 
        orderpair5.setOrderType(AllowedOrder.desc); 
        OrderPair[] pairs5 = new OrderPair[1]; 
        pairs5[0] = orderpair5; 
        param5.setOrderpair(pairs5); 

        buildSQLInput5.setBuildSQL(param5); 
 
        BuildSQLResponse res5 = client.buildSQL(buildSQLInput5);
                
        displayRESTResponseObject(res5);

        
        
		System.out.println("\n******************************");
		System.out.println("\n***    get Datacollections ***");
		System.out.println("\n******************************");
		
		BuildSQL buildSQLInputDC = new BuildSQL();	
		
		QueryParameters paramDC = new QueryParameters();
		paramDC.setDbname(AllowedDatabase.ispyb);
		AllowedTable tableDC = AllowedTable.datacollection;
		paramDC.setTablename(tableDC);
		paramDC.setFedid(fedid);
		paramDC.setToken(token);
        String[] columnsDC = {"datacollectionnumber", "wavelength", "omegastart" ,"polarisation","rotationaxis", "exposuretime","beamsizeatsamplex","beamsizeatsampley", "transmission", "resolution", "comments", "imageprefix", "datacollectionid", "blsampleid", "sessionid", "starttime", "endtime", "numberofimages", "imagedirectory"};  

		paramDC.setColumnNames(columnsDC);
		ConstraintTriplet[] tripletDC = new ConstraintTriplet[1];

		tripletDC[0] = new ConstraintTriplet();
		tripletDC[0].setConstraintColumn("sessionid") ; 
		AllowedOperator opDC = AllowedOperator.value2;
		tripletDC[0].setConstraintOperator(opDC); 
		tripletDC[0].setConstraintValue("-1");

		paramDC.setConstraints(tripletDC);

		paramDC.setRangestart(0);
		paramDC.setRangeend(10);

		OrderPair orderpairDC = new OrderPair();
		orderpairDC.setOrderColumn("starttime");
		orderpairDC.setOrderType(AllowedOrder.desc);
		OrderPair[] pairsDC = new OrderPair[1];
		pairsDC[0] = orderpairDC;
		paramDC.setOrderpair(pairsDC);

		buildSQLInputDC.setBuildSQL(paramDC);
		
		
		BuildSQLResponse resDC = client.buildSQL(buildSQLInputDC);		
		//displayRESTResponseObject(resDC);
					

	
*/

//////////////////////////////////////////////////////////////////////////////////////////////////

		/*
		
		System.out.println("\n*************************");
		System.out.println("\n***    GetFile  V1.0 ****");
		System.out.println("\n*************************");
		String downloadDir = System.getProperty("user.home");
		
		GetFile getFileInput = new GetFile();
		GetFileParameters paramF = new GetFileParameters();
		paramF.setFedid(cred.getFedid());
		paramF.setToken(token);

		//paramF.setFileid("59909995"); // file that doesn't exist, to test ws exception
		//paramF.setFileid("/dls/p45/data/2013/cm5952-1/2014-02-25/fake140034/th_8_1_0001.cbf"); 
		paramF.setFileid("/dls/i03/data/2012/nt5862-1/processed/cd44/cd44_3_2_/fast_dp/scala.log");
		
		paramF.setImageflag(AllowedFlag.nonimage);
		getFileInput.setGetFile(paramF);
		GetFileResponse responseF = client.getFile(getFileInput );
		
		System.out.println("\nresponse: " + responseF.getMessage());
				
		if(responseF.getFile() != null){
			System.out.println("\nfilename: " + responseF.getFileName());
			try {
				File receivedFile = new File(combine(downloadDir, "scala.log")); //responseF.getFileName()));
				FileOutputStream outputStream = new FileOutputStream(receivedFile);
				responseF.getFile().writeTo(outputStream);
				outputStream.flush();
				System.out.println("\nfile downloaded to: " +receivedFile.getAbsolutePath());

			} catch (IOException e) {
				e.printStackTrace();
			}
	}//endif
		
		*/

//////////////////////////////////////////////////////////////////////////////////////////////////
		

/*		
		System.out.println("\n***********************");
		System.out.println("\n***  UpdateComments ***");
		System.out.println("\n***********************");
		
		UpdateComments updateCommentsInput = new UpdateComments();	
		
		UpdateCommentsParameters updateParameters = new UpdateCommentsParameters();
		updateParameters.setFedid(cred.getFedid());
		updateParameters.setToken(token);
		updateParameters.setDatacollectionid("493770");
		updateParameters.setComments("xxxxxxxxxxxxx");
		
		updateCommentsInput.setUpdateComments(updateParameters );
		
		UpdateCommentsResponse res = client.UpdateComments(updateCommentsInput);
		
		Confirmation conf = res.getStatus();
		String confValue = conf.getValue();
		
		System.out.println("\nconfValue: " + confValue);
		System.out.println("\nmessage: " + res.getMessage());

		
		
		System.out.println("\n***********************");
		System.out.println("\n***  GetVisitInfo   ***");
		System.out.println("\n***********************");
		
		GetVisitInfo getVisitInfo = new GetVisitInfo();	
		GetVisitInfoParameters getVisitInfoParams = new GetVisitInfoParameters();	
		
		
		getVisitInfoParams.setFedid(cred.getFedid());
		getVisitInfoParams.setToken(token);
		getVisitInfoParams.setStarttime("01-01-2013");
		getVisitInfoParams.setEndtime("01-02-2013");
		//getVisitInfoParams.setBeamline("");
		
		getVisitInfo.setGetVisitInfo(getVisitInfoParams);
		
		GetVisitInfoResponse gviRes = client.getVisitInfo(getVisitInfo);
		Confirmation gviConf = gviRes.getStatus();
		String gviConfValue = gviConf.getValue();
		
		System.out.println("\nconfValue: " + gviConfValue);
		System.out.println("\nmessage: " + gviRes.getMessage());
	
		displayRESTResponseObject(gviRes);

		
		System.out.println("\n******************************");
		System.out.println("\n***  GetMacromolecule V1.0 ***");
		System.out.println("\n******************************");
		
		BuildSQL buildSQLInputM = new BuildSQL();     
        
        QueryParameters paramM = new QueryParameters(); 
        paramM.setDbname(AllowedDatabase.ispyb); 
        paramM.setTablename(AllowedTable.macromolecule); 
        
        paramM.setFedid(fedid); 
        paramM.setToken(token); 
        String[] columnsM = {"macromoleculeid", "proposalid", "safetylevelid", "name", "acronym", "molecularmass", "extintioncoefficient", "sequence", "creationdate", "comments"};  
        // extintioncoefficient is misspelled because that's the spelling used in the schema ....
        
        paramM.setColumnNames(columnsM); 
        ConstraintTriplet[] tripletM = new ConstraintTriplet[1]; 

        tripletM[0] = new ConstraintTriplet(); 
        tripletM[0].setConstraintColumn("macromoleculeid") ;  
        tripletM[0].setConstraintOperator(AllowedOperator.value2);  
        tripletM[0].setConstraintValue("-1"); 

        paramM.setConstraints(tripletM); 
 
        paramM.setRangestart(0); 
        paramM.setRangeend(10); 

        OrderPair orderpairM = new OrderPair(); 
        orderpairM.setOrderColumn("macromoleculeid"); 
        orderpairM.setOrderType(AllowedOrder.desc); 
        OrderPair[] pairsM = new OrderPair[1]; 
        pairsM[0] = orderpairM; 
        paramM.setOrderpair(pairsM); 

        buildSQLInputM.setBuildSQL(paramM); 
 
        BuildSQLResponse resM = client.buildSQL(buildSQLInputM);
                
        displayRESTResponseObject(resM);


		System.out.println("\n********************");
		System.out.println("\n***  GetRun V1.0 ***");
		System.out.println("\n********************");
		
		BuildSQL buildSQLInputR = new BuildSQL();     
        
        QueryParameters paramR = new QueryParameters(); 
        paramR.setDbname(AllowedDatabase.ispyb); 
        paramR.setTablename(AllowedTable.run); 
        
        paramR.setFedid(fedid); 
        paramR.setToken(token); 
        String[] columnsR = {"runid", "timeperframe", "timestart", "timeend", "energy", "creationdate", "frameaverage", "framecount", "transmission", "normalization"};  
        
        paramR.setColumnNames(columnsR); 
        ConstraintTriplet[] tripletR = new ConstraintTriplet[1]; 

        tripletR[0] = new ConstraintTriplet(); 
        tripletR[0].setConstraintColumn("runid") ;  
        tripletR[0].setConstraintOperator(AllowedOperator.value2);  
        tripletR[0].setConstraintValue("-1"); 

        paramR.setConstraints(tripletR); 
 
        paramR.setRangestart(0); 
        paramR.setRangeend(10); 

        OrderPair orderpairR = new OrderPair(); 
        orderpairR.setOrderColumn("runid"); 
        orderpairR.setOrderType(AllowedOrder.desc); 
        OrderPair[] pairsR = new OrderPair[1]; 
        pairsR[0] = orderpairR; 
        paramR.setOrderpair(pairsR); 

        buildSQLInputR.setBuildSQL(paramR); 
 
        BuildSQLResponse resR = client.buildSQL(buildSQLInputR);
                
        displayRESTResponseObject(resR);

        
		System.out.println("\n**********************");
		System.out.println("\n***  GetModel V1.0 ***");
		System.out.println("\n**********************");
		
		BuildSQL buildSQLInputM2 = new BuildSQL();     
        
        QueryParameters paramM2 = new QueryParameters(); 
        paramM2.setDbname(AllowedDatabase.ispyb); 
        paramM2.setTablename(AllowedTable.model); 
        
        paramM2.setFedid(fedid); 
        paramM2.setToken(token); 
        String[] columnsM2 = {"modelid", "name", "pdbfile", "fitfile", "firfile", "logfile", "rfactor", "chisqrt", "volume", "rg", "dmax"};  
        
        paramM2.setColumnNames(columnsM2); 
        ConstraintTriplet[] tripletM2 = new ConstraintTriplet[1]; 

        tripletM2[0] = new ConstraintTriplet(); 
        tripletM2[0].setConstraintColumn("modelid") ;  
        tripletM2[0].setConstraintOperator(AllowedOperator.value2);  
        tripletM2[0].setConstraintValue("-1"); 

        paramM2.setConstraints(tripletM2); 
 
        paramM2.setRangestart(0); 
        paramM2.setRangeend(10); 

        OrderPair orderpairM2 = new OrderPair(); 
        orderpairM2.setOrderColumn("modelid"); 
        orderpairM2.setOrderType(AllowedOrder.desc); 
        OrderPair[] pairsM2 = new OrderPair[1]; 
        pairsM2[0] = orderpairM2; 
        paramM2.setOrderpair(pairsM2); 

        buildSQLInputM2.setBuildSQL(paramM2); 
 
        BuildSQLResponse resM2 = client.buildSQL(buildSQLInputM2);
                
        displayRESTResponseObject(resM2);
		
		
		System.out.println("\n***********************************");
		System.out.println("\n***  GetSAXSDatacollection V1.0 ***");
		System.out.println("\n***********************************");
		
		BuildSQL buildSQLInputSDC = new BuildSQL();     
        
        QueryParameters paramSDC = new QueryParameters(); 
        paramSDC.setDbname(AllowedDatabase.ispyb); 
        paramSDC.setTablename(AllowedTable.saxsdatacollection); 
        
        paramSDC.setFedid(fedid); 
        paramSDC.setToken(token); 
        String[] columnsSDC = {"datacollectionid", "blsessionid", "experimentid", "comments"};  
        
        paramSDC.setColumnNames(columnsSDC); 
        ConstraintTriplet[] tripletSDC = new ConstraintTriplet[1]; 

        tripletSDC[0] = new ConstraintTriplet(); 
        tripletSDC[0].setConstraintColumn("datacollectionid") ;  
        tripletSDC[0].setConstraintOperator(AllowedOperator.value2);  
        tripletSDC[0].setConstraintValue("-1"); 

        paramSDC.setConstraints(tripletSDC); 
 
        paramSDC.setRangestart(0); 
        paramSDC.setRangeend(10); 

        OrderPair orderpairSDC = new OrderPair(); 
        orderpairSDC.setOrderColumn("datacollectionid"); 
        orderpairSDC.setOrderType(AllowedOrder.desc); 
        OrderPair[] pairsSDC = new OrderPair[1]; 
        pairsSDC[0] = orderpairSDC; 
        paramSDC.setOrderpair(pairsSDC); 

        buildSQLInputSDC.setBuildSQL(paramSDC); 
 
        BuildSQLResponse resSDC = client.buildSQL(buildSQLInputSDC);
                
        displayRESTResponseObject(resSDC);
		
//		System.out.println("\n***********************");
//		System.out.println("\n***    BuildSQL (2) ***");
//		System.out.println("\n***********************");
//		
//		BuildSQL buildSQLInput2 = new BuildSQL();
//		
//		QueryParameters param2 = new QueryParameters();
//		param2.setCredentials(cred);
//		param2.setDbname(AllowedDatabase.ispyb);
//		param2.setTablename(AllowedTable.blsession);
//		String[] columns2 = {"sessionId", "beamlineName", "visit_number"};
//
//		param2.setColumnNames(columns2);
//		
//		ConstraintTriplet[] triplet2 = new ConstraintTriplet[1];
//		triplet2[0] = new ConstraintTriplet();
//		triplet2[0].setConstraintColumn("sessionid") ; 
//		AllowedOperator opb = AllowedOperator.value2;
//		triplet2[0].setConstraintOperator(opb) ; 
//		triplet2[0].setConstraintValue("3045");
//		
//		param2.setConstraints(triplet2);
//
//		param2.setRangeend(100);
//		param2.setRangeend(200);
//				
//		buildSQLInput2.setBuildSQL(param2);
//		
//		BuildSQLResponse res2 = client.buildSQL(buildSQLInput2);
//		displayBuildQueryResult(res2);
//
//		//////////////
//		
//		System.out.println("\n***********************");
//		System.out.println("\n***    BuildSQL (3) ***");
//		System.out.println("\n***********************");
//		BuildSQL buildSQLInput3 = new BuildSQL();
//		
//		QueryParameters param3 = new QueryParameters();
//		param3.setCredentials(cred);
//		param3.setDbname(AllowedDatabase.ispyb);
//		param3.setTablename(AllowedTable.datacollection);
//		String[] columns3 = {"dataCollectionId", "experimentType", "runStatus", "imageDirectory", "imagePrefix", "imageSuffix", 
//		"dataCollectionNumber"};
//
//		param3.setColumnNames(columns3);
//		
//		ConstraintTriplet[] triplet3 = new ConstraintTriplet[1];
//		triplet3[0] = new ConstraintTriplet();
//		triplet3[0].setConstraintColumn("sessionid") ; 
//		triplet3[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet3[0].setConstraintValue("3045");
//		
//		param3.setConstraints(triplet3);
//
//		param3.setRangeend(100);
//		param3.setRangeend(200);
//				
//		buildSQLInput3.setBuildSQL(param3);
//		
//		BuildSQLResponse res3 = client.buildSQL(buildSQLInput3);
//		displayBuildQueryResult(res3);
//		
//		//////////////
//		
//		System.out.println("\n***********************");
//		System.out.println("\n***    BuildSQL (4) ***");
//		System.out.println("\n***********************");
//		
//		BuildSQL buildSQLInput4 = new BuildSQL();
//
//		QueryParameters param4 = new QueryParameters();
//		param4.setCredentials(cred);
//		param4.setDbname(AllowedDatabase.ispyb);
//		param4.setTablename(AllowedTable.image);
//		String[] columns4 = {"imageId", "imageNumber", "fileName", "fileLocation", "comments"};
//
//		param4.setColumnNames(columns4);
//
//		ConstraintTriplet[] triplet4 = new ConstraintTriplet[1];
//		triplet4[0] = new ConstraintTriplet();
//		triplet4[0].setConstraintColumn("datacollectionid") ; 
//		triplet4[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet4[0].setConstraintValue("40782");
//
//		param4.setConstraints(triplet4);
//
//		param4.setRangeend(100);
//		param4.setRangeend(200);
//
//		buildSQLInput4.setBuildSQL(param4);
//
//		BuildSQLResponse res4 = client.buildSQL(buildSQLInput4);
//		displayBuildQueryResult(res4);
//	
//		//////////////
//		
//		System.out.println("\n***********************");
//		System.out.println("\n***    BuildSQL (4b) ***");
//		System.out.println("\n***********************");
//		
//		BuildSQL buildSQLInput4b = new BuildSQL();
//
//		QueryParameters param4b = new QueryParameters();
//		param4b.setCredentials(cred);
//		param4b.setDbname(AllowedDatabase.ispyb);
//		param4b.setTablename(AllowedTable.imagequalityindicators);
//		String[] columns4b = {"imageQualityIndicatorsId", "autoProcProgramId", "spotTotal", "inResTotal", "goodBraggCandidates", "iceRings", "method1Res", "method2Res", "maxUnitCell", "totalIntegratedSignal"};
//
//		param4b.setColumnNames(columns4b);
//
//		ConstraintTriplet[] triplet4b = new ConstraintTriplet[1];
//		triplet4b[0] = new ConstraintTriplet();
//		triplet4b[0].setConstraintColumn("imageid") ; 
//		triplet4b[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet4b[0].setConstraintValue("56573212");
//
//		param4b.setConstraints(triplet4b);
//
//		param4b.setRangeend(0);
//		param4b.setRangeend(100);
//
//		buildSQLInput4b.setBuildSQL(param4b);
//
//		BuildSQLResponse res4b = client.buildSQL(buildSQLInput4b);
//		displayBuildQueryResult(res4b);
//	
//		//////////////
//		
//		System.out.println("\n***********************");
//		System.out.println("\n***    BuildSQL (5) ***");
//		System.out.println("\n***********************");
//		
//		BuildSQL buildSQLInput5 = new BuildSQL();
//
//		QueryParameters param5 = new QueryParameters();
//		param5.setCredentials(cred);
//		param5.setDbname(AllowedDatabase.ispyb);
//		param5.setTablename(AllowedTable.screening);
//		String[] columns5 = {"screeningId", "programVersion", "comments"};
//
//		param5.setColumnNames(columns5);
//
//		ConstraintTriplet[] triplet5 = new ConstraintTriplet[1];
//		triplet5[0] = new ConstraintTriplet();
//		triplet5[0].setConstraintColumn("datacollectionid") ; 
//		triplet5[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet5[0].setConstraintValue("43304");
//
//		param5.setConstraints(triplet5);
//
//		param5.setRangeend(0);
//		param5.setRangeend(10);
//
//		buildSQLInput5.setBuildSQL(param5);
//
//		BuildSQLResponse res5 = client.buildSQL(buildSQLInput5);
//		displayBuildQueryResult(res5);
//	
//		//////////////
//		
//		System.out.println("\n***********************");
//		System.out.println("\n***    BuildSQL (6) ***");
//		System.out.println("\n***********************");
//		
//		BuildSQL buildSQLInput6 = new BuildSQL();
//
//		QueryParameters param6 = new QueryParameters();
//		param6.setCredentials(cred);
//		param6.setDbname(AllowedDatabase.ispyb);
//		param6.setTablename(AllowedTable.screeningoutput);
//		String[] columns6 = {"screeningOutputId", "statusDescription", "numSpotsFound", "numSpotsUsed", "numSpotsRejected", "mosaicity", "iOverSigma", "diffractionRings", "screeningSuccess", "mosaicityEstimated"};
//
//		param6.setColumnNames(columns6);
//
//		ConstraintTriplet[] triplet6 = new ConstraintTriplet[1];
//		triplet6[0] = new ConstraintTriplet();
//		triplet6[0].setConstraintColumn("screeningid") ; 
//		triplet6[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet6[0].setConstraintValue("1938");
//
//		param6.setConstraints(triplet6);
//
//		param6.setRangeend(0);
//		param6.setRangeend(10);
//
//		buildSQLInput6.setBuildSQL(param6);
//
//		BuildSQLResponse res6 = client.buildSQL(buildSQLInput6);
//		displayBuildQueryResult(res6);
//	
//		//////////////
//		
//		System.out.println("\n***********************");
//		System.out.println("\n***    BuildSQL (7) ***");
//		System.out.println("\n***********************");
//		
//		BuildSQL buildSQLInput7 = new BuildSQL();
//
//		QueryParameters param7 = new QueryParameters();
//		param7.setCredentials(cred);
//		param7.setDbname(AllowedDatabase.ispyb);
//		param7.setTablename(AllowedTable.screeningoutputlattice);
//		String[] columns7 = {"screeningOutputLatticeId", "spaceGroup", "pointGroup", "bravaisLattice", "unitCell_a", "unitCell_b", "unitCell_c", "unitCell_alpha", "unitCell_beta", "unitCell_gamma"};
//
//		param7.setColumnNames(columns7);
//
//		ConstraintTriplet[] triplet7 = new ConstraintTriplet[1];
//		triplet7[0] = new ConstraintTriplet();
//		triplet7[0].setConstraintColumn("screeningoutputid") ; 
//		triplet7[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet7[0].setConstraintValue("363635");
//
//		param7.setConstraints(triplet7);
//
//		param7.setRangeend(0);
//		param7.setRangeend(10);
//
//		buildSQLInput7.setBuildSQL(param7);
//
//		BuildSQLResponse res7 = client.buildSQL(buildSQLInput7);
//		displayBuildQueryResult(res7);
//		
//		//////////////
//		
//		System.out.println("\n***********************");
//		System.out.println("\n***    BuildSQL (8) ***");
//		System.out.println("\n***********************");
//
//		BuildSQL buildSQLInput8 = new BuildSQL();
//
//		QueryParameters param8 = new QueryParameters();
//		param8.setCredentials(cred);
//		param8.setDbname(AllowedDatabase.ispyb);
//		param8.setTablename(AllowedTable.screeningstrategy);
//		String[] columns8 = {"screeningStrategyId", "phiStart", "phiEnd", "rotation", "exposureTime", "resolution", "completeness", "multiplicity"};
//
//		param8.setColumnNames(columns8);
//
//		ConstraintTriplet[] triplet8 = new ConstraintTriplet[1];
//		triplet8[0] = new ConstraintTriplet();
//		triplet8[0].setConstraintColumn("screeningoutputid") ; 
//		triplet8[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet8[0].setConstraintValue("363635");
//
//		param8.setConstraints(triplet8);
//
//		param8.setRangeend(0);
//		param8.setRangeend(10);
//
//		buildSQLInput8.setBuildSQL(param8);
//
//		BuildSQLResponse res8 = client.buildSQL(buildSQLInput8);
//		displayBuildQueryResult(res8);
//		
//		//////////////
//		
//		System.out.println("\n***********************");
//		System.out.println("\n***    BuildSQL (9) ***");
//		System.out.println("\n***********************");
//
//		BuildSQL buildSQLInput9 = new BuildSQL();
//
//		QueryParameters param9 = new QueryParameters();
//		param9.setCredentials(cred);
//		param9.setDbname(AllowedDatabase.ispyb);
//		param9.setTablename(AllowedTable.screeningstrategywedge);
//		String[] columns9 = {"screeningStrategyWedgeId", "resolution", "completeness", "multiplicity", "numberOfImages"};
//
//		param9.setColumnNames(columns9);
//
//		ConstraintTriplet[] triplet9 = new ConstraintTriplet[1];
//		triplet9[0] = new ConstraintTriplet();
//		triplet9[0].setConstraintColumn("screeningstrategyid") ; 
//		triplet9[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet9[0].setConstraintValue("356184");
//
//		param9.setConstraints(triplet9);
//
//		param9.setRangeend(0);
//		param9.setRangeend(10);
//
//		buildSQLInput9.setBuildSQL(param9);
//
//		BuildSQLResponse res9 = client.buildSQL(buildSQLInput9);
//		displayBuildQueryResult(res9);
//		
//		//////////////
//		
//		System.out.println("\n***********************");
//		System.out.println("\n***   BuildSQL (10) ***");
//		System.out.println("\n***********************");
//
//		BuildSQL buildSQLInput10 = new BuildSQL();
//
//		QueryParameters param10 = new QueryParameters();
//		param10.setCredentials(cred);
//		param10.setDbname(AllowedDatabase.ispyb);
//		param10.setTablename(AllowedTable.screeningstrategysubwedge);
//		String[] columns10 = {"screeningStrategySubWedgeId", "axisStart", "axisEnd", "resolution", "completeness", "multiplicity", "numberOfImages"};
//
//		param10.setColumnNames(columns10);
//
//		ConstraintTriplet[] triplet10 = new ConstraintTriplet[1];
//		triplet10[0] = new ConstraintTriplet();
//		triplet10[0].setConstraintColumn("screeningstrategywedgeid") ; 
//		triplet10[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet10[0].setConstraintValue("306110");
//
//		param10.setConstraints(triplet10);
//
//		param10.setRangeend(0);
//		param10.setRangeend(10);
//
//		buildSQLInput10.setBuildSQL(param10);
//
//		BuildSQLResponse res10 = client.buildSQL(buildSQLInput10);
//		displayBuildQueryResult(res10);
//		
//		//////////////
//		
//		System.out.println("\n***********************");
//		System.out.println("\n***   BuildSQL (11) ***");
//		System.out.println("\n***********************");
//
//		BuildSQL buildSQLInput11 = new BuildSQL();
//
//		QueryParameters param11 = new QueryParameters();
//		param11.setCredentials(cred);
//		param11.setDbname(AllowedDatabase.ispyb);
//		param11.setTablename(AllowedTable.autoprocintegration);
//		String[] columns11 = {"autoProcIntegrationId", "autoProcProgramId", "startImageNumber", "endImageNumber", 
//				"refinedDetectorDistance", "refinedXBeam", "refinedYBeam", "rotationAxisX", "rotationAxisY", "rotationAxisZ", 
//				"beamVectorX", "beamVectorY", "beamVectorZ"};
//
//		param11.setColumnNames(columns11);
//
//		ConstraintTriplet[] triplet11 = new ConstraintTriplet[1];
//		triplet11[0] = new ConstraintTriplet();
//		triplet11[0].setConstraintColumn("datacollectionid") ; 
//		triplet11[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet11[0].setConstraintValue("357494");
//
//		param11.setConstraints(triplet11);
//
//		param11.setRangeend(0);
//		param11.setRangeend(10);
//
//		buildSQLInput11.setBuildSQL(param11);
//
//		BuildSQLResponse res11 = client.buildSQL(buildSQLInput11);
//		displayBuildQueryResult(res11);
//
//		//////////////
//		
//		System.out.println("\n***********************");
//		System.out.println("\n***   BuildSQL (12) ***");
//		System.out.println("\n***********************");
//
//		BuildSQL buildSQLInput12 = new BuildSQL();
//
//		QueryParameters param12 = new QueryParameters();
//		param12.setCredentials(cred);
//		param12.setDbname(AllowedDatabase.ispyb);
//		param12.setTablename(AllowedTable.autoprocprogram);
//		String[] columns12 = {"autoProcProgramId", "processingCommandLine", "processingPrograms", "processingStatus"};
//
//		param12.setColumnNames(columns12);
//
//		ConstraintTriplet[] triplet12 = new ConstraintTriplet[1];
//		triplet12[0] = new ConstraintTriplet();
//		triplet12[0].setConstraintColumn("autoprocprogramid") ; 
//		triplet12[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet12[0].setConstraintValue("13214571");
//
//		param12.setConstraints(triplet12);
//
//		param12.setRangeend(0);
//		param12.setRangeend(10);
//
//		buildSQLInput12.setBuildSQL(param12);
//
//		BuildSQLResponse res12 = client.buildSQL(buildSQLInput12);
//		displayBuildQueryResult(res12);
//		
//		//////////////
//
//		System.out.println("\n***********************");
//		System.out.println("\n***   BuildSQL (13) ***");
//		System.out.println("\n***********************");
//
//		BuildSQL buildSQLInput13 = new BuildSQL();
//
//		QueryParameters param13 = new QueryParameters();
//		param13.setCredentials(cred);
//		param13.setDbname(AllowedDatabase.ispyb);
//		param13.setTablename(AllowedTable.autoprocprogramattachment);
//		String[] columns13 = {"autoProcProgramAttachmentId", "autoProcProgramId", "fileType", "fileName", "filePath"};
//
//		param13.setColumnNames(columns13);
//
//		ConstraintTriplet[] triplet13 = new ConstraintTriplet[1];
//		triplet13[0] = new ConstraintTriplet();
//		triplet13[0].setConstraintColumn("autoprocprogramid") ; 
//		triplet13[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet13[0].setConstraintValue("13214571");
//
//		param13.setConstraints(triplet13);
//
//		param13.setRangeend(0);
//		param13.setRangeend(10);
//
//		buildSQLInput13.setBuildSQL(param13);
//
//		BuildSQLResponse res13 = client.buildSQL(buildSQLInput13);
//		displayBuildQueryResult(res13);
//		
//		//////////////
//
//		System.out.println("\n***********************");
//		System.out.println("\n***   BuildSQL (14) ***");
//		System.out.println("\n***********************");
//
//		BuildSQL buildSQLInput14 = new BuildSQL();
//
//		QueryParameters param14 = new QueryParameters();
//		param14.setCredentials(cred);
//		param14.setDbname(AllowedDatabase.ispyb);
//		param14.setTablename(AllowedTable.autoprocscaling_has_int);
//		String[] columns14 = {"autoProcScaling_has_intId", "autoProcScalingId", "autoProcIntegrationId", "recordTimeStamp"}; 
//
//		param14.setColumnNames(columns14);
//
//		ConstraintTriplet[] triplet14 = new ConstraintTriplet[1];
//		triplet14[0] = new ConstraintTriplet();
//		triplet14[0].setConstraintColumn("autoprocintegrationid") ; 
//		triplet14[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet14[0].setConstraintValue("149666");
//
//		param14.setConstraints(triplet14);
//
//		param14.setRangeend(0);
//		param14.setRangeend(10);
//
//		buildSQLInput14.setBuildSQL(param14);
//
//		BuildSQLResponse res14 = client.buildSQL(buildSQLInput14);
//		displayBuildQueryResult(res14);
//		
//		//////////////
//
//		System.out.println("\n***********************");
//		System.out.println("\n***   BuildSQL (15) ***");
//		System.out.println("\n***********************");
//
//		BuildSQL buildSQLInput15 = new BuildSQL();
//
//		QueryParameters param15 = new QueryParameters();
//		param15.setCredentials(cred);
//		param15.setDbname(AllowedDatabase.ispyb);
//		param15.setTablename(AllowedTable.autoprocscaling);
//		String[] columns15 = {"autoProcScalingId", "autoProcId", "recordTimeStamp"}; 
//
//		param15.setColumnNames(columns15);
//
//		ConstraintTriplet[] triplet15 = new ConstraintTriplet[1];
//		triplet15[0] = new ConstraintTriplet();
//		triplet15[0].setConstraintColumn("autoprocscalingid") ; 
//		triplet15[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet15[0].setConstraintValue("151859");
//
//		param15.setConstraints(triplet15);
//
//		param15.setRangeend(0);
//		param15.setRangeend(10);
//
//		buildSQLInput15.setBuildSQL(param15);
//
//		BuildSQLResponse res15 = client.buildSQL(buildSQLInput15);
//		displayBuildQueryResult(res15);
//		
//		//////////////
//
//		System.out.println("\n***********************");
//		System.out.println("\n***   BuildSQL (16) ***");
//		System.out.println("\n***********************");
//
//		BuildSQL buildSQLInput16 = new BuildSQL();
//
//		QueryParameters param16 = new QueryParameters();
//		param16.setCredentials(cred);
//		param16.setDbname(AllowedDatabase.ispyb);
//		param16.setTablename(AllowedTable.autoproc);
//		String[] columns16 =   {"autoProcId", "autoProcProgramId", "spaceGroup", "refinedCell_A", "refinedCell_B", "refinedCell_C", 
//				"refinedCell_alpha", "refinedCell_beta", "refinedCell_gamma", "recordTimeStamp"};
//
//		param16.setColumnNames(columns16);
//
//		ConstraintTriplet[] triplet16 = new ConstraintTriplet[1];
//		triplet16[0] = new ConstraintTriplet();
//		triplet16[0].setConstraintColumn("autoprocid") ; 
//		triplet16[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet16[0].setConstraintValue("151859");
//
//		param16.setConstraints(triplet16);
//
//		param16.setRangeend(0);
//		param16.setRangeend(10);
//
//		buildSQLInput16.setBuildSQL(param16);
//
//		BuildSQLResponse res16 = client.buildSQL(buildSQLInput16);
//		displayBuildQueryResult(res16);
//		
//		//////////////
//
//		System.out.println("\n***********************");
//		System.out.println("\n***   BuildSQL (17) ***");
//		System.out.println("\n***********************");
//
//		BuildSQL buildSQLInput17 = new BuildSQL();
//
//		QueryParameters param17 = new QueryParameters();
//		param17.setCredentials(cred);
//		param17.setDbname(AllowedDatabase.ispyb);
//		param17.setTablename(AllowedTable.autoprocscalingstatistics);
//		String[] columns17 = {"autoProcScalingStatisticsId", "autoProcScalingId", "scalingStatisticsType", "comments", 
//				"resolutionLimitLow", "resolutionLimitHigh", "rmerge", "nTotalObservations", "nTotalUniqueObservations", 
//				"meanIOverSigI", "completeness", "multiplicity", "recordTimeStamp"}; 
//
//		param17.setColumnNames(columns17);
//
//		ConstraintTriplet[] triplet17 = new ConstraintTriplet[1];
//		triplet17[0] = new ConstraintTriplet();
//		triplet17[0].setConstraintColumn("autoprocscalingid") ; 
//		triplet17[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet17[0].setConstraintValue("151859");
//
//		param17.setConstraints(triplet17);
//
//		param17.setRangeend(0);
//		param17.setRangeend(10);
//
//		buildSQLInput17.setBuildSQL(param17);
//
//		BuildSQLResponse res17 = client.buildSQL(buildSQLInput17);
//		displayBuildQueryResult(res17);
	
	
//		System.out.println("\n*************************************");
//		System.out.println("\n***    BuildSQl to get image      ***");
//		System.out.println("\n*************************************");
//		
//		BuildSQL buildSQLInput5 = new BuildSQL();
//
//		QueryParameters param5 = new QueryParameters();
//		param5.setCredentials(cred);
//		param5.setDbname(AllowedDatabase.ispyb);
//		param5.setTablename(AllowedTable.image);
//		String[] columns5 = {"fileName", "fileLocation"};
//
//		param5.setColumnNames(columns5);
//
//		ConstraintTriplet[] triplet5 = new ConstraintTriplet[1];
//		triplet5[0] = new ConstraintTriplet();
//		triplet5[0].setConstraintColumn("imageid") ; 
//		triplet5[0].setConstraintOperator(AllowedOperator.value1) ; 
//		triplet5[0].setConstraintValue("1386986");
//
//		param5.setConstraints(triplet5);
//
//		param5.setRangeend(100);
//		param5.setRangeend(200);
//
//		buildSQLInput5.setBuildSQL(param5);
//
//		BuildSQLResponse res5 = client.buildSQL(buildSQLInput5);
//		displayBuildQueryResult(res5);

		
	
//		
//			
//		//////////////
//		
//		System.out.println("\n*******************************");
//		System.out.println("\n***  DownloadFilebyLocation ***");
//		System.out.println("\n*******************************");
//		String downloadDirectory = "c:\\dawn";
//		
//		DownloadFilebyLocation getDownloadFileInput = new DownloadFilebyLocation();
//		DownloadFileParameters downlaodParam = new DownloadFileParameters();
//		downlaodParam.setCredentials(cred);
//		downlaodParam.setFilepath("/dls/i03/data/2012/nt6397-88/jpegs/questionmark.png");
//		getDownloadFileInput.setDownloadFilebyLocation(downlaodParam );
//		DownloadFilebyLocationResponse downlodFileResponse = client.downloadFilebyLocation(getDownloadFileInput);
//		
//		System.out.println("\nresponse: " + downlodFileResponse.getMessage());
//				
//		if(downlodFileResponse.getFile() != null){
//			System.out.println("\nfilename: " + downlodFileResponse.getFileName());
//		try {
//			File receivedFile = new File(combine(downloadDirectory, downlodFileResponse.getFileName()));
//			FileOutputStream outputStream = new FileOutputStream(receivedFile);
//			downlodFileResponse.getFile().writeTo(outputStream);
//			outputStream.flush();
//			System.out.println("\nfile downloaded to: " +receivedFile.getAbsolutePath());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}//endif
		
		
//		System.out.println("\n****************************");
//		System.out.println("\n***    GetSnapshots V1.0 ***");
//		System.out.println("\n****************************");
//		String downloadDirSnapshots = "c:\\dawn";
//		
//		GetSnapshots getSnapshotsInput = new GetSnapshots();
//		GetSnapshotsParameters snapshotsParam = new GetSnapshotsParameters();
//		snapshotsParam.setToken(token);
//		snapshotsParam.setFedid(cred.getFedid());
//		snapshotsParam.setDatacollectionid("59904990");
//		snapshotsParam.setImageresolution(AllowedResolution.low);
//
//		getSnapshotsInput.setGetSnapshots(snapshotsParam);
//		GetSnapshotsResponse snapshotsResponse = client.getSnapshots(getSnapshotsInput);
//		
//		System.out.println("\nresponse: " + snapshotsResponse.getMessage());
//				
//		if(snapshotsResponse.getFileName1() != null){
//			System.out.println("\nfilename1: " + snapshotsResponse.getFileName1());
//		try {
//			File receivedFile = new File(combine(downloadDirSnapshots, snapshotsResponse.getFileName1()));
//			FileOutputStream outputStream = new FileOutputStream(receivedFile);
//			snapshotsResponse.getSnapshot1().writeTo(outputStream);
//			outputStream.flush();
//			System.out.println("\nfile1 downloaded to: " +receivedFile.getAbsolutePath());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}//endif
//		
//		if(snapshotsResponse.getFileName2() != null){
//			System.out.println("\nfilename2: " + snapshotsResponse.getFileName2());
//		try {
//			File receivedFile = new File(combine(downloadDirSnapshots, snapshotsResponse.getFileName2()));
//			FileOutputStream outputStream = new FileOutputStream(receivedFile);
//			snapshotsResponse.getSnapshot2().writeTo(outputStream);
//			outputStream.flush();
//			System.out.println("\nfile2 downloaded to: " +receivedFile.getAbsolutePath());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}//endif
//		
//		if(snapshotsResponse.getFileName3() != null){
//			System.out.println("\nfilename3: " + snapshotsResponse.getFileName3());
//		try {
//			File receivedFile = new File(combine(downloadDirSnapshots, snapshotsResponse.getFileName3()));
//			FileOutputStream outputStream = new FileOutputStream(receivedFile);
//			snapshotsResponse.getSnapshot3().writeTo(outputStream);
//			outputStream.flush();
//			System.out.println("\nfile3 downloaded to: " +receivedFile.getAbsolutePath());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}//endif
//		
//		if(snapshotsResponse.getFileName4() != null){
//			System.out.println("\nfilename4: " + snapshotsResponse.getFileName4());
//		try {
//			File receivedFile = new File(combine(downloadDirSnapshots, snapshotsResponse.getFileName4()));
//			FileOutputStream outputStream = new FileOutputStream(receivedFile);
//			snapshotsResponse.getSnapshot4().writeTo(outputStream);
//			outputStream.flush();
//			System.out.println("\nfile4 downloaded to: " +receivedFile.getAbsolutePath());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}//endif
//
	
		System.out.println("\n********************");
		System.out.println("\n***  Logout V1.0 ***");
		System.out.println("\n********************");
		
		Logout logoutInput = new Logout();
		LogoutParameters params = new LogoutParameters();
		params.setFedid(fedid);
		params.setToken(token);
		logoutInput.setLogout(params);

		LogoutResponse logoutResponse = client.logout(logoutInput);
		
        System.out.println("response message" +  logoutResponse.getMessage());
	
        System.out.println("\n========================");
		System.out.println("\n===  TESTS FINISHED! ===");
		System.out.println("\n========================");
	

		*/

	} //end main

	
	// display SOAP query object
//	private static void displaySOAPQueryObject(EchoString echoInput) {
//		
//		try {
//								
//			System.out.println(echoInput.getOMElement(echoInput.MY_QNAME, OMAbstractFactory.getSOAP12Factory()).toStringWithConsume());
//			
//		} catch (ADBException e1) {
//			e1.printStackTrace();
//		} catch (XMLStreamException e) {
//			e.printStackTrace();
//		}
//	}
	
	// display REST query object
		private static void displayRESTResponseObject(BuildSQLResponse input) {
			
			try {
									
				System.out.println(input.getOMElement(input.MY_QNAME, OMAbstractFactory.getOMFactory()));
				
			} catch (ADBException e1) {
				e1.printStackTrace();
			}
		}

		private static void displayRESTResponseObject(GetVisitInfoResponse input) {
			
			try {
									
				System.out.println(input.getOMElement(input.MY_QNAME, OMAbstractFactory.getOMFactory()));
				
			} catch (ADBException e1) {
				e1.printStackTrace();
			}
		}

		
//	private static void displayBuildQueryResult(BuildSQLResponse res){
//		System.out.println("message: " + res.getMessage());
//		System.out.println("number of columns: " + res.getNumberColumns());
//		System.out.println("columns names: ");
//		System.out.println("--------------");
//
//		if(res.getColumnNames() != null){
//			for(int i=0; i< res.getColumnNames().length; i++){
//				System.out.println("getColumnNames["+ i+ "]: " + res.getColumnNames()[i]);
//			}
//			System.out.println("nb of rows: " + res.getNumberRows());
//			System.out.println("rows content: ");
//			for(int i=0; i< res.getNumberRows(); i++){
//				System.out.println("row["+ i+ "]: " + res.getRow()[i]);
//			}
//		}
//		System.out.println("--------------");
//
//	}
//	
//	private static void displayOperators(){
//		System.out.println("AllowedOperator.value1: " + AllowedOperator.value1);
//		System.out.println("AllowedOperator.value2: " + AllowedOperator.value2);
//		System.out.println("AllowedOperator.value3: " + AllowedOperator.value3);
//		System.out.println("AllowedOperator.value4: " + AllowedOperator.value4);
//	}
			
	
    public static String combine (String path1, String path2)
    {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }

}
