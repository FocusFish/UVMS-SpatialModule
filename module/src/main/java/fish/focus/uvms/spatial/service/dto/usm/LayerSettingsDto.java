/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.service.dto.usm;

import java.util.ArrayList;
import java.util.List;

public class LayerSettingsDto {

    private List<LayersDto> baseLayers;

    private List<LayersDto> portLayers;

    private List<LayersDto> additionalLayers;

    private List<LayerAreaDto> areaLayers;

    public LayerSettingsDto() {}

    public LayerSettingsDto(List<LayersDto> baseLayers, List<LayersDto> portLayers, List<LayersDto> additionalLayers, List<LayerAreaDto> areaLayers) {
        this.baseLayers = baseLayers;
        this.portLayers = portLayers;
        this.additionalLayers = additionalLayers;
        this.areaLayers = areaLayers;
    }

    public List<LayerAreaDto> getAreaLayers() {
        return areaLayers;
    }

    public void setAreaLayers(List<LayerAreaDto> areaLayers) {
        this.areaLayers = areaLayers;
    }

    public List<LayersDto> getAdditionalLayers() {
        return additionalLayers;
    }

    public void setAdditionalLayers(List<LayersDto> additionalLayers) {
        this.additionalLayers = additionalLayers;
    }

    public List<LayersDto> getBaseLayers() {
        return baseLayers;
    }

    public void setBaseLayers(List<LayersDto> baseLayers) {
        this.baseLayers = baseLayers;
    }

    public List<LayersDto> getPortLayers() {
        return portLayers;
    }

    public void setPortLayers(List<LayersDto> portLayers) {
        this.portLayers = portLayers;
    }

    public void addBaseLayer(LayersDto layersDto) {
        if (baseLayers == null) {
            baseLayers = new ArrayList<>();
        }
        baseLayers.add(layersDto);
    }

    public  void addPortLayer(LayersDto layersDto) {
        if (portLayers == null) {
            portLayers = new ArrayList<>();
        }
        portLayers.add(layersDto);
    }

    public void addAdditionalLayer(LayersDto layersDto) {
        if (additionalLayers == null) {
            additionalLayers = new ArrayList<>();
        }
        additionalLayers.add(layersDto);
    }

    public void addAreaLayer(LayerAreaDto layerAreaDto) {
        if (areaLayers == null) {
            areaLayers = new ArrayList<>();
        }
        areaLayers.add(layerAreaDto);
    }
}