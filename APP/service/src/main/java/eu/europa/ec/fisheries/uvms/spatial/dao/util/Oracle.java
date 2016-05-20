package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public class Oracle extends AbstractGisFunction {

    @Override
    public String stIntersects(Double latitude, Double longitude) {
        return "SDO_GEOM.SDO_OVERLAPBDYINTERSECT('geom'," +
               "SDO_UTIL.FROM_WKTGEOMETRY('POINT(" + longitude + " " + latitude + ")')) = 'TRUE' ";
    }

    private String stDistance(Double latitude, Double longitude) {
        return "SDO_GEOM.SDO_DISTANCE('geom', SDO_UTIL.FROM_WKTGEOMETRY('POINT(" + longitude + " " + latitude + ")'), 0.005)'";
    }

    @Override
    public String closestAreaToPoint(String typeName, String tableName, Double latitude, Double longitude) {
        return null;
    }

    @Override
    public String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit) {

        return "(SELECT '" + typeName + "' as type, gid, code, name, geom, "
                + stDistance(latitude, longitude) + " AS distance " +
                "FROM spatial." + tableName + " WHERE enabled = 'Y' AND ROWNUM <= " + limit +
                " ORDER BY distance ASC)";
    }
}
