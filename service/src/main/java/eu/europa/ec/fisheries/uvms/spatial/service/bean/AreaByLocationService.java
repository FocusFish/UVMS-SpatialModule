package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaIdentifierDto;

import java.util.List;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
public interface AreaByLocationService {
    List<AreaTypeEntry> getAreaTypesByLocation(AreaByLocationSpatialRQ request);

    List<AreaIdentifierDto> getAreaTypesByLocation(double lat, double lon, int crs);
}
