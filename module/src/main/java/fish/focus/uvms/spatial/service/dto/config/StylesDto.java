/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.service.dto.config;

public class StylesDto {

    private String geom;

    private String label;

    private String labelGeom;

    /**
     * No args constructor for use in serialization
     */
    public StylesDto() {}

    public StylesDto(String geom, String label, String labelGeom) {
        this.geom = geom;
        this.label = label;
        this.labelGeom = labelGeom;
    }

    public StylesDto(String geom) {
        this.geom = geom;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelGeom() {
        return labelGeom;
    }

    public void setLabelGeom(String labelGeom) {
        this.labelGeom = labelGeom;
    }
}