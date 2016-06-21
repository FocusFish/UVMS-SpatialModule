package eu.europa.ec.fisheries.uvms.spatial.service.bean.helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.LayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.StylesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.*;
import eu.europa.ec.fisheries.uvms.spatial.util.AreaTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.util.LayerTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by padhyad on 3/31/2016.
 */
public class MapConfigHelper {

    private static final String USER_AREA = "userarea";

    private static final String PROVIDER_FORMAT_BING = "BING";

    private static final String GEOSERVER = "geoserver";

    private static Logger LOGGER =  LoggerFactory.getLogger(LoggerFactory.class);

    public static boolean isVisibilitySettingsOverriddenByReport(VisibilitySettingsDto visibilitySettingsDto) {
        boolean isOverridden = false;
        if (visibilitySettingsDto.getVisibilityPositionsDto() != null) {
            if (visibilitySettingsDto.getVisibilityPositionsDto().getLabels() != null && (visibilitySettingsDto.getVisibilityPositionsDto().getLabels().getOrder() != null || visibilitySettingsDto.getVisibilityPositionsDto().getLabels().getValues() != null)) {
                isOverridden = true;
            }
            if (visibilitySettingsDto.getVisibilityPositionsDto().getPopup() != null && (visibilitySettingsDto.getVisibilityPositionsDto().getPopup().getOrder() != null || visibilitySettingsDto.getVisibilityPositionsDto().getPopup().getValues() != null)) {
                isOverridden = true;
            }
            if (visibilitySettingsDto.getVisibilityPositionsDto().getTable() != null && (visibilitySettingsDto.getVisibilityPositionsDto().getTable().getOrder() != null || visibilitySettingsDto.getVisibilityPositionsDto().getTable().getValues() != null)) {
                isOverridden = true;
            }
        }
        if (visibilitySettingsDto.getVisibilitySegmentDto() != null) {
            if (visibilitySettingsDto.getVisibilitySegmentDto().getLabels() != null && (visibilitySettingsDto.getVisibilitySegmentDto().getLabels().getOrder() != null || visibilitySettingsDto.getVisibilitySegmentDto().getLabels().getValues() != null)) {
                isOverridden = true;
            }
            if (visibilitySettingsDto.getVisibilitySegmentDto().getPopup() != null && (visibilitySettingsDto.getVisibilitySegmentDto().getPopup().getOrder() != null || visibilitySettingsDto.getVisibilitySegmentDto().getPopup().getValues() != null)) {
                isOverridden = true;
            }
            if (visibilitySettingsDto.getVisibilitySegmentDto().getTable() != null && (visibilitySettingsDto.getVisibilitySegmentDto().getTable().getOrder() != null || visibilitySettingsDto.getVisibilitySegmentDto().getTable().getValues() != null)) {
                isOverridden = true;
            }
        }
        if (visibilitySettingsDto.getVisibilityTracksDto() != null) {
            if (visibilitySettingsDto.getVisibilityTracksDto().getTable() != null && (visibilitySettingsDto.getVisibilityTracksDto().getTable().getOrder() != null || visibilitySettingsDto.getVisibilityTracksDto().getTable().getValues() != null)) {
                isOverridden = true;
            }
        }
        return isOverridden;
    }

    public static boolean isRemoveLayer(ServiceLayerEntity serviceLayer, String bingApiKey) {
        if (serviceLayer.getProviderFormat().getServiceType().equalsIgnoreCase(PROVIDER_FORMAT_BING) && bingApiKey == null) {
            return true;
        }
        return false;
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

    public static ConfigurationDto getAdminConfiguration(String adminPreference) throws IOException {
        return (adminPreference == null || adminPreference.isEmpty()) ? new ConfigurationDto() : getConfiguration(adminPreference);
    }

    public static ConfigurationDto getUserConfiguration(String userPreference) throws IOException {
        return (userPreference == null || userPreference.isEmpty()) ? new ConfigurationDto() : getConfiguration(userPreference);
    }

    public static VisibilitySettingsDto getVisibilitySettings(String visibilitySettings) throws ServiceException {
        if (visibilitySettings == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            return mapper.readValue(visibilitySettings, VisibilitySettingsDto.class);
        } catch (IOException e) {
            throw new ServiceException("Parse Exception from Json to Object", e);
        }
    }

    public static Map<String, ReferenceDataPropertiesDto> getReferenceDataSettings(String referenceData) throws ServiceException {
        if (referenceData == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            Object obj = mapper.readValue(referenceData, Map.class);
            String jsonString = mapper.writeValueAsString(obj);
            return mapper.readValue(jsonString, TypeFactory.defaultInstance().constructMapType(Map.class, String.class, ReferenceDataPropertiesDto.class));

        } catch (IOException e) {
            throw new ServiceException("Parse Exception from Json to Object", e);
        }
    }

    public static StyleSettingsDto getStyleSettings(String styleSettings) throws ServiceException {
        if (styleSettings == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            return mapper.readValue(styleSettings, StyleSettingsDto.class);
        } catch (IOException e) {
            throw new ServiceException("Parse Exception from Json to Object", e);
        }
    }

    public static ConfigurationDto getConfiguration(String configString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(configString, ConfigurationDto.class);
    }

    public static String getJson(ConfigurationDto config) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(config);
    }

    public static String getVisibilitySettingsJson(VisibilitySettingsDto visibilitySettings) throws ServiceException {
        if (visibilitySettings == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(visibilitySettings);
        } catch (IOException e) {
            throw new ServiceException("Parse Exception from Object to json", e);
        }
    }

    public static String getReferenceDataSettingsJson(Map<String, ReferenceDataPropertiesDto> referenceData) throws ServiceException {
        if (referenceData == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(referenceData);
        } catch (IOException e) {
            throw new ServiceException("Parse Exception from Object to json", e);
        }
    }

    public static String getStyleSettingsJson(StyleSettingsDto styleSettings) throws ServiceException {
        if (styleSettings == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(styleSettings);
        } catch (IOException e) {
            throw new ServiceException("Parse Exception from Object to json", e);
        }
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
        LayerSettingsDto layerSettingsDto = new LayerSettingsDto();
        for (ReportConnectServiceAreasEntity layer : reportConnectServiceArea) {
            LayerTypeEnum layerTypeEnum = LayerTypeEnum.getLayerType(layer.getLayerType());
            switch (layerTypeEnum) {
                case BASE:
                    LayersDto baseLayersDto = new LayersDto(layer.getServiceLayer().getName(), String.valueOf(layer.getServiceLayer().getId()), layer.getServiceLayer().getSubType(),Long.valueOf(layer.getLayerOrder()));
                    layerSettingsDto.addBaseLayer(baseLayersDto);
                    break;
                case ADDITIONAL:
                    LayersDto additionalLayersDto = new LayersDto(layer.getServiceLayer().getName(), String.valueOf(layer.getServiceLayer().getId()), layer.getServiceLayer().getSubType(), Long.valueOf(layer.getLayerOrder()));
                    layerSettingsDto.addAdditionalLayer(additionalLayersDto);
                    break;
                case PORT:
                    LayersDto portLayersDto = new LayersDto(layer.getServiceLayer().getName(), String.valueOf(layer.getServiceLayer().getId()), layer.getServiceLayer().getSubType(), Long.valueOf(layer.getLayerOrder()));
                    layerSettingsDto.addPortLayer(portLayersDto);
                    break;
                case AREA:
                    AreaTypeEnum areaTypeEnum = AreaTypeEnum.valueOf(layer.getAreaType());
                    LayerAreaDto areaLayersDto = new LayerAreaDto(areaTypeEnum, String.valueOf(layer.getServiceLayer().getId()), Long.valueOf(layer.getLayerOrder()));
                    areaLayersDto.setName(layer.getServiceLayer().getName());
                    areaLayersDto.setSubType(layer.getServiceLayer().getSubType());

                    if (areaTypeEnum.equals(AreaTypeEnum.userarea)) {
                        areaLayersDto.setGid(Long.parseLong(layer.getSqlFilter()));
                    }
                    if (areaTypeEnum.equals(AreaTypeEnum.areagroup)) {
                        areaLayersDto.setAreaGroupName(layer.getSqlFilter());
                    }
                    layerSettingsDto.addAreaLayer(areaLayersDto);
                    break;
            }
        }
        return layerSettingsDto;
    }

    public static List<Long> getServiceLayerIds(List<? extends LayersDto> layers) {
        if(layers == null || layers.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> ids = new ArrayList<>();
        for (LayersDto layer : layers) {
            ids.add(Long.parseLong(layer.getServiceLayerId()));
        }
        return ids;
    }

    public static List<Long> getUserAreaIds(List<LayerAreaDto> layers) {
        if (layers == null || layers.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> userAreaIds = new ArrayList<>();
        for (LayerAreaDto layerDto : layers) {
            if (layerDto.getAreaType().getType().equalsIgnoreCase(USER_AREA)) {
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
        layerDto.setServerType(serviceLayerEntity.getIsInternal() ? GEOSERVER : null);
        layerDto.setLayerGeoName(serviceLayerEntity.getGeoName());
        if(!(StringUtils.isEmpty(serviceLayerEntity.getStyleGeom()) && StringUtils.isEmpty(serviceLayerEntity.getStyleLabel()) && StringUtils.isEmpty(serviceLayerEntity.getStyleLabelGeom()))) {
            layerDto.setStyles(new StylesDto(serviceLayerEntity.getStyleGeom(), serviceLayerEntity.getStyleLabel(), serviceLayerEntity.getStyleLabelGeom()));
        }
        if (type.equalsIgnoreCase("BING")) {
            layerDto.setApiKey(bingApiKey);
        }
        setCql(referenceData, layerDto, serviceLayerEntity.getAreaType());
        return layerDto;
    }

    private static void setCql(Map<String, ReferenceDataPropertiesDto> referenceData, LayerDto layerDto, AreaLocationTypesEntity areaType) {
        for (Map.Entry<String, ReferenceDataPropertiesDto> entry : referenceData.entrySet()) {
            if (areaType.getTypeName().equalsIgnoreCase(entry.getKey())) {
                ReferenceDataPropertiesDto referenceDataPropertiesDto = entry.getValue();
                switch (referenceDataPropertiesDto.getSelection()) {
                    case "custom" :
                        if (referenceDataPropertiesDto.getCodes().isEmpty()) {
                            layerDto.setIsWarning(true);
                            layerDto.setCql(null);
                        } else {
                            layerDto.setIsWarning(null);
                            StringBuilder cql = new StringBuilder();
                            cql.append("code in (");
                            cql.append(getConcatenateString(referenceDataPropertiesDto.getCodes()));
                            cql.append(")");
                            layerDto.setCql(cql.toString().replaceAll(", $", ""));
                        }
                        break;
                    case "all" :
                        layerDto.setCql(null);
                        layerDto.setIsWarning(null);
                        break;
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
        for (String layer : permittedServiceLayers) {
            if (serviceLayerName.equalsIgnoreCase(layer)) {
                return true;
            }
        }
        return false;
    }
}
