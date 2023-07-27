package org.homio.addon.z2m.service;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.homio.api.model.DeviceDefinitionModel.getDeviceIcon;
import static org.homio.api.model.DeviceDefinitionModel.getDeviceIconColor;
import static org.homio.api.model.endpoint.DeviceEndpoint.ENDPOINT_DEVICE_STATUS;
import static org.homio.api.model.endpoint.DeviceEndpoint.ENDPOINT_LAST_SEEN;
import static org.homio.api.util.CommonUtils.OBJECT_MAPPER;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.homio.addon.z2m.model.Z2MDeviceEntity;
import org.homio.addon.z2m.model.Z2MLocalCoordinatorEntity;
import org.homio.addon.z2m.service.properties.Z2MEndpointAction;
import org.homio.addon.z2m.service.properties.Z2MEndpointLastSeen;
import org.homio.addon.z2m.service.properties.inline.Z2MEndpointGeneral;
import org.homio.addon.z2m.service.properties.inline.Z2MEndpointUnknown;
import org.homio.addon.z2m.service.properties.inline.Z2MPropertyDeviceStatusEndpoint;
import org.homio.addon.z2m.util.ApplianceModel;
import org.homio.addon.z2m.util.ApplianceModel.Z2MDeviceDefinition.Options;
import org.homio.addon.z2m.util.Z2MPropertyConfigService;
import org.homio.addon.z2m.util.Z2MPropertyModel;
import org.homio.api.EntityContext;
import org.homio.api.EntityContextService.MQTTEntityService;
import org.homio.api.model.DeviceDefinitionModel;
import org.homio.api.model.Icon;
import org.homio.api.model.Status;
import org.homio.api.state.DecimalType;
import org.homio.api.ui.UI;
import org.homio.api.util.CommonUtils;
import org.homio.api.util.Lang;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

@Log4j2
public class Z2MDeviceService {

    private final Z2MLocalCoordinatorService coordinatorService;
    @Getter
    private final Map<String, Z2MEndpoint> endpoints = new ConcurrentHashMap<>();
    @Getter
    private final Z2MDeviceEntity deviceEntity;
    @Getter
    private final EntityContext entityContext;
    @Getter
    private final Z2MPropertyConfigService configService;
    @Getter
    private String availability;
    @Getter
    private ApplianceModel applianceModel;
    private boolean initialized = false;
    private String currentMQTTTopic;

    public Z2MDeviceService(Z2MLocalCoordinatorService coordinatorService, ApplianceModel applianceModel) {
        this.coordinatorService = coordinatorService;
        this.configService = coordinatorService.getConfigService();
        this.entityContext = coordinatorService.getEntityContext();
        this.deviceEntity = new Z2MDeviceEntity(this, applianceModel.getIeeeAddress());
        this.applianceModel = applianceModel;

        deviceUpdated(applianceModel);
        addMissingProperties(coordinatorService, applianceModel);

        entityContext.ui().updateItem(deviceEntity);
        entityContext.ui().sendSuccessMessage(Lang.getServerMessage("ENTITY_CREATED", "${%s}".formatted(this.applianceModel.getName())));
        setEntityOnline();
    }

    public String getIeeeAddress() {
        return deviceEntity.getIeeeAddress();
    }

    public void dispose() {
        log.warn("[{}]: Dispose zigbee device: {}", coordinatorService.getEntityID(), deviceEntity.getTitle());
        removeMqttListeners();
        entityContext.ui().updateItem(deviceEntity);
        downLinkQualityToZero();
        initialized = false;
    }

    public synchronized void deviceUpdated(ApplianceModel applianceModel) {
        // user changed friendly name via z2m frontend or change file manually - update listeners
        if (initialized && !applianceModel.getMQTTTopic().equals(currentMQTTTopic)) {
            removeMqttListeners();
            addMqttListeners();
        }

        this.applianceModel = applianceModel;
        createOrUpdateDeviceGroup();
        removeRedundantExposes();
        for (Options expose : applianceModel.getDefinition().getExposes()) {
            // types like switch has no property but has 'type'/'endpoint'/'features'
            if (expose.getProperty() == null) {
                addExposeByFeatures(expose);
            } else {
                addPropertyOptional(expose.getProperty(), key -> buildExposeProperty(expose));
            }
        }
        addPropertyOptional(ENDPOINT_LAST_SEEN, key -> {
            Z2MEndpointLastSeen propertyLastSeen = new Z2MEndpointLastSeen();
            propertyLastSeen.init(this, Options.dynamicExpose(ENDPOINT_LAST_SEEN, ApplianceModel.NUMBER_TYPE), true);
            return propertyLastSeen;
        });
        // add device status
        addPropertyOptional(ENDPOINT_DEVICE_STATUS, key -> new Z2MPropertyDeviceStatusEndpoint(this));
    }

    public void setEntityOnline() {
        if (!initialized) {
            log.warn("[{}]: Initialize zigbee device: {}", coordinatorService.getEntityID(), deviceEntity.getTitle());
            addMqttListeners();
            entityContext.event().fireEvent("zigbee-%s".formatted(applianceModel.getIeeeAddress()), deviceEntity.getStatus());
            initialized = true;
        } else {
            log.debug("[{}]: Zigbee device: {} was initialized before", coordinatorService.getEntityID(), deviceEntity.getTitle());
        }
    }

    public Z2MPropertyModel getPropertyModel(String action) {
        return configService.getFileMeta().getDeviceProperties().get(action);
    }

    public Set<String> getExposes() {
        return endpoints.keySet();
    }

    public void sendRequest(String path, String payload) {
        coordinatorService.sendRequest(path, payload);
    }

    public @NotNull Z2MLocalCoordinatorEntity getCoordinatorEntity() {
        return coordinatorService.getEntity();
    }

    public void updateDeviceConfiguration(Z2MDeviceService deviceService, String propertyName, Object value) {
        coordinatorService.updateDeviceConfiguration(deviceService, propertyName, value);
    }

    public String getModel() {
        return applianceModel.getDefinition().getModel();
    }

    private void addMqttListeners() {
        currentMQTTTopic = applianceModel.getMQTTTopic();
        String topic = getMqttFQDNTopic();

        coordinatorService.getMqttEntityService().addListener(topic, applianceModel.getIeeeAddress(), value -> {
            String payload = value == null ? "" : value.toString();
            if (!payload.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    mqttUpdate(jsonObject);
                } catch (Exception ex) {
                    log.error("[{}]: Unable to parse json for entity: '{}' from: '{}'", coordinatorService.getEntityID(),
                        deviceEntity.getTitle(), payload);
                }
            }
        });

        coordinatorService.getMqttEntityService().addListener(topic + "/availability", applianceModel.getIeeeAddress(), payload -> {
            availability = payload == null ? null : payload.toString();
            entityContext.event().fireEvent("zigbee-%s".formatted(applianceModel.getIeeeAddress()), deviceEntity.getStatus());
            entityContext.ui().updateItem(this.deviceEntity);
            if ("offline".equals(availability)) {
                downLinkQualityToZero();
            }
        });
    }

    private String getMqttFQDNTopic() {
        return "%s/%s".formatted(coordinatorService.getEntity().getBasicTopic(), currentMQTTTopic);
    }

    private void removeMqttListeners() {
        MQTTEntityService mqttEntityService = coordinatorService.getMqttEntityService();
        if (mqttEntityService != null) { // may be NPE during dispose if device not configured yet
            String topic = getMqttFQDNTopic();
            mqttEntityService.removeListener(topic, applianceModel.getIeeeAddress());
            mqttEntityService.removeListener(topic + "/availability", applianceModel.getIeeeAddress());
        }
    }

    @Override
    public String toString() {
        return applianceModel.toString();
    }

    public JsonNode getConfiguration() {
        return coordinatorService
            .getConfiguration()
            .getDevices()
            .getOrDefault(applianceModel.getIeeeAddress(), OBJECT_MAPPER.createObjectNode());
    }

    public void updateConfiguration(String key, Object value) {
        this.coordinatorService.updateDeviceConfiguration(this, key, value);
    }

    public void publish(@NotNull String topic, @NotNull JSONObject payload) {
        if (topic.startsWith("bridge/'")) {
            this.coordinatorService.publish(topic, payload);
        } else {
            this.coordinatorService.publish(currentMQTTTopic + "/" + topic, payload);
        }
    }

    public String getDeviceFullName() {
        String name;
        if (this.getConfiguration().has("name")) {
            name = this.getConfiguration().get("name").asText();
        } else if (getModel() != null) {
            name = "${zigbee.device.%s~%s}".formatted(getModel(), applianceModel.getDefinition().getDescription());
        } else {
            name = applianceModel.getDefinition().getDescription();
        }
        return "%s(%s) [${%s}]".formatted(name, this.applianceModel.getIeeeAddress(), defaultIfEmpty(this.deviceEntity.getPlace(), "place_not_set"));
    }

    public Z2MEndpoint addDynamicProperty(String key, Supplier<Z2MEndpoint> supplier) {
        return addPropertyOptional(key, s -> {
            Z2MEndpoint property = supplier.get();
            // store missing property in file to next reload
            coordinatorService.addMissingProperty(deviceEntity.getIeeeAddress(), property);
            return property;
        });
    }

    private void mqttUpdate(JSONObject payload) {
        boolean updated = false;
        List<String> sb = new ArrayList<>();
        for (String key : payload.keySet()) {
            try {
                boolean feed = false;
                for (Z2MEndpoint property : endpoints.values()) {
                    if (property.feedPayload(key, payload)) {
                        feed = true;
                        updated = true;
                        sb.add("%s - %s".formatted(property.getDescription(), property.getValue().toString()));
                    }
                }
                if (!feed) {
                    Set<String> ignoreExposes = getListOfRedundantProperties();
                    if (!ignoreExposes.contains(key)) {
                        log.info("[{}]: Create dynamic created property '{}'. Device: {}", coordinatorService.getEntityID(), key,
                            applianceModel.getIeeeAddress());
                        Z2MEndpoint missedZ2MEndpoint = buildExposeProperty(Options.dynamicExpose(key, getFormatFromPayloadValue(payload, key)));
                        missedZ2MEndpoint.mqttUpdate(payload);

                        addPropertyOptional(key, s -> missedZ2MEndpoint);
                        entityContext.ui().updateItem(deviceEntity);
                    }
                }
            } catch (Exception ex) {
                log.error("Unable to handle Z2MProperty: {}. Payload: {}. Msg: {}",
                    key, payload, CommonUtils.getErrorMessage(ex));
            }
        }
        if (updated && !payload.keySet().contains(ENDPOINT_LAST_SEEN)) {
            // fire set value as current milliseconds if device has no last_seen property for some reason
            endpoints.get(ENDPOINT_LAST_SEEN).mqttUpdate(null);
        }
        if (deviceEntity.isLogEvents() && !sb.isEmpty()) {
            entityContext.ui().sendInfoMessage(applianceModel.getGroupDescription(), String.join("\n", sb));
        }
    }

    /**
     * Remove exposes from z2m device in such cases:
     * <p>
     * 1) expose exists inside file 'zigbee-devices.json' array 'ignoreProperties'
     * <p>
     * 2) expose exists inside 'alias' array inside another expose in file 'zigbee-devices.json'
     */
    private void removeRedundantExposes() {
        Set<String> ignoreExposes = getListOfRedundantProperties();

        applianceModel.getDefinition().getExposes().removeIf(e -> {
            if (configService.getFileMeta().getIgnoreProperties().contains(e.getName()) || ignoreExposes.contains(e.getName())) {
                log.info("[{}]: ({}): Skip property: {}",
                    coordinatorService.getEntityID(),
                    applianceModel.getIeeeAddress(), e.getName());
                return true;
            }
            return false;
        });
    }

    @NotNull
    private Set<String> getListOfRedundantProperties() {
        return Stream.concat(
                         endpoints.keySet().stream(),
                         applianceModel.getDefinition().getExposes().stream().map(Options::getName))
                     .map(this::getPropertyModel)
                     .filter(p -> p != null && p.getAlias() != null)
                     .flatMap(p -> p.getAlias().stream())
                     .collect(Collectors.toSet());
    }

    private void addMissingProperties(Z2MLocalCoordinatorService coordinatorService, ApplianceModel applianceModel) {
        for (Pair<String, String> missingProperty : coordinatorService.getMissingProperties(applianceModel.getIeeeAddress())) {
            if ("action_event".equals(missingProperty.getValue())) {
                addPropertyOptional(missingProperty.getKey(), key -> Z2MEndpointAction.createActionEvent(key, this, entityContext));
            }
        }
    }

    private Z2MEndpoint addPropertyOptional(String key, Function<String, Z2MEndpoint> propertyProducer) {
        if (!endpoints.containsKey(key)) {
            endpoints.put(key, propertyProducer.apply(key));
            entityContext.event().fireEvent("zigbee-%s-%s".formatted(applianceModel.getIeeeAddress(), key), Status.ONLINE);
        }
        return endpoints.get(key);
    }

    private String getFormatFromPayloadValue(JSONObject payload, String key) {
        try {
            payload.getInt(key);
            return ApplianceModel.NUMBER_TYPE;
        } catch (Exception ignore) {
        }
        try {
            payload.getBoolean(key);
            return ApplianceModel.BINARY_TYPE;
        } catch (Exception ignore) {
        }
        return ApplianceModel.UNKNOWN_TYPE;
    }

    // usually expose.getName() is enough but in case of color - name - 'color_xy' but property is
    // 'color'
    private <T> T getValueFromMap(Map<String, T> map, Options expose) {
        return map.getOrDefault(expose.getName(), map.get(expose.getProperty()));
    }

    private void addExposeByFeatures(Options expose) {
        if (expose.getFeatures() != null) {
            for (Options feature : expose.getFeatures()) {
                addPropertyOptional(feature.getProperty(), key -> buildExposeProperty(feature));
            }
        } else {
            log.error("[{}]: Device expose {} has no features", coordinatorService.getEntityID(), expose);
        }
    }

    private Z2MEndpoint buildExposeProperty(Options expose) {
        Class<? extends Z2MEndpoint> z2mCluster = getValueFromMap(configService.getConverters(), expose);
        Z2MEndpoint z2MEndpoint;
        if (z2mCluster == null) {
            Z2MPropertyModel z2MPropertyModel = getValueFromMap(configService.getFileMeta().getDeviceProperties(), expose);
            if (z2MPropertyModel != null) {
                z2MEndpoint = new Z2MEndpointGeneral(z2MPropertyModel.getIcon(), z2MPropertyModel.getIconColor());
                z2MEndpoint.setUnit(z2MPropertyModel.getUnit());
            } else {
                z2MEndpoint = new Z2MEndpointUnknown();
            }
        } else {
            z2MEndpoint = CommonUtils.newInstance(z2mCluster);
        }
        boolean createVariable = !configService.getFileMeta().getPropertiesWithoutVariables().contains(expose.getProperty());
        z2MEndpoint.init(this, expose, createVariable);
        return z2MEndpoint;
    }

    private void createOrUpdateDeviceGroup() {
        Icon icon = new Icon(
            getDeviceIcon(findDevices(), "fas fa-server"),
            getDeviceIconColor(findDevices(), UI.Color.random())
        );
        entityContext.var().createGroup("z2m", this.deviceEntity.getEntityID(), getDeviceFullName(), true,
            icon, this.applianceModel.getGroupDescription());
    }

    public @NotNull List<DeviceDefinitionModel> findDevices() {
        return configService.findDevices(this);
    }

    private void downLinkQualityToZero() {
        Optional.ofNullable(endpoints.get(Z2MEndpointGeneral.ENDPOINT_SIGNAL)).ifPresent(z2MProperty -> {
            if (!z2MProperty.getValue().stringValue().equals("0")) {
                z2MProperty.setValue(new DecimalType(0));
            }
        });
    }
}
