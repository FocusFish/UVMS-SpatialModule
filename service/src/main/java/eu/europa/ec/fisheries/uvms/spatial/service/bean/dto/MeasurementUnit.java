package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import org.apache.commons.lang.StringUtils;

public enum MeasurementUnit {

    METER(1),
    KILOMETER(1000),
    NAUTICAL_MILE(1852),
    MILE(1609.344);

    private double ratio;

    MeasurementUnit(double ratio) {
        this.ratio = ratio;
    }

    public double getRatio() {
        return ratio;
    }

    public static MeasurementUnit getMeasurement(String unit) {
        for (MeasurementUnit measurementUnit : values()) {
            if (measurementUnit.name().equalsIgnoreCase(unit)) {
                return measurementUnit;
            }
        }
        throw new SpatialServiceException(SpatialServiceErrors.WRONG_MEASUREMENT_UNIT, unit);
    }
}
