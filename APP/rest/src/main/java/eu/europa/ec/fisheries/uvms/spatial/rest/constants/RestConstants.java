/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries � European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.rest.constants;

public final class RestConstants {

    private RestConstants(){}

    public static final String REST_URL = "/rest";
    public static final String IMAGE_URL = "/image";
    public static final String SERVICE_LAYER_PATH = "/servicelayer";

    public static final String MODULE_NAME = "spatial";

    public static final String VIEW = "view";
    public static final String LOCATION_TYPE = "locationType";
    public static final String PUBLIC = "PUBLIC";

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-ControlDto-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_METHODS_ALL = "*";
    
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-ControlDto-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOWED_METHODS = "GET, POST, DELETE, PUT, OPTIONS";
    
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-ControlDto-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS_ALL = "Content-Type";
}