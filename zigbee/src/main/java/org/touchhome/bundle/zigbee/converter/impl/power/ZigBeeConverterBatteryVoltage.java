package org.touchhome.bundle.zigbee.converter.impl.power;

import static com.zsmartsystems.zigbee.zcl.clusters.ZclPowerConfigurationCluster.ATTR_BATTERYVOLTAGE;
import static com.zsmartsystems.zigbee.zcl.protocol.ZclClusterType.POWER_CONFIGURATION;

import com.zsmartsystems.zigbee.zcl.ZclAttribute;
import com.zsmartsystems.zigbee.zcl.clusters.ZclPowerConfigurationCluster;
import java.math.BigDecimal;
import org.touchhome.bundle.api.EntityContextVar.VariableType;
import org.touchhome.bundle.api.state.QuantityType;
import org.touchhome.bundle.api.util.Units;
import org.touchhome.bundle.zigbee.converter.impl.ZigBeeConverter;
import org.touchhome.bundle.zigbee.converter.impl.ZigBeeInputBaseConverter;

/**
 * Battery Voltage The current battery voltage
 */
@ZigBeeConverter(name = "battery_voltage", linkType = VariableType.Float,
                 color = "#CF8B34", clientCluster = ZclPowerConfigurationCluster.CLUSTER_ID, category = "Energy")
public class ZigBeeConverterBatteryVoltage extends ZigBeeInputBaseConverter<ZclPowerConfigurationCluster> {

  public ZigBeeConverterBatteryVoltage() {
    super(POWER_CONFIGURATION, ATTR_BATTERYVOLTAGE);
  }

  @Override
  protected void updateValue(Object val, ZclAttribute attribute) {
    Integer value = (Integer) val;
    if (value == 0xFF) {
      // The value 0xFF indicates an invalid or unknown reading.
      return;
    }
    BigDecimal valueInVolt = BigDecimal.valueOf(value, 1);
    updateChannelState(new QuantityType<>(valueInVolt, Units.VOLT));
  }
}
