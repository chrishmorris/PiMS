package org.pimslims.crystallization.xml;

import javax.xml.bind.JAXBException;

import org.pimslims.access.Access;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.xmlio.XmlConvertor;

public class ScreenExportUtil {

    public static String export(final String screenName) throws BusinessException, JAXBException {
        final String ScreenXML;
        final AbstractModel model = ModelImpl.getModel();
        final ReadableVersion rv = model.getReadableVersion(Access.ADMINISTRATOR);
        try {
            final RefHolder screen = rv.findFirst(RefHolder.class, RefHolder.PROP_NAME, screenName);
            if (screen == null) {
                throw new BusinessException("Can not find screen:" + screenName);
            }

            final ScreenXMLBean screenXmlBean = new ScreenXMLBean(screen);
            /* 
             * TODO use XSD
            as I said in my previous reply, the xsd is in the same folder as the screens:
            https://www.pims-lims.org/svn/pims/licenced-releases/xtalPiMS_V1_2/XtalPiMS_BusinessAPI_PIMSDBImpl/data/screens/XtalPiMS_Screens.xsd

             * */
            ScreenXML = XmlConvertor.convertBeanToXml(screenXmlBean, null);
        } finally {
            rv.abort();
        }

        return ScreenXML;

    }
}
