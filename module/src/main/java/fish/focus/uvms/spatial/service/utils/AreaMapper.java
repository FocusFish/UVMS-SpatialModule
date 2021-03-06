package fish.focus.uvms.spatial.service.utils;

import fish.focus.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import fish.focus.uvms.spatial.model.schemas.AreaSimpleType;
import fish.focus.uvms.spatial.model.schemas.AreaType;
import fish.focus.uvms.spatial.service.dto.BaseAreaDto;
import fish.focus.uvms.spatial.service.entity.BaseAreaEntity;
import fish.focus.uvms.spatial.service.entity.UserAreasEntity;
import java.util.ArrayList;
import java.util.List;

public class AreaMapper {

    public static List<AreaSimpleType> mapToAreaSimpleType(List<? extends BaseAreaEntity> baseList, AreaType areaType){
        List<AreaSimpleType> responseList = new ArrayList<>();
        for (BaseAreaEntity area: baseList) {
            AreaSimpleType areaSimpleType = new AreaSimpleType(areaType.value(), area.getCode(), area.getGeometryWKT());
            responseList.add(areaSimpleType);
        }
        return responseList;
    }


    public static List<AreaExtendedIdentifierType> mapToAreaExtendedIdentifierType(List<BaseAreaDto> areaList){
        List<AreaExtendedIdentifierType> response = new ArrayList<>();

        for (BaseAreaDto area: areaList) {
            AreaExtendedIdentifierType areaExtendedIdentifierType = new AreaExtendedIdentifierType(String.valueOf(area.getGid()), area.getType(), area.getCode(), area.getName());
            response.add(areaExtendedIdentifierType);
        }
        return response;
    }

    public static List<BaseAreaDto> mapToBaseAreaDtoListFromUserAreas(List<UserAreasEntity> entityList){
        List<BaseAreaDto> dtoList = new ArrayList<>();
        for (UserAreasEntity entity: entityList) {
            dtoList.add(mapToBaseAreaDtoFromUserArea(entity));
        }
        return dtoList;
    }

    public static BaseAreaDto mapToBaseAreaDtoFromUserArea(UserAreasEntity entity){
        BaseAreaDto dto = new BaseAreaDto(entity.getType().name(), entity.getId(), entity.getCode(), entity.getName());
        return dto;
    }

    public static List<BaseAreaDto> mapToBaseAreaDtoList(List<? extends BaseAreaEntity> entityList){
        List<BaseAreaDto> dtoList = new ArrayList<>();
        for (BaseAreaEntity entity: entityList) {
            dtoList.add(mapToBaseAreaDto(entity));
        }
        return dtoList;
    }

    public static BaseAreaDto mapToBaseAreaDto(BaseAreaEntity entity){
        BaseAreaDto dto = new BaseAreaDto();
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setGeometryWKT(entity.getGeometryWKT());


        return dto;
    }

}
