package org.touchhome.bundle.zigbee.converter.config;

import lombok.Getter;

@Getter
public class ZclFanControlConfig {

  /*private final ZclFanControlCluster fanControlCluster;
  private final boolean supportFanModeSequence;

  private int fanModeSequence;

  public ZclFanControlConfig(ZigBeeEndpointEntity entity, ZclCluster cluster, Logger log) {
    this.fanModeSequence = entity.getFanModeSequence();

    fanControlCluster = (ZclFanControlCluster) cluster;
    ZclLevelControlConfig.initCluster(fanControlCluster.discoverAttributes(false), log,
        fanControlCluster.getZigBeeAddress(), fanControlCluster.getClusterName());

    this.supportFanModeSequence = fanControlCluster.isAttributeSupported(ZclFanControlCluster.ATTR_FANMODESEQUENCE);
  }

  public void updateConfiguration(ZigBeeEndpointEntity entity) {
    if (fanModeSequence != entity.getFanModeSequence()) {
      fanModeSequence = entity.getFanModeSequence();
      Integer response = configureAttribute(fanControlCluster, ZclFanControlCluster.ATTR_FANMODESEQUENCE, fanModeSequence);
      if (response != null && response != fanModeSequence) {
        fanModeSequence = response;
        entity.setFanModeSequence(response);
        entity.setOutdated(true);
      }
    }
  }*/
}
