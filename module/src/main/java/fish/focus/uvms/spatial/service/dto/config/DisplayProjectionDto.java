/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package fish.focus.uvms.spatial.service.dto.config;

import fish.focus.uvms.spatial.model.schemas.CoordinatesFormat;
import fish.focus.uvms.spatial.model.schemas.ScaleBarUnits;

public class DisplayProjectionDto {

    private Integer epsgCode;

    private CoordinatesFormat formats;

    private ScaleBarUnits units;

    public DisplayProjectionDto() {
    }

    public DisplayProjectionDto(Integer epsgCode, CoordinatesFormat formats, ScaleBarUnits units) {
        this.epsgCode = epsgCode;
        this.formats = formats;
        this.units = units;
    }

    public Integer getEpsgCode() {
        return epsgCode;
    }

    public void setEpsgCode(Integer epsgCode) {
        this.epsgCode = epsgCode;
    }

    public CoordinatesFormat getFormats() {
        return formats;
    }

    public void setFormats(CoordinatesFormat formats) {
        this.formats = formats;
    }

    public ScaleBarUnits getUnits() {
        return units;
    }

    public void setUnits(ScaleBarUnits units) {
        this.units = units;
    }
}