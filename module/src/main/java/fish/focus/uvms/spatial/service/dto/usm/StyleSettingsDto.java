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

public class StyleSettingsDto {

    @NotNull
    private PositionsDto positions;

    @NotNull
    private SegmentsDto segments;

    private AlarmsDto alarms;

    public StyleSettingsDto() {}

    public StyleSettingsDto(PositionsDto positions, SegmentsDto segments) {
        this.positions = positions;
        this.segments = segments;
    }

    public PositionsDto getPositions() {
        return positions;
    }

    public void setPositions(PositionsDto positionsDto) {
        this.positions = positionsDto;
    }

    public SegmentsDto getSegments() {
        return segments;
    }

    public void setSegments(SegmentsDto segmentsDto) {
        this.segments = segmentsDto;
    }

    public AlarmsDto getAlarms() {
        return alarms;
    }

    public void setAlarms(AlarmsDto alarms) {
        this.alarms = alarms;
    }

    @Override
    public String toString() {
        return "ClassPojo [positions = " + positions + ", segments = " + segments + "]";
    }
}