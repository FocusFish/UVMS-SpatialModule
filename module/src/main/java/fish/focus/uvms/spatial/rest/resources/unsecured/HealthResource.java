/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package fish.focus.uvms.spatial.rest.resources.unsecured;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fish.focus.uvms.spatial.service.bean.PropertiesBean;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class HealthResource {

    private static final Logger log = LoggerFactory.getLogger(HealthResource.class);


    private static final String APPLICATION_VERSION = "application.version";
    private static final String APPLICATION_NAME = "application.name";

    @EJB
    private PropertiesBean propertiesBean;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getDoctor() {
        Map<String, Object> properties = new HashMap<>();
        Map<String, Map<String, Object>> metrics = new HashMap<>();
        properties.put(APPLICATION_VERSION, propertiesBean.getProperty(APPLICATION_VERSION));
        metrics.put(propertiesBean.getProperty(APPLICATION_NAME), properties);
        return Response.ok(metrics).build();
    }
}
