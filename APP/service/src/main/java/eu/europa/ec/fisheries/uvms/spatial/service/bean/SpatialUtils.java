package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PointType;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

@Slf4j
public class SpatialUtils {

    private static final String EPSG = "EPSG:";
    public static final int DEFAULT_SRID = 4326;

    private SpatialUtils() {
    }

    static Point convertToPointInWGS84(PointType schemaPoint) {

        Integer crs = DEFAULT_SRID;

        if (schemaPoint.getCrs() != null){
            crs = schemaPoint.getCrs();
        }

        return convertToPointInWGS84(schemaPoint.getLongitude(), schemaPoint.getLatitude(), crs);
    }

    static Geometry translate(Double tx, Double ty, Geometry geometry) {

        AffineTransform translate= AffineTransform.getTranslateInstance(tx, ty);
        Coordinate[] source = geometry.getCoordinates();
        Coordinate[] target = new Coordinate[source.length];

        for (int i= 0; i < source.length; i++){
            Coordinate sourceCoordinate = source[i];
            Point2D p = new Point2D.Double(sourceCoordinate.x,sourceCoordinate.y);
            Point2D transform = translate.transform(p, null);
            target[i] = new Coordinate(transform.getX(), transform.getY());
        }

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Geometry targetGeometry;

        if (geometry instanceof Point){
            targetGeometry = geometryFactory.createPoint(target[0]);
        }
        else if (geometry instanceof Polygon){
           targetGeometry = geometryFactory.createPolygon(target);
        }
        else {
            throw new UnsupportedOperationException("Geometry type not supported");
        }

        return targetGeometry;
    }

    static Point convertToPointInWGS84(double lon, double lat, int crs) {
        try {

            GeometryFactory gf = new GeometryFactory();
            Point point = gf.createPoint(new Coordinate(lon, lat));

            if (!isDefaultCrs(crs)) {

                CoordinateReferenceSystem inputCrs = CRS.decode(EPSG + crs);
                MathTransform mathTransform = CRS.findMathTransform(inputCrs, DefaultGeographicCRS.WGS84, false);
                point = (Point) JTS.transform(point, mathTransform);

            }

            point.setSRID(DEFAULT_SRID);
            return point;
        } catch (FactoryException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.NO_SUCH_CRS_CODE_ERROR, String.valueOf(crs), ex);
        } catch (MismatchedDimensionException | TransformException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, ex);
        }
    }

    public static boolean isDefaultCrs(int crs) {
        return crs == DEFAULT_SRID;
    }

    public static Map<String, List<Property>> readShapeFile(Path shapeFilePath, CoordinateReferenceSystem sourceCRS) throws IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("url", shapeFilePath.toUri().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        Map<String, List<Property>> geometries = new HashMap<>();
        String typeName = dataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(Filter.INCLUDE);
        FeatureIterator<SimpleFeature> iterator = collection.features();

        try {

            CoordinateReferenceSystem targetCRS = CRS.decode(EPSG + DEFAULT_SRID);
            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                geometries.put(feature.getID(), new ArrayList<>(feature.getProperties()));
                transformCRSToDefault(feature, sourceCRS, targetCRS, transform);
            }
            return geometries;

        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, e);
        }
        finally {
            iterator.close();
            dataStore.dispose();
        }
    }

    public static List<UploadProperty> readAttribute(Path shapeFilePath) throws IOException {

        List<UploadProperty> properties = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("url", shapeFilePath.toUri().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        String typeName = dataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(Filter.INCLUDE);
        FeatureIterator<SimpleFeature> iterator = collection.features();
        SimpleFeature next = iterator.next();

        try {

            List<AttributeDescriptor> attributeDescriptors = next.getFeatureType().getAttributeDescriptors();
            for (AttributeDescriptor attributeDescriptor : attributeDescriptors){
                String localPart = attributeDescriptor.getName().getLocalPart();
                switch (localPart){
                    case "the_geom":
                    case "geom":
                    case "name":
                    case "code":
                    case "gid":
                    case "enabled":
                    case "enabled_on":
                        break;
                    default:
                        properties.add(new UploadProperty().withName(localPart).withType(attributeDescriptor.getType().getBinding().getSimpleName())); // TODO nullpointer checks
                }
            }
            return properties;

        } finally {
            iterator.close();
            dataStore.dispose();
        }
    }

    private static void transformCRSToDefault(SimpleFeature feature, CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS, MathTransform transform) throws FactoryException, TransformException {
        Geometry sourceGeometry = (Geometry) feature.getDefaultGeometry();
        if (sourceGeometry != null) {
            if (sourceCRS != targetCRS) {
                Geometry targetGeometry = JTS.transform(sourceGeometry, transform);
                targetGeometry.setSRID(DEFAULT_SRID);
                feature.setDefaultGeometry(targetGeometry);
            } else {
                sourceGeometry.setSRID(DEFAULT_SRID);
            }
        }
    }

}
