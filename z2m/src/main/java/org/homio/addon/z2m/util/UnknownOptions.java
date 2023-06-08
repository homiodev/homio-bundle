package org.homio.addon.z2m.util;

import static org.homio.api.util.CommonUtils.OBJECT_MAPPER;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class UnknownOptions {

    @JsonIgnore private final Map<String, JsonNode> unknownFields = new HashMap<>();

    @SuppressWarnings("unused")
    @JsonAnyGetter
    public Map<String, JsonNode> otherFields() {
        return unknownFields;
    }

    @JsonAnySetter
    public void setOtherField(String name, JsonNode value) {
        unknownFields.put(name, value);
    }

    public @NotNull ObjectNode getOrCreateObjectNode(String key) {
        JsonNode node = unknownFields.get(key);
        if (!(node instanceof ObjectNode)) {
            node = OBJECT_MAPPER.createObjectNode();
            unknownFields.put(key, node);
        }
        return (ObjectNode) node;
    }
}
