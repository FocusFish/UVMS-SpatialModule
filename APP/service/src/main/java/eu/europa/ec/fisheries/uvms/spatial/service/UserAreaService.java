package eu.europa.ec.fisheries.uvms.spatial.service;

import java.util.Date;
import java.util.List;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.UserAreaGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;

public interface UserAreaService {

	UserAreaLayerDto getUserAreaLayerDefination(String userName, String scopeName);

	List<AreaDetails> getUserAreaDetailsWithExtentById(AreaTypeEntry areaTypeEntry, String userName, boolean isPowerUser, String scopeName) throws ServiceException;

	List<AreaDetails> getUserAreaDetailsById(AreaTypeEntry areaTypeEntry, String userName, boolean isPowerUser, String scopeName) throws ServiceException;

	List<UserAreaDto> searchUserAreasByCriteria(String userName, String scopeName, String searchCriteria, boolean isPowerUser) throws ServiceException;

	List<UserAreaGeoJsonDto> searchUserAreasByType(String userName, String scopeName, String type, boolean isPowerUser) throws ServiceException;

	Long storeUserArea(UserAreaGeoJsonDto userAreaDto, String userName) throws ServiceException;

    Long updateUserArea(UserAreaGeoJsonDto userAreaDto, String userName, boolean isPowerUser, String scopeName) throws ServiceException;

	void deleteUserArea(Long userAreaId, String userName, boolean isPowerUser, String scopeName) throws ServiceException;

	List<String> getUserAreaTypes(String userName, String scopeName, boolean isPowerUser) throws ServiceException;

	/**
	 * <p>Update Start date and End date for user areas. If the user is having scope <code><B>MANAGE_ANY_USER_AREA</B></code>
	 * then all the user areas created by the user and shared user areas with the current scope of the logged in user
	 * will be modified</p>
	 *
	 * <p>If user does not have <code>MANAGE_ANY_USER_AREA</code> scope then only the user areas created by the user will be updated</p>
	 *
	 * <p><code>StartDate</code> and <code>EndDate</code> can be NULL or Empty or a Valid Date</p>
	 *
	 * @param remoteUser User Name
	 * @param startDate Start Date
	 * @param endDate End Date
	 * @param type Type
	 * @param isPowerUser Is User have scope <code>MANAGE_ANY_USER_AREA</code>
	 * @throws ServiceException Exception if the dates cannot be updated
	 */
	void updateUserAreaDates(String remoteUser, Date startDate, Date endDate, String type, boolean isPowerUser) throws ServiceException;
}
