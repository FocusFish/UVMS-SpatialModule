package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreasByLocationType;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.util.ModelUtils.createSuccessResponseMessage;
import static eu.europa.ec.fisheries.uvms.util.SpatialUtils.convertToPointInWGS84;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
@Stateless
@Local(AreaByLocationService.class)
@Transactional
public class AreaByLocationServiceBean implements AreaByLocationService {

    @EJB
    private SpatialRepository repository;

    @EJB
    private CrudService crudService;

    @Override
    @SpatialExceptionHandler(responseType = AreaByLocationSpatialRS.class)
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public AreaByLocationSpatialRS getAreasByLocation(AreaByLocationSpatialRQ request) {
        List<AreaLocationTypesEntity> systemAreaTypes = getAreaTypes();

        Point point = convertToPointInWGS84(request.getPoint());

        List<AreaTypeEntry> areaTypes = Lists.newArrayList();
        for (AreaLocationTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();

            List<Integer> resultList = repository.findAreasIdByLocation(point, areaDbTable);
            for (Integer id : resultList) {
                AreaTypeEntry area = new AreaTypeEntry(String.valueOf(id), areaTypeName);
                areaTypes.add(area);
            }
        }

        return createSuccessGetAreasByLocationResponse(new AreasByLocationType(areaTypes));
    }


    @Override
    public List<AreaDto> getAreasByLocationRest(double lat, double lon, int crs) {
        List<AreaLocationTypesEntity> systemAreaTypes = getAreaTypes();

        Point point = convertToPointInWGS84(lon, lat, crs);

        List<AreaDto> areaTypes = Lists.newArrayList();
        for (AreaLocationTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();

            List<Integer> resultList = repository.findAreasIdByLocation(point, areaDbTable);
            for (Integer id : resultList) {
                AreaDto areaDto = new AreaDto(String.valueOf(id), areaTypeName);
                areaTypes.add(areaDto);
            }
        }

        return areaTypes;
    }

    private List<AreaLocationTypesEntity> getAreaTypes() {
        return crudService.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);
    }

    private AreaByLocationSpatialRS createSuccessGetAreasByLocationResponse(AreasByLocationType areasByLocation) {
        return new AreaByLocationSpatialRS(createSuccessResponseMessage(), areasByLocation);
    }

}
