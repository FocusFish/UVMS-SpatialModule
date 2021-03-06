/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.rest.resources.secured;

import fish.focus.uvms.commons.rest.resource.UnionVMSResource;
import fish.focus.uvms.spatial.rest.dto.FileUploadForm;
import fish.focus.uvms.spatial.service.bean.FileUploadBean;
import fish.focus.uvms.spatial.service.dto.upload.AreaUploadMapping;
import fish.focus.uvms.spatial.service.dto.upload.AreaUploadMetadata;
import fish.focus.uvms.spatial.service.entity.AreaUpdateEntity;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/files")
public class FileUploadRestResource extends UnionVMSResource {

    @Inject
    private FileUploadBean fileUploadBean;

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Path("/upload/{code}")
    public Response upload(AreaUploadMapping mapping,
                           @PathParam("code") int code) throws IOException {

        fileUploadBean.upsertReferenceData(mapping,  code);
        return createSuccessResponse();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/metadata")
    public Response metadata(@MultipartForm FileUploadForm form, @Context HttpServletRequest servletRequest) {

        AreaUpdateEntity updateEntity = fileUploadBean.saveUploadedAreaDefinitionsToDB(form.getData(), form.getAreaType(), servletRequest.getRemoteUser());
        AreaUploadMetadata metadata = fileUploadBean.getShapeFileAndAreaMetadata(updateEntity);
        return createSuccessResponse(metadata);
    }
}