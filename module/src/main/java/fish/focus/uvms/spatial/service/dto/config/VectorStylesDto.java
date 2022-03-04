/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.service.dto.config;

import fish.focus.uvms.spatial.service.dto.usm.AlarmsDto;

public class VectorStylesDto {

    private PositionDto positions;

    private SegmentDto segments;

    private AlarmsDto alarms;

    /**
     * No args constructor for use in serialization
     */
    public VectorStylesDto() {
    }

    public VectorStylesDto(PositionDto positionDto, SegmentDto segmentDto, AlarmsDto alarmsDto) {
        this.positions = positionDto;
        this.segments = segmentDto;
        this.alarms = alarmsDto;
    }

    public PositionDto getPositions() {
        return positions;
    }

    public void setPositions(PositionDto positionDto) {
        this.positions = positionDto;
    }

    public SegmentDto getSegments() {
        return segments;
    }

    public void setSegments(SegmentDto segmentDto) {
        this.segments = segmentDto;
    }

    public AlarmsDto getAlarms() {
        return alarms;
    }

    public void setAlarms(AlarmsDto alarmsDto) {
        this.alarms = alarmsDto;
    }
}