package eu.europa.ec.fisheries.uvms.spatial.service;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.SpatialFunction;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.BookmarkEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.config.SysConfigEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.bookmark.Bookmark;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import org.opengis.feature.Property;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SpatialRepository {

    List<AreaLayerDto> findSystemAreaLayerMapping();

    List<AreaLayerDto> findSystemAreaAndLocationLayerMapping();

    List<UserAreaLayerDto> findUserAreaLayerMapping();

    List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, Number gid);

    List<Map<String, String>> findAllCountriesDesc();

    EezEntity getEezById(Long id) throws ServiceException;

    List<ProjectionDto> findProjectionByMap(long reportId);

    List<ProjectionDto> findProjectionById(Long id);

    List<ReportConnectServiceAreasEntity> findReportConnectServiceAreas(long reportId);

    List<ServiceLayerEntity> findServiceLayerEntityByIds(List<Long> ids);

    ReportConnectSpatialEntity findReportConnectSpatialByReportId(Long reportId) throws ServiceException;

    List<ReportConnectSpatialEntity> findReportConnectSpatialByConnectId(final Long id) throws ServiceException;

    boolean saveOrUpdateMapConfiguration(ReportConnectSpatialEntity mapConfiguration) throws ServiceException;

    void updateSystemConfigs(List<SysConfigEntity> sysConfigs);

    void updateSystemConfig(String name, String value) throws ServiceException;

    String findSystemConfigByName(String name) throws ServiceException;

    List<SysConfigEntity> findSystemConfigs();

    List<ServiceLayerDto> findServiceLayerBySubType(List<String> subAreaTypes, boolean isWithBing);

    List<AreaDto> getAllUserAreas(String userName, String scopeName);

    List<AreaDto> getAllUserAreaGroupNames(String userName, String scopeName);

    List<Long> getAllSharedGids(String userName, String scopeName, String type);

    List<AreaDto> findAllUserAreasByGids(List<Long> gids);

    UserAreasEntity findUserAreaById(Long userAreaId, String userName, Boolean isPowerUser, String scopeName) throws ServiceException;

    PortAreasEntity findPortAreaById(Long id) throws ServiceException;

    BookmarkEntity create(BookmarkEntity bookmark) throws ServiceException;

    Set<Bookmark> listBookmarksBy(String userName) throws ServiceException;

    void deleteBookmark(Long id) throws ServiceException;

    BookmarkEntity getBookmarkBy(Long id) throws ServiceException;

    List<ProjectionEntity> findProjection(Integer srsCode) throws ServiceException;

    List<ProjectionEntity> findProjection() throws ServiceException;

    void deleteReportConnectServiceAreas(Long id);

    void deleteReportConnectServiceAreas(Set<ReportConnectServiceAreasEntity> reportConnectServiceAreas);

    ReportConnectSpatialEntity findReportConnectSpatialByReportIdAndConnectId(Long reportId, Long id) throws ServiceException;

    List<EezEntity> findEezByIntersect(Point point) throws ServiceException;

    List<PortAreasEntity> findPortAreaByIntersect(Point point) throws ServiceException;

    List<RfmoEntity> findRfmoByIntersect(Point point) throws ServiceException;

    List<UserAreasEntity> findUserAreaByIntersect(Point point) throws ServiceException;

    List<AreaLocationTypesEntity> findAreaLocationTypeByTypeName(String typeName) throws ServiceException;

    List<AreaLocationTypesEntity> findAllIsPointIsSystemWide(Boolean isLocation, Boolean isSystemWide) throws ServiceException;

    List<AreaLocationTypesEntity> findAllIsLocation(Boolean isLocation) throws ServiceException;

    ServiceLayerEntity getServiceLayerBy(String locationType) throws ServiceException;

    ServiceLayerEntity getServiceLayerBy(Long id) throws ServiceException;

    ServiceLayerEntity getByAreaLocationType(String areaLocationType) throws ServiceException;

    List<UserAreasEntity> findUserAreasByType(String userName, String scopeName, String type, boolean isPowerUser) throws ServiceException;

    List<UserAreasEntity> findUserArea(String userName, String scopeName, boolean isPowerUser) throws ServiceException;

    List<UserAreasEntity> listUserAreaByCriteria(String userName, String scopeName, String searchCriteria, boolean isPowerUser) throws ServiceException;

    List<UserAreasEntity> findUserAreaDetailsByLocation(String userName, Point point) throws ServiceException;

    UserAreasEntity getUserAreaByUserNameAndName(String userName, String name) throws ServiceException;

    UserAreasEntity save(UserAreasEntity userAreasEntity) throws ServiceException;

    UserAreasEntity update(UserAreasEntity userAreasEntity) throws ServiceException;

    PortEntity create(PortEntity portsEntity) throws ServiceException;

    EezEntity create(EezEntity eezEntity) throws ServiceException;

    void deleteEntity(ReportConnectSpatialEntity entity);

    PortAreasEntity create(PortAreasEntity portAreasEntity) throws ServiceException;

    List closestArea(final List<AreaLocationTypesEntity> entities, final SpatialFunction spatialFunction, final Point point);

    List intersectingArea(final List<AreaLocationTypesEntity> entities, final SpatialFunction spatialFunction, final Point point);

    List<AreaLocationTypesEntity> listAllArea() throws ServiceException;

    List<AreaLocationTypesEntity> listAllAreaAndLocation() throws ServiceException;

    void deleteUserArea(UserAreasEntity userAreaById);

    PortAreasEntity update(PortAreasEntity persistentPortArea) throws ServiceException;

    RfmoEntity create(RfmoEntity rfmoEntity) throws ServiceException;

    List<UserAreasEntity> findUserAreaByUserNameAndScopeName(String userName, String scopeName) throws ServiceException;

    void replaceEez(Map<String, List<Property>> features) throws ServiceException;

    void replaceRfmo(Map<String, List<Property>> features) throws ServiceException;

    void replacePort(Map<String, List<Property>> features) throws ServiceException;

    void replacePortArea(Map<String, List<Property>> features) throws ServiceException;

    BaseAreaEntity findEezById(Long id) throws ServiceException;

    BaseAreaEntity findPortById(Long id) throws ServiceException;

    BaseAreaEntity findRfmoById(Long id) throws ServiceException;

    BaseAreaEntity findUserAreaById(Long id) throws ServiceException;

}