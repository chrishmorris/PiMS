
package org.pimslims.crystallization.servlet.upload;



import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.tools.OrderXmlLoader;

/**
 * 
 * @author Chris Morris
 * @version
 */
public class OrderUpload extends XmlUpload {

    /**
     * 
     */
    private static final long serialVersionUID = 7643062815567251311L;
    
    
    
    @Override
    protected String save(DataStorage dataStorage, java.io.InputStream uploadedStream) throws BusinessException {
        String name;
        name = new OrderXmlLoader(uploadedStream, dataStorage ).save();
        return name;
    }

    @Override
    protected String makePath(String name) {
        String path = "/ViewGroup.jsp?name=" + name;
        return path;
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "upload an order";
    }
}
