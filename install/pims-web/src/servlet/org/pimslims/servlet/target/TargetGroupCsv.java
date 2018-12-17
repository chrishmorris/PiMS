package org.pimslims.servlet.target;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.AbstractCsvData;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.servlet.AbstractCsvServlet;

public class TargetGroupCsv extends AbstractCsvServlet {

    /**
     * Show a report on a target group, in CSV format
     * 
     * @baseURL http://localhost:8080/pims/TargetGroupCsv/org.pimslims.model.target.TargetGroup:4239/targetGroup.csv
     * 
     * @author cm65
     * 
     */
    public static class TargetGroupData implements AbstractCsvData {

        private final String[] attributeNames;

        private final Iterator<Target> iterator;

        public TargetGroupData(final TargetGroup group, final AbstractModel model) {
            final MetaClass metaClass = model.getMetaClass(org.pimslims.model.target.Target.class.getName());
            final Map<String, MetaAttribute> attributes = metaClass.getAttributes();
            final Collection<String> a = attributes.keySet();
            this.attributeNames = new String[a.size()];
            a.toArray(this.attributeNames);
            this.iterator = group.getTargets().iterator();
        }

        public String[] getHeaders() {
            return this.attributeNames;
        }

        public String[] next() {
            final Target target = this.iterator.next();
            final String[] ret = new String[this.attributeNames.length];
            for (int i = 0; i < ret.length; i++) {
                final Object value = target.get_Value(this.attributeNames[i]);
                if (null == value) {
                    ret[i] = "";
                } else {
                    ret[i] = value.toString();
                }
            }
            return ret;
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

    }

    @Override
    protected AbstractCsvData getCsvData(final ReadableVersion version, final String hook, Map<String, String> parms)
        throws ServletException {
        final TargetGroup group = version.get(hook);
        return new TargetGroupData(group, this.getModel());
    }

}
