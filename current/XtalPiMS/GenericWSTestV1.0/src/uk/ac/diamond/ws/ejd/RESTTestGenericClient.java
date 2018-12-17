package uk.ac.diamond.ws.ejd;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;

import uk.ac.diamond.genericws.client.BadDataExceptionException;
import uk.ac.diamond.genericws.client.GenericWSV10Stub;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.BuildSQL;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.BuildSQLResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.DownloadFilebyLocation;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.DownloadFilebyLocationResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.EchoStringResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetAuthTokenResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetFile;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetFileResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetSnapshots;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetSnapshotsResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetUsersForVisit;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetUsersForVisitResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetVersionResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetVisitInfo;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.GetVisitInfoResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.LogoutResponse;
import uk.ac.diamond.genericws.client.GenericWSV10Stub.UpdateCommentsResponse;


public class RESTTestGenericClient {

	GenericWSV10Stub stub;

	static final String ENDPOINT = "https://ispyb.diamond.ac.uk/ws/GenericWSV1.0.GenericWSV1.0HttpsEndpoint/";
	//static final String ENDPOINT = "https://sci-serv5.diamond.ac.uk:8443/axis2/services/GenericWSV1.0.GenericWSV1.0HttpsEndpoint/";
	//static final String ENDPOINT = "http://sci-serv5.diamond.ac.uk:8056/axis2/services/GenericWSV1.0.GenericWSV1.0HttpsEndpoint/";
	
	
	RESTTestGenericClient() {
	
		try {
        			
			this.stub = new GenericWSV10Stub(ENDPOINT);
	        ServiceClient serviceClient = stub._getServiceClient();
	        	        
	        Options options = new Options();
	        EndpointReference to = new EndpointReference();
	        to.setAddress(ENDPOINT);
	        
	        options.setManageSession(true);
	        // to stop ' 411 Error: Length Required'
	        options.setProperty(HTTPConstants.CHUNKED, false);
	        options.setProperty(Constants.Configuration.ENABLE_REST, Constants.VALUE_TRUE);
	        
	        options.setTo(to);
	                            
            serviceClient.setOptions(options);
            
            /*
             * ssl support
             */
	        String certificatePassword = "changeit";     
	        
	        /*
             * GlobalSign certificates
             * 
             * */
	        System.out.println("using Globalsign certificates...");
	        // TODO get the same file from the relative 'certs' directory
	        //System.setProperty("javax.net.ssl.trustStore", "/home/smw81327/synchlink/mytruststore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", certificatePassword);

	        //System.setProperty("javax.net.debug", "ssl");
            
        } catch (AxisFault e) {
            throw new RuntimeException(e);
        }
			
		System.out.println("GenericWSStub created");	
		
	}

	
	public GetVersionResponse getVersion(uk.ac.diamond.genericws.client.GenericWSV10Stub.GetVersion versionInput){
		
		   GenericWSV10Stub.GetVersionResponse getVersionResponse = null;
			
				System.out.println("sending GetVersion request..." + versionInput.getGetVersion());
				try {
					
					getVersionResponse = this.stub.getVersion(versionInput);
					System.out.println("\nGetVersion response: " + getVersionResponse.getGetVersionResponse());

				} catch (RemoteException e) {
					System.out.println("problem running GetVersion: " + e);
					e.printStackTrace();
				} catch (BadDataExceptionException e) {
					System.out.println("problem running GetVersion: " + e);
					e.printStackTrace();

				} 
				
			return getVersionResponse;
		}
	
   public EchoStringResponse echoString(GenericWSV10Stub.EchoString echoStringInput){
		
	   EchoStringResponse echoStringResponse = null;
		
			System.out.println("sending EchoString request..." + echoStringInput.getEchoString());
			try {
				
				echoStringResponse = this.stub.echoString(echoStringInput);
				System.out.println("\nEchoString response: " + echoStringResponse.getEchoStringResponse());

			} catch (RemoteException e) {
				System.out.println("problem running EchoString: " + e);
			} catch (BadDataExceptionException e) {
				System.out.println("problem running EchoString: " + e);
			} 
			
		return echoStringResponse;
	}
   
   public LogoutResponse logout(GenericWSV10Stub.Logout logoutInput){
		
	      LogoutResponse logoutResponse = null;
		
			System.out.println("sending Logout request..." + logoutInput.getLogout().getFedid() + " - " + logoutInput.getLogout().getToken());
			try {
				
				logoutResponse = this.stub.logout(logoutInput);
				System.out.println("\nLogout response: " + logoutResponse.getMessage());

			} catch (RemoteException e) {
				System.out.println("problem running Logout: " + e);
			} catch (BadDataExceptionException e) {
				System.out.println("problem running Logout: " + e);
			} 
			
		return logoutResponse;
	}
	
   public GetAuthTokenResponse GetAuthToken(GenericWSV10Stub.GetAuthToken authTokenInput){
		
	   GetAuthTokenResponse getAuthTokenResponse = null;
		
			System.out.println("sending GetAuthToken request..." + authTokenInput.getGetAuthToken());
			try {
				
				getAuthTokenResponse = this.stub.getAuthToken(authTokenInput);
				System.out.println("\nGetAuthToken response - Token= " + getAuthTokenResponse.getToken());

			} catch (RemoteException e) {
				System.out.println("problem running GetAuthToken: " + e);
				e.printStackTrace();
			} catch (BadDataExceptionException e) {
				System.out.println("problem running GetAuthToken: " + e);
				e.printStackTrace();
			} 
			
		return getAuthTokenResponse;
	}
   
   
	public BuildSQLResponse buildSQL(BuildSQL buildSQLInput){
		
		BuildSQLResponse buildSQLResponse = null;
		
			System.out.println("sending BuildSQL request..." + buildSQLInput.getBuildSQL().getDbname());
			try {
				
				buildSQLResponse = this.stub.buildSQL(buildSQLInput);
				System.out.println("\ndone");

			} catch (RemoteException e) {
				System.out.println("problem running BuildSQL: " + e);
				e.printStackTrace();
			} catch (BadDataExceptionException e) {
				System.out.println("problem running BuildSQL: " + e);
				e.printStackTrace();
			} 
			
		return buildSQLResponse;
	}
	
	public GetVisitInfoResponse getVisitInfo(GetVisitInfo getVisitInfoInput){
		
		GetVisitInfoResponse getVisitInfoResponse = null;
		
			System.out.println("sending GetVisitInfo request..." + getVisitInfoInput.getGetVisitInfo().toString());
			try {
				
				getVisitInfoResponse = this.stub.getVisitInfo(getVisitInfoInput);
				System.out.println("\ndone");

			} catch (RemoteException e) {
				System.out.println("problem running getVisitInfo: " + e);
				e.printStackTrace();
			} catch (BadDataExceptionException e) {
				System.out.println("problem running getVisitInfo: " + e);
				e.printStackTrace();
			} 
			
		return getVisitInfoResponse;
	}

	public GetUsersForVisitResponse getUsersForVisit(GetUsersForVisit getUsersForVisitInput){
        
        GetUsersForVisitResponse getUsersForVisitResponse = null;
       
                System.out.println("sending GetUsersForVisit request..." + getUsersForVisitInput.getGetUsersForVisit().toString());
                try {
                       
                        getUsersForVisitResponse = this.stub.getUsersForVisit(getUsersForVisitInput);
                        System.out.println("\ndone");

                } catch (RemoteException e) {
                        System.out.println("Remote problem running getUsersForVisit: " + e);
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                } catch (BadDataExceptionException e) {
                        System.out.println("Bad Data problem running getUsersForVisit: " + e);
                        e.printStackTrace();
                }
               
        return getUsersForVisitResponse;
	}
	
	public GetFileResponse getFile(GetFile getFileInput){
		
		GetFileResponse getFileResponse = null;
		
			System.out.println("sending getFile request...");
			try {
				
				getFileResponse = this.stub.getFile(getFileInput);

			} catch (RemoteException e) {
				System.out.println("problem running getFile: " + e);
			} catch (BadDataExceptionException e) {
				System.out.println("problem running getFile: " + e);
			} 
			
		return getFileResponse;
	}
	
	public GetSnapshotsResponse getSnapshots(GetSnapshots getSnapshotsInput){
		
		GetSnapshotsResponse getSnapshotsResponse = null;
		
			System.out.println("sending GetSnapshots request...");
			try {
				
				getSnapshotsResponse = this.stub.getSnapshots(getSnapshotsInput);

			} catch (RemoteException e) {
				System.out.println("problem running GetSnapshots: " + e);
			} catch (BadDataExceptionException e) {
				System.out.println("problem running GetSnapshots: " + e);
			} 
			
		return getSnapshotsResponse;
	}
	
	public UpdateCommentsResponse UpdateComments(GenericWSV10Stub.UpdateComments UpdateCommentsInput){
		
		UpdateCommentsResponse updateCommentsResponse = null;
		
			System.out.println("sending UpdateComments request...");
			try {
				
				updateCommentsResponse = this.stub.updateComments(UpdateCommentsInput);

			} catch (RemoteException e) {
				System.out.println("problem running UpdateComments: " + e);
			} catch (BadDataExceptionException e) {
				System.out.println("problem running UpdateComments: " + e);
			} 
			
		return updateCommentsResponse;
	}

	public DownloadFilebyLocationResponse downloadFilebyLocation(DownloadFilebyLocation getFileInput){
		
	DownloadFilebyLocationResponse downloadFilebyLocationResponse = null;
		
			System.out.println("sending downloadFilebyLocation request...");
			try {
				
				downloadFilebyLocationResponse = this.stub.downloadFilebyLocation(getFileInput);

			} catch (RemoteException e) {
				System.out.println("problem running downloadFilebyLocation: " + e);
			} catch (BadDataExceptionException e) {
				System.out.println("problem running downloadFilebyLocation: " + e);
			} 
			
		return downloadFilebyLocationResponse;
	}
	
}
