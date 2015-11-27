package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;

import java.util.List;

public interface MapConfigService {

    MapConfigDto getReportConfig(int reportId);

    MapConfigurationType getMapConfigurationType(final Long reportId) throws ServiceException;

    List<ProjectionDto> getAllProjections();

    MapConfigurationType saveOrUpdateMapConfiguration(SpatialSaveOrUpdateMapConfigurationRQ spatialSaveMapConfigurationRQ);
}
