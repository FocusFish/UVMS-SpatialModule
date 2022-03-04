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

public class VisibilitySettingsDto {

    @NotNull
    private VisibilityPositionsDto positions;

    @NotNull
    private VisibilitySegmentDto segments;

    @NotNull
    private VisibilityTracksDto tracks;

    public VisibilitySettingsDto() {}

    public VisibilitySettingsDto(VisibilityPositionsDto visibilityPositionsDto, VisibilitySegmentDto visibilitySegmentDto, VisibilityTracksDto visibilityTracksDto) {
        this.positions = visibilityPositionsDto;
        this.segments = visibilitySegmentDto;
        this.tracks = visibilityTracksDto;
    }

    public VisibilityPositionsDto getPositions() {
        return positions;
    }

    public void setPositions(VisibilityPositionsDto visibilityPositionsDto) {
        this.positions = visibilityPositionsDto;
    }

    public VisibilitySegmentDto getSegments() {
        return segments;
    }

    public void setSegments(VisibilitySegmentDto visibilitySegmentDto) {
        this.segments = visibilitySegmentDto;
    }

    public VisibilityTracksDto getTracks() {
        return tracks;
    }

    public void setTracks(VisibilityTracksDto visibilityTracksDto) {
        this.tracks = visibilityTracksDto;
    }
}