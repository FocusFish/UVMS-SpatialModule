/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.rest.resources.secured;

import fish.focus.uvms.commons.date.JsonBConfigurator;
import fish.focus.uvms.spatial.rest.constants.RestConstants;
import fish.focus.uvms.spatial.rest.util.SpatialRestExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath(RestConstants.REST_URL)
public class RestActivator extends Application {

    final static Logger LOG = LoggerFactory.getLogger(RestActivator.class);

    private final Set<Object> singletons = new HashSet<>();
    private final Set<Class<?>> set = new HashSet<>();

    public RestActivator() {
        set.add(AreaResource.class);
        set.add(XMLResource.class);
        set.add(ConfigResource.class);
        set.add(UserAreaRestResource.class);
        set.add(ImageResource.class);
        set.add(GeometryUtilsResource.class);
        set.add(FileUploadRestResource.class);
        set.add(JsonBConfigurator.class);
        set.add(SpatialRestExceptionMapper.class);
        LOG.info(RestConstants.MODULE_NAME + " module starting up");
    }

    @Override
    public Set<Class<?>> getClasses() {
        return set;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}