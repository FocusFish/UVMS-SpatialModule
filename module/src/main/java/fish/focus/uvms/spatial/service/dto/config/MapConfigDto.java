/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package fish.focus.uvms.spatial.service.dto.config;

import fish.focus.uvms.spatial.service.dto.usm.VisibilitySettingsDto;

public class MapConfigDto {

    private MapDto map;

    private VectorStylesDto vectorStyles;

    private VisibilitySettingsDto visibilitySettings;

    /**
     * No args constructor for use in serialization
     */
    public MapConfigDto() {
    }

    public MapConfigDto(MapDto map, VectorStylesDto vectorStyles, VisibilitySettingsDto visibilitySettings) {
        this.map = map;
        this.vectorStyles = vectorStyles;
        this.visibilitySettings = visibilitySettings;
    }

    public MapDto getMap() {
        return map;
    }

    public void setMap(MapDto map) {
        this.map = map;
    }

    public VectorStylesDto getVectorStyles() {
        return vectorStyles;
    }

    public void setVectorStyles(VectorStylesDto vectorStyles) {
        this.vectorStyles = vectorStyles;
    }

    public VisibilitySettingsDto getVisibilitySettings() {
        return visibilitySettings;
    }

    public void setVisibilitySettings(VisibilitySettingsDto visibilitySettings) {
        this.visibilitySettings = visibilitySettings;
    }
}