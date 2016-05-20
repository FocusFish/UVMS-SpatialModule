package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Map;

@Entity
@NamedQueries({
        @NamedQuery(name = PortEntity.DISABLE, query = "UPDATE PortEntity SET enabled = 'N'"),
        @NamedQuery(name = PortEntity.LIST_ORDERED_BY_DISTANCE, query ="FROM PortEntity WHERE enabled = 'Y' ORDER BY distance(geom, :shape) ASC") /// TODO create dao test
})
@Table(name = "port")
public class PortEntity extends BaseSpatialEntity {

    public static final String PORT_BY_COORDINATE = "portEntity.ByCoordinate";
    public static final String DISABLE = "portsEntity.disable";
    public static final String LIST_ORDERED_BY_DISTANCE = "portsEntity.listOrderedByDistance";

    private static final String COUNTRY_CO = "country_co";
    private static final String FISHING_PO = "fishing_po";
    private static final String LANDING_PL = "landing_pl";
    private static final String COMMERCIAL = "commercial";

    @Column(name = "country_code", length = 3)
    @ColumnAliasName(aliasName = "countrycode")
    private String countryCode;

    @Column(name = "fishing_port", length = 1)
    @ColumnAliasName(aliasName = "fishingport")
    private String fishingPort;

    @Column(name = "landing_place")
    @ColumnAliasName(aliasName = "landingplace")
    private String landingPlace;

    @Column(name = "commercial_port")
    @ColumnAliasName(aliasName = "commercialport")
    private String commercialPort;

    public PortEntity() {
        // why JPA why
    }

    public PortEntity(Map<String, Object> values) throws ServiceException {
        super(values);
        setCountryCode(readStringProperty(values, COUNTRY_CO));
        setFishingPort(readStringProperty(values, FISHING_PO));
        setLandingPlace(readStringProperty(values, LANDING_PL));
        setCommercialPort(readStringProperty(values, COMMERCIAL));
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFishingPort() {
        return fishingPort;
    }

    public void setFishingPort(String fishingPort) {
        this.fishingPort = fishingPort;
    }

    public String getLandingPlace() {
        return landingPlace;
    }

    public void setLandingPlace(String landingPlace) {
        this.landingPlace = landingPlace;
    }

    public String getCommercialPort() {
        return commercialPort;
    }

    public void setCommercialPort(String commercialPort) {
        this.commercialPort = commercialPort;
    }

}
