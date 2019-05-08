package eu.europa.ec.fisheries.uvms.spatial.service.Service2.utils;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaSimpleType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.BaseAreaEntity2;

import java.util.ArrayList;
import java.util.List;

public class AreaSimpleTypeMapper {

    public static List<AreaSimpleType> mapToAreaSimpleType(List<? extends BaseAreaEntity2> baseList, AreaType areaType){
        List<AreaSimpleType> responseList = new ArrayList<>();
        for (BaseAreaEntity2 area: baseList) {
            AreaSimpleType areaSimpleType = new AreaSimpleType(areaType.value(), area.getCode(), area.getGeometry());
            responseList.add(areaSimpleType);
        }
        return responseList;
    }

}
