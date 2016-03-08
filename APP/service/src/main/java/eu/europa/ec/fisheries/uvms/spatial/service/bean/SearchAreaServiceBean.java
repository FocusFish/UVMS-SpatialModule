package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum.getNamedQueryByType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.FilterAreasDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Stateless
@Local(SearchAreaService.class)
@Slf4j
public class SearchAreaServiceBean implements SearchAreaService {
	
	private static final String GID = "gid";
    private static final String TYPE_NAME = "typeName";
    private static final String AREA_TYPE = "areaType";
	
    @EJB
    private SpatialRepository repository;
    
	@Override
    @Transactional
    public List<Map<String, String>> getAreasByFilter(String areaType, String filter) {
		validateAreaType(areaType);
		List<Map<String, String>> mapList = repository.findAreaByFilter(areaType, filter);
		return updateAreaType(mapList, areaType);
	}

    @Override
    @Transactional
    public List<Map<String, String>> getSelectedAreaColumns(List<AreaTypeEntry> areaTypes) {
		List<Map<String, String>> columnMapList = new ArrayList<Map<String, String>>();
		for(AreaTypeEntry areaTypeEntry : areaTypes) {
			validateArea(areaTypeEntry.getAreaType().value(), areaTypeEntry.getId());
			columnMapList.add(getSelectedColumnMap(areaTypeEntry.getAreaType().value(), areaTypeEntry.getId()));
		}
		return columnMapList;
	}
	
	private List<Map<String, String>> updateAreaType(List<Map<String, String>> mapList, String areaType) {
		for (int i=0 ; i<mapList.size() ; i++) {
			Map<String, String> tempMap = mapList.get(i);
			tempMap.put(AREA_TYPE, areaType.toUpperCase());
			mapList.set(i, tempMap);
		}
		return mapList;
	}
	
	private Map<String, String> getSelectedColumnMap(String areaType, String gid) {
		String namedQuery = getNamedQueryByType(areaType);
		Map<String, String> columnMap = getFirstMap(repository.findSelectedAreaColumns(namedQuery, Long.parseLong(gid)));
		columnMap.put(GID, gid);
		columnMap.put(AREA_TYPE, areaType.toUpperCase());
		return columnMap;
	}
	
	private Map<String, String> getFirstMap(List<Map<String, String>> listMap) {
		if (listMap.isEmpty()) {
			throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND);
		}
		return listMap.get(0);
	}
	
	private void validateArea(String areaType, String gid) {
		validateAreaType(areaType);
		validateId(gid);
	}

    protected void validateId(String id) {
        if (!StringUtils.isNumeric(id)) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, id);
        }
    }

    @SneakyThrows
    private AreaLocationTypesEntity getAreaLocationType(String type) {
        Map<String, String> parameters = ImmutableMap.<String, String>builder().put(TYPE_NAME, type.toUpperCase()).build();
        List<AreaLocationTypesEntity> areasLocationTypes = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_TYPE_BY_NAME, parameters, 1);
        if (areasLocationTypes.isEmpty()) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_LOCATION_TYPE, areasLocationTypes);
        }
        return areasLocationTypes.get(0);
    }
	private void validateAreaType(String areaType) {
		getAreaLocationType(areaType);
	}
}
