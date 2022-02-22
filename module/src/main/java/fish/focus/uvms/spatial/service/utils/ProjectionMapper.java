package fish.focus.uvms.spatial.service.utils;

import fish.focus.uvms.spatial.service.dto.ProjectionDto;
import fish.focus.uvms.spatial.service.entity.ProjectionEntity;
import java.util.ArrayList;
import java.util.List;

public class ProjectionMapper {

    public static List<ProjectionDto> mapToProjectionDto(List<ProjectionEntity> entityList){
        List<ProjectionDto> dtoList = new ArrayList<>();
        for (ProjectionEntity entity:entityList) {
            dtoList.add(new ProjectionDto(entity.getId(), entity.getName(), entity.getSrsCode(), entity.getUnits(), entity.getFormats(), entity. getWorld(), entity.getExtent()));
        }

        return dtoList;
    }
}
