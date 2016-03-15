package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialUtils.convertToPointInWGS84;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Stateless
@Local(AreaService.class)
@Transactional
@Slf4j
public class AreaServiceBean implements AreaService {

    @EJB
    private SpatialRepository repository;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<Area> getClosestAreas(ClosestAreaSpatialRQ request) {
        Point point = convertToPointInWGS84(request.getPoint());
        MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(request.getUnit().name());

        Map<String, String> areaType2TableName = getAreaType2TableNameMap();
        List<Area> closestAreas = newArrayList();
        for (AreaType areaType : request.getAreaTypes().getAreaTypes()) {
            if (areaType!= null) {
                String areaDbTable = areaType2TableName.get(areaType.value());

                List<ClosestAreaDto> closestAreaList = repository.findClosestArea(point, measurementUnit, areaDbTable);
                validateResponse(closestAreaList);

                ClosestAreaDto closestAreaDto = closestAreaList.get(0);
                if (closestAreaDto != null) {
                    Area closestAreaEntry = new Area();
                    closestAreaEntry.setId(closestAreaDto.getId());
                    closestAreaEntry.setDistance(closestAreaDto.getDistance());
                    closestAreaEntry.setUnit(request.getUnit());
                    closestAreaEntry.setCode(closestAreaDto.getCode());
                    closestAreaEntry.setName(closestAreaDto.getName());
                    closestAreaEntry.setAreaType(areaType);
                    closestAreas.add(closestAreaEntry);
                }
            }
        }

        return closestAreas;
    }

    @Override
    public List<ClosestAreaDto> getClosestAreas(double lat, double lon, int crs, String unit, List<String> types) {
        Point point = SpatialUtils.convertToPointInWGS84(lon, lat, crs);
        MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(unit);

        List<String> areaTypes = SpatialUtils.toUpperCase(types);
        Map<String, String> areaType2TableName = getAreaType2TableNameMap();
        List<ClosestAreaDto> closestAreas = newArrayList();
        for (String areaType : areaTypes) {
            String areaDbTable = areaType2TableName.get(areaType);
            validateAreaType(areaType, areaDbTable);

            List<ClosestAreaDto> closestAreaList = repository.findClosestArea(point, measurementUnit, areaDbTable);
            validateResponse(closestAreaList);

            if (isNotEmpty(closestAreaList)) {
                ClosestAreaDto closestAreaDto = closestAreaList.get(0);
                if (closestAreaDto != null) {
                    closestAreaDto.setAreaType(areaType);
                    closestAreaDto.setUnit(measurementUnit.name());
                    closestAreas.add(closestAreaDto);
                }
            }
        }

        return closestAreas;
    }

    private void validateAreaType(String areaType, String areaDbTable) {
        if (areaDbTable == null) {
            throw new SpatialServiceException(SpatialServiceErrors.WRONG_AREA_TYPE, areaType);
        }
    }

    @SneakyThrows
    private Map<String, String> getAreaType2TableNameMap() {
        List<AreaLocationTypesEntity> areas = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_ALL_AREAS);
        Map<String, String> areaMap = Maps.newHashMap();
        for (AreaLocationTypesEntity area : areas) {
            areaMap.put(area.getTypeName().toUpperCase(), area.getAreaDbTable());
        }
        return areaMap;
    }

    private void validateResponse(List<ClosestAreaDto> closestAreaList) {
        if (closestAreaList.size() > 1) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

}