/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package fish.focus.uvms.spatial.service.mapper;

import fish.focus.uvms.spatial.service.dto.config.ConfigDto;
import fish.focus.uvms.spatial.service.dto.usm.ConfigurationDto;

public class ConfigurationMapper {

    public static ConfigurationDto mergeConfiguration(ConfigurationDto source, ConfigurationDto target) {
        if ( source == null || target == null) {
            return target;
        }
        target.setSystemSettings((source.getSystemSettings() == null ? target.getSystemSettings() : source.getSystemSettings()));
        target.setStylesSettings(source.getStylesSettings() == null ? target.getStylesSettings() : source.getStylesSettings());
        target.setToolSettings(source.getToolSettings() == null ? target.getToolSettings() : source.getToolSettings());
        target.setLayerSettings(source.getLayerSettings() == null ? target.getLayerSettings() : source.getLayerSettings());
        target.setVisibilitySettings(source.getVisibilitySettings() == null ? target.getVisibilitySettings() : source.getVisibilitySettings());
        target.setMapSettings(source.getMapSettings() == null ? target.getMapSettings() : source.getMapSettings());
        target.setReferenceDataSettings(source.getReferenceDataSettings() == null ? target.getReferenceDataSettings() : source.getReferenceDataSettings());
        return target;
    }

    public static ConfigDto mergeNoMapConfiguration(ConfigurationDto source, ConfigurationDto target) {
        if ( source == null || target == null) {
            return new ConfigDto();
        }
        ConfigDto configDto = new ConfigDto();
        configDto.setVisibilitySettings(source.getVisibilitySettings() == null ? target.getVisibilitySettings() : source.getVisibilitySettings());
        return configDto;
    }

    public static ConfigurationDto mergeUserConfiguration(ConfigurationDto source, ConfigurationDto target) {
        if ( source == null || target == null) {
            return target;
        }
        target.setStylesSettings(target.getStylesSettings() == null ? source.getStylesSettings() : target.getStylesSettings());
        target.setVisibilitySettings(target.getVisibilitySettings() == null ? source.getVisibilitySettings() : target.getVisibilitySettings());
        target.setMapSettings(target.getMapSettings() == null ? source.getMapSettings() : target.getMapSettings());
        target.setLayerSettings(target.getLayerSettings() == null ? source.getLayerSettings() : target.getLayerSettings());
        target.setReferenceDataSettings(target.getReferenceDataSettings() == null ? source.getReferenceDataSettings() : target.getReferenceDataSettings());
        return target;
    }

    public static ConfigurationDto resetUserConfiguration(ConfigurationDto source, ConfigurationDto target) {
        if ( source == null || target == null) {
            return target;
        }
        target.setStylesSettings(source.getStylesSettings() != null ? null : target.getStylesSettings());
        target.setVisibilitySettings(source.getVisibilitySettings() != null ? null : target.getVisibilitySettings());
        target.setMapSettings(source.getMapSettings() != null ? null : target.getMapSettings());
        target.setLayerSettings(source.getLayerSettings() != null ? null : target.getLayerSettings());
        target.setReferenceDataSettings(source.getReferenceDataSettings() != null ? null : target.getReferenceDataSettings());
        return target;
    }

    public static ConfigurationDto getDefaultNodeConfiguration(ConfigurationDto configurationDto, ConfigurationDto adminConfigurationDto) {
        ConfigurationDto defaultNodeConfigurationDto = new ConfigurationDto();
        defaultNodeConfigurationDto.setStylesSettings(configurationDto.getStylesSettings() != null ? adminConfigurationDto.getStylesSettings() : null);
        defaultNodeConfigurationDto.setVisibilitySettings(configurationDto.getVisibilitySettings() != null ? adminConfigurationDto.getVisibilitySettings() : null);
        defaultNodeConfigurationDto.setMapSettings(configurationDto.getMapSettings() != null ? adminConfigurationDto.getMapSettings() : null);
        defaultNodeConfigurationDto.setLayerSettings(configurationDto.getLayerSettings() != null ? adminConfigurationDto.getLayerSettings() : null);
        defaultNodeConfigurationDto.setReferenceDataSettings(configurationDto.getReferenceDataSettings() != null ? adminConfigurationDto.getReferenceDataSettings() : null);
        return defaultNodeConfigurationDto;
    }
}