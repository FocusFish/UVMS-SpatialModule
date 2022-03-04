package fish.focus.uvms.spatial.service.utils;

import fish.focus.uvms.spatial.service.dto.ProjectionDto;
import fish.focus.uvms.spatial.service.entity.ProjectionEntity;
import java.util.ArrayList;
import java.util.List;

public class ProjectionMapper {

    public static List<ProjectionDto> mapToProjectionDto(List<ProjectionEntity> entityList){
        List<ProjectionDto> dtoList = new ArrayList<>();
        for (ProjectionEntity entity:entityList) {
            dtoList.add(mapToProjectionDto(entity));
        }

        return dtoList;
    }

    public static ProjectionDto mapToProjectionDto(ProjectionEntity projection) {
        return new ProjectionDto(projection.getId(), projection.getName(), projection.getSrsCode(), projection.getUnits(), projection.getFormats(), projection.getWorld(), projection.getExtent());
    }
}
