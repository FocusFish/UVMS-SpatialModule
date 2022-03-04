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

public class MapSettingsDto {

    @NotNull
    private boolean refreshStatus;

    @NotNull
    private String scaleBarUnits;

    @NotNull
    private String coordinatesFormat;

    @NotNull
    private int mapProjectionId;

    @NotNull
    private int refreshRate;

    @NotNull
    private int displayProjectionId;

    public MapSettingsDto() {}

    public MapSettingsDto(boolean refreshStatus, String scaleBarUnits, String coordinatesFormat, int mapProjectionId, int refreshRate, int displayProjectionId) {
        this.refreshRate = refreshRate;
        this.scaleBarUnits =scaleBarUnits;
        this.coordinatesFormat = coordinatesFormat;
        this.mapProjectionId = mapProjectionId;
        this.refreshStatus = refreshStatus;
        this.displayProjectionId = displayProjectionId;
    }

    public boolean getRefreshStatus() {
        return refreshStatus;
    }

    public void setRefreshStatus(boolean refreshStatus) {
        this.refreshStatus = refreshStatus;
    }

    public String getScaleBarUnits() {
        return scaleBarUnits;
    }

    public void setScaleBarUnits(String scaleBarUnits) {
        this.scaleBarUnits = scaleBarUnits;
    }

    public String getCoordinatesFormat() {
        return coordinatesFormat;
    }

    public void setCoordinatesFormat(String coordinatesFormat) {
        this.coordinatesFormat = coordinatesFormat;
    }

    public int getMapProjectionId() {
        return mapProjectionId;
    }

    public void setMapProjectionId(int mapProjectionId) {
        this.mapProjectionId = mapProjectionId;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }

    public int getDisplayProjectionId() {
        return displayProjectionId;
    }

    public void setDisplayProjectionId(int displayProjectionId) {
        this.displayProjectionId = displayProjectionId;
    }

    @Override
    public String toString() {
        return "ClassPojo [refreshState = " + refreshStatus + ", scaleBarUnits = " + scaleBarUnits + ", coordinatesFormat = " + coordinatesFormat + ", mapProjection = " + mapProjectionId + ", refreshRate = " + refreshRate + ", displayProjection = " + displayProjectionId + "]";
    }
}