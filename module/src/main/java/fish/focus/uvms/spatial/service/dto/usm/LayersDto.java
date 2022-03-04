/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.service.dto.usm;

public class LayersDto implements Comparable<LayersDto> {

    private String name;

    private String serviceLayerId;

    private String subType;

    private Long order;

    private String areaLocationTypeName;
    
    public LayersDto() {}

    public LayersDto(String name, String serviceLayerId, String subType, Long order) {
        this.name = name;
        this.serviceLayerId = serviceLayerId;
        this.subType = subType;
        this.order = order;
    }

    public LayersDto(String serviceLayerId, Long order) {
        this.serviceLayerId = serviceLayerId;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceLayerId() {
        return serviceLayerId;
    }

    public void setServiceLayerId(String serviceLayerId) {
        this.serviceLayerId = serviceLayerId;
    }

    @Override
    public String toString() {
        return "ClassPojo [type = " + name + ", serviceLayerId = " + serviceLayerId + "]";
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getAreaLocationTypeName() {
        return areaLocationTypeName;
    }

    public void setAreaLocationTypeName(String areaLocationTypeName) {
        this.areaLocationTypeName = areaLocationTypeName;
    }

    @Override
    public int compareTo(LayersDto layersDto) {
        if (this.getOrder() == null || layersDto.getOrder() == null) {
            return 1;
        }
        return Long.compare(this.getOrder(), layersDto.getOrder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LayersDto layersDto = (LayersDto) o;

        if (name != null ? !name.equals(layersDto.name) : layersDto.name != null) return false;
        if (serviceLayerId != null ? !serviceLayerId.equals(layersDto.serviceLayerId) : layersDto.serviceLayerId != null)
            return false;
        if (subType != null ? !subType.equals(layersDto.subType) : layersDto.subType != null) return false;
        if (order != null ? !order.equals(layersDto.order) : layersDto.order != null) return false;
        return !(areaLocationTypeName != null ? !areaLocationTypeName.equals(layersDto.areaLocationTypeName) : layersDto.areaLocationTypeName != null);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + serviceLayerId.hashCode();
        result = 31 * result + subType.hashCode();
        result = 31 * result + areaLocationTypeName.hashCode();
        return result;
    }
}