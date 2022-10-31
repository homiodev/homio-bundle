package org.touchhome.bundle.zigbee.converter.impl;

import com.zsmartsystems.zigbee.ZigBeeEndpoint;
import com.zsmartsystems.zigbee.zcl.clusters.ZclIasZoneCluster;
import com.zsmartsystems.zigbee.zcl.clusters.iaszone.ZoneTypeEnum;

/**
 * Contact sensor
 */
@ZigBeeConverter(name = "zigbee:ias_contactportal1", clientCluster = ZclIasZoneCluster.CLUSTER_ID, category = "Door")
public class ZigBeeConverterIasContactPortal1 extends ZigBeeConverterIas {

  @Override
  public boolean initializeConverter() {
    bitTest = CIE_ALARM1;
    return super.initializeConverter();
  }

  @Override
  public boolean acceptEndpoint(ZigBeeEndpoint endpoint) {
    return supportsIasChannel(endpoint, ZoneTypeEnum.CONTACT_SWITCH);
  }
}
