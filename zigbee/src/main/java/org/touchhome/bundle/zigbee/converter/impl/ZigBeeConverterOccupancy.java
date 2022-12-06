package org.touchhome.bundle.zigbee.converter.impl;

import com.zsmartsystems.zigbee.zcl.ZclAttribute;
import com.zsmartsystems.zigbee.zcl.clusters.ZclOccupancySensingCluster;
import com.zsmartsystems.zigbee.zcl.protocol.ZclClusterType;
import org.touchhome.bundle.api.EntityContextVar.VariableType;
import org.touchhome.bundle.api.state.OnOffType;

@ZigBeeConverter(name = "zigbee:sensor_occupancy", linkType = VariableType.Boolean,
    clientCluster = ZclOccupancySensingCluster.CLUSTER_ID, category = "Motion")
public class ZigBeeConverterOccupancy extends ZigBeeInputBaseConverter {

  public ZigBeeConverterOccupancy() {
    super(ZclClusterType.OCCUPANCY_SENSING, ZclOccupancySensingCluster.ATTR_OCCUPANCY);
  }

  @Override
  protected void updateValue(Object val, ZclAttribute attribute) {
    Integer value = (Integer) val;
    if (value != null && value == 1) {
      updateChannelState(OnOffType.ON);
    } else {
      updateChannelState(OnOffType.OFF);
    }
  }
}
