package eu.europa.ec.fisheries.uvms.spatial.service.rest.dto;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.math.BigInteger;

public class EezDto {

    private static final SimpleFeatureType EEZ_FEATURE = build();

    private static final String GID = "gid";
    private static final String GEOMETRY = "geometry";
    private static final String EEZ = "eez";
    private static final String COUNTRY = "country";
    private static final String SOV_ID = "sovId";
    private static final String EEZ_ID = "eezId";
    private static final String ISO_3_DIGIT = "iso3Digit";
    private static final String MRG_ID = "mrgId";
    private static final String AREA_M_2 = "areaM2";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String MRGID_EEZ = "mrgidEez";
    private static final String SOVEREIGN = "sovereign";
    private static final String REMARKS = "remarks";

    private Geometry geometry;
    private String eez;
    private String country;
    private String sovereign;
    private String remarks;
    private String iso3Digit;
    private String dateChange;
    private Integer sovId;
    private Integer eezId;
    private Integer mrgidEez;
    private BigInteger mrgid;
    private BigInteger gid;
    private Double areaM2;
    private Double longitude;
    private Double latitude;

    private static SimpleFeatureType build() {
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84); //TODO check it
        sb.setName("EEZ");
        sb.add(GEOMETRY, MultiPolygon.class);
        sb.add(GID, BigInteger.class);
        sb.add(EEZ, String.class);
        sb.add(COUNTRY, String.class);
        sb.add(SOV_ID, Integer.class);
        sb.add(EEZ_ID, Integer.class);
        sb.add(ISO_3_DIGIT, String.class);
        sb.add(MRG_ID, BigInteger.class);
        sb.add(AREA_M_2, Double.class);
        sb.add(LONGITUDE, Double.class);
        sb.add(LATITUDE, Double.class);
        sb.add(MRGID_EEZ, Integer.class);
        sb.add(SOVEREIGN, String.class);
        sb.add(REMARKS, String.class);
        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature() {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(EEZ_FEATURE);
        featureBuilder.set(GEOMETRY, getGeometry());
        featureBuilder.set(GID, getGid());
        featureBuilder.set(EEZ, getEez());
        featureBuilder.set(COUNTRY, getCountry());
        featureBuilder.set(SOV_ID, getSovId());
        featureBuilder.set(EEZ_ID, getEezId());
        featureBuilder.set(ISO_3_DIGIT, getIso3Digit());
        featureBuilder.set(MRG_ID, getMrgid());
        featureBuilder.set(AREA_M_2, getAreaM2());
        featureBuilder.set(LONGITUDE, getLongitude());
        featureBuilder.set(LATITUDE, getLatitude());
        featureBuilder.set(MRGID_EEZ, getMrgidEez());
        featureBuilder.set(SOVEREIGN, getSovereign());
        featureBuilder.set(REMARKS, getRemarks());
        return featureBuilder.buildFeature(String.valueOf(getEezId()));
    }

    public BigInteger getGid() {
        return gid;
    }

    public void setGid(BigInteger gid) {
        this.gid = gid;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getEez() {
        return eez;
    }

    public void setEez(String eez) {
        this.eez = eez;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSovereign() {
        return sovereign;
    }

    public void setSovereign(String sovereign) {
        this.sovereign = sovereign;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getSovId() {
        return sovId;
    }

    public void setSovId(int sovId) {
        this.sovId = sovId;
    }

    public Integer getEezId() {
        return eezId;
    }

    public void setEezId(int eezId) {
        this.eezId = eezId;
    }

    public String getIso3Digit() {
        return iso3Digit;
    }

    public void setIso3Digit(String iso3Digit) {
        this.iso3Digit = iso3Digit;
    }

    public BigInteger getMrgid() {
        return mrgid;
    }

    public void setMrgid(BigInteger mrgid) {
        this.mrgid = mrgid;
    }

    public String getDateChang() {
        return dateChange;
    }

    public void setDateChang(String dateChang) {
        this.dateChange = dateChang;
    }

    public Double getAreaM2() {
        return areaM2;
    }

    public void setAreaM2(double areaM2) {
        this.areaM2 = areaM2;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Integer getMrgidEez() {
        return mrgidEez;
    }

    public void setMrgidEez(int mrgidEez) {
        this.mrgidEez = mrgidEez;
    }

}
