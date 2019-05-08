/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
public class GeometryUtilsResource extends UnionVMSResource {


    private static final Logger log = LoggerFactory.getLogger(GeometryUtilsResource.class);

    @EJB
    private SpatialService service;

    @EJB
    private AreaService areaService;

    @EJB
    private SpatialRepository repository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/buffer")
    public Response buffer(Map<String, Object> payload) {

        Response response;

        try {
            Double latitude = Double.valueOf(String.valueOf(payload.get("latitude")));
            Double longitude = Double.valueOf(String.valueOf(payload.get("longitude")));
            Double buffer = Double.valueOf(String.valueOf(payload.get("buffer")));
            response = createSuccessResponse(service.calculateBuffer(latitude, longitude, buffer));
        } catch (Exception ex) {
            String error = "[ Error when calculating buffer. ] ";
            log.error(error, ex);
            response = createErrorResponse(error);
        }

        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/transform")
    public Response transform(Map<String, Object> payload){

        Response response = null;
        Geometry translate;

        try {
            Double latitude = Double.valueOf(String.valueOf(payload.get("x")));
            Double longitude = Double.valueOf(String.valueOf(payload.get("y")));
            String wkt = String.valueOf(String.valueOf(payload.get("wkt")));
            //Geometry geometry = new WKTReader2().read(wkt);
            //translate = GeometryUtils2.transform(latitude, longitude, geometry);
            //response = createSuccessResponse(new WKTWriter2().write(translate));
        }
        catch (Exception ex){
            String error = "[ Error when translating. ] ";
            log.error(error, ex);
            response = createErrorResponse(error);
        }
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/translate")
    public Response translateToDefault(Map<String, Object> payload){

        Response response;
        Geometry translate = null;

        try {
            Double longitude = Double.valueOf(String.valueOf(payload.get("x")));
            Double latitude = Double.valueOf(String.valueOf(payload.get("y")));
            Integer sourceCode = Integer.valueOf(String.valueOf(payload.get("sourceCode")));
           // translate = GeometryUtils2.toGeographic(latitude, longitude, sourceCode);
            final Double x = translate.getCoordinates()[0].x;
            final Double y = translate.getCoordinates()[0].y;
            final Integer srid = translate.getSRID();
            final Map<String, Object> result = new HashMap<>();
            result.put("lon", x);
            result.put("lat", y);
            result.put("srid", srid);
            //result.put("wkt", new WKTWriter2().write(translate));
            response = createSuccessResponse(result);
        }
        catch (Exception ex){
            String error = "[ Error when translating. ] ";
            log.error(error, ex);
            response = createErrorResponse(error);
        }
        return response;
    }
}