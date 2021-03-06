package fish.focus.uvms.spatial.rest.resources.unsecured;

import fish.focus.uvms.commons.date.JsonBConfigurator;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("spatialnonsecure")
public class RestActivatorSpatial extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(JsonBConfigurator.class);
        resources.add(SpatialRestResource.class);
        return resources;
    }

}
