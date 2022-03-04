/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package fish.focus.uvms.spatial.service.dto.config;

public class LayerDto {

    private String type;

    private String areaType;

    private String title;

    private Boolean isBaseLayer;

    private String shortCopyright;

    private String longCopyright;

    private String url;

    private String serverType;

    private String layerGeoName;

    private StylesDto styles;

    private Long gid;

    private String name;

    private String apiKey;

    private String cqlAll;

    private String cqlActive;

    private String cql;

    private Boolean warning;

    private String typeName;

    /**
     * No args constructor for use in serialization
     */
    public LayerDto() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsBaseLayer() {
        return isBaseLayer;
    }

    public void setIsBaseLayer(Boolean isBaseLayer) {
        this.isBaseLayer = isBaseLayer;
    }

    public String getShortCopyright() {
        return shortCopyright;
    }

    public void setShortCopyright(String shortCopyright) {
        this.shortCopyright = shortCopyright;
    }

    public String getLongCopyright() {
        return longCopyright;
    }

    public void setLongCopyright(String longCopyright) {
        this.longCopyright = longCopyright;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getLayerGeoName() {
        return layerGeoName;
    }

    public void setLayerGeoName(String layerGeoName) {
        this.layerGeoName = layerGeoName;
    }

    public StylesDto getStyles() {
        return styles;
    }

    public void setStyles(StylesDto styles) {
        this.styles = styles;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCqlAll() {
        return cqlAll;
    }

    public void setCqlAll(String cqlAll) {
        this.cqlAll = cqlAll;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getCqlActive() {
        return cqlActive;
    }

    public void setCqlActive(String cqlActive) {
        this.cqlActive = cqlActive;
    }

    public String getCql() {
        return cql;
    }

    public void setCql(String cql) {
        this.cql = cql;
    }

    public Boolean isWarning() {
        return warning;
    }

    public void setWarning(Boolean isWarning) {
        this.warning = isWarning;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}