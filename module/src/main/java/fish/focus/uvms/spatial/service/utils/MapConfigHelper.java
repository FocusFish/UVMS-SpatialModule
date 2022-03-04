/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package fish.focus.uvms.spatial.service.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import fish.focus.uvms.spatial.service.dto.config.LayerDto;
import fish.focus.uvms.spatial.service.dto.config.StylesDto;
import fish.focus.uvms.spatial.service.dto.usm.ConfigurationDto;
import fish.focus.uvms.spatial.service.dto.usm.LayerAreaDto;
import fish.focus.uvms.spatial.service.dto.usm.LayerSettingsDto;
import fish.focus.uvms.spatial.service.dto.usm.LayersDto;
import fish.focus.uvms.spatial.service.dto.usm.ReferenceDataPropertiesDto;
import fish.focus.uvms.spatial.service.dto.usm.StyleSettingsDto;
import fish.focus.uvms.spatial.service.dto.usm.VisibilitySettingsDto;
import fish.focus.uvms.spatial.service.entity.AreaLocationTypesEntity;
import fish.focus.uvms.spatial.service.entity.ReportConnectServiceAreasEntity;
import fish.focus.uvms.spatial.service.entity.ServiceLayerEntity;
import fish.focus.uvms.spatial.service.enums.AreaTypeEnum;
import fish.focus.uvms.spatial.service.enums.LayerTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapConfigHelper {

    private static final String USER_AREA = "userarea";
    private static final String PROVIDER_FORMAT_BING = "BING";
    private static final String GEOSERVER = "geoserver";
    private static Logger LOGGER =  LoggerFactory.getLogger(MapConfigHelper.class);

    private static Jsonb jsonb = JsonbBuilder.create();

    private MapConfigHelper() {

    }

    public static boolean isVisibilitySettingsOverriddenByReport(VisibilitySettingsDto visibilitySettingsDto) {
        boolean isOverridden = false;
        if (visibilitySettingsDto.getPositions() != null) {
            if (visibilitySettingsDto.getPositions().getLabels() != null && (visibilitySettingsDto.getPositions().getLabels().getOrder() != null || visibilitySettingsDto.getPositions().getLabels().getValues() != null)) {
                isOverridden = true;
            }
            if (visibilitySettingsDto.getPositions().getPopup() != null && (visibilitySettingsDto.getPositions().getPopup().getOrder() != null || visibilitySettingsDto.getPositions().getPopup().getValues() != null)) {
                isOverridden = true;
            }
            if (visibilitySettingsDto.getPositions().getTable() != null && (visibilitySettingsDto.getPositions().getTable().getOrder() != null || visibilitySettingsDto.getPositions().getTable().getValues() != null)) {
                isOverridden = true;
            }
        }
        if (visibilitySettingsDto.getSegments() != null) {
            if (visibilitySettingsDto.getSegments().getLabels() != null && (visibilitySettingsDto.getSegments().getLabels().getOrder() != null || visibilitySettingsDto.getSegments().getLabels().getValues() != null)) {
                isOverridden = true;
            }
            if (visibilitySettingsDto.getSegments().getPopup() != null && (visibilitySettingsDto.getSegments().getPopup().getOrder() != null || visibilitySettingsDto.getSegments().getPopup().getValues() != null)) {
                isOverridden = true;
            }
            if (visibilitySettingsDto.getSegments().getTable() != null && (visibilitySettingsDto.getSegments().getTable().getOrder() != null || visibilitySettingsDto.getSegments().getTable().getValues() != null)) {
                isOverridden = true;
            }
        }
        if (visibilitySettingsDto.getTracks() != null) {
            if (visibilitySettingsDto.getTracks().getTable() != null && (visibilitySettingsDto.getTracks().getTable().getOrder() != null || visibilitySettingsDto.getTracks().getTable().getValues() != null)) {
                isOverridden = true;
            }
        }
        return isOverridden;
    }

    public static boolean isRemoveLayer(ServiceLayerEntity serviceLayer, String bingApiKey) {
        return serviceLayer.getProviderFormat().getServiceType().equalsIgnoreCase(PROVIDER_FORMAT_BING) &&
                (bingApiKey == null || bingApiKey.trim().equals(""));
    }

    public static List<ServiceLayerEntity> sortServiceLayers(List<ServiceLayerEntity> serviceLayers, List<Long> ids) {
        List<ServiceLayerEntity> sortedServiceLayers = new ArrayList<>();
        if (serviceLayers!= null && ids != null) {
            for (Long id : ids) {
                for (ServiceLayerEntity serviceLayerEntity : serviceLayers) {
                    if (id == serviceLayerEntity.getId()) {
                        sortedServiceLayers.add(serviceLayerEntity);
                    }
                }
            }
        }
        return sortedServiceLayers;
    }

    public static ConfigurationDto getAdminConfiguration(String adminPreference) {
        return (adminPreference == null || adminPreference.isEmpty()) ? new ConfigurationDto() : getConfiguration(adminPreference);
    }

    public static ConfigurationDto getUserConfiguration(String userPreference) {
        return (userPreference == null || userPreference.isEmpty()) ? new ConfigurationDto() : getConfiguration(userPreference);
    }

    public static VisibilitySettingsDto getVisibilitySettings(String visibilitySettings) {
        if (visibilitySettings == null) {
            return null;
        }
        return JsonbBuilder.create().fromJson(visibilitySettings, VisibilitySettingsDto.class);
    }

    public static Map<String, ReferenceDataPropertiesDto> getReferenceDataSettings(String referenceData) {
        if (referenceData == null) {
            return null;
        }
        return JsonbBuilder.create().fromJson(referenceData, Map.class);
    }

    public static StyleSettingsDto getStyleSettings(String styleSettings) {
        if (styleSettings == null) {
            return null;
        }
        return jsonb.fromJson(styleSettings, StyleSettingsDto.class);
    }

    public static ConfigurationDto getConfiguration(String configString) {
        return jsonb.fromJson(configString, ConfigurationDto.class);
    }

    public static String getJson(ConfigurationDto config) throws IOException {
        return jsonb.toJson(config);
    }

    public static String getVisibilitySettingsJson(VisibilitySettingsDto visibilitySettings) {
        if (visibilitySettings == null) {
            return null;
        }
        return jsonb.toJson(visibilitySettings);
    }

    public static String getReferenceDataSettingsJson(Map<String, ReferenceDataPropertiesDto> referenceData) {
        if (referenceData == null) {
            return null;
        }
        return jsonb.toJson(referenceData);
    }

    public static String getStyleSettingsJson(StyleSettingsDto styleSettings) {
        if (styleSettings == null) {
            return null;
        }
        return jsonb.toJson(styleSettings);
    }

    public static String getAreaGroupCqlAll(String userName, String scopeName, String areaGroupName) {
        StringBuilder cql = new StringBuilder("type=" + "'" + areaGroupName + "'");
        LOGGER.info("cql All for geo server : \n" + cql);
        return cql.toString();
    }

    public static String getAreaGroupCqlActive(String startDate, String endDate) {
        StringBuilder cql = new StringBuilder();
        if (startDate != null && endDate != null) {
            cql.append("(").
                    append("(").append("start_date IS NULL").append(" AND ").append("end_date IS NULL").append(")").append(" OR ").
                    append("(").append("NOT ( ").append("start_date > ").append("'").append(endDate).append("'").append(" OR ").append("end_date < ").append("'").append(startDate).append("'").append(")").append(")").append(" OR ").
                    append("(").append("start_date IS NULL").append(" AND ").append("end_date >= ").append("'").append(startDate).append("'").append(")").append(" OR ").
                    append("(").append("end_date IS NULL").append(" AND ").append("start_date <= ").append("'").append(endDate).append("'").append(")").
                    append(")");
        } else if (startDate == null && endDate != null) {
            cql.append("(").append("start_date <= ").append("'").append(endDate).append("'").append(" OR ").append("start_date IS NULL").append(")");
        } else {
            return null;
        }
        LOGGER.info("cql Active for geo server : \n" + cql);
        return cql.toString();
    }

    public static LayerSettingsDto getLayerSettingsForMap(Set<ReportConnectServiceAreasEntity> reportConnectServiceArea) {

        LayerSettingsDto result = new LayerSettingsDto();

        if (reportConnectServiceArea != null && !reportConnectServiceArea.isEmpty()) {

            for (ReportConnectServiceAreasEntity layer : reportConnectServiceArea) {

                LayerTypeEnum layerTypeEnum = LayerTypeEnum.getLayerType(layer.getLayerType());

                if (layerTypeEnum == null) {
                    continue;
                }

                ServiceLayerEntity serviceLayer = layer.getServiceLayer();
                if (serviceLayer != null) {
                    addLayer(result, serviceLayer, layerTypeEnum, layer);
                }
            }
        }
        return result;
    }

    private static void addLayer(LayerSettingsDto result, ServiceLayerEntity serviceLayerEntity, LayerTypeEnum layerTypeEnum, ReportConnectServiceAreasEntity layer) {
        switch (layerTypeEnum) {
            case BASE:
                LayersDto baseLayersDto = new LayersDto(serviceLayerEntity.getName(), String.valueOf(serviceLayerEntity.getId()), serviceLayerEntity.getSubType(), Long.valueOf(layer.getLayerOrder()));
                result.addBaseLayer(baseLayersDto);
                break;
            case ADDITIONAL:
                LayersDto additionalLayersDto = new LayersDto(serviceLayerEntity.getName(), String.valueOf(serviceLayerEntity.getId()), serviceLayerEntity.getSubType(), Long.valueOf(layer.getLayerOrder()));
                result.addAdditionalLayer(additionalLayersDto);
                break;
            case PORT:
                LayersDto portLayersDto = new LayersDto(serviceLayerEntity.getName(), String.valueOf(serviceLayerEntity.getId()), serviceLayerEntity.getSubType(), Long.valueOf(layer.getLayerOrder()));
                result.addPortLayer(portLayersDto);
                break;
            case AREA:
                addAreaLayer(result, layer);
                break;
        }
    }

    private static void addAreaLayer(LayerSettingsDto layerSettingsDto, ReportConnectServiceAreasEntity layer) {
        AreaTypeEnum areaTypeEnum = AreaTypeEnum.getEnumFromValue(layer.getAreaType());
        LayerAreaDto areaLayersDto = new LayerAreaDto(areaTypeEnum, String.valueOf(layer.getServiceLayer().getId()), (long) layer.getLayerOrder());
        areaLayersDto.setName(layer.getServiceLayer().getName());
        areaLayersDto.setSubType(layer.getServiceLayer().getSubType());

        if (AreaTypeEnum.userarea.equals(areaTypeEnum)) {
            areaLayersDto.setGid(Long.parseLong(layer.getSqlFilter()));
        }
        if (AreaTypeEnum.areagroup.equals(areaTypeEnum)) {
            areaLayersDto.setAreaGroupName(layer.getSqlFilter());
        }
        layerSettingsDto.addAreaLayer(areaLayersDto);
    }

    public static List<Long> getServiceLayerIds(List<? extends LayersDto> layers) {
        if(layers == null || layers.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> ids = new ArrayList<>();
        for (LayersDto layer : layers) {
            String serviceLayerId = layer.getServiceLayerId();
            if (serviceLayerId != null) {
                ids.add(Long.parseLong(serviceLayerId));
            }
        }
        return ids;
    }

    public static List<Long> getUserAreaIds(List<LayerAreaDto> layers) {
        if (layers == null || layers.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> userAreaIds = new ArrayList<>();
        for (LayerAreaDto layerDto : layers) {
            if (layerDto.getAreaType() != null && USER_AREA.equalsIgnoreCase(layerDto.getAreaType().getType())) {
                userAreaIds.add(layerDto.getGid());
            }
        }
        return userAreaIds;
    }

    public static LayerDto convertToServiceLayer(ServiceLayerEntity serviceLayerEntity, String geoServerUrl, String bingApiKey, boolean isBaseLayer, Map<String, ReferenceDataPropertiesDto> referenceData) {
        LayerDto layerDto = new LayerDto();
        String type = serviceLayerEntity.getProviderFormat().getServiceType();
        layerDto.setType(type);
        layerDto.setTitle(serviceLayerEntity.getName());
        layerDto.setIsBaseLayer(isBaseLayer);
        layerDto.setShortCopyright(serviceLayerEntity.getShortCopyright());
        layerDto.setLongCopyright(serviceLayerEntity.getLongCopyright());
        if(!(type.equalsIgnoreCase("OSM") || type.equalsIgnoreCase("OSEA") || type.equalsIgnoreCase("BING"))) {
            layerDto.setUrl(geoServerUrl.concat(serviceLayerEntity.getProviderFormat().getServiceType().toLowerCase()));
        }
        if(type.equalsIgnoreCase("WMS") && !serviceLayerEntity.getInternal()) {
            layerDto.setUrl(serviceLayerEntity.getServiceUrl());
        }
        layerDto.setServerType(serviceLayerEntity.getInternal() ? GEOSERVER : null);
        layerDto.setLayerGeoName(serviceLayerEntity.getGeoName());
        if(!(StringUtils.isEmpty(serviceLayerEntity.getStyleGeom()) && StringUtils.isEmpty(serviceLayerEntity.getStyleLabel()) && StringUtils.isEmpty(serviceLayerEntity.getStyleLabelGeom()))) {
            layerDto.setStyles(new StylesDto(serviceLayerEntity.getStyleGeom(), serviceLayerEntity.getStyleLabel(), serviceLayerEntity.getStyleLabelGeom()));
        }
        if (type.equalsIgnoreCase("BING")) {
            layerDto.setApiKey(bingApiKey);
        }
        setCql(referenceData, layerDto, serviceLayerEntity.getAreaType());
        layerDto.setTypeName(serviceLayerEntity.getAreaType().getTypeName());
        return layerDto;
    }

    private static void setCql(Map<String, ReferenceDataPropertiesDto> referenceData, LayerDto layerDto, AreaLocationTypesEntity areaType) {

        if (areaType != null) {
            for (Map.Entry<String, ReferenceDataPropertiesDto> entry : referenceData.entrySet()) {
                if (areaType.getTypeName().equalsIgnoreCase(entry.getKey())) {
                    ReferenceDataPropertiesDto referenceDataPropertiesDto = entry.getValue();
                    switch (referenceDataPropertiesDto.getSelection()) {
                        case "custom" :
                            if (referenceDataPropertiesDto.getCodes().isEmpty()) {
                                layerDto.setWarning(true);
                                layerDto.setCql(null);
                            } else {
                                layerDto.setWarning(null);
                                StringBuilder cql = new StringBuilder();
                                cql.append("code in (");
                                cql.append(getConcatenateString(referenceDataPropertiesDto.getCodes()));
                                cql.append(")");
                                layerDto.setCql(cql.toString().replaceAll(", $", ""));
                            }
                            break;
                        case "all" :
                            layerDto.setCql(null);
                            layerDto.setWarning(null);
                            break;
                    }

                }
            }
        }
    }

    private static String getConcatenateString(List<String> codes) {
        StringBuilder concatStr = new StringBuilder();
        for (String code : codes) {
            concatStr.append("'" + code + "'" + ",");
        }
        return concatStr.toString().replaceAll(",$", "");
    }

    public static boolean isServiceLayerPermitted(String serviceLayerName, Collection<String> permittedServiceLayers) {
    	if (permittedServiceLayers != null && !permittedServiceLayers.isEmpty()) {
	        for (String layer : permittedServiceLayers) {
	            if (serviceLayerName.equalsIgnoreCase(layer)) {
	                return true;
	            }
	        }
    	}
        return false;
    }
}