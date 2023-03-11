package org.touchhome.bundle.z2m.util;

import com.fasterxml.jackson.databind.JsonNode;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.touchhome.bundle.api.EntityContextVar.VariableType;
import org.touchhome.bundle.api.EntityContextWidget.Fill;
import org.touchhome.bundle.api.EntityContextWidget.Stepped;
import org.touchhome.bundle.api.EntityContextWidget.ToggleType;
import org.touchhome.bundle.api.entity.widget.AggregationType;
import org.touchhome.bundle.api.entity.zigbee.ZigBeeProperty;
import org.touchhome.bundle.z2m.model.Z2MDeviceEntity;
import org.touchhome.bundle.z2m.util.Z2MDeviceDefinitionDTO.Options.Chart;
import org.touchhome.bundle.z2m.widget.WidgetBuilder.MainWidgetRequest;

@Getter
@Setter
public class Z2MDeviceDefinitionDTO {

    // for description inside json file only
    private String name;
    private String icon;
    private String iconColor;
    private Set<String> model;
    private List<WidgetDefinition> widgets;
    private JsonNode options;

    public enum WidgetType {
        color, toggle, display, compose, line, barTime
    }

    @Setter
    public static class WidgetDefinition {

        // z-index
        private Integer index;
        // block height/width
        private Integer blockWidth;
        private Integer blockHeight;
        // widget height inside layout
        private Integer widgetHeight;
        // Background color
        private String background;
        @Getter
        private @NotNull WidgetType type;
        @Getter
        private boolean autoDiscovery;
        @Getter
        private @Nullable String leftProperty; // "none" special key to ignore any left property
        @Getter
        private @Nullable String centerProperty;
        @Getter
        private @Nullable String rightProperty;
        private @Nullable List<ItemDefinition> props;
        @Getter private @Nullable List<WidgetDefinition> compose;
        private @Nullable String icon;
        // specify ui label(useful in case of 'compose' widget type)
        private @Nullable String name;
        @Getter
        private @Nullable String layout;
        @Getter
        private Options options = new Options();
        @Getter
        private @Nullable List<Requests> requests;

        public @NotNull List<ZigBeeProperty> getProps(Z2MDeviceEntity z2MDeviceEntity) {
            if (this.isAutoDiscovery()) {
                if (type == WidgetType.toggle) {
                    return z2MDeviceEntity.getDeviceService().getProperties().values().stream()
                                          .filter(p -> p.getExpose().getName().startsWith("state"))
                                          .collect(Collectors.toList());
                }
            }
            Stream<ZigBeeProperty> stream = Stream.empty();
            if (props != null) {
                stream = props.stream().map(p -> z2MDeviceEntity.getDeviceService().getProperties().get(p.getName()));
            }
            if (type == WidgetType.compose) {
                if (getCompose() == null) {
                    throw new IllegalArgumentException("compose type has to have compose array");
                }
                stream = getCompose().stream().flatMap(s -> s.getProps(z2MDeviceEntity).stream());
            }
            return stream.filter(Objects::nonNull).collect(Collectors.toList());
        }

        public String getName() {
            return StringUtils.defaultIfEmpty(name, type.name());
        }

        public String getIcon() {
            if (icon != null) {return icon;}
            switch (type) {
                case color:
                    return "fas fa-palette";
                case toggle:
                    return "fas fa-toggle-on";
                case display:
                    return "fas fa-display";
            }
            return null;
        }

        public List<ZigBeeProperty> getIncludeProperties(MainWidgetRequest request) {
            Set<String> topIncludeProperties = request.getWidgetRequest().getIncludeProperties().stream()
                                                      .map(ZigBeeProperty::getKey).collect(Collectors.toSet());
            List<ZigBeeProperty> allPossibleProperties = request.getItem().getProps(request.getWidgetRequest().getEntity());
            return allPossibleProperties.stream()
                                        .filter(zigBeeProperty -> topIncludeProperties.contains(zigBeeProperty.getKey()))
                                        .collect(Collectors.toList());
        }

        public @Nullable Z2MDeviceDefinitionDTO.WidgetDefinition.ItemDefinition getProperty(String key) {
            return props == null ? null : props.stream().filter(f -> f.name.equals(key)).findAny().orElse(null);
        }

        public int getBlockWidth(int defaultValue) {
            return Math.max(1, blockWidth == null ? defaultValue : blockWidth);
        }

        public int getBlockHeight(int defaultValue) {
            return Math.max(1, blockHeight == null ? defaultValue : blockHeight);
        }

        public int getWidgetHeight(int defaultValue) {
            return Math.max(1, widgetHeight == null ? defaultValue : widgetHeight);
        }

        public int getZIndex(int defaultValue) {
            return index == null ? defaultValue : index;
        }

        public String getBackground() {
            return background;
        }

        @SneakyThrows
        public static void replaceField(String path, Object value, WidgetDefinition widgetDefinition) {
            String[] split = path.split("\\.");
            if (split.length == 1) {
                FieldUtils.writeDeclaredField(widgetDefinition, split[0], value);
            }
            Object parentObject = widgetDefinition;
            Field cursor = FieldUtils.getDeclaredField(WidgetDefinition.class, split[0], true);
            for (int i = 1; i < split.length; i++) {
                parentObject = FieldUtils.readField(cursor, parentObject, true);
                cursor = FieldUtils.getDeclaredField(parentObject.getClass(), split[i], true);
            }
            FieldUtils.writeField(cursor, parentObject, value, true);
        }

        @Getter
        @Setter
        public static class ItemDefinition {

            private String name;
            private String icon;
            private String iconColor;
            private JsonNode iconThreshold;
            private JsonNode iconColorThreshold;
            private String valueConverter;
            private String valueColor;
            private boolean valueSourceClickHistory = true;
            private int valueConverterRefreshInterval = 0;
            private Chart chart;
        }
    }

    @Getter
    @Setter
    public static class Requests {

        private String name;
        private String value;
        private RequestType type;
        private String title;
        private String target;
        private float min = 0;
        private float max = 255;

        public enum RequestType {
            number
        }
    }

    @Getter
    @Setter
    public static class Options {

        // For chart
        private int pointsPerHour = 60;
        private double pointRadius = 0D;
        private boolean showDynamicLine = false;
        private String dynamicLineColor;
        private String pointBorderColor;
        private ToggleType toggleType = ToggleType.Regular;

        private boolean showAxisX = true;
        private boolean showAxisY = true;
        private boolean showChartFullScreenButton = true;

        // for display fire push value
        private Source pushSource;
        private String valueOnClick;
        private String valueOnDoubleClick;
        private String valueOnHoldClick;
        private String valueOnHoldReleaseClick;
        private String pushConfirmMessage;
        private JsonNode animation;
        private Chart chart;

        @Getter
        @Setter
        public static class Chart {

            private Source source;
            private String color;
            private Stepped stepped = Stepped.False;
            private Fill fill = Fill.Origin;
            private int lineBorderWidth = 2;
            private AggregationType aggregateFunc = AggregationType.AverageNoZero;
            private int opacity = 50;
            private int height = 30;
            private boolean smoothing = true;
            private Integer min;
            private Integer max;
            private boolean fillEmptyValues;
        }

        @Getter
        @Setter
        public static class Source {

            private SourceType kind;
            private String value;
            private VariableType variableType;

            public enum SourceType {
                broadcasts, property, variable
            }
        }
    }
}
