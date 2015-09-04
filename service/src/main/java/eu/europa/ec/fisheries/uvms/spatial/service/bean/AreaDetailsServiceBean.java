package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.*;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
import eu.europa.ec.fisheries.uvms.util.ColumnAliasNameHelper;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static eu.europa.ec.fisheries.uvms.util.ModelUtils.createSuccessResponseMessage;

@Stateless
@Local(AreaDetailsService.class)
@Transactional
public class AreaDetailsServiceBean implements AreaDetailsService {

    private static Logger LOG = LoggerFactory.getLogger(AreaDetailsServiceBean.class.getName());

    private ImmutableMap<String, Class> entityMap = ImmutableMap.<String, Class>builder()
            .put(AreaType.EEZ.value(), EezEntity.class)
            .put(AreaType.RFMO.value(), RfmoEntity.class)
            .put(AreaType.COUNTRY.value(), CountriesEntity.class)
            .put(AreaType.FAO.value(), FaoEntity.class)
            .put(AreaType.GFCM.value(), GfcmEntity.class)
            .put(AreaType.RAC.value(), RacEntity.class)
            .put(AreaType.S_TAT_RECT.value(), StatRectEntity.class)
            .build();

    @EJB
    private CrudService crudService;

    @SuppressWarnings("unchecked")
    @Override
    @SpatialExceptionHandler(responseType = AreaDetailsSpatialResponse.class)
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public AreaDetailsSpatialResponse getAreaDetails(AreaDetailsSpatialRequest request) {
        AreaDetailsSpatialResponse response = null;
        String areaTypeName = request.getAreaType().getAreaType();
        LOG.info("Area Type name received : " + areaTypeName);
        Map<String, String> parameters = newHashMap();
        parameters.put("areaDbTable", areaTypeName);
        List<AreaTypesEntity> areasTypes = crudService.findEntityByNamedQuery(AreaTypesEntity.class, QueryNameConstants.FIND_AREAS_BY_ID, parameters, 1);
        if (!areasTypes.isEmpty()) {
            if (isSystemAreaType(areaTypeName)) {
                Map<String, String> properties = getSystemAreaDetails(areasTypes.get(0), request.getAreaType().getId());
                response = createAreaDetailsSpatialResponse(properties, request);
            } else {
                // TODO handle user areas
                throw new NotImplementedException("Not yet implemented");
            }
        } else {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_TYPE, areaTypeName);
        }
        return response;
    }

    private boolean isSystemAreaType(String areaTypeName) {
        for (String key : entityMap.keySet()) {
            if (key.equalsIgnoreCase(areaTypeName)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getSystemAreaDetails(AreaTypesEntity areaTypeEntity, String id) {
        Map<String, String> properties = newHashMap();
        LOG.info("Area Type entity to be retrieved : " + areaTypeEntity.getTypeName());
        Class entityClass = entityMap.get(areaTypeEntity.getTypeName());
        if (entityClass != null) {
            Object object = crudService.findEntityById(entityClass, Integer.parseInt(id));
            if (object != null) {
                properties = ColumnAliasNameHelper.getFieldMap(object);
            } else {
                throw new SpatialServiceException(SpatialServiceErrors.AREA_NOT_FOUND, areaTypeEntity.getTypeName());
            }
        } else {
            throw new SpatialServiceException(SpatialServiceErrors.AREA_NOT_FOUND, areaTypeEntity.getTypeName());
        }
        return properties;
    }

    private AreaDetailsSpatialResponse createAreaDetailsSpatialResponse(Map<String, String> properties, AreaDetailsSpatialRequest request) {
        List<AreaProperty> areaProperties = new ArrayList<AreaProperty>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            areaProperties.add(new AreaProperty(entry.getKey(), entry.getValue()));
        }
        AreaDetails areaDetails = new AreaDetails();
        areaDetails.setAreaType(request.getAreaType());
        areaDetails.setAreaProperty(areaProperties);
        AreaDetailsSpatialResponse response = new AreaDetailsSpatialResponse();
        response.setAreaDetails(areaDetails);
        response.setResponseMessage(createSuccessResponseMessage());
        return response;
    }

}
