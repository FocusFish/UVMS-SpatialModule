package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.CalculateService;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.geometry.jts.WKTWriter2;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Slf4j
@Stateless
@Local(CalculateService.class)
public class CalculationServiceBean implements CalculateService {

    @Override
    public String calculateBuffer(final Double latitude, final Double longitude, final Double buffer) {

        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(longitude, latitude));
        Geometry geometry = point.buffer(buffer);
        return new WKTWriter2().write(geometry);

    }

    @Override
    public String translate(final Double tx, final Double ty, final String wkt) throws ServiceException {

        try {

            Geometry geometry = new WKTReader2().read(wkt);
            Geometry translate = SpatialUtils.translate(tx, ty, geometry);
            return new WKTWriter2().write(translate);

        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
}
