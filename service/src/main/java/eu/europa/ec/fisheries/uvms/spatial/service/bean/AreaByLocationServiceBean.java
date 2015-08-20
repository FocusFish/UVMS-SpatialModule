package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.handler.SpatialExceptionHandler;
import eu.europa.ec.fisheries.uvms.spatial.util.QueryNameConstants;
import lombok.SneakyThrows;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.valueOf;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
@Stateless
@Local(AreaByLocationService.class)
@Transactional
@Interceptors(value = ExceptionHandlerInterceptor.class)
public class AreaByLocationServiceBean extends AbstractServiceBean implements AreaByLocationService {

    private static final String SCHEMA_NAME = "spatial";
    private static final String SEPARATOR = ".";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String CRS = "crs";

    @Override
    @SneakyThrows(CommonGenericDAOException.class)
    @SpatialExceptionHandler(responseType = AreaByLocationSpatialRS.class)
    public AreaByLocationSpatialRS getAreasByLocation(double lat, double lon, int crs) {
        List<AreaTypesEntity> systemAreaTypes = commonDao.findEntityByNamedQuery(AreaTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);

        for (AreaTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            HashMap<String, String> paramaters = createParamaters(lat, lon, crs);
            String nativeQuery = "SELECT gid FROM " + SCHEMA_NAME + SEPARATOR + areaDbTable;
            List<String> resultList = commonDao.findEntityByNativeQuery(nativeQuery);
            for (String id : resultList) {
                System.out.println(id);
            }
            System.out.println("Test");
        }

        return createSuccessGetAreasByLocationResponse();
    }

    private HashMap<String, String> createParamaters(double lat, double lon, int crs) {
        HashMap<String, String> result = Maps.newHashMap();
        result.put(LAT, valueOf(lat));
        result.put(LON, valueOf(lon));
        result.put(CRS, valueOf(crs));
        return result;
    }

    private AreaByLocationSpatialRS createSuccessGetAreasByLocationResponse() {
        return new AreaByLocationSpatialRS(createSuccessResponseMessage(), null);
    }

}
