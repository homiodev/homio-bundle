package org.touchhome.bundle.camera;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.exception.NotFoundException;
import org.touchhome.bundle.api.ui.field.action.UIActionResponse;
import org.touchhome.bundle.api.ui.field.action.impl.StatefulContextMenuAction;
import org.touchhome.bundle.camera.entity.BaseVideoCameraEntity;
import org.touchhome.bundle.camera.entity.BaseVideoStreamEntity;
import org.touchhome.bundle.camera.widget.WidgetCameraEntity;
import org.touchhome.bundle.camera.widget.WidgetCameraSeriesEntity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/rest/camera")
@RequiredArgsConstructor
public class CameraController {

    private final EntityContext entityContext;

    @GetMapping("/{entityID}")
    public List<CameraEntityResponse> getCameraData(@PathVariable("entityID") String entityID) {
        WidgetCameraEntity entity = entityContext.getEntity(entityID);
        List<CameraEntityResponse> result = new ArrayList<>();
        for (WidgetCameraSeriesEntity item : entity.getSeries()) {
            BaseVideoStreamEntity baseVideoStreamEntity = entityContext.getEntity(item.getDataSource());
            if (baseVideoStreamEntity != null) {
                result.add(new CameraEntityResponse(entityContext.getEntity(item.getDataSource())));
            } else {
                log.warn("Camera entity: <{}> not found", item.getDataSource());
            }
        }
        return result;
    }

    @PostMapping("/{entityID}/series/{seriesEntityID}/action")
    public void fireCameraAction(@PathVariable("entityID") String entityID,
                                 @PathVariable("seriesEntityID") String seriesEntityID,
                                 @RequestBody CameraActionRequest cameraActionRequest) {
        WidgetCameraEntity entity = entityContext.getEntity(entityID);
        WidgetCameraSeriesEntity series = entity.getSeries().stream().filter(s -> s.getEntityID().equals(seriesEntityID)).findAny().orElse(null);
        if (series == null) {
            throw new NotFoundException("Unable to find series: " + seriesEntityID + " for entity: " + entity.getTitle());
        }
        BaseVideoCameraEntity baseVideoCameraEntity = entityContext.getEntity(series.getDataSource());
        if (baseVideoCameraEntity == null) {
            throw new NotFoundException("Unable to find base camera for series: " + series.getTitle());
        }
        Set<StatefulContextMenuAction> statefulContextMenuActions = baseVideoCameraEntity.getCameraHandler().getCameraActions(false);
        StatefulContextMenuAction statefulContextMenuAction = statefulContextMenuActions.stream().filter(ca -> ca.getName().equals(cameraActionRequest.name)).findAny().orElseThrow(
                () -> new RuntimeException("No camera action " + cameraActionRequest.name + "found"));
        statefulContextMenuAction.getAction().accept(new JSONObject().put("value", cameraActionRequest.value));
    }

    @Setter
    private static class CameraActionRequest {
        private String name;
        private String value;
    }

    @Getter
    private static class CameraEntityResponse {
        private final BaseVideoStreamEntity source;
        private final Set<UIActionResponse> actions;

        public CameraEntityResponse(BaseVideoStreamEntity source) {
            this.source = source;
            Set<StatefulContextMenuAction> actions = source.getActions(true);
            this.actions = actions.stream().map(UIActionResponse::new).collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }
}
