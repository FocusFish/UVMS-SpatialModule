/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.service.dto.config;

import java.util.List;

public class ServiceLayersDto {

    private List<LayerDto> port;

    private List<LayerDto> additional;

    private List<LayerDto> baseLayers;

    private List<LayerDto> areas;

    public ServiceLayersDto() {}

    public ServiceLayersDto(List<LayerDto> portLayers,
                            List<LayerDto> additionalLayers,
                            List<LayerDto> baseLayers,
                            List<LayerDto> systemLayers) {
        this.port = portLayers;
        this.additional = additionalLayers;
        this.baseLayers = baseLayers;
        this.areas = systemLayers;

    }

    public List<LayerDto> getPort() {
        return port;
    }

    public void setPort(List<LayerDto> portLayers) {
        this.port = portLayers;
    }

    public List<LayerDto> getAdditional() {
        return additional;
    }

    public void setAdditional(List<LayerDto> additionalLayers) {
        this.additional = additionalLayers;
    }

    public List<LayerDto> getBaseLayers() {
        return baseLayers;
    }

    public void setBaseLayers(List<LayerDto> baseLayers) {
        this.baseLayers = baseLayers;
    }

    public List<LayerDto> getAreas() {
        return areas;
    }

    public void setAreas(List<LayerDto> systemLayers) {
        this.areas = systemLayers;
    }
}