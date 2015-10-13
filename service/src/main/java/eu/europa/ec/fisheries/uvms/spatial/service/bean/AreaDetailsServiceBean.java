package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static eu.europa.ec.fisheries.uvms.spatial.util.ColumnAliasNameHelper.getFieldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import org.apache.commons.lang3.NotImplementedException;

import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Local(AreaDetailsService.class)
@Transactional
@Slf4j
public class AreaDetailsServiceBean extends SpatialServiceBean implements AreaDetailsService {

    @Override
    public AreaDetails getAreaDetails(AreaDetailsSpatialRequest request) {
    	return getAreaDetailsById(request.getAreaType());
    }
    
    @Override
    public AreaDetails getAreaDetailsById(AreaTypeEntry request) {
    	AreaLocationTypesEntity areaType = getAreaLocationType(request.getAreaType());
    	if (areaType.getIsSystemWide()) {
    		return getSystemAreaDetails(request, areaType);
    	} else {
    		// TODO Get area details for custom areas and User Areas
    		throw new NotImplementedException("Not implemented");
    	} 
    }    
    
    @Override
    public List<AreaDetails> getAreaDetailsByLocation(AreaTypeEntry request) {
    	AreaLocationTypesEntity areaType = getAreaLocationType(request.getAreaType());
    	if (areaType.getIsSystemWide()) {
    		List allAreas = getAllAreaByCoordinates(request, areaType);
    		return getAllAreaDetails(allAreas, request);
    	} else {
    		// TODO Get area details for custom areas and User Areas
    		throw new NotImplementedException("Not implemented");
    	}
    }
    
    private List<AreaDetails> getAllAreaDetails(List allAreas, AreaTypeEntry request) {
    	List<AreaDetails> areaDetailsList = new ArrayList<AreaDetails>();
		for (int i = 0 ; i < allAreas.size() ; i ++) {
			Map<String, String> properties = getFieldMap(allAreas.get(i));
			areaDetailsList.add(createAreaDetailsSpatialResponse(properties, request));
		}
		return areaDetailsList;
	}

	private AreaDetails getSystemAreaDetails(AreaTypeEntry request, AreaLocationTypesEntity areaType) {
		validateId(request.getId());
    	Map<String, String> properties = getAreaLocationDetailsById(Integer.parseInt(request.getId()), areaType);
    	return createAreaDetailsSpatialResponse(properties, request);
    }

    private AreaDetails createAreaDetailsSpatialResponse(Map<String, String> properties, AreaTypeEntry areaType) {
        List<AreaProperty> areaProperties = new ArrayList<AreaProperty>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            AreaProperty areaProperty = new AreaProperty();
            areaProperty.setPropertyName(entry.getKey());
            areaProperty.setPropertyValue(entry.getValue());
            areaProperties.add(areaProperty);
        }

        AreaDetails areaDetails = new AreaDetails();
        areaDetails.setAreaType(areaType);
        areaDetails.getAreaProperties().addAll(areaProperties);
        return areaDetails;
    }
}
