/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.service.dto.usm;

import java.util.List;

public class VisibilityAttributesDto {

    private Boolean isAttributeVisible;

    private List<String> order;

    private List<String> values;

    public VisibilityAttributesDto(){}

    public VisibilityAttributesDto(List<String> order, List<String> values, Boolean isAttributeVisible) {
        this.order = order;
        this.values = values;
        this.isAttributeVisible = isAttributeVisible;
    }

    public List<String> getOrder() {
        return order;
    }

    public void setOrder(List<String> order) {
        this.order = order;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public Boolean isAttributeVisible() {
        return isAttributeVisible;
    }

    public void setIsAttributeVisible(Boolean isAttributeVisible) {
        this.isAttributeVisible = isAttributeVisible;
    }
}