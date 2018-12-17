package org.pimslims.presentation.order;

import java.util.List;
import java.util.Map;

public interface IPlateOrderBean {

    public abstract String getLocationName();

    public abstract String getProtocolName();

    public abstract String getHolderName();

    public abstract String getPlateTypeName();

    public abstract List<IOrderBean> getOrderBeans();

    public abstract Map<org.pimslims.util.File, String> getAnnotations();

}
