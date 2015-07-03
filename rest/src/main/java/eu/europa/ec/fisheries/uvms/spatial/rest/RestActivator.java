package eu.europa.ec.fisheries.uvms.spatial.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationPath(RestConstants.MODULE_REST)
public class RestActivator extends Application {

    final static Logger LOG = LoggerFactory.getLogger(RestActivator.class);

    private final Set<Object> singletons = new HashSet<>();
    private final Set<Class<?>> set = new HashSet<>();

   /* public RestActivator() {
        set.add(RestResource.class);
        set.add(MovementSearchGroupResource.class);
        set.add(TempMovementResource.class);
        LOG.info(RestConstants.MODULE_NAME + " module starting up");
    }*/

    @Override
    public Set<Class<?>> getClasses() {
        return set;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}
