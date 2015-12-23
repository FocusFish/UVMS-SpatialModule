package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.mapper.ReportConnectSpatialMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ConfigurationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.LayersDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.SystemSettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.VisibilitySettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.MapConfigMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.ProjectionMapper;
import eu.europa.ec.fisheries.uvms.spatial.validator.SpatialValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.spatial.service.mapper.ConfigurationMapper.*;

@Stateless
@Local(MapConfigService.class)
@Transactional
@Slf4j
public class MapConfigServiceBean implements MapConfigService {

    private static final String SCALE = "scale";

    private static final String MOUSECOORDS = "mousecoords";

    private static final String NAME = "name";

    private static final String GEO_SERVER = "geo_server_url";

    private static final Integer DEFAULT_EPSG = 3857;

    @EJB
    private SpatialRepository repository;

    @Override
    @SneakyThrows
    public List<ProjectionDto> getAllProjections() {

        List<ProjectionEntity> projections = repository.findAllEntity(ProjectionEntity.class); // TODO projectionDAO

        return ProjectionMapper.INSTANCE.projectionEntityListToProjectionDtoList(projections);

    }

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW) // annotation required to send error response
    public void handleDeleteMapConfiguration(SpatialDeleteMapConfigurationRQ request) throws ServiceException {
        SpatialValidator.validate(request);

        repository.deleteBy(request.getSpatialConnectIds());
    }

    @Override
    public MapConfigurationType getMapConfigurationType(final Long reportId) throws ServiceException {
        SpatialValidator.validate(reportId);

        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialBy(reportId);

        return ReportConnectSpatialMapper.INSTANCE.reportConnectSpatialEntityToMapConfigurationType(entity);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public SpatialGetMapConfigurationRS getMapConfiguration(SpatialGetMapConfigurationRQ mapConfigurationRQ) throws ServiceException {
        long reportId = mapConfigurationRQ.getReportId();

        return new SpatialGetMapConfigurationRS(getMapConfigurationType(reportId));

    }

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public SpatialSaveOrUpdateMapConfigurationRS handleSaveOrUpdateSpatialMapConfiguration(final SpatialSaveOrUpdateMapConfigurationRQ request) {
        SpatialValidator.validate(request);

        ReportConnectSpatialEntity entity =
                ReportConnectSpatialMapper.INSTANCE.mapConfigurationTypeToReportConnectSpatialEntity(request.getMapConfiguration());

        repository.saveOrUpdateMapConfiguration(entity);
        return createSaveOrUpdateMapConfigurationResponse();

    }

    private SpatialSaveOrUpdateMapConfigurationRS createSaveOrUpdateMapConfigurationResponse() {
        return new SpatialSaveOrUpdateMapConfigurationRS();
    }

    @Override
    @SneakyThrows
    public ConfigurationDto retrieveAdminConfiguration(String config) {
        ConfigurationDto configurationDto = getAdminConfiguration(config);
        configurationDto.setSystemSettings(getConfigSystemSettings());
        return configurationDto;
    }

    @Override
    @SneakyThrows
    public ConfigurationDto retrieveUserConfiguration(String config, String defaultConfig) {
        ConfigurationDto userConfig = getUserConfiguration(config);
        ConfigurationDto adminConfig = getAdminConfiguration(defaultConfig);
        return mergeUserConfiguration(adminConfig, userConfig);
    }

    @Override
    @SneakyThrows
    public String saveAdminJson(ConfigurationDto configurationDto, String defaultConfig) {
        ConfigurationDto defaultConfigurationDto = retrieveAdminConfiguration(defaultConfig);
        setConfigSystemSettings(configurationDto.getSystemSettings(), defaultConfigurationDto.getSystemSettings()); // Update system config in spatial DB
        configurationDto.setLayerSettings(defaultConfigurationDto.getLayerSettings()); // TODO fix layer settings, currently fixed value
        configurationDto.setSystemSettings(null); // Not saving system settings in USM
        return getJson(configurationDto);
    }

    @Override
    @SneakyThrows
    public String saveUserJson(ConfigurationDto configurationDto, String userPref) {
        if(configurationDto == null) {
            throw new ServiceException("Invalid JSON");
        }
        ConfigurationDto userConfig = getUserConfiguration(userPref);
        return getJson(mergeConfiguration(configurationDto, userConfig));
    }

    @Override
    @SneakyThrows
    public String resetUserJson(ConfigurationDto configurationDto, String userPref) {
        if(configurationDto == null) {
            throw new ServiceException("Invalid JSON");
        }
        ConfigurationDto userConfig = getUserConfiguration(userPref);
        return getJson(resetUserConfiguration(configurationDto, userConfig));
    }

    @Override
    @SneakyThrows
    public ConfigurationDto getNodeDefaultValue(ConfigurationDto configurationDto, String adminConfig) {
        if(configurationDto == null || adminConfig == null) {
            throw new ServiceException("Invalid JSON");
        }
        return getDefaultNodeConfiguration(configurationDto, getAdminConfiguration(adminConfig));
    }

    @Override
    @SneakyThrows
    public ConfigDto getReportConfigWithoutMap(String userPref, String adminPref) {
        ConfigDto configDto = mergeNoMapConfiguration(getUserConfiguration(userPref), getAdminConfiguration(adminPref)); //Returns merged config object between Admin and User configuration from USM
        return configDto;
    }

    @Override
    @SneakyThrows
    public MapConfigDto getReportConfig(int reportId, String userPreferences, String adminPreferences) {
        ConfigurationDto configurationDto = mergeConfiguration(getUserConfiguration(userPreferences), getAdminConfiguration(adminPreferences)); //Returns merged config object between Admin and User configuration from USM
        return new MapConfigDto(getMap(configurationDto, reportId), getVectorStyles(configurationDto), getVisibilitySettings(configurationDto));
    }

    private MapDto getMap(ConfigurationDto configurationDto, int reportId) throws ServiceException {
        ProjectionDto projection = getMapProjection(reportId, configurationDto);
        List<ControlDto> controls = getControls(reportId, configurationDto);
        List<TbControlDto> tbControls = getTbControls(configurationDto);
        List<LayerDto> layers = getServiceAreaLayer(reportId, configurationDto, projection);
        return new MapDto(projection, controls, tbControls, layers);
    }

    private ProjectionDto getMapProjection(int reportId, ConfigurationDto configurationDto) {
        List<ProjectionDto> projectionDtoList = repository.findProjectionByMap(reportId);
        if (projectionDtoList != null && !projectionDtoList.isEmpty()) { // Get Map Projection for report
            return projectionDtoList.get(0);
        } else { // If not available use Map Projection configured in USM
            int projectionId = configurationDto.getMapSettings().getMapProjectionId();
            return getProjection(projectionId);
        }
    }

    private List<ControlDto> getControls(int reportId, ConfigurationDto configurationDto) throws ServiceException {
        List<ControlDto> controls = MapConfigMapper.INSTANCE.getControls(configurationDto.getToolSettings().getControl());
        DisplayProjectionDto displayProjection = getDisplayProjection(reportId, configurationDto);
        return updateControls(controls, displayProjection.getUnits().value(),
                displayProjection.getEpsgCode(), displayProjection.getFormats().value());
    }

    private ProjectionDto getProjection(Integer id) {
        List<ProjectionDto> projectionDtoList = repository.findProjectionById(id.longValue());
        return (projectionDtoList != null && !projectionDtoList.isEmpty()) ? projectionDtoList.get(0) : null;
    }

    private List<TbControlDto> getTbControls(ConfigurationDto configurationDto) {
        return MapConfigMapper.INSTANCE.getTbControls(configurationDto.getToolSettings().getTbControl());
    }

    private List<LayerDto> getServiceAreaLayer(int reportId, ConfigurationDto configurationDto, ProjectionDto projection) throws ServiceException {
        List<ReportConnectServiceAreasEntity> reportConnectServiceAreas = getReportConnectServiceAreas(reportId, projection);
        String geoServerUrl = getGeoServerUrl();
        if (reportConnectServiceAreas != null && !reportConnectServiceAreas.isEmpty()) { // If report is having service layer then return it
            List<LayerDto> layerDtos = new ArrayList<LayerDto>();
            for (ReportConnectServiceAreasEntity reportConnectServiceArea : reportConnectServiceAreas) {
                layerDtos.add(reportConnectServiceArea.convertToServiceLayer(geoServerUrl));
            }
            return layerDtos;
        } else { // otherwise get the default layer configuration from USM
            return getServiceAreaLayersFromConfig(configurationDto, geoServerUrl, projection);
        }
    }

    private VectorStylesDto getVectorStyles(ConfigurationDto configurationDto) {
        return MapConfigMapper.INSTANCE.getStyleDtos(configurationDto.getStylesSettings());
    }

    private VisibilitySettingsDto getVisibilitySettings(ConfigurationDto configurationDto) {
        return configurationDto.getVisibilitySettings();
    }

    private List<LayerDto> getServiceAreaLayersFromConfig(ConfigurationDto configurationDto, String geoServerUrl, ProjectionDto projection) throws ServiceException {
        List<LayersDto> overlayLayers = configurationDto.getLayerSettings().getOverlayLayers(); // Get Service Layers for Overlay layers
        List<Integer> overlayServiceLayerIds = getServiceLayerIds(overlayLayers);
        List<ServiceLayerEntity> overlayServiceLayerEntities = sort(getServiceLayers(overlayServiceLayerIds, projection), overlayServiceLayerIds);
        List<LayerDto> layerDtos = getLayerDtos(overlayServiceLayerEntities, geoServerUrl, false);

        List<LayersDto> baseLayers = configurationDto.getLayerSettings().getBaseLayers(); // Get Service Layers for base layers
        List<Integer> baseServiceLayerIds = getServiceLayerIds(baseLayers);
        List<ServiceLayerEntity> baseServiceLayerEntities = sort(getServiceLayers(baseServiceLayerIds, projection), baseServiceLayerIds);
        layerDtos.addAll(getLayerDtos(baseServiceLayerEntities, geoServerUrl, true));
        return layerDtos;
    }

    private List<ServiceLayerEntity> sort(List<ServiceLayerEntity> overlayServiceLayerEntities, List<Integer> ids) {
        List<ServiceLayerEntity> tempList = new ArrayList<ServiceLayerEntity>();
        for(Integer id : ids) {
            for(ServiceLayerEntity serviceLayerEntity : overlayServiceLayerEntities) {
                if (id.equals(serviceLayerEntity.getId())) {
                    tempList.add(serviceLayerEntity);
                }
            }
        }
        return tempList;
    }

    private List<LayerDto> getLayerDtos(List<ServiceLayerEntity> serviceLayerEntities, String geoserverUrl, boolean isBaseLayer) {
        List<LayerDto> layerDtos = new ArrayList<LayerDto>();
        for (ServiceLayerEntity serviceLayerEntity : serviceLayerEntities) {
            layerDtos.add(serviceLayerEntity.convertToServiceLayer(geoserverUrl, isBaseLayer));
        }
        return layerDtos;
    }

    private String getGeoServerUrl() throws ServiceException {
        Map<String, String> parameters = ImmutableMap.<String, String>builder().put(NAME, GEO_SERVER).build();
        return repository.findSystemConfigByName(parameters);
    }

    private List<Integer> getServiceLayerIds(List<LayersDto> layers) {
        List<Integer> ids = new ArrayList<>();
        for (LayersDto layer : layers) {
            ids.add(Integer.parseInt(layer.getServiceLayerId()));
        }
        return ids;
    }

    private List<ReportConnectServiceAreasEntity> getReportConnectServiceAreas(int reportId, ProjectionDto projection) {
        List<ReportConnectServiceAreasEntity> reportConnectServiceAreas = repository.findReportConnectServiceAreas(reportId);
        if (reportConnectServiceAreas != null) {
            Iterator<ReportConnectServiceAreasEntity> areaIterator = reportConnectServiceAreas.iterator();
            while (areaIterator.hasNext()) {
                ReportConnectServiceAreasEntity reportConnectServiceArea = areaIterator.next();
                if (!projection.getEpsgCode().equals(DEFAULT_EPSG) && !DEFAULT_EPSG.equals(reportConnectServiceArea.getServiceLayer().getSrsCode())) {
                    areaIterator.remove();
                }
            }
            Collections.sort(reportConnectServiceAreas);

        }
        return reportConnectServiceAreas;
    }

    private List<ServiceLayerEntity> getServiceLayers(List<Integer> ids, ProjectionDto projection) {
        List<ServiceLayerEntity> serviceLayers = repository.findServiceLayerEntityByIds(ids);
        Iterator<ServiceLayerEntity> layerIterator = serviceLayers.iterator();
        while(layerIterator.hasNext()) {
            ServiceLayerEntity serviceLayer = layerIterator.next();
            if(!projection.getEpsgCode().equals(DEFAULT_EPSG) && DEFAULT_EPSG.equals(serviceLayer.getSrsCode())) {
                layerIterator.remove();
            }
        }
        return serviceLayers;
    }

    private List<ControlDto> updateControls(List<ControlDto> controls, String scaleBarUnit, int epsgCode, String coordinateFormat) {
        for (ControlDto controlDto : controls) {
            if (controlDto.getType().equalsIgnoreCase(SCALE)) {
                controlDto.setUnits(scaleBarUnit);
            }
            if (controlDto.getType().equalsIgnoreCase(MOUSECOORDS)) {
                controlDto.setEpsgCode(epsgCode);
                controlDto.setFormat(coordinateFormat);
            }
        }
        return controls;
    }

    private DisplayProjectionDto getDisplayProjection(Integer reportId, ConfigurationDto configurationDto) throws ServiceException {
        DisplayProjectionDto displayProjectionDto = new DisplayProjectionDto();
        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialBy(reportId.longValue());

        if (entity != null && entity.getProjectionByDisplayProjId() != null) { // Check value in DB
            displayProjectionDto.setEpsgCode(entity.getProjectionByDisplayProjId().getSrsCode());
        } else { // If not get from config
            ProjectionDto projection = getProjection(configurationDto.getMapSettings().getDisplayProjectionId());
            displayProjectionDto.setEpsgCode(projection.getEpsgCode());
        }

        if (entity != null && entity.getScaleBarType() != null) { // Check value in DB
            displayProjectionDto.setUnits(entity.getScaleBarType());
        } else {  // If not get from config
            displayProjectionDto.setUnits(ScaleBarUnits.fromValue(configurationDto.getMapSettings().getScaleBarUnits()));
        }

        if (entity != null && entity.getDisplayFormatType() != null) { // Check value in DB
            displayProjectionDto.setFormats(entity.getDisplayFormatType());
        } else {  // If not get from config
            displayProjectionDto.setFormats(CoordinatesFormat.fromValue(configurationDto.getMapSettings().getCoordinatesFormat()));
        }

        return displayProjectionDto;
    }

    private ConfigurationDto getAdminConfiguration(String adminPreference) throws IOException {
        return (adminPreference == null || adminPreference.isEmpty()) ? new ConfigurationDto() : getConfiguration(adminPreference);
    }

    private ConfigurationDto getUserConfiguration(String userPreference) throws IOException {
        return (userPreference == null || userPreference.isEmpty()) ? new ConfigurationDto() : getConfiguration(userPreference);
    }

    private ConfigurationDto getConfiguration(String configString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(configString, ConfigurationDto.class);
    }

    private String getJson(ConfigurationDto config) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(config);
    }

    private SystemSettingsDto getConfigSystemSettings() throws ServiceException {
        SystemSettingsDto systemSettingsDto = new SystemSettingsDto();
        systemSettingsDto.setGeoserverUrl(getGeoServerUrl());
        return systemSettingsDto;
    }

    private void setConfigSystemSettings(SystemSettingsDto systemSettingsDto, SystemSettingsDto defaultSystemSettingsDto) throws ServiceException {
        String geoServerUrl = systemSettingsDto.getGeoserverUrl();
        String defaultGeoServerUrl = defaultSystemSettingsDto.getGeoserverUrl();
        if (geoServerUrl != null && geoServerUrl != defaultGeoServerUrl) {
            Map<String, String> parameters = ImmutableMap.<String, String>builder().put(NAME, GEO_SERVER).build();
            repository.updateSystemConfig(parameters, geoServerUrl);
        }
    }
}
