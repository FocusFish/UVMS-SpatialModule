/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.commons.geometry.utils.GeometryUtils;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.PortAreaDao2;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.PortDao2;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.SpatialQueriesDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.BaseAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.PortAreaEntity2;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.PortEntity2;
import eu.europa.ec.fisheries.uvms.spatial.service.utils.GeometryMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.utils.MeasurementUnit;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class AreaServiceBean2 {

    private static final String MULTIPOINT = "MULTIPOINT";

    private static final String GID = "gid";
    private static final String AREA_TYPE = "areaType";

    @Inject
    private PortDao2 portDao;

    @Inject
    private PortAreaDao2 portAreaDao;

    @Inject
    SpatialQueriesDao spatialQueriesDao;





    public List<PortEntity2> getPortsByAreaCodes(List<String> codes){
        return portDao.getPortsByAreaCodes(codes);
    }

    public List<PortAreaEntity2> getPortAreasByPoint(Double lat,  Double lon){
        try {
            Point point = (Point) GeometryUtils.toGeographic(lat, lon, 4326);
            return portAreaDao.getPortAreasByPoint(point);

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BaseAreaDto> getAreasByPoint(Double lat, Double lon){

        try {
            Point point = (Point) GeometryUtils.toGeographic(lat, lon, 4326);
            return spatialQueriesDao.getAreasByPoint(point);

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public PortEntity2 findClosestPortByPosition(Double lat,  Double lon){
        try {

            Point point = (Point) GeometryUtils.toGeographic(lat, lon, 4326);
            return portDao.getClosestPort(point);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }


    public SpatialEnrichmentRS getSpatialEnrichment(SpatialEnrichmentRQ request){
        try {

            PointType pointType = request.getPoint();

            Point point = (Point) GeometryUtils.toGeographic(pointType.getLatitude(), pointType.getLongitude(), 4326);
            return computeSpatialEnrichment(point);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }



    private SpatialEnrichmentRS computeSpatialEnrichment(Point point){


        List<PortAreaEntity2> areaTypesByLocation = portAreaDao.getPortAreasByPoint(point);

        List<BaseAreaDto> closestAreas = spatialQueriesDao.getClosestAreaByPoint(point);

        PortEntity2 closestLocations = portDao.getClosestPort(point);

        SpatialEnrichmentRS response = new SpatialEnrichmentRS();

        if(areaTypesByLocation != null){
            AreasByLocationType areasByLocationType = new AreasByLocationType();

            for( PortAreaEntity2 entity : areaTypesByLocation){
                AreaExtendedIdentifierType areaExtendedIdentifierType = new AreaExtendedIdentifierType();
                areaExtendedIdentifierType.setCode(entity.getCode());
                areaExtendedIdentifierType.setName(entity.getName());
                areaExtendedIdentifierType.setAreaType(AreaType.PORTAREA);
                areaExtendedIdentifierType.setId(String.valueOf(entity.getId()));
                areasByLocationType.getAreas().add(areaExtendedIdentifierType);
            }
            response.setAreasByLocation(areasByLocationType);
        }

        if (closestAreas != null) {
            ClosestAreasType closestAreasType = new ClosestAreasType();
            for(BaseAreaDto baseArea : closestAreas){
                Area area = new Area();
                area.setAreaType(baseArea.getType());
                area.setCode(baseArea.getCode());
                area.setDistance(baseArea.getDistance() / MeasurementUnit.NAUTICAL_MILES.getRatio());
                area.setId(String.valueOf(baseArea.getGid()));
                area.setName(baseArea.getName());
                area.setUnit(UnitType.NAUTICAL_MILES);

                closestAreasType.getClosestAreas().add(area);
            }
            response.setClosestAreas(closestAreasType);
        }

        if (closestLocations != null){
            ClosestLocationsType locationType = new ClosestLocationsType();
            Location location = new Location();
            location.setCentroid(closestLocations.getCentroid());
            location.setCode(closestLocations.getCode());
            location.setCountryCode(closestLocations.getCountryCode());

            double distanceInMeters = distanceMeter(point.getY(), point.getX(), closestLocations.getGeom().getCentroid().getY(), closestLocations.getGeom().getCentroid().getX());

            location.setDistance(distanceInMeters / MeasurementUnit.NAUTICAL_MILES.getRatio());
            location.setEnabled(closestLocations.getEnabled());
            location.setExtent(closestLocations.getExtent());
            location.setGid(String.valueOf(closestLocations.getId()));
            location.setId(String.valueOf(closestLocations.getId()));
            location.setLocationType(LocationType.PORT);
            location.setName(closestLocations.getName());
            location.setUnit(UnitType.NAUTICAL_MILES);
            location.setWkt(closestLocations.getGeometry());


            locationType.getClosestLocations().add(location);
            response.setClosestLocations(locationType);
        }
        return response;
    }


    private static final int EARTH_RADIUS_METER = 6371000;
    private static double distanceMeter(double prevLat, double prevLon, double currentLat, double currentLon) {
        double lat1Rad = Math.toRadians(prevLat);
        double lat2Rad = Math.toRadians(currentLat);
        double deltaLonRad = Math.toRadians(currentLon - prevLon);

        return Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.cos(deltaLonRad))
                * EARTH_RADIUS_METER;
    }



}