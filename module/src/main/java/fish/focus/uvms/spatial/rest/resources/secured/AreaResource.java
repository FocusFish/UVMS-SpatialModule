/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package fish.focus.uvms.spatial.rest.resources.secured;

import fish.focus.uvms.spatial.model.schemas.*;
import fish.focus.uvms.spatial.service.bean.AreaServiceBean;
import fish.focus.uvms.spatial.service.dao.AreaLocationTypesDao;
import fish.focus.uvms.spatial.service.dto.BaseAreaDto;
import fish.focus.uvms.spatial.service.dto.PortDistanceInfoDto;
import fish.focus.uvms.spatial.service.entity.AreaLocationTypesEntity;
import fish.focus.uvms.spatial.service.utils.AreaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
@Path("/area")
@Stateless
public class AreaResource {

    private static final Logger log = LoggerFactory.getLogger(AreaResource.class);

    @Inject
    private AreaServiceBean areaServiceBean;

    @Inject
    private AreaLocationTypesDao areaLocationTypesDao;


    /**
     * Return the list of all area types.
     *
     *
     * @responseMessage 200 ok
     * @responseMessage 404 not found
     */
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/types")
    public Response getAreaTypes() {
        log.info("Getting user areas list");
        List<AreaLocationTypesEntity> areaList = areaLocationTypesDao.findByIsLocation(false);
        List<String> response = new ArrayList<>();
        for (AreaLocationTypesEntity entity: areaList) {
            response.add(entity.getTypeName());
        }
        return Response.ok(response).build();
    }


    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/location/details")
    public ClosestLocationSpatialRS getLocationByPoint(ClosestLocationSpatialRQ request) {

        ClosestLocationSpatialRS response = new ClosestLocationSpatialRS();
        PortDistanceInfoDto closestPort = areaServiceBean.findClosestPortByPosition(request.getPoint().getLatitude(), request.getPoint().getLongitude());

        if (closestPort != null){
            Location location = new Location(String.valueOf(closestPort.getPort().getId()), String.valueOf(closestPort.getPort().getId()), LocationType.PORT, closestPort.getPort().getCode(), closestPort.getPort().getName(), closestPort.getDistance(), UnitType.METERS, closestPort.getPort().getCentroid(), closestPort.getPort().getGeometryWKT(), closestPort.getPort().getExtent(), closestPort.getPort().getEnabled(), closestPort.getPort().getCountryCode());
            ClosestLocationsType locationType = new ClosestLocationsType();
            locationType.getClosestLocations().add(location);
            response.setClosestLocations(locationType);
        }

        return response;

    }


    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/details")
    public AreaByLocationSpatialRS getAreasByPoint(AreaByLocationSpatialRQ request) {

        AreaByLocationSpatialRS response = new AreaByLocationSpatialRS();
        List<BaseAreaDto> areaList = areaServiceBean.getAreasByPoint(request.getPoint().getLatitude(), request.getPoint().getLatitude());
        if(areaList != null){
            List<AreaExtendedIdentifierType> areaExtendedIdentifierTypes = AreaMapper.mapToAreaExtendedIdentifierType(areaList);
            AreasByLocationType areasByLocationType = new AreasByLocationType();
            areasByLocationType.getAreas().addAll(areaExtendedIdentifierTypes);
            response.setAreasByLocation(areasByLocationType);
        }

        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/type/code")
    public Response byCode(AreaByCodeRequest areaByCodeRequest) {

        List<AreaSimpleType> areaSimpleTypeList = areaServiceBean.getAreasByCode(areaByCodeRequest);
        AreaByCodeResponse response = new AreaByCodeResponse();
        response.setAreaSimples(areaSimpleTypeList);
        return Response.ok(response).build();
    }



    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/layers")
    public Response getSystemAreaLayerMapping(@Context HttpServletRequest request)  {
        return Response.ok(areaLocationTypesDao.findSystemAreaLayerMapping()).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/allNonUserAreas")
    public Response getAllNonUserAreas(){
        return Response.ok(areaServiceBean.getAllNonUserAreaTypes()).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/getAreaLayer/{areaType}")
    public Response getAreaLayer(@PathParam("areaType") String areaType){
        return Response.ok(areaServiceBean.getAllAreasOfType(AreaType.fromValue(areaType))).build();
    }

}