package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.entity.ExclusiveEconomicZone;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/spatial")
@Stateless
public class SpatialResource {

    final static Logger LOG = LoggerFactory.getLogger(SpatialResource.class);

    @EJB
    SpatialService spatialService;

    @EJB
    private CrudService crudService;

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("getEezById")
    public ResponseDto getEezById(int eezId) {
        try {
            LOG.info("Getting user areas list");
            return new ResponseDto(crudService.find(ExclusiveEconomicZone.class, eezId), ResponseCode.OK);
        } catch (Exception e) {
            LOG.error("[ Error when getting vessel list. ] ", e);// TODO veesel list?
            throw new RuntimeException("Please fix it");
        }
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("getAreaTypes")
    public ResponseDto getAreaTypes() {
        try {
            LOG.info("Getting user areas list");
            return null;//new ResponseDto(spatialService.getAreaTypes(), ResponseCode.OK);
        } catch (Exception e) {
            LOG.error("[ Error when getting vessel list. ] ", e); //TODO veesel list?
            throw new RuntimeException("Please fix it");
        }
    }
}
