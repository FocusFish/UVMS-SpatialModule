/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package fish.focus.uvms.spatial.service.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import fish.focus.uvms.spatial.service.utils.SpatialValidator;
import fish.focus.uvms.spatial.service.utils.MapConfigHelper;
import fish.focus.uvms.spatial.service.utils.ProjectionMapper;
import fish.focus.uvms.spatial.model.schemas.CoordinatesFormat;
import fish.focus.uvms.spatial.model.schemas.MapConfigurationType;
import fish.focus.uvms.spatial.model.schemas.ScaleBarUnits;
import fish.focus.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import fish.focus.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import fish.focus.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;
import fish.focus.uvms.spatial.service.dao.AreaDao;
import fish.focus.uvms.spatial.service.dao.ProjectionDao;
import fish.focus.uvms.spatial.service.dao.ReportConnectSpatialDao;
import fish.focus.uvms.spatial.service.dao.ServiceLayerDao;
import fish.focus.uvms.spatial.service.dao.SysConfigDao;
import fish.focus.uvms.spatial.service.dto.ProjectionDto;
import fish.focus.uvms.spatial.service.dto.config.ConfigDto;
import fish.focus.uvms.spatial.service.dto.config.ControlDto;
import fish.focus.uvms.spatial.service.dto.config.DisplayProjectionDto;
import fish.focus.uvms.spatial.service.dto.config.LayerDto;
import fish.focus.uvms.spatial.service.dto.config.MapConfigDto;
import fish.focus.uvms.spatial.service.dto.config.MapDto;
import fish.focus.uvms.spatial.service.dto.config.RefreshDto;
import fish.focus.uvms.spatial.service.dto.config.ServiceLayersDto;
import fish.focus.uvms.spatial.service.dto.config.TbControlDto;
import fish.focus.uvms.spatial.service.dto.config.VectorStylesDto;
import fish.focus.uvms.spatial.service.dto.usm.ConfigurationDto;
import fish.focus.uvms.spatial.service.dto.usm.LayerAreaDto;
import fish.focus.uvms.spatial.service.dto.usm.LayerSettingsDto;
import fish.focus.uvms.spatial.service.dto.usm.LayersDto;
import fish.focus.uvms.spatial.service.dto.usm.ReferenceDataPropertiesDto;
import fish.focus.uvms.spatial.service.dto.usm.ReportProperties;
import fish.focus.uvms.spatial.service.dto.usm.StyleSettingsDto;
import fish.focus.uvms.spatial.service.dto.usm.SystemSettingsDto;
import fish.focus.uvms.spatial.service.dto.usm.VisibilitySettingsDto;
import fish.focus.uvms.spatial.service.entity.ProjectionEntity;
import fish.focus.uvms.spatial.service.entity.ReportConnectServiceAreasEntity;
import fish.focus.uvms.spatial.service.entity.ReportConnectSpatialEntity;
import fish.focus.uvms.spatial.service.entity.ServiceLayerEntity;
import fish.focus.uvms.spatial.service.entity.SysConfigEntity;
import fish.focus.uvms.spatial.service.entity.UserAreasEntity;
import fish.focus.uvms.spatial.service.enums.AreaTypeEnum;
import fish.focus.uvms.spatial.service.enums.LayerTypeEnum;
import fish.focus.uvms.spatial.service.mapper.ConfigurationMapper;
import fish.focus.uvms.spatial.service.mapper.MapConfigMapper;
import fish.focus.uvms.spatial.service.mapper.ReportConnectSpatialMapper;

@Stateless
public class MapConfigServiceBean {

    private static final String SCALE = "scale";
    private static final String MOUSECOORDS = "mousecoords";
    private static final String GEO_SERVER = "geo_server_url";
    private static final String BING_API_KEY = "bing_api_key";
    private static final String PROVIDER_FORMAT_BING = "BING";

    @Inject
    private ReportConnectSpatialDao reportConnectSpatialDao;

    @Inject
    private ProjectionDao projectionDao;

    @Inject
    private SysConfigDao sysConfigDao;

    @Inject
    private ServiceLayerDao serviceLayerDao;

    @Inject
    private AreaDao areaDao;

    public MapConfigDto getReportConfig(int reportId, String userPreferences, String adminPreferences, String userName, String scopeName, String timeStamp, Collection<String> permittedServiceLayers) {
        ConfigurationDto configurationDto = ConfigurationMapper.mergeConfiguration(MapConfigHelper.getUserConfiguration(userPreferences), MapConfigHelper.getAdminConfiguration(adminPreferences)); //Returns merged config object between Admin and User configuration from USM
        return new MapConfigDto(getMap(configurationDto, reportId, userName, scopeName, timeStamp, permittedServiceLayers),
                getVectorStyles(configurationDto, reportId),
                getVisibilitySettings(configurationDto, reportId));
    }

    public MapConfigDto getBasicReportConfig(String userPreferences, String adminPreferences) {
        ConfigurationDto configurationDto = ConfigurationMapper.mergeConfiguration(MapConfigHelper.getUserConfiguration(userPreferences), MapConfigHelper.getAdminConfiguration(adminPreferences)); //Returns merged config object between Admin and User configuration from USM
        ProjectionDto projection = getMapProjection(null, configurationDto);
        List<ControlDto> controls = getControls(null, configurationDto);
        ServiceLayersDto serviceLayersDto = new ServiceLayersDto();

        // null guard
        if(configurationDto.getLayerSettings() == null){
            return new MapConfigDto();
        }
        serviceLayersDto.setBaseLayers(getLayerDtoList(configurationDto.getLayerSettings().getBaseLayers(), true, configurationDto.getReferenceDataSettings()));
        return new MapConfigDto(new MapDto(projection, controls, null, serviceLayersDto, null), null, null);
    }

    public ConfigurationDto retrieveAdminConfiguration(String config, Collection<String> permittedServiceLayers) {
        ConfigurationDto configurationDto = MapConfigHelper.getAdminConfiguration(config);
        updateLayerSettings(configurationDto.getLayerSettings(), permittedServiceLayers);
        updateReferenceDataSettings(configurationDto.getReferenceDataSettings(), permittedServiceLayers);
        configurationDto.setSystemSettings(getConfigSystemSettings());
        return configurationDto;
    }

    public ConfigurationDto retrieveUserConfiguration(String config, String defaultConfig, String userName, Collection<String> permittedServiceLayers) {
        ConfigurationDto userConfig = MapConfigHelper.getUserConfiguration(config);
        ConfigurationDto adminConfig = MapConfigHelper.getAdminConfiguration(defaultConfig);
        ConfigurationDto mergedConfig = ConfigurationMapper.mergeUserConfiguration(adminConfig, userConfig);
        updateLayerSettings(mergedConfig.getLayerSettings(), permittedServiceLayers);
        updateReferenceDataSettings(mergedConfig.getReferenceDataSettings(), permittedServiceLayers);
        return mergedConfig;
    }

    public ConfigDto getReportConfigWithoutMap(String userPref, String adminPref) {
        return ConfigurationMapper.mergeNoMapConfiguration(MapConfigHelper.getUserConfiguration(userPref), MapConfigHelper.getAdminConfiguration(adminPref));
    }

    public MapConfigurationType getMapConfigurationType(final Long reportId, Collection<String> permittedServiceLayers) {

        if (reportId == null) {
            throw new IllegalArgumentException("ARGUMENT CAN NOT BE NULL");
        }

        ReportConnectSpatialEntity entity = reportConnectSpatialDao.find(reportId);

        if (entity == null) {
            return null;
        }

        LayerSettingsDto layerSettingsDto = MapConfigHelper.getLayerSettingsForMap(entity.getReportConnectServiceAreas());

        MapConfigurationType result = ReportConnectSpatialMapper.INSTANCE.reportConnectSpatialEntityToMapConfigurationType(entity);
        VisibilitySettingsDto visibilitySettings = MapConfigHelper.getVisibilitySettings(entity.getVisibilitySettings());
        result.setVisibilitySettings(MapConfigMapper.INSTANCE.getVisibilitySettingsType(visibilitySettings));
        StyleSettingsDto styleSettingsDto = MapConfigHelper.getStyleSettings(entity.getStyleSettings());
        result.setStyleSettings(MapConfigMapper.INSTANCE.getStyleSettingsType(styleSettingsDto));

        updateLayerSettings(layerSettingsDto, permittedServiceLayers);

        result.setLayerSettings(MapConfigMapper.INSTANCE.getLayerSettingsType(layerSettingsDto));
        Map<String, ReferenceDataPropertiesDto> referenceData = MapConfigHelper.getReferenceDataSettings(entity.getReferenceData());

        updateReferenceDataSettings(referenceData, permittedServiceLayers);

        result.setReferenceDatas(MapConfigMapper.INSTANCE.getReferenceDataType(referenceData));

        return result;
    }

    public SpatialSaveOrUpdateMapConfigurationRS handleSaveOrUpdateSpatialMapConfiguration(final SpatialSaveOrUpdateMapConfigurationRQ request) {
        SpatialValidator.validate(request);
        ReportConnectSpatialEntity entity = getReportConnectSpatialEntity(request);

        if (entity == null) {
            throw new IllegalArgumentException("MAP CONFIGURATION CAN NOT BE NULL");
        }

        VisibilitySettingsDto visibilitySettings = MapConfigMapper.INSTANCE.getVisibilitySettingsDto(request.getMapConfiguration().getVisibilitySettings());
        entity.setVisibilitySettings(MapConfigHelper.getVisibilitySettingsJson(visibilitySettings));
        StyleSettingsDto styleSettings = MapConfigMapper.INSTANCE.getStyleSettingsDto(request.getMapConfiguration().getStyleSettings());
        entity.setStyleSettings(MapConfigHelper.getStyleSettingsJson(styleSettings));
        LayerSettingsDto layerSettingsDto = MapConfigMapper.INSTANCE.getLayerSettingsDto(request.getMapConfiguration().getLayerSettings());
        updateReportConnectServiceAreasEntity(entity, layerSettingsDto);
        Map<String, ReferenceDataPropertiesDto> referenceData = MapConfigMapper.INSTANCE.getReferenceData(request.getMapConfiguration().getReferenceDatas());
        entity.setReferenceData(MapConfigHelper.getReferenceDataSettingsJson(referenceData));
        reportConnectSpatialDao.merge(entity);
        return new SpatialSaveOrUpdateMapConfigurationRS();
    }

    public void handleDeleteMapConfiguration(SpatialDeleteMapConfigurationRQ request) {

        if (request == null) {
            throw new IllegalArgumentException("ARGUMENT CAN NOT BE NULL");
        }

        List<Long> spatialConnectIds = request.getSpatialConnectIds();

        if (spatialConnectIds != null && !spatialConnectIds.isEmpty()) {
            reportConnectSpatialDao.deleteById(spatialConnectIds);
        }
    }

    private MapDto getMap(ConfigurationDto configurationDto, Integer reportId, String userName, String scopeName, String timeStamp, Collection<String> permittedServiceLayer) {
        ProjectionDto projection = getMapProjection(reportId, configurationDto);
        List<ControlDto> controls = getControls(reportId, configurationDto);
        List<TbControlDto> tbControls = getTbControls(configurationDto);
        ServiceLayersDto layers = getServiceAreaLayer(reportId, configurationDto, userName, scopeName, timeStamp, permittedServiceLayer);
        RefreshDto refreshDto = getRefreshDto(configurationDto);
        return new MapDto(projection, controls, tbControls, layers, refreshDto);
    }

    private VectorStylesDto getVectorStyles(ConfigurationDto configurationDto, Integer reportId) {
        if (reportId != null) {
            ReportConnectSpatialEntity entity = reportConnectSpatialDao.find(reportId.longValue());
            if (entity != null && entity.getStyleSettings() != null) {
                StyleSettingsDto styleSettingsDto = MapConfigHelper.getStyleSettings(entity.getStyleSettings());
                if ((styleSettingsDto.getPositions() != null && styleSettingsDto.getPositions().getStyle() != null) ||
                        (styleSettingsDto.getSegments() != null && styleSettingsDto.getSegments().getStyle() != null) ||
                        (styleSettingsDto.getAlarms() != null)) {
                    return MapConfigMapper.INSTANCE.getStyleDtos(styleSettingsDto); // Style Settings is overridden by Report. Return the report configured style settings
                }
            }
        }
        return MapConfigMapper.INSTANCE.getStyleDtos(configurationDto.getStylesSettings()); // Return merged style settings from Admin and User config
    }

    private VisibilitySettingsDto getVisibilitySettings(ConfigurationDto configurationDto, Integer reportId) {
        if (reportId != null) {
            ReportConnectSpatialEntity entity = reportConnectSpatialDao.find(reportId.longValue());
            if (entity != null && entity.getVisibilitySettings() != null) {
                VisibilitySettingsDto visibilitySettingsDto = MapConfigHelper.getVisibilitySettings(entity.getVisibilitySettings());
                if (MapConfigHelper.isVisibilitySettingsOverriddenByReport(visibilitySettingsDto)) {
                    return visibilitySettingsDto; // VisibilitySettings is overridden by Report. Return the report configured visibility settings
                }
            }
        }
        return configurationDto.getVisibilitySettings(); // Return merged visibility settings from Admin and User Config
    }

    private ProjectionDto getMapProjection(Integer reportId, ConfigurationDto configurationDto) {
        if (reportId != null) {
            List<ProjectionEntity> projections = reportConnectSpatialDao.findProjectionByMap(reportId);
            List<ProjectionDto> projectionDtoList = ProjectionMapper.mapToProjectionDto(projections);
            if (projectionDtoList != null && !projectionDtoList.isEmpty()) { // Get Map Projection for report
                return projectionDtoList.get(0);
            }
        }
        if(configurationDto.getMapSettings() == null){
            return null;
        }
        return getProjection(configurationDto.getMapSettings().getMapProjectionId());
    }

    private ProjectionDto getProjection(Integer id) {
        ProjectionEntity projection = projectionDao.findProjectionById(id.longValue());
        if (projection != null) {
            return ProjectionMapper.mapToProjectionDto(projection);
        }
        return null;
    }

    private List<ControlDto> getControls(Integer reportId, ConfigurationDto configurationDto) {

        // guard against null
        if (configurationDto == null) return new ArrayList<>();
        if (configurationDto.getToolSettings() == null) return new ArrayList<>();
        if (configurationDto.getToolSettings().getControl() == null) return new ArrayList<>();



        List<ControlDto> controls = MapConfigMapper.INSTANCE.getControls(configurationDto.getToolSettings().getControl());
        DisplayProjectionDto displayProjection = getDisplayProjection(reportId, configurationDto);
        return updateControls(controls, displayProjection.getUnits().value(),
                displayProjection.getEpsgCode(), displayProjection.getFormats().value());
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

    private DisplayProjectionDto getDisplayProjection(Integer reportId, ConfigurationDto configurationDto) {
        DisplayProjectionDto displayProjectionDto = new DisplayProjectionDto();
        ReportConnectSpatialEntity entity = null;
        if(reportId != null) {
            entity = reportConnectSpatialDao.find(reportId.longValue());
        }

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

    private List<TbControlDto> getTbControls(ConfigurationDto configurationDto) {

        // null guard
        if(configurationDto == null) return new ArrayList<>();
        if(configurationDto.getToolSettings() == null) return new ArrayList<>();

        return MapConfigMapper.INSTANCE.getTbControls(configurationDto.getToolSettings().getTbControl());
    }

    private ServiceLayersDto getServiceAreaLayer(Integer reportId, ConfigurationDto configurationDto, String userName, String scopeName, String timeStamp, Collection<String> permittedServiceLayers) {

        ReportConnectSpatialEntity entity = null;
        if (reportId != null) {
            entity = reportConnectSpatialDao.find((long) reportId);
        }
        LayerSettingsDto layerSettingsDto = null;
        Map<String, ReferenceDataPropertiesDto> referenceData = null;

        if (entity != null) {
            layerSettingsDto = (entity.getReportConnectServiceAreas() != null && !entity.getReportConnectServiceAreas().isEmpty()) ?
                    MapConfigHelper.getLayerSettingsForMap(entity.getReportConnectServiceAreas()) : configurationDto.getLayerSettings();
            referenceData = entity.getReferenceData() != null ?
                    MapConfigHelper.getReferenceDataSettings(entity.getReferenceData()) : configurationDto.getReferenceDataSettings();
        } else {

            // null guard
            if(configurationDto.getLayerSettings() != null){
                layerSettingsDto = configurationDto.getLayerSettings();
            }
            // null guard
            if(configurationDto.getReferenceDataSettings() != null){
                referenceData = configurationDto.getReferenceDataSettings();
            }
        }

        return getServiceAreaLayers(layerSettingsDto, userName, scopeName, timeStamp, reportId, referenceData, configurationDto.getReportProperties(), permittedServiceLayers);
    }

    private ServiceLayersDto getServiceAreaLayers(LayerSettingsDto layerSettingsDto,
            String userName, String scopeName, String timeStamp,
            Integer reportId, Map<String, ReferenceDataPropertiesDto> referenceData,
            ReportProperties reportProperties, Collection<String> allowedServiceLayers) {

        ServiceLayersDto serviceLayersDto = new ServiceLayersDto();
        // null guard
        if(layerSettingsDto != null) {
        serviceLayersDto.setPort(getLayerDtoList(layerSettingsDto.getPortLayers(), false, referenceData)); // Get Service Layers for Port layers
        serviceLayersDto.setAreas(getAreaLayerDtoList(layerSettingsDto.getAreaLayers(), false, userName, scopeName, timeStamp, reportId, referenceData, reportProperties)); // // Get Service Layers for system layers and User Layers
        serviceLayersDto.setAdditional(getLayerDtoList(layerSettingsDto.getAdditionalLayers(), false, referenceData)); // Get Service Layers for Additional layers
        serviceLayersDto.setBaseLayers(getLayerDtoList(layerSettingsDto.getBaseLayers(), true, referenceData)); // Get Service Layers for base layers
        }
        
        if (allowedServiceLayers != null) {
            filterAllForbiddenLayers(serviceLayersDto, allowedServiceLayers);
        }
        return serviceLayersDto;
    }

    private List<LayerDto> getLayerDtoList(List<? extends LayersDto> layersDtos,
            boolean isBackground, Map<String, ReferenceDataPropertiesDto> referenceData) {
        if (layersDtos == null || layersDtos.isEmpty()) {
            return null;
        }
        Collections.sort(layersDtos);
        List<Long> serviceLayerIds = MapConfigHelper.getServiceLayerIds(layersDtos);
        List<ServiceLayerEntity> serviceLayerEntities = getServiceLayers(serviceLayerIds, getBingApiKey());
        return getLayerDtos(serviceLayerEntities, isBackground, referenceData);
    }

    private List<LayerDto> getLayerDtos(List<ServiceLayerEntity> serviceLayerEntities, boolean isBaseLayer,
            Map<String, ReferenceDataPropertiesDto> referenceData) {
        List<LayerDto> layerDtos = new ArrayList<>();
        for (ServiceLayerEntity serviceLayerEntity : serviceLayerEntities) {
            layerDtos.add(MapConfigHelper.convertToServiceLayer(serviceLayerEntity, getGeoServerUrl(), getBingApiKey(), isBaseLayer, referenceData));
        }
        return layerDtos;
    }

    private String getBingApiKey() {
        List<SysConfigEntity> systemConfig = sysConfigDao.findSystemConfigByName(BING_API_KEY);
        return (systemConfig != null && !systemConfig.isEmpty()) ? systemConfig.get(0).getValue() : null;
    }

    private String getGeoServerUrl() {
        List<SysConfigEntity> systemConfig = sysConfigDao.findSystemConfigByName(GEO_SERVER);
        return (systemConfig != null && !systemConfig.isEmpty()) ? systemConfig.get(0).getValue() : null;
    }

    private List<ServiceLayerEntity> getServiceLayers(List<Long> ids, String bingApiKey) {
        List<ServiceLayerEntity> serviceLayers = MapConfigHelper.sortServiceLayers(serviceLayerDao.findServiceLayerEntityByIds(ids), ids);
        Iterator<ServiceLayerEntity> layerIterator = serviceLayers.iterator();
        while(layerIterator.hasNext()) {
            ServiceLayerEntity serviceLayer = layerIterator.next();
            if (MapConfigHelper.isRemoveLayer(serviceLayer, bingApiKey)) {
                layerIterator.remove();
            }
        }
        return serviceLayers;
    }

    private List<LayerDto> getAreaLayerDtoList(List<LayerAreaDto> layersDtos, boolean isBackground, String userName,
            String scopeName, String timeStamp, Integer reportId, Map<String, ReferenceDataPropertiesDto> referenceData,
            ReportProperties reportProperties) {
        if (layersDtos == null || layersDtos.isEmpty()) {
            return null;
        }
        Collections.sort(layersDtos);
        List<LayerDto> layerDtoList = new ArrayList<>();
        for (LayerAreaDto layerAreaDto : layersDtos) {
            List<ServiceLayerEntity> serviceLayers = getServiceLayers(Arrays.asList(Long.parseLong(layerAreaDto
                    .getServiceLayerId())), getBingApiKey());
            if (serviceLayers != null && !serviceLayers.isEmpty()) {
                ServiceLayerEntity serviceLayer = serviceLayers.get(0);
                List<LayerDto> layerDtos = getLayerDtos(Arrays.asList(serviceLayer), isBackground, referenceData);
                if (layerDtos != null && !layerDtos.isEmpty()) {
                    LayerDto layerDto = layerDtos.get(0);
                    if (layerAreaDto.getAreaType().equals(AreaTypeEnum.userarea)) {
                        UserAreasEntity userArea = areaDao.find(UserAreasEntity.class, layerAreaDto.getGid());
                        layerDto.setGid(userArea != null ? userArea.getId() : null);
                        layerDto.setTitle(userArea != null ? userArea.getName() : null);
                        layerDto.setAreaType(AreaTypeEnum.userarea.getType().toUpperCase());
                    } else if (layerAreaDto.getAreaType().equals(AreaTypeEnum.areagroup)) {
                        layerDto.setCqlAll(MapConfigHelper.getAreaGroupCqlAll(userName, scopeName, layerAreaDto
                                .getAreaGroupName()));
                        layerDto.setAreaType(AreaTypeEnum.areagroup.getType().toUpperCase());
                        layerDto.setTitle(layerAreaDto.getAreaGroupName());
                    } else if (layerAreaDto.getAreaType().equals(AreaTypeEnum.sysarea)) {
                        layerDto.setAreaType(AreaTypeEnum.sysarea.getType().toUpperCase());
                    }
                    layerDtoList.add(layerDto);
                }
            }
        }
        return layerDtoList;
    }

    private void filterAllForbiddenLayers(ServiceLayersDto layerSettingsDto, final Collection<String> permittedLayersNames) {
        filterLayerList(layerSettingsDto.getPort(), permittedLayersNames);
        filterLayerList(layerSettingsDto.getAreas(), permittedLayersNames);
        filterLayerList(layerSettingsDto.getAdditional(), permittedLayersNames);
        filterLayerList(layerSettingsDto.getBaseLayers(), permittedLayersNames);
    }

    private void filterLayerList(List<? extends LayerDto> layers, final Collection<String> permittedLayersNames) {
        if (layers != null) {
            Iterator<? extends LayerDto> iterator = layers.iterator();
            while (iterator.hasNext()) {
                LayerDto layer = iterator.next();

                if (!permittedLayersNames.contains(layer.getTypeName()) ) {
                    iterator.remove();
                }
            }
        }
    }

    private RefreshDto getRefreshDto(ConfigurationDto configurationDto) {

        if(configurationDto == null) return null;
        if(configurationDto.getMapSettings() == null) return null;

        return new RefreshDto(configurationDto.getMapSettings().getRefreshStatus(), configurationDto.getMapSettings().getRefreshRate());
    }

    private void updateLayerSettings(LayerSettingsDto layerSettingsDto, Collection<String> permittedServiceLayers) {

        String bingApiKey = getBingApiKey();
        List<Long> ids = new ArrayList<>(); // Get All the Ids to query for Service layer all together

        if(layerSettingsDto == null){
            return;
        }

        ids.addAll(MapConfigHelper.getServiceLayerIds(layerSettingsDto.getAdditionalLayers()));
        ids.addAll(MapConfigHelper.getServiceLayerIds(layerSettingsDto.getBaseLayers()));
        ids.addAll(MapConfigHelper.getServiceLayerIds(layerSettingsDto.getPortLayers()));
        ids.addAll(MapConfigHelper.getServiceLayerIds(layerSettingsDto.getAreaLayers()));

        if (ids.isEmpty()) {
            return; // There is no Areas in the LayersSettings. Returning the call
        }

        List<ServiceLayerEntity> serviceLayers = serviceLayerDao.findServiceLayerEntityByIds(ids); // Get Service layers by all the ids

        //Update the layers
        updateLayer(layerSettingsDto.getAdditionalLayers(), serviceLayers, bingApiKey);
        updateLayer(layerSettingsDto.getBaseLayers(), serviceLayers, bingApiKey);
        updateLayer(layerSettingsDto.getPortLayers(), serviceLayers, bingApiKey);
        updateLayer(layerSettingsDto.getAreaLayers(), serviceLayers, bingApiKey);
        updateAreaLayer(layerSettingsDto.getAreaLayers());

        if (permittedServiceLayers != null && !permittedServiceLayers.isEmpty()) {
            filterAllForbiddenLayers(layerSettingsDto, permittedServiceLayers);
        }
    }

    private void updateLayer(List<? extends LayersDto> layers, List<ServiceLayerEntity> serviceLayers, String bingApiKey) {
        if (layers != null) {
            List<LayersDto> layersToExclude = new ArrayList<>();
            for (LayersDto layersDto : layers) {
                for (ServiceLayerEntity serviceLayerEntity : serviceLayers) {
                    if (Long.parseLong(layersDto.getServiceLayerId()) == serviceLayerEntity.getId()) {
                        if (serviceLayerEntity.getProviderFormat() != null && PROVIDER_FORMAT_BING.equalsIgnoreCase(serviceLayerEntity.getProviderFormat().getServiceType()) && bingApiKey == null) {
                            layersToExclude.add(layersDto);
                        } else {
                            layersDto.setName(serviceLayerEntity.getName());
                            layersDto.setSubType(serviceLayerEntity.getSubType());
                            layersDto.setAreaLocationTypeName(serviceLayerEntity.getAreaType().getTypeName());
                        }
                        break;
                    }
                }
            }
            layers.removeAll(layersToExclude);
        }
        sortLayer(layers);
    }

    private void updateAreaLayer(List<LayerAreaDto> layers) {
        List<Long> userAreaIds = MapConfigHelper.getUserAreaIds(layers);
        if (!userAreaIds.isEmpty()) {
            List<UserAreasEntity> areas = userAreaIds.stream()
                    .map(id -> areaDao.find(UserAreasEntity.class, id))
                    .collect(Collectors.toList());
            for (LayerAreaDto layerDto : layers) {
                for (UserAreasEntity areaDto :  areas) {
                    if (Objects.equals(layerDto.getGid(), areaDto.getId())) {
                        layerDto.setAreaName(areaDto.getName());
                        layerDto.setDesc(areaDto.getAreaDesc());
                    }
                }
            }
        }
        sortLayer(layers);
    }

    private void sortLayer(List<? extends LayersDto> layers) {
        if(layers != null) {
            Collections.sort(layers);
            for (LayersDto layerDto : layers) {
                layerDto.setOrder(null);
            }
        }
    }

    private void filterAllForbiddenLayers(LayerSettingsDto layerSettingsDto, final Collection<String> permittedLayersNames) {
        filterList(layerSettingsDto.getPortLayers(), permittedLayersNames);
        filterList(layerSettingsDto.getAreaLayers(), permittedLayersNames);
        filterList(layerSettingsDto.getAdditionalLayers(), permittedLayersNames);
        filterList(layerSettingsDto.getBaseLayers(), permittedLayersNames);
    }

    private void filterList(List<? extends LayersDto> layers, final Collection<String> permittedLayersNames) {
        if (layers != null) {
            Iterator<? extends LayersDto> iterator = layers.iterator();
            while (iterator.hasNext()) {
                LayersDto layer = iterator.next();

                if (!permittedLayersNames.contains(layer.getAreaLocationTypeName()) ) {
                    iterator.remove();
                }
            }
        }
    }

    private void updateReferenceDataSettings(Map<String, ReferenceDataPropertiesDto> referenceData, Collection<String> permittedServiceLayers) {
        if (referenceData != null) {
            Iterator<Map.Entry<String, ReferenceDataPropertiesDto>> iterator = referenceData.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, ReferenceDataPropertiesDto> referenceDataEntry = iterator.next();
                if (!MapConfigHelper.isServiceLayerPermitted(referenceDataEntry.getKey(), permittedServiceLayers)) {
                    iterator.remove();
                }
            }
        }
    }

    private SystemSettingsDto getConfigSystemSettings() {
        SystemSettingsDto systemSettingsDto = new SystemSettingsDto();
        systemSettingsDto.setGeoserverUrl(getGeoServerUrl());
        systemSettingsDto.setBingApiKey(getBingApiKey());
        return systemSettingsDto;
    }

    private void updateReportConnectServiceAreasEntity(ReportConnectSpatialEntity entity, LayerSettingsDto layerSettingsDto) {
        if(layerSettingsDto == null) {
            clearReportConectServiceAreas(entity);
            return;
        }
        Set<ReportConnectServiceAreasEntity> serviceAreas = createReportConnectServiceAreas(entity, layerSettingsDto);
        if (serviceAreas != null && !serviceAreas.isEmpty()) {
            Set<ReportConnectServiceAreasEntity> areas = entity.getReportConnectServiceAreas();
            if (areas == null) {
                entity.setReportConnectServiceAreas(serviceAreas);
            } else {
                areas.clear();
                areas.addAll(serviceAreas);
                entity.setReportConnectServiceAreas(areas);
            }
        } else {
            clearReportConectServiceAreas(entity);
        }
    }

    private void clearReportConectServiceAreas(ReportConnectSpatialEntity entity) {
        Set<ReportConnectServiceAreasEntity> areas = entity.getReportConnectServiceAreas();
        if(areas != null) {
            areas.clear();
            entity.setReportConnectServiceAreas(areas);
        }
    }

    private Set<ReportConnectServiceAreasEntity> createReportConnectServiceAreas(ReportConnectSpatialEntity reportConnectSpatialEntity, LayerSettingsDto layerSettingsDto) {
        Set<ReportConnectServiceAreasEntity> reportConnectServiceAreasEntities = createReportConnectServiceAreasPerLayer(layerSettingsDto.getAreaLayers(), reportConnectSpatialEntity, LayerTypeEnum.AREA);
        reportConnectServiceAreasEntities.addAll(createReportConnectServiceAreasPerLayer(layerSettingsDto.getPortLayers(), reportConnectSpatialEntity, LayerTypeEnum.PORT));
        reportConnectServiceAreasEntities.addAll(createReportConnectServiceAreasPerLayer(layerSettingsDto.getAdditionalLayers(), reportConnectSpatialEntity, LayerTypeEnum.ADDITIONAL));
        reportConnectServiceAreasEntities.addAll(createReportConnectServiceAreasPerLayer(layerSettingsDto.getBaseLayers(), reportConnectSpatialEntity, LayerTypeEnum.BASE));
        return reportConnectServiceAreasEntities;
    }

    private Set<ReportConnectServiceAreasEntity> createReportConnectServiceAreasPerLayer(List<? extends LayersDto> layers, ReportConnectSpatialEntity reportConnectSpatialEntity, LayerTypeEnum layerTypeEnum) {
        Set<ReportConnectServiceAreasEntity> reportConnectServiceAreasEntities = new HashSet<>();
        for (LayersDto layer : layers) {
            ReportConnectServiceAreasEntity reportConnectServiceAreasEntity = new ReportConnectServiceAreasEntity();
            reportConnectServiceAreasEntity.setReportConnectSpatial(reportConnectSpatialEntity);
            List<ServiceLayerEntity> serviceLayerEntities = serviceLayerDao.findServiceLayerEntityByIds(Arrays.asList(Long.parseLong(layer.getServiceLayerId())));
            reportConnectServiceAreasEntity.setServiceLayer((serviceLayerEntities != null && !serviceLayerEntities.isEmpty()) ? serviceLayerEntities.get(0) : null);
            reportConnectServiceAreasEntity.setLayerOrder(layer.getOrder().intValue());
            reportConnectServiceAreasEntity.setLayerType(layerTypeEnum.getType());
            if (layer instanceof LayerAreaDto) {
                reportConnectServiceAreasEntity.setAreaType(((LayerAreaDto) layer).getAreaType().getType());
                if (((LayerAreaDto)layer).getAreaType().equals(AreaTypeEnum.userarea)) {
                    reportConnectServiceAreasEntity.setSqlFilter(String.valueOf(((LayerAreaDto) layer).getGid()));
                } else if (((LayerAreaDto)layer).getAreaType().equals(AreaTypeEnum.areagroup)) {
                    reportConnectServiceAreasEntity.setSqlFilter(((LayerAreaDto) layer).getAreaGroupName());
                }
            }
            reportConnectServiceAreasEntities.add(reportConnectServiceAreasEntity);
        }
        return reportConnectServiceAreasEntities;
    }

    private ReportConnectSpatialEntity getReportConnectSpatialEntity(final SpatialSaveOrUpdateMapConfigurationRQ request) {
        List<ReportConnectSpatialEntity> entities = reportConnectSpatialDao.findByReportIdAndConnectId(request.getMapConfiguration().getReportId(), request.getMapConfiguration().getSpatialConnectId());

        ReportConnectSpatialEntity entity;
        if (entities != null && !entities.isEmpty()) {
            entity = entities.get(0);
            entity.setScaleBarType(request.getMapConfiguration().getScaleBarUnits());
            entity.setDisplayFormatType(request.getMapConfiguration().getCoordinatesFormat());

            Set<ReportConnectServiceAreasEntity> reportConnectServiceAreas = entity.getReportConnectServiceAreas();
            List<Long> ids = new ArrayList<>();
            for (ReportConnectServiceAreasEntity r : reportConnectServiceAreas) {
                ids.add(r.getId());
            }
            reportConnectSpatialDao.deleteById(ids);
        } else {
            entity = ReportConnectSpatialMapper.INSTANCE.mapConfigurationTypeToReportConnectSpatialEntity(request.getMapConfiguration());
        }

        Long mapProjectionId = request.getMapConfiguration().getMapProjectionId();
        if (mapProjectionId != null){
            ProjectionEntity mapProjection = projectionDao.findProjectionById(mapProjectionId);
            entity.setProjectionByMapProjId(mapProjection);
        }
        Long displayProjectionId = request.getMapConfiguration().getDisplayProjectionId();
        if (displayProjectionId != null){
            ProjectionEntity displayProjection = projectionDao.findProjectionById(displayProjectionId);
            entity.setProjectionByDisplayProjId(displayProjection);
        }

        return entity;
    }
}
