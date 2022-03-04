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

public class VisibilityPositionsDto {

    @NotNull
    private VisibilityAttributesDto popup;

    @NotNull
    private VisibilityAttributesDto labels;

    @NotNull
    private VisibilityAttributesDto table;

    public VisibilityPositionsDto() {}

    public VisibilityPositionsDto(VisibilityAttributesDto popup, VisibilityAttributesDto labels, VisibilityAttributesDto table) {
        this.popup = popup;
        this.labels = labels;
        this.table = table;
    }

    public VisibilityAttributesDto getPopup() {
        return popup;
    }

    public void setPopup(VisibilityAttributesDto popup) {
        this.popup = popup;
    }

    public VisibilityAttributesDto getLabels() {
        return labels;
    }

    public void setLabels(VisibilityAttributesDto labels) {
        this.labels = labels;
    }

    public VisibilityAttributesDto getTable() {
        return table;
    }

    public void setTable(VisibilityAttributesDto table) {
        this.table = table;
    }
}