package org.pimslims.ws.client.stubs;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated
 * in the org.pimslims.ws.client.stubs package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML
 * content. The Java representation of XML content can consist of schema derived interfaces and classes
 * representing the binding of schema type definitions, element declarations and model groups. Factory methods
 * for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetFileList_QNAME =
        new QName("http://server.ws.pimslims.org/", "getFileList");

    private final static QName _UploadResponse_QNAME =
        new QName("http://server.ws.pimslims.org/", "uploadResponse");

    private final static QName _GetFileListResponse_QNAME =
        new QName("http://server.ws.pimslims.org/", "getFileListResponse");

    private final static QName _Upload_QNAME = new QName("http://server.ws.pimslims.org/", "upload");

    private final static QName _UploadArg1_QNAME = new QName("", "arg1");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for
     * package: org.pimslims.ws.client.stubs
     * 
     */
    public ObjectFactory() {
        // why empty?
    }

    /**
     * Create an instance of {@link GetFileListResponse }
     * 
     */
    public GetFileListResponse createGetFileListResponse() {
        return new GetFileListResponse();
    }

    /**
     * Create an instance of {@link Upload }
     * 
     */
    public Upload createUpload() {
        return new Upload();
    }

    /**
     * Create an instance of {@link GetFileList }
     * 
     */
    public GetFileList createGetFileList() {
        return new GetFileList();
    }

    /**
     * Create an instance of {@link UploadResponse }
     * 
     */
    public UploadResponse createUploadResponse() {
        return new UploadResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileList }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://server.ws.pimslims.org/", name = "getFileList")
    public JAXBElement<GetFileList> createGetFileList(final GetFileList value) {
        return new JAXBElement<GetFileList>(ObjectFactory._GetFileList_QNAME, GetFileList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadResponse }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://server.ws.pimslims.org/", name = "uploadResponse")
    public JAXBElement<UploadResponse> createUploadResponse(final UploadResponse value) {
        return new JAXBElement<UploadResponse>(ObjectFactory._UploadResponse_QNAME, UploadResponse.class,
            null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileListResponse }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://server.ws.pimslims.org/", name = "getFileListResponse")
    public JAXBElement<GetFileListResponse> createGetFileListResponse(final GetFileListResponse value) {
        return new JAXBElement<GetFileListResponse>(ObjectFactory._GetFileListResponse_QNAME,
            GetFileListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Upload }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://server.ws.pimslims.org/", name = "upload")
    public JAXBElement<Upload> createUpload(final Upload value) {
        return new JAXBElement<Upload>(ObjectFactory._Upload_QNAME, Upload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg1", scope = Upload.class)
    public JAXBElement<byte[]> createUploadArg1(final byte[] value) {
        return new JAXBElement<byte[]>(ObjectFactory._UploadArg1_QNAME, byte[].class, Upload.class, (value));
    }

}
