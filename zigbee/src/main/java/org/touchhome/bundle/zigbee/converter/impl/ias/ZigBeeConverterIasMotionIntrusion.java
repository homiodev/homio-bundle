package org.touchhome.bundle.zigbee.converter.impl.ias;

import com.zsmartsystems.zigbee.zcl.clusters.ZclIasZoneCluster;
import com.zsmartsystems.zigbee.zcl.clusters.iaszone.ZoneTypeEnum;
import org.touchhome.bundle.api.EntityContextVar.VariableType;
import org.touchhome.bundle.zigbee.converter.impl.ZigBeeConverter;

/**
 * Motion intrusion sensor Converter for the IAS motion sensor.
 */
@ZigBeeConverter(name = "zigbee:ias_motionintrusion", linkType = VariableType.Float,
                 clientCluster = ZclIasZoneCluster.CLUSTER_ID, category = "Motion")
public class ZigBeeConverterIasMotionIntrusion extends ZigBeeConverterIas {

  public ZigBeeConverterIasMotionIntrusion() {
    super(ZoneTypeEnum.MOTION_SENSOR);
  }

  @Override
  public void initialize() {
    bitTest = CIE_ALARM1;
    super.initialize();
  }
}
