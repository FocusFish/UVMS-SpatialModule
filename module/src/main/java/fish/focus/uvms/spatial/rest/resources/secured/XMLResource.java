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
import fish.focus.uvms.spatial.service.dto.BaseAreaDto;
import fish.focus.uvms.spatial.service.utils.AreaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
@Path("/xml")
public class XMLResource {

    private static final Logger log = LoggerFactory.getLogger(XMLResource.class);


    @Inject
    private AreaServiceBean areaServiceBean;

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/enrichment")
    public SpatialEnrichmentRS spatialEnrichment(SpatialEnrichmentRQ request){
        return areaServiceBean.getSpatialEnrichment(request);
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/filter-areas")
    public FilterAreasSpatialRS computeAreaFilter(FilterAreasSpatialRQ request) throws Exception{
        //return areaService.computeAreaFilter(request);
        return null;
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/areas-by-location")
    public AreaByLocationSpatialRS getAreasByPoint(AreaByLocationSpatialRQ request)  throws Exception {
        AreaByLocationSpatialRS response = new AreaByLocationSpatialRS();
        List<BaseAreaDto> areaList = areaServiceBean.getAreasByPoint(request.getPoint().getLatitude(), request.getPoint().getLongitude());
        List<AreaExtendedIdentifierType> areaTypesByLocation = AreaMapper.mapToAreaExtendedIdentifierType(areaList);
        if(areaList != null){

            AreasByLocationType areasByLocationType = new AreasByLocationType();
            areasByLocationType.getAreas().addAll(areaTypesByLocation);
            response.setAreasByLocation(areasByLocationType);
        }
        return response;
    }

}