/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package fish.focus.uvms.spatial.service.dto.config;

import java.util.ArrayList;
import java.util.List;
import fish.focus.uvms.spatial.service.dto.ProjectionDto;

public class MapDto {

    private ProjectionDto projection;
    private List<ControlDto> control = new ArrayList<ControlDto>();
    private List<TbControlDto> tbControl = new ArrayList<TbControlDto>();
    private ServiceLayersDto layers;
    private RefreshDto refresh;

    /**
     * No args constructor for use in serialization
     * 
     */
    public MapDto() {}

    public MapDto(ProjectionDto projectionDto, List<ControlDto> controlDtos, List<TbControlDto> tbControlDtos, ServiceLayersDto serviceLayersDto, RefreshDto refreshDto) {
        this.projection = projectionDto;
        this.control = controlDtos;
        this.tbControl = tbControlDtos;
        this.layers = serviceLayersDto;
        this.refresh = refreshDto;
    }

    public ProjectionDto getProjection() {
        return projection;
    }

    public void setProjection(ProjectionDto projectionDto) {
        this.projection = projectionDto;
    }

    public List<ControlDto> getControl() {
        return control;
    }

    public void setControl(List<ControlDto> controlDtos) {
        this.control = controlDtos;
    }

    public List<TbControlDto> getTbControl() {
        return tbControl;
    }

    public void setTbControl(List<TbControlDto> tbControlDtos) {
        this.tbControl = tbControlDtos;
    }

    public ServiceLayersDto getLayers() {
        return layers;
    }

    public void setLayers(ServiceLayersDto serviceLayersDto) {
        this.layers = serviceLayersDto;
    }

    public RefreshDto getRefresh() {
        return refresh;
    }

    public void setRefresh(RefreshDto refreshDto) {
        this.refresh = refreshDto;
    }
}