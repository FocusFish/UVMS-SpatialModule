package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public class Oracle extends AbstractGisFunction {

    @Override
    public String stIntersects(Double latitude, Double longitude) {
        return "SDO_RELATE(GEOM, SDO_GEOMETRY('POINT(" + longitude + " " + latitude + ")', 8307), 'mask=contains') = 'TRUE' AND enabled = 'Y'";
    }

    @Override
    public String closestAreaToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit) {

     return "(SELECT '" + typeName + "' AS type, gid, code, name," + " SDO_NN_DISTANCE(1) AS dist, " +
             "geom AS closest FROM spatial." + tableName + " WHERE enabled = 'Y' " +
             "AND SDO_NN(geom, SDO_GEOMETRY('POINT(" + longitude + " " + latitude + ")', 8307), " +
             "'sdo_num_res=" + limit + "', 1) = 'TRUE' ORDER BY dist ASC)";
    }

    @Override
    public String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit) {

        return "(SELECT '" + typeName + "' as type, gid, code, name, geom, "
                + stDistance(latitude, longitude) + " AS distance " +
                "FROM spatial." + tableName + " WHERE enabled = 'Y' " +
                "ORDER BY distance ASC OFFSET 0 ROWS FETCH NEXT " + limit + " ROWS ONLY)";
    }

    private String stDistance(Double latitude, Double longitude) {
        return "SDO_GEOM.SDO_DISTANCE(geom, SDO_GEOMETRY('POINT(" + longitude + " " + latitude + ")', 8307), 0.05, 'unit=M')";
    }
}
