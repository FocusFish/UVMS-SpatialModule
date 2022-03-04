/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.rest.resources.secured;

import fish.focus.uvms.commons.rest.dto.ResponseDto;
import fish.focus.uvms.commons.rest.resource.UnionVMSResource;
import fish.focus.uvms.exception.ServiceException;
import fish.focus.uvms.rest.security.bean.USMService;
import fish.focus.uvms.spatial.service.bean.MapConfigServiceBean;
import fish.focus.uvms.spatial.service.dao.ProjectionDao;
import fish.focus.uvms.spatial.service.dto.ProjectionDto;
import fish.focus.uvms.spatial.service.dto.config.ConfigDto;
import fish.focus.uvms.spatial.service.dto.config.ConfigResourceDto;
import fish.focus.uvms.spatial.service.dto.config.MapConfigDto;
import fish.focus.uvms.spatial.service.dto.usm.ConfigurationDto;
import fish.focus.uvms.spatial.service.dto.usm.USMSpatial;
import fish.focus.uvms.spatial.service.utils.ProjectionMapper;
import fish.focus.wsdl.user.types.Dataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
@Path("/config")
@Stateless
public class ConfigResource extends UnionVMSResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigResource.class);


    private static final String DEFAULT_CONFIG = "DEFAULT_CONFIG";
    private static final String USER_CONFIG = "USER_CONFIG";

    @Inject
    private ProjectionDao projectionDao;

    @Inject
    private MapConfigServiceBean mapConfigServiceBean;

    @Inject
    private USMService usmService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public ResponseDto<MapConfigDto> getReportMapConfig(@PathParam("id") int id, ConfigResourceDto config, @HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        final String username = servletRequest.getRemoteUser();
        String applicationName = servletRequest.getServletContext().getInitParameter("usmApplication");
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        LOG.info("Getting mapDefaultSRIDToEPSG configuration for report with id = {}", id);

        List<Dataset> permittedServiceLayerDatasets = usmService.getDatasetsPerCategory(USMSpatial.CATEGORY_SERVICE_LAYER, username, USMSpatial.APPLICATION_NAME, roleName, scopeName);

        Collection<String> permittedLayersNames = new HashSet<>(permittedServiceLayerDatasets.size());
        for(Dataset dataset : permittedServiceLayerDatasets) {
            permittedLayersNames.add(dataset.getName());
        }

        MapConfigDto mapConfig = mapConfigServiceBean.getReportConfig(id, userPref, adminPref, username, scopeName, config.getTimeStamp(), permittedLayersNames);
        return new ResponseDto<>(mapConfig, HttpServletResponse.SC_OK);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/basic")
    public ResponseDto<MapConfigDto> getBasicReportMapConfig(@HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        final String username = servletRequest.getRemoteUser();
        String applicationName = servletRequest.getServletContext().getInitParameter("usmApplication");
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        MapConfigDto mapConfig = mapConfigServiceBean.getBasicReportConfig(userPref, adminPref);
        return new ResponseDto<>(mapConfig, HttpServletResponse.SC_OK);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/admin")
    public ResponseDto<ConfigurationDto> getAdminPreferences(@HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        String applicationName = servletRequest.getServletContext().getInitParameter("usmApplication");
        String adminConfig = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);

        List<Dataset> permittedServiceLayerDatasets = usmService.getDatasetsPerCategory(USMSpatial.CATEGORY_SERVICE_LAYER, servletRequest.getRemoteUser(), USMSpatial.APPLICATION_NAME, roleName, scopeName);

        Collection<String> permittedLayersNames = new HashSet<>(permittedServiceLayerDatasets.size());
        for(Dataset dataset : permittedServiceLayerDatasets) {
            permittedLayersNames.add(dataset.getName());
        }

        return new ResponseDto<>(mapConfigServiceBean.retrieveAdminConfiguration(adminConfig, permittedLayersNames), HttpServletResponse.SC_OK);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user")
    public ResponseDto<ConfigurationDto> getUserPreferences(@HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        String applicationName = servletRequest.getServletContext().getInitParameter("usmApplication");
        final String username = servletRequest.getRemoteUser();
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        List<Dataset> permittedServiceLayerDatasets = usmService.getDatasetsPerCategory(USMSpatial.CATEGORY_SERVICE_LAYER, servletRequest.getRemoteUser(), USMSpatial.APPLICATION_NAME, roleName, scopeName);

        Collection<String> permittedLayersNames = new HashSet<>(permittedServiceLayerDatasets.size());
        for(Dataset dataset : permittedServiceLayerDatasets) {
            permittedLayersNames.add(dataset.getName());
        }

        return new ResponseDto<>(mapConfigServiceBean.retrieveUserConfiguration(userPref, adminPref, username, permittedLayersNames), HttpServletResponse.SC_OK);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/report")
    public ResponseDto<ConfigDto> getReportConfig(@HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        final String username = servletRequest.getRemoteUser();
        String applicationName = servletRequest.getServletContext().getInitParameter("usmApplication");
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        return new ResponseDto<>(mapConfigServiceBean.getReportConfigWithoutMap(userPref, adminPref), HttpServletResponse.SC_OK);
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/projections")
    public ResponseDto<List<ProjectionDto>> getAllProjections() {
        LOG.info("Getting all projections");
        List<ProjectionDto> projections = ProjectionMapper.mapToProjectionDto(projectionDao.findAll());
        return new ResponseDto<>(projections, HttpServletResponse.SC_OK);
    }



}