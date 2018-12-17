package uk.ac.diamond.ws.ejd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axis2.databinding.ADBException;

import uk.ac.diamond.genericws.client.GenericWSV10Stub.AllowedDatabase;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.AllowedOperator;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.AllowedOrder;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.AllowedTable;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.BuildSQL;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.BuildSQLResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.ConstraintTriplet;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.Credentials;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetAuthToken;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetAuthTokenResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetUsersForVisit;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetUsersForVisitParameters;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetUsersForVisitResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.OrderPair;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.QueryParameters;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetVisitInfoResponse;

public class VisitsAndProteinAcronyms {
	
	private static String fedid=null;
	private static String password=null;
	private static String authToken=null;
	private static RESTTestGenericClient client;
	
    static {
    	Properties properties=new Properties();
    	File file=new File("conf/Properties");
    	try {
			InputStream in = new FileInputStream(file);
			properties.load(in);
			VisitsAndProteinAcronyms.fedid=properties.getProperty("diamond.fedid");
			VisitsAndProteinAcronyms.password=properties.getProperty("diamond.password");
			VisitsAndProteinAcronyms.client = new RESTTestGenericClient();
		} catch (IOException e) {
			if (e instanceof FileNotFoundException) {
                System.err.println("Could not find connection details at: " + file.getAbsolutePath());
            }
            throw new RuntimeException(e);
	    }
	}
	
	public static void main(String args[]){
		
		String trustStore = System.getProperty("javax.net.ssl.trustStore");
        if (trustStore == null) {
            System.out.println("javax.net.ssl.trustStore is not defined");
        } else {
            System.out.println("javax.net.ssl.trustStore = " + trustStore);
        }
        
		String proposalResponse=VisitsAndProteinAcronyms.getProposals(0, 1000);
		System.out.println(proposalResponse);

		String proposalResponse2=VisitsAndProteinAcronyms.getProposalById("26166");
		System.out.println(proposalResponse2);

        String visitResponse=VisitsAndProteinAcronyms.getVisits(0, 1000);
        System.out.println(visitResponse);
		
        String acronymResponse=VisitsAndProteinAcronyms.getProteinAcronymsForProposal("26166", 0, 1000);
        System.out.println(acronymResponse);
        
        String usersForVisitResponse=VisitsAndProteinAcronyms.getUsersForVisit("mx4025-1");
        System.out.println(usersForVisitResponse);

	} //end main

	public static String getAuthToken(){
		
		if(VisitsAndProteinAcronyms.authToken!=null){
			return VisitsAndProteinAcronyms.authToken;
		}
		
		// set credentials object to be used by all queries
		uk.ac.diamond.genericws.client.GenericWSV10Stub.Credentials cred = new uk.ac.diamond.genericws.client.GenericWSV10Stub.Credentials();
		cred.setFedid(VisitsAndProteinAcronyms.fedid);
		cred.setPassword(VisitsAndProteinAcronyms.password);
			
		//System.out.println(VisitsAndProteinAcronyms.fedid+":"+VisitsAndProteinAcronyms.password);
		System.out.println("\n**************************");
		System.out.println("\n***  GetAuthToken V1.0 ***");
		System.out.println("\n**************************");
		
		GetAuthToken getAuthTokenInput = new GetAuthToken();
		getAuthTokenInput.setGetAuthToken(cred);
		
		GetAuthTokenResponse response = VisitsAndProteinAcronyms.client.GetAuthToken(getAuthTokenInput);
        System.out.println("*** response="+response);
        //TODO Response can be null
        //org.apache.axis2.AxisFault: ispyb.diamond.ac.uk
        //Caused by: java.net.UnknownHostException: ispyb.diamond.ac.uk
        VisitsAndProteinAcronyms.authToken= response.getToken();
        return VisitsAndProteinAcronyms.authToken;
	}

	public static String getProposalById(String proposalId){
		ConstraintTriplet[] triplet = getConstraintTripletInArray("proposalid", AllowedOperator.value1, proposalId);
		return getProposalXml(0, 1, triplet);
	}
	public static String getProposals(int startRow, int numRows){
		ConstraintTriplet[] triplet = getConstraintTripletInArray("proposalid", AllowedOperator.value2, "-1");
		return getProposalXml(startRow, numRows, triplet);
	}
	private static String getProposalXml(int startRow, int numRows, ConstraintTriplet[] triplet){
		System.out.println("\n*************************");
		System.out.println("\n***  GetProposal V1.0 ***");
		System.out.println("\n***  JAR by ejd53     ***");
		System.out.println("\n*************************");
		
		String authToken=VisitsAndProteinAcronyms.getAuthToken();

		BuildSQL buildSQLInput2 = new BuildSQL();     
        
        QueryParameters param2 = new QueryParameters(); 
        
        param2.setEverything(true); //staff flag, show everything not just visits, etc., that I'm on
        
        param2.setDbname(AllowedDatabase.ispyb); 
        AllowedTable table2 = AllowedTable.proposal; 
        param2.setTablename(table2); 
        
        param2.setFedid(VisitsAndProteinAcronyms.fedid); 
        param2.setToken(authToken); 
        String[] columns2 = {"proposalid", "proposalcode", "proposalnumber"};  // {"proposalid", "title", "proposalcode", "proposalnumber"};  
 
        param2.setColumnNames(columns2); 

        param2.setConstraints(triplet); 
 
        param2.setRangestart(startRow); 
        param2.setRangeend((startRow+numRows)-1); 

        OrderPair orderpair2 = new OrderPair(); 
        orderpair2.setOrderColumn("proposalid"); 
        orderpair2.setOrderType(AllowedOrder.desc); 
        OrderPair[] pairs2 = new OrderPair[1]; 
        pairs2[0] = orderpair2; 
        param2.setOrderpair(pairs2); 

        buildSQLInput2.setBuildSQL(param2); 
        BuildSQLResponse res2 = VisitsAndProteinAcronyms.client.buildSQL(buildSQLInput2);
                
        return responseToString(res2);
	}
	
	public static String getVisitById(String visitId){
		ConstraintTriplet[] triplet = getConstraintTripletInArray("sessionid", AllowedOperator.value1, visitId);
		return getVisitXml(0, 1, triplet);
	}
	public static String getVisits(int startRow, int numRows){
		ConstraintTriplet[] triplet = getConstraintTripletInArray("sessionid", AllowedOperator.value2, "-1");
		return getVisitXml(startRow, numRows, triplet);
	}
	public static String getVisitsByProposalId(String proposalId, int startRow, int numRows){
		ConstraintTriplet[] triplet = getConstraintTripletInArray("proposalid", AllowedOperator.value1, proposalId);
		return getVisitXml(startRow, numRows, triplet);
	}
	private static String getVisitXml(int startRow, int numRows, ConstraintTriplet[] triplet){
	    System.out.println("\n***********************");
		System.out.println("\n***  GetVisits V1.0 ***");
		System.out.println("\n***********************");
		
		String authToken=VisitsAndProteinAcronyms.getAuthToken();

		BuildSQL buildSQLInput1 = new BuildSQL();	
		
		QueryParameters param1 = new QueryParameters();

        param1.setEverything(true); //staff flag, show everything not just visits, etc., that I'm on

		param1.setDbname(AllowedDatabase.ispyb);
		AllowedTable table = AllowedTable.blsession;
		param1.setTablename(table);
		param1.setFedid(VisitsAndProteinAcronyms.fedid);
		param1.setToken(authToken);
		String[] columns1 = {"sessionid", "proposalid", "visit_number"}; 
			// {"sessionid", "proposalid", "startdate", "beamlinename", "beamlineoperator", "projectcode", "visit_number"}; 
	
		param1.setColumnNames(columns1);

		param1.setConstraints(triplet);
		
		param1.setRangestart(startRow);
		param1.setRangeend(startRow+numRows);
	
		OrderPair orderpair = new OrderPair();
		orderpair.setOrderColumn("startdate");
		orderpair.setOrderType(AllowedOrder.desc);
		OrderPair[] pairs = new OrderPair[1];
		pairs[0] = orderpair;
		param1.setOrderpair(pairs);
	
		buildSQLInput1.setBuildSQL(param1);
	
		BuildSQLResponse res1 = client.buildSQL(buildSQLInput1);		
		return responseToString(res1);
	
	}

	public static String getProteinAcronymsForProposal(String proposalId, int startRow, int numRows){
		 System.out.println("\n*************************");
         System.out.println("\n***  GetProteins V1.0 ***");
         System.out.println("\n*************************");
        
         String authToken=VisitsAndProteinAcronyms.getAuthToken();

         BuildSQL buildSQLInput1a = new BuildSQL();     
        
         QueryParameters param1a = new QueryParameters();
         
         param1a.setEverything(true); //staff flag, show everything not just visits, etc., that I'm on

         param1a.setDbname(AllowedDatabase.ispyb);
         AllowedTable table1a = AllowedTable.protein;
         param1a.setTablename(table1a);
         param1a.setFedid(VisitsAndProteinAcronyms.fedid);
         param1a.setToken(authToken);
         param1a.setEverything(false); // get all

         String[] columns1a = {"proteinid", "acronym", "proposalid"};
                         //{"proteinid", "name", "acronym", "molecularmass", "proteintype", "personid", "bltimestamp", "iscreatedbysamplesheet", "sequence"};
         param1a.setColumnNames(columns1a);

         ConstraintTriplet[] triplet1a = new ConstraintTriplet[1];
         triplet1a[0] = new ConstraintTriplet();
         triplet1a[0].setConstraintColumn("proposalid") ;
         AllowedOperator op1a = AllowedOperator.value1;
         triplet1a[0].setConstraintOperator(op1a);
         triplet1a[0].setConstraintValue(proposalId);
         param1a.setConstraints(triplet1a);
        
         param1a.setRangestart(startRow);
         param1a.setRangeend(startRow+numRows);

         OrderPair orderpair1a = new OrderPair();
         orderpair1a.setOrderColumn("proteinid");
         orderpair1a.setOrderType(AllowedOrder.desc);
         OrderPair[] pairs1a = new OrderPair[1];
         pairs1a[0] = orderpair1a;
         param1a.setOrderpair(pairs1a);

         buildSQLInput1a.setBuildSQL(param1a);

         BuildSQLResponse res1a = client.buildSQL(buildSQLInput1a);
         String response=responseToString(res1a);
         return response;
	}

	public static String getUsersForVisit(String visitName){
		String authToken=VisitsAndProteinAcronyms.getAuthToken();
		GetUsersForVisit userGetter=new GetUsersForVisit();
		GetUsersForVisitParameters params=new GetUsersForVisitParameters();
		params.setFedid(VisitsAndProteinAcronyms.fedid);
		params.setToken(authToken);
		params.setVisit(visitName);
		userGetter.setGetUsersForVisit(params);
		GetUsersForVisitResponse resp=client.getUsersForVisit(userGetter);
		String ret="";
		try {
			ret=(resp.getOMElement(BuildSQLResponse.MY_QNAME, OMAbstractFactory.getOMFactory())).toString();
		} catch (ADBException e1) {
			e1.printStackTrace();
		}
		return ret;
	}
	/**
	 * Get a ConstraintTriplet[] describing a specified query constraint, eg "proposalid > -1".
	 * 
	 * @param columnName The column name on which to constrain
	 * @param operator One of AllowedOperator.value1 (=), .value2 (>), .value3 (<) or .value4 (<>).
	 * @param value The value in the column on which to constrain.
	 * @return A ConstraintTriplet[] with one member, a ConstraintTriplet representing the specified constraint.
	 */
	private static ConstraintTriplet[] getConstraintTripletInArray(String columnName, AllowedOperator operator, String value){
		ConstraintTriplet[] triplet = new ConstraintTriplet[1];
		triplet[0] = new ConstraintTriplet();
		triplet[0].setConstraintColumn(columnName); 
		triplet[0].setConstraintOperator(operator); 
		triplet[0].setConstraintValue(value);
		return triplet;
	}
	
	private static String responseToString(BuildSQLResponse input){
		String ret="";
		try {
			ret=(input.getOMElement(BuildSQLResponse.MY_QNAME, OMAbstractFactory.getOMFactory())).toString();
		} catch (ADBException e1) {
			e1.printStackTrace();
		}
		return ret;
	}
	
	// display REST query object
	private static void displayRESTResponseObject(BuildSQLResponse input) {
		
		try {
								
			System.out.println(input.getOMElement(BuildSQLResponse.MY_QNAME, OMAbstractFactory.getOMFactory()));
			
		} catch (ADBException e1) {
			e1.printStackTrace();
		}
	}



    public static String combine (String path1, String path2)
    {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }

}
