package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialUtils.convertToPointInWGS84;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Point;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;

@Stateless
@Local(UserAreaService.class)
@Transactional
public class UserAreaServiceBean implements UserAreaService {
	
    @EJB
    private SpatialRepository repository;
    
    private static final String USER_NAME = "userName";
    
    private static final String SCOPE_NAME = "scopeName";
	
	public UserAreaLayerDto getUserAreaLayerDefination(String userName, String scopeName) {
		List<UserAreaLayerDto> userAreaLayerDtoList = getUserAreaLayerMapping();
		UserAreaLayerDto userAreaLayerDto = userAreaLayerDtoList.get(0);
		userAreaLayerDto.setIdList(getUserAreaGuid(userName, scopeName));
		return userAreaLayerDto;
	}
	
	public List<UserAreaDto> getUserAreaDetails(Coordinate coordinate, String userName, String scopeName) {
		Point point = convertToPointInWGS84(coordinate.getLongitude(), coordinate.getLatitude(), coordinate.getCrs());
		List<UserAreaDto> userAreaDetails = repository.findUserAreaDetails(userName, scopeName, point);
		return userAreaDetails;
	}
	
	public List<UserAreaDto> searchUserAreasByCriteria(String userName, String scopeName, String searchCriteria) {
		List<UserAreaDto> userAreaDetails = repository.findUserAreaDetailsBySearchCriteria(userName, scopeName, searchCriteria);
		return userAreaDetails;
	}	
	
	private List<UserAreaLayerDto> getUserAreaLayerMapping() {
		return repository.findUserAreaLayerMapping();
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> getUserAreaGuid(String userName, String scopeName) {
		try {
			Map<String, String> parameters = ImmutableMap.<String, String>builder().put(USER_NAME, userName).put(SCOPE_NAME, scopeName).build();
			return repository.findEntityByNamedQuery(Long.class, QueryNameConstants.FIND_GID_BY_USER, parameters);
		} catch (ServiceException e) {
			throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
		}
	}
}
