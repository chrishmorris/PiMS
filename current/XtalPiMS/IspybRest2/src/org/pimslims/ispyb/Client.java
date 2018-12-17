package org.pimslims.ispyb;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;

import uk.ac.diamond.ispyb.client.BadDataExceptionException;
import uk.ac.diamond.ispyb.client.IspybServiceStub;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Authenticate;
import uk.ac.diamond.ispyb.client.IspybServiceStub.AuthenticateResponse;
import uk.ac.diamond.ispyb.client.IspybServiceStub.BeamlineExportedInformation;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Credentials;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalDetails;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DiffractionPlan;
import uk.ac.diamond.ispyb.client.IspybServiceStub.GetDeliveryStatus;
import uk.ac.diamond.ispyb.client.IspybServiceStub.GetDeliveryStatusResponse;
import uk.ac.diamond.ispyb.client.IspybServiceStub.GetResults;
import uk.ac.diamond.ispyb.client.IspybServiceStub.GetReturnShipment;
import uk.ac.diamond.ispyb.client.IspybServiceStub.ShipmentInfo;
import uk.ac.diamond.ispyb.client.IspybServiceStub.SubmitCrystalDetails;
import uk.ac.diamond.ispyb.client.IspybServiceStub.SubmitCrystalDetailsResponse;
import uk.ac.diamond.ispyb.client.IspybServiceStub.SubmitDiffractionPlan;
import uk.ac.diamond.ispyb.client.IspybServiceStub.SubmitDiffractionPlanResponse;
import uk.ac.diamond.ispyb.client.LoginExceptionException;

public class Client {

    /**
     * ENDPOINT String
     */
    public static final String ENDPOINT =
        "https://ispyb.diamond.ac.uk:443/ws/IspybService.IspybServiceHttpEndpoint/";

    private final IspybServiceStub service;

    Logger logger = Logger.getLogger("org.pimslims.ispyb.Client");

    /**
     * @param certificatePassword TODO
     * @param certificateFilePath TODO
     * @param service
     * @throws AxisFault
     */
    public Client() {
        super();
        try {

            // How to support a self-signed certificate:
            // System.setProperty("javax.net.ssl.trustStore",
            // certificateFilePath);
            // System.setProperty("javax.net.ssl.trustStorePassword",
            // certificatePassword);
            //

            this.service = new IspybServiceStub();
            ServiceClient serviceClient = this.service._getServiceClient();
            Options options = new Options();
            EndpointReference to = new EndpointReference();
            to.setAddress(ENDPOINT);
            //to
            //.setAddress("http://sci-serv4.diamond.ac.uk:8080/axis2/services/IspybService.IspybServiceHttpEndpoint/");

            options.setManageSession(true);
            options.setTo(to);
            options.setProperty(Constants.Configuration.ENABLE_REST, Constants.VALUE_TRUE);
            // fix for: org.apache.axis2.AxisFault: Transport error: 411 Error:
            // Length Required
            options.setProperty(HTTPConstants.CHUNKED, false);

            /*
             * tomcat server authentication
             */
            HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
            auth.setUsername("ispybuser");
            auth.setPassword("password");
            // set if realm or domain is known
            options.setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE, auth);
            //            

            try {
                HttpTransportProperties.ProxyProperties pp = new HttpTransportProperties.ProxyProperties();
                Integer port = new Integer(System.getProperty("http.proxyPort"));
                pp.setProxyPort(port);
                pp.setProxyName(System.getProperty("http.proxyHost"));
                options.setProperty(HTTPConstants.PROXY, pp);
            } catch (NumberFormatException e) {
                // no proxy port
            }
            // could pp.setUserName("guest");
            // could pp.setPassWord("guest");

            serviceClient.setOptions(options);
            logger.log(Level.INFO, "new Client()");
        } catch (AxisFault e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param fedId
     * @param password
     * @param crystalShipping
     * @return
     * @throws RemoteException
     * @throws BadDataExceptionException
     * @throws LoginExceptionException
     * @see org.pimslims.ispyb.generated.IspybServiceStub#crystalShipping(org.pims_lims.www.services.ispyb.CrystalShippingE)
     */
    public boolean authenticate(String fedId, String password) throws BadDataExceptionException,
        LoginExceptionException {
        try {
            Credentials credentials = new Credentials();
            credentials.setFedid(fedId);
            credentials.setPassword(password);
            Authenticate auth = new Authenticate();
            auth.setAuthenticate(credentials);
            AuthenticateResponse response = service.authenticate(auth);
            String message = response.getMessage();
            logger.log(Level.INFO, "Client.authenticate: " + message);
            return !"Failed".equals(message);
        } catch (RemoteException e) {
            Throwable cause = e;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            throw new RuntimeException(cause);
        }
    }

    /**
     * Client.crystalDetails
     * 
     * @param details
     * @return
     * @throws BadDataExceptionException
     * @throws NotFoundException
     * @throws AxisFault
     */
    public String crystalDetails(CrystalDetails details) throws BadDataExceptionException, AxisFault,
        NotFoundException {
        try {
            SubmitCrystalDetails element =
                new uk.ac.diamond.ispyb.client.IspybServiceStub.SubmitCrystalDetails();
            element.setSubmitCrystalDetails(details);
            SubmitCrystalDetailsResponse response = service.submitCrystalDetails(element);
            return response.getSubmitCrystalDetailsResponse();
        } catch (RemoteException e) {
            processException(e);
            return null;
        }
    }

    /**
     * Client.diffractionPlan
     * 
     * @param plan
     * @throws BadDataExceptionException
     * @throws NotFoundException
     * @throws AxisFault
     */
    public String diffractionPlan(DiffractionPlan plan) throws BadDataExceptionException, AxisFault,
        NotFoundException {
        try {
            SubmitDiffractionPlan element =
                new uk.ac.diamond.ispyb.client.IspybServiceStub.SubmitDiffractionPlan();
            element.setSubmitDiffractionPlan(plan);
            SubmitDiffractionPlanResponse response = service.submitDiffractionPlan(element);
            return response.getSubmitDiffractionPlanResponse();
        } catch (RemoteException e) {
            processException(e);
            return null;
        }
    }

    /**
     * @param crystalShipping
     * @return
     * @throws RemoteException
     * @throws BadDataExceptionException
     * @throws AxisFault
     * @throws NotFoundException
     * @see org.pimslims.ispyb.generated.IspybServiceStub#crystalShipping(org.pims_lims.www.services.ispyb.CrystalShippingE)
     */
    public String crystalShipping(uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalShipping shipping)
        throws BadDataExceptionException, AxisFault, NotFoundException {
        try {
            uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalShippingE element =
                new uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalShippingE();
            element.setCrystalShipping(shipping);
            uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalShippingResponse response =
                service.crystalShipping(element);
            return response.getCrystalShippingResponse();
        } catch (RemoteException e) {
            processException(e);
            return null;
        }
    }

    static final Pattern SQL_ERROR = Pattern.compile("(problem with sql query .*?)\r?\n?");

    static final Pattern NOT_FOUND = Pattern.compile("no .*? found in ISPYB database!.*?\r?\n?");

    private void processException(RemoteException e) throws AxisFault, NotFoundException {
        if (e instanceof AxisFault) {
            String message = e.getMessage();
            if (NOT_FOUND.matcher(message).matches()) {
                throw new NotFoundException(e);
            }
            Matcher m = SQL_ERROR.matcher(message);
            if (m.matches()) {
                throw new RuntimeException("Sorry, there has been an error in ISPyB: " + m.group(1), e);
            }
            throw (AxisFault) e;
        }
        throw new RuntimeException(e);
    }

    /**
     * @param shipmentId
     * @return
     * @throws RemoteException
     * @throws BadDataExceptionException
     * @throws AxisFault
     * @throws NotFoundException
     * @see org.pimslims.ispyb.generated.IspybServiceStub#getDeliveryStatus(org.pims_lims.www.services.ispyb.GetDeliveryStatus)
     */
    public String getDeliveryStatus(ShipmentInfo info) throws BadDataExceptionException, AxisFault,
        NotFoundException {

        GetDeliveryStatus request = new GetDeliveryStatus();
        request.setGetDeliveryStatus(info);
        try {
            GetDeliveryStatusResponse response = service.getDeliveryStatus(request);
            //Confirmation status = response.getStatus();
            return response.getMessage();
        } catch (RemoteException e) {
            processException(e);
            return null;
        }
    }

    /**
     * @param getResults
     * @param shipmentInfo
     * @return
     * @throws RemoteException
     * @throws BadDataExceptionException
     * @throws AxisFault
     * @throws NotFoundException
     * @see org.pimslims.ispyb.generated.IspybServiceStub#getResults(org.pims_lims.www.services.ispyb.GetResults)
     */
    public BeamlineExportedInformation getResults(
        uk.ac.diamond.ispyb.client.IspybServiceStub.ShipmentInfo info) throws BadDataExceptionException,
        AxisFault, NotFoundException {
        GetResults request = new GetResults();
        request.setGetResults(info);
        try {
            return service.getResults(request).getBeamlineExportedInformation();
        } catch (RemoteException e) {
            processException(e);
            return null;
        }
    }

    public BeamlineExportedInformation getResults(String projectUUID, String shipmentName) throws AxisFault,
        BadDataExceptionException, NotFoundException {
        return this.getResults(IspybBeanFactory.makeShipmentInfo(projectUUID, shipmentName));
    }

    /**
     * @param getReturnShipment
     * @return
     * @throws RemoteException
     * @throws BadDataExceptionException
     * @throws AxisFault
     * @throws NotFoundException
     * @see org.pimslims.ispyb.generated.IspybServiceStub#getReturnShipment(org.pims_lims.www.services.ispyb.GetReturnShipment)
     */
    public String getReturnShipment(uk.ac.diamond.ispyb.client.IspybServiceStub.ShipmentInfo info)
        throws BadDataExceptionException, AxisFault, NotFoundException {
        GetReturnShipment request = new GetReturnShipment();
        request.setGetReturnShipment(info);
        try {
            return service.getReturnShipment(request).getMessage();
        } catch (RemoteException e) {
            processException(e);
            return null;
        }
    }

}
