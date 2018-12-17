
/**
 * WSPlateMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 */
        package uk.ac.ox.oppf.www.wsplate;

        /**
        *  WSPlateMessageReceiverInOut message receiver
        */

        public class WSPlateMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        WSPlateSkeletonInterface skel = (WSPlateSkeletonInterface)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null)){

        

            if("updatedPriority".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponseElement updatedPriorityResponseElement38 = null;
	                        uk.ac.ox.oppf.www.wsplate.UpdatedPriorityElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.UpdatedPriorityElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.UpdatedPriorityElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               updatedPriorityResponseElement38 =
                                                   
                                                   
                                                         skel.updatedPriority(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), updatedPriorityResponseElement38, false);
                                    } else 

            if("getPlateID".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.GetPlateIDResponseElement getPlateIDResponseElement40 = null;
	                        uk.ac.ox.oppf.www.wsplate.GetPlateIDElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.GetPlateIDElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.GetPlateIDElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getPlateIDResponseElement40 =
                                                   
                                                   
                                                         skel.getPlateID(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getPlateIDResponseElement40, false);
                                    } else 

            if("listSamples".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.ListSamplesResponseElement listSamplesResponseElement42 = null;
	                        uk.ac.ox.oppf.www.wsplate.ListSamplesElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.ListSamplesElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.ListSamplesElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               listSamplesResponseElement42 =
                                                   
                                                   
                                                         skel.listSamples(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), listSamplesResponseElement42, false);
                                    } else 

            if("getFirstDrop".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.GetFirstDropResponseElement getFirstDropResponseElement44 = null;
	                        uk.ac.ox.oppf.www.wsplate.GetFirstDropElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.GetFirstDropElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.GetFirstDropElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getFirstDropResponseElement44 =
                                                   
                                                   
                                                         skel.getFirstDrop(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getFirstDropResponseElement44, false);
                                    } else 

            if("getImageProcessor".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.GetImageProcessorResponseElement getImageProcessorResponseElement46 = null;
	                        uk.ac.ox.oppf.www.wsplate.GetImageProcessorElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.GetImageProcessorElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.GetImageProcessorElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getImageProcessorResponseElement46 =
                                                   
                                                   
                                                         skel.getImageProcessor(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getImageProcessorResponseElement46, false);
                                    } else 

            if("getPlateInfo".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponseElement getPlateInfoResponseElement48 = null;
	                        uk.ac.ox.oppf.www.wsplate.GetPlateInfoElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.GetPlateInfoElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.GetPlateInfoElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getPlateInfoResponseElement48 =
                                                   
                                                   
                                                         skel.getPlateInfo(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getPlateInfoResponseElement48, false);
                                    } else 

            if("getPlateTypes".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponseElement getPlateTypesResponseElement50 = null;
	                        uk.ac.ox.oppf.www.wsplate.GetPlateTypesElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.GetPlateTypesElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.GetPlateTypesElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getPlateTypesResponseElement50 =
                                                   
                                                   
                                                         skel.getPlateTypes(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getPlateTypesResponseElement50, false);
                                    } else 

            if("getDefaultCaptureProfile".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileResponseElement getDefaultCaptureProfileResponseElement52 = null;
	                        uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getDefaultCaptureProfileResponseElement52 =
                                                   
                                                   
                                                         skel.getDefaultCaptureProfile(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getDefaultCaptureProfileResponseElement52, false);
                                    } else 

            if("getCapturePoints".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.GetCapturePointsResponseElement getCapturePointsResponseElement54 = null;
	                        uk.ac.ox.oppf.www.wsplate.GetCapturePointsElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.GetCapturePointsElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.GetCapturePointsElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getCapturePointsResponseElement54 =
                                                   
                                                   
                                                         skel.getCapturePoints(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getCapturePointsResponseElement54, false);
                                    } else 

            if("getPlateType".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponseElement getPlateTypeResponseElement56 = null;
	                        uk.ac.ox.oppf.www.wsplate.GetPlateTypeElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.GetPlateTypeElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.GetPlateTypeElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getPlateTypeResponseElement56 =
                                                   
                                                   
                                                         skel.getPlateType(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getPlateTypeResponseElement56, false);
                                    } else 

            if("listProjects".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement listProjectsResponseElement58 = null;
	                        uk.ac.ox.oppf.www.wsplate.ListProjectsElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.ListProjectsElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.ListProjectsElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               listProjectsResponseElement58 =
                                                   
                                                   
                                                         skel.listProjects(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), listProjectsResponseElement58, false);
                                    } else 

            if("canListSamples".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.CanListSamplesResponse canListSamplesResponse60 = null;
	                        canListSamplesResponse60 =
                                                     
                                                 skel.canListSamples()
                                                ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), canListSamplesResponse60, false);
                                    } else 

            if("supportsPriority".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponseElement supportsPriorityResponseElement62 = null;
	                        uk.ac.ox.oppf.www.wsplate.SupportsPriorityElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.SupportsPriorityElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.SupportsPriorityElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               supportsPriorityResponseElement62 =
                                                   
                                                   
                                                         skel.supportsPriority(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), supportsPriorityResponseElement62, false);
                                    } else 

            if("imagedPlate".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.ImagedPlateResponseElement imagedPlateResponseElement64 = null;
	                        uk.ac.ox.oppf.www.wsplate.ImagedPlateElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.ImagedPlateElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.ImagedPlateElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               imagedPlateResponseElement64 =
                                                   
                                                   
                                                         skel.imagedPlate(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), imagedPlateResponseElement64, false);
                                    } else 

            if("skippedImaging".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.SkippedImagingResponseElement skippedImagingResponseElement66 = null;
	                        uk.ac.ox.oppf.www.wsplate.SkippedImagingElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.SkippedImagingElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.SkippedImagingElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               skippedImagingResponseElement66 =
                                                   
                                                   
                                                         skel.skippedImaging(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), skippedImagingResponseElement66, false);
                                    } else 

            if("uploadImages".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.UploadImagesResponseElement uploadImagesResponseElement68 = null;
	                        uk.ac.ox.oppf.www.wsplate.UploadImagesElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.UploadImagesElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.UploadImagesElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               uploadImagesResponseElement68 =
                                                   
                                                   
                                                         skel.uploadImages(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), uploadImagesResponseElement68, false);
                                    } else 

            if("imagingPlate".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.ImagingPlateResponseElement imagingPlateResponseElement70 = null;
	                        uk.ac.ox.oppf.www.wsplate.ImagingPlateElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.ImagingPlateElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.ImagingPlateElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               imagingPlateResponseElement70 =
                                                   
                                                   
                                                         skel.imagingPlate(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), imagingPlateResponseElement70, false);
                                    } else 

            if("getImagingTasks".equals(methodName)){
                
                uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponseElement getImagingTasksResponseElement72 = null;
	                        uk.ac.ox.oppf.www.wsplate.GetImagingTasksElement wrappedParam =
                                                             (uk.ac.ox.oppf.www.wsplate.GetImagingTasksElement)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    uk.ac.ox.oppf.www.wsplate.GetImagingTasksElement.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getImagingTasksResponseElement72 =
                                                   
                                                   
                                                         skel.getImagingTasks(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getImagingTasksResponseElement72, false);
                                    
            } else {
              throw new java.lang.RuntimeException("method not found");
            }
        

        newMsgContext.setEnvelope(envelope);
        }
        } catch (GetFirstDropError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"GetFirstDropFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (ImagedPlateError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"ImagedPlateFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (UpdatedPriorityError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"UpdatedPriorityFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (ImagingPlateError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"ImagingPlateFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (UploadImagesError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"UploadImagesFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (GetPlateTypeError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"GetPlateTypeFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (SupportsPriorityError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"SupportsPriorityFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (ListSamplesError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"ListSamplesFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (GetCapturePointsError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"GetCapturePointsFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (GetPlateTypesError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"GetPlateTypesFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (GetImagingTasksError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"GetImagingTasksFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (CanListSamplesError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"CanListSamplesFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (GetImageProcessorError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"GetImageProcessorFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (GetPlateInfoError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"GetPlateInfoFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (GetPlateIDError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"GetPlateIDFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (ListProjectsError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"ListProjectsFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (GetDefaultCaptureProfileError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"GetDefaultCaptureProfileFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
         catch (SkippedImagingError e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"SkippedImagingFault");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
        
        catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
        }
        
        //
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.UpdatedPriorityElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.UpdatedPriorityElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.UpdatedPriorityFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.UpdatedPriorityFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetPlateIDElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateIDElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetPlateIDResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateIDResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetPlateIDFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateIDFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.ListSamplesElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.ListSamplesElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.ListSamplesResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.ListSamplesResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.ListSamplesFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.ListSamplesFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetFirstDropElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetFirstDropElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetFirstDropResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetFirstDropResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetFirstDropFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetFirstDropFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetImageProcessorElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetImageProcessorElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetImageProcessorResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetImageProcessorResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetImageProcessorFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetImageProcessorFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetPlateInfoElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateInfoElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetPlateInfoFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateInfoFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetPlateTypesElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateTypesElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetPlateTypesFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateTypesFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetCapturePointsElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetCapturePointsElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetCapturePointsResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetCapturePointsResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetCapturePointsFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetCapturePointsFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetPlateTypeElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateTypeElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetPlateTypeFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateTypeFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.ListProjectsElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.ListProjectsElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.ListProjectsFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.ListProjectsFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.CanListSamplesResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.CanListSamplesResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.CanListSamplesFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.CanListSamplesFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.SupportsPriorityElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.SupportsPriorityElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.SupportsPriorityFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.SupportsPriorityFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.ImagedPlateElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.ImagedPlateElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.ImagedPlateResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.ImagedPlateResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.ImagedPlateFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.ImagedPlateFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.SkippedImagingElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.SkippedImagingElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.SkippedImagingResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.SkippedImagingResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.SkippedImagingFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.SkippedImagingFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.UploadImagesElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.UploadImagesElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.UploadImagesResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.UploadImagesResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.UploadImagesFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.UploadImagesFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.ImagingPlateElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.ImagingPlateElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.ImagingPlateResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.ImagingPlateResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.ImagingPlateFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.ImagingPlateFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetImagingTasksElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetImagingTasksElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponseElement param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponseElement.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(uk.ac.ox.oppf.www.wsplate.GetImagingTasksFault param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetImagingTasksFault.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponseElement wrapupdatedPriority(){
                                uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.GetPlateIDResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateIDResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.GetPlateIDResponseElement wrapgetPlateID(){
                                uk.ac.ox.oppf.www.wsplate.GetPlateIDResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.GetPlateIDResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.ListSamplesResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.ListSamplesResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.ListSamplesResponseElement wraplistSamples(){
                                uk.ac.ox.oppf.www.wsplate.ListSamplesResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.ListSamplesResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.GetFirstDropResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetFirstDropResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.GetFirstDropResponseElement wrapgetFirstDrop(){
                                uk.ac.ox.oppf.www.wsplate.GetFirstDropResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.GetFirstDropResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.GetImageProcessorResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetImageProcessorResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.GetImageProcessorResponseElement wrapgetImageProcessor(){
                                uk.ac.ox.oppf.www.wsplate.GetImageProcessorResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.GetImageProcessorResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponseElement wrapgetPlateInfo(){
                                uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponseElement wrapgetPlateTypes(){
                                uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileResponseElement wrapgetDefaultCaptureProfile(){
                                uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.GetCapturePointsResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetCapturePointsResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.GetCapturePointsResponseElement wrapgetCapturePoints(){
                                uk.ac.ox.oppf.www.wsplate.GetCapturePointsResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.GetCapturePointsResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponseElement wrapgetPlateType(){
                                uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement wraplistProjects(){
                                uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.CanListSamplesResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.CanListSamplesResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.CanListSamplesResponse wrapcanListSamples(){
                                uk.ac.ox.oppf.www.wsplate.CanListSamplesResponse wrappedElement = new uk.ac.ox.oppf.www.wsplate.CanListSamplesResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponseElement wrapsupportsPriority(){
                                uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.ImagedPlateResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.ImagedPlateResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.ImagedPlateResponseElement wrapimagedPlate(){
                                uk.ac.ox.oppf.www.wsplate.ImagedPlateResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.ImagedPlateResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.SkippedImagingResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.SkippedImagingResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.SkippedImagingResponseElement wrapskippedImaging(){
                                uk.ac.ox.oppf.www.wsplate.SkippedImagingResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.SkippedImagingResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.UploadImagesResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.UploadImagesResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.UploadImagesResponseElement wrapuploadImages(){
                                uk.ac.ox.oppf.www.wsplate.UploadImagesResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.UploadImagesResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.ImagingPlateResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.ImagingPlateResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.ImagingPlateResponseElement wrapimagingPlate(){
                                uk.ac.ox.oppf.www.wsplate.ImagingPlateResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.ImagingPlateResponseElement();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponseElement param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponseElement.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponseElement wrapgetImagingTasks(){
                                uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponseElement wrappedElement = new uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponseElement();
                                return wrappedElement;
                         }
                    


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (uk.ac.ox.oppf.www.wsplate.UpdatedPriorityElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.UpdatedPriorityElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.UpdatedPriorityFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.UpdatedPriorityFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetPlateIDElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetPlateIDElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetPlateIDResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetPlateIDResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetPlateIDFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetPlateIDFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.ListSamplesElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.ListSamplesElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.ListSamplesResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.ListSamplesResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.ListSamplesFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.ListSamplesFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetFirstDropElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetFirstDropElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetFirstDropResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetFirstDropResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetFirstDropFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetFirstDropFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetImageProcessorElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetImageProcessorElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetImageProcessorResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetImageProcessorResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetImageProcessorFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetImageProcessorFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetPlateInfoElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetPlateInfoElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetPlateInfoFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetPlateInfoFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetPlateTypesElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetPlateTypesElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetPlateTypesFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetPlateTypesFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetCapturePointsElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetCapturePointsElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetCapturePointsResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetCapturePointsResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetCapturePointsFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetCapturePointsFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetPlateTypeElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetPlateTypeElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetPlateTypeFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetPlateTypeFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.ListProjectsElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.ListProjectsElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.ListProjectsFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.ListProjectsFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.CanListSamplesResponse.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.CanListSamplesResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.CanListSamplesFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.CanListSamplesFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.SupportsPriorityElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.SupportsPriorityElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.SupportsPriorityFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.SupportsPriorityFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.ImagedPlateElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.ImagedPlateElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.ImagedPlateResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.ImagedPlateResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.ImagedPlateFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.ImagedPlateFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.SkippedImagingElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.SkippedImagingElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.SkippedImagingResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.SkippedImagingResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.SkippedImagingFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.SkippedImagingFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.UploadImagesElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.UploadImagesElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.UploadImagesResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.UploadImagesResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.UploadImagesFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.UploadImagesFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.ImagingPlateElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.ImagingPlateElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.ImagingPlateResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.ImagingPlateResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.ImagingPlateFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.ImagingPlateFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetImagingTasksElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetImagingTasksElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponseElement.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponseElement.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (uk.ac.ox.oppf.www.wsplate.GetImagingTasksFault.class.equals(type)){
                
                           return uk.ac.ox.oppf.www.wsplate.GetImagingTasksFault.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    

        /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
        private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
        org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
        }

        private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }

        return f;
    }

        }//end of class
    