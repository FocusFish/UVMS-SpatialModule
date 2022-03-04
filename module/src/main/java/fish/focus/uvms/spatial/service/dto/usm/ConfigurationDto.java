/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.service.dto.usm;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class ConfigurationDto {

    @NotNull
    private ToolSettingsDto toolSettings;

    @NotNull
    private StyleSettingsDto stylesSettings;

    @NotNull
    private SystemSettingsDto systemSettings;

    private LayerSettingsDto layerSettings;

    @NotNull
    private MapSettingsDto mapSettings;

    @NotNull
    private VisibilitySettingsDto visibilitySettings;

    private ReportProperties reportProperties;

    private Map<String, ReferenceDataPropertiesDto> referenceDataSettings;

    public ConfigurationDto() {}

    public ConfigurationDto(ToolSettingsDto toolSettings,
                            StyleSettingsDto stylesSettings,
                            SystemSettingsDto systemSettings,
                            LayerSettingsDto layerSettings,
                            MapSettingsDto mapSettings,
                            VisibilitySettingsDto visibilitySettings,
                            ReportProperties reportProperties,
                            Map<String, ReferenceDataPropertiesDto> referenceData) {
        this.toolSettings = toolSettings;
        this.systemSettings = systemSettings;
        this.stylesSettings = stylesSettings;
        this.layerSettings = layerSettings;
        this.mapSettings = mapSettings;
        this.visibilitySettings = visibilitySettings;
        this.reportProperties = reportProperties;
        this.referenceDataSettings = referenceData;
    }

    public ToolSettingsDto getToolSettings() {
        return toolSettings;
    }

    public void setToolSettings(ToolSettingsDto toolSettings) {
        this.toolSettings = toolSettings;
    }

    public StyleSettingsDto getStylesSettings() {
        return stylesSettings;
    }

    public void setStylesSettings(StyleSettingsDto stylesSettings) {
        this.stylesSettings = stylesSettings;
    }

    public SystemSettingsDto getSystemSettings() {
        return systemSettings;
    }

    public void setSystemSettings(SystemSettingsDto systemSettings) {
        this.systemSettings = systemSettings;
    }

    public LayerSettingsDto getLayerSettings() {
        return layerSettings;
    }

    public void setLayerSettings(LayerSettingsDto layerSettings) {
        this.layerSettings = layerSettings;
    }

    public MapSettingsDto getMapSettings() {
        return mapSettings;
    }

    public void setMapSettings(MapSettingsDto mapSettings) {
        this.mapSettings = mapSettings;
    }

    public VisibilitySettingsDto getVisibilitySettings() {
        return visibilitySettings;
    }

    public void setVisibilitySettings(VisibilitySettingsDto visibilitySettings) {
        this.visibilitySettings = visibilitySettings;
    }

    public ReportProperties getReportProperties() {
        return reportProperties;
    }

    public void setReportProperties(ReportProperties reportProperties) {
        this.reportProperties = reportProperties;
    }

    public Map<String, ReferenceDataPropertiesDto> getReferenceDataSettings() {
        return referenceDataSettings;
    }

    public void setReferenceDataSettings(Map<String, ReferenceDataPropertiesDto> referenceData) {
        this.referenceDataSettings = referenceData;
    }

    @Override
    public String toString() {
        return "ClassPojo [toolSettings = " + toolSettings + ", stylesSettings = " + stylesSettings + ", systemSettings = " + systemSettings + ", layerSettings = " + layerSettings + ", mapSettings = " + mapSettings + "]";
    }
}