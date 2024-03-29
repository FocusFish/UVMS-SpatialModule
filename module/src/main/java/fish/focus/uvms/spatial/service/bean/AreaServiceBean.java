/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package fish.focus.uvms.spatial.service.bean;

import fish.focus.uvms.spatial.model.schemas.*;
import fish.focus.uvms.spatial.service.dao.AreaDao;
import fish.focus.uvms.spatial.service.dao.AreaLocationTypesDao;
import fish.focus.uvms.spatial.service.dao.SpatialQueriesDao;
import fish.focus.uvms.spatial.service.dto.AreaLayerDto;
import fish.focus.uvms.spatial.service.dto.BaseAreaDto;
import fish.focus.uvms.spatial.service.dto.PortDistanceInfoDto;
import fish.focus.uvms.spatial.service.entity.PortAreaEntity;
import fish.focus.uvms.spatial.service.entity.PortEntity;
import fish.focus.uvms.spatial.service.entity.UserAreasEntity;
import fish.focus.uvms.spatial.service.utils.AreaMapper;
import fish.focus.uvms.spatial.service.utils.GeometryUtils;
import fish.focus.uvms.spatial.service.utils.MeasurementUnit;
import org.locationtech.jts.geom.Point;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class AreaServiceBean {

    private static final String MULTIPOINT = "MULTIPOINT";

    private static final String GID = "gid";
    private static final String AREA_TYPE = "areaType";

    @Inject
    private AreaDao areaDao;

    @Inject
    SpatialQueriesDao spatialQueriesDao;

    @Inject
    AreaLocationTypesDao areaLocationTypesDao;



    public List<AreaSimpleType> getAreasByCode(AreaByCodeRequest areaByCodeRequest){        //should this really return area simple type???
        Map<AreaType, List<String>> requestMap = new HashMap();
        for (AreaSimpleType areaSimpleType: areaByCodeRequest.getAreaSimples()) {
            AreaType areaType = AreaType.fromValue(areaSimpleType.getAreaType().toUpperCase());
            if(!requestMap.containsKey(areaType)) {
                List<String> stringList = new ArrayList<>();
                requestMap.put(areaType, stringList);
            }
            requestMap.get(areaType).add(areaSimpleType.getAreaCode());
        }

        List<AreaSimpleType> responseList = new ArrayList<>();
        for (AreaType areaType : requestMap.keySet()) {
            switch (areaType){
                case EEZ:
                    responseList.addAll(AreaMapper.mapToAreaSimpleType(areaDao.getEEZByAreaCodes(requestMap.get(areaType)), areaType));
                    break;
                case FAO:
                    responseList.addAll(AreaMapper.mapToAreaSimpleType(areaDao.getFAOByAreaCodes(requestMap.get(areaType)), areaType));
                    break;
                case GFCM:
                    responseList.addAll(AreaMapper.mapToAreaSimpleType(areaDao.getGFCMByAreaCodes(requestMap.get(areaType)), areaType));
                    break;
                case RFMO:
                    responseList.addAll(AreaMapper.mapToAreaSimpleType(areaDao.getRFMOByAreaCodes(requestMap.get(areaType)), areaType));
                    break;
                case PORT:
                    responseList.addAll(AreaMapper.mapToAreaSimpleType(areaDao.getPortsByAreaCodes(requestMap.get(areaType)), areaType));
                    break;
                case PORTAREA:
                    responseList.addAll(AreaMapper.mapToAreaSimpleType(areaDao.getPortAreaByAreaCodes(requestMap.get(areaType)), areaType));
                    break;
                case USERAREA:
                    responseList.addAll(AreaMapper.mapToAreaSimpleType(areaDao.getUserAreasByAreaCodes(requestMap.get(areaType)), areaType));
                    break;
                case STATRECT:
                    responseList.addAll(AreaMapper.mapToAreaSimpleType(areaDao.getStatRectByAreaCodes(requestMap.get(areaType)), areaType));
                    break;
                default:

            }
        }

        return responseList;
    }



    public List<PortEntity> getPortsByAreaCodes(List<String> codes){
        return areaDao.getPortsByAreaCodes(codes);
    }

    public List<PortAreaEntity> getPortAreasByPoint(Double lat, Double lon){
        Point point = (Point) GeometryUtils.createPoint(lat, lon);
        return getPortAreasByPoint(point);
    }

    public List<PortAreaEntity> getPortAreasByPoint(Point point){
        return areaDao.getPortAreasByPoint(point);
    }

    public List<BaseAreaDto> getAreasByPoint(Double lat, Double lon){

        Point point = (Point) GeometryUtils.createPoint(lat, lon);
        return spatialQueriesDao.getAreasByPoint(point);

    }

    public PortDistanceInfoDto findClosestPortByPosition(Double lat, Double lon){
        Point point = (Point) GeometryUtils.createPoint(lat, lon);
        return areaDao.getClosestPort(point);
    }


    public SpatialEnrichmentRS getSpatialEnrichment(SpatialEnrichmentRQ request){

        PointType pointType = request.getPoint();

        Point point = (Point) GeometryUtils.createPoint(pointType.getLatitude(), pointType.getLongitude());
        return computeSpatialEnrichment(point);
    }



    private SpatialEnrichmentRS computeSpatialEnrichment(Point point){
        //Asked Mats Börje what to do about the closest area query and since he could not think of a use case for
        // it he suggested that we put this on pause until the case was investigated further.
        //After further investigation (on 14/04/2020) it was concluded that none could find a good use case for this query
        //and since it consumes a lot of resources it is now removed.

        List<BaseAreaDto> areaTypesByLocation = spatialQueriesDao.getAreasByPoint(point);

        PortDistanceInfoDto closestLocation = areaDao.getClosestPort(point);

        SpatialEnrichmentRS response = new SpatialEnrichmentRS();

        if(areaTypesByLocation != null){
            AreasByLocationType areasByLocationType = new AreasByLocationType();

            for( BaseAreaDto entity : areaTypesByLocation){
                AreaExtendedIdentifierType areaExtendedIdentifierType = new AreaExtendedIdentifierType();
                areaExtendedIdentifierType.setCode(entity.getCode());
                areaExtendedIdentifierType.setName(entity.getName());
                areaExtendedIdentifierType.setAreaType(entity.getType());
                areaExtendedIdentifierType.setId(String.valueOf(entity.getGid()));
                areasByLocationType.getAreas().add(areaExtendedIdentifierType);
            }
            response.setAreasByLocation(areasByLocationType);
        }

        if (closestLocation != null){
            ClosestLocationsType locationType = new ClosestLocationsType();
            Location location = new Location();
            location.setCentroid(closestLocation.getPort().getCentroid());
            location.setCode(closestLocation.getPort().getCode());
            location.setCountryCode(closestLocation.getPort().getCountryCode());

            double distanceInMeters = closestLocation.getDistance();

            location.setDistance(distanceInMeters / MeasurementUnit.NAUTICAL_MILES.getRatio());
            location.setEnabled(closestLocation.getPort().getEnabled());
            location.setExtent(closestLocation.getPort().getExtent());
            location.setGid(String.valueOf(closestLocation.getPort().getId()));
            location.setId(String.valueOf(closestLocation.getPort().getId()));
            location.setLocationType(LocationType.PORT);
            location.setName(closestLocation.getPort().getName());
            location.setUnit(UnitType.NAUTICAL_MILES);
            location.setWkt(closestLocation.getPort().getGeometryWKT());


            locationType.getClosestLocations().add(location);
            response.setClosestLocations(locationType);
        }
        return response;
    }


    public UserAreasEntity upsertUserArea(UserAreasEntity newArea){

        if(newArea.getId() == null){
            return areaDao.create(newArea);
        }
        return areaDao.update(newArea);
    }

    public boolean deleteUserArea(Long id){
        UserAreasEntity toBeDeleted = areaDao.find(UserAreasEntity.class, id);
        if(toBeDeleted == null){
            return false; //throw exception maybe?
        }
        areaDao.delete(toBeDeleted);
        return true;
    }


    public AreaLayerDto getUserAreaLayerDefinition(String userName, String scopeName) {

        AreaLayerDto userAreaLayer = areaLocationTypesDao.findUserAreaLayerMapping();
        List<UserAreasEntity> userAreasEntityList = areaDao.findByUserNameAndScopeName(userName, scopeName);
        userAreaLayer.setIdList(AreaMapper.mapToBaseAreaDtoListFromUserAreas(userAreasEntityList));
        return userAreaLayer;
    }

    public AreaLayerDto getDistinctUserAreaGroups(String userName, String scopeName) {

        AreaLayerDto userAreaLayer = areaLocationTypesDao.findUserAreaLayerMapping();
        List<String> userAreasEntityList = areaDao.selectDistinctUserAreaGroupsByUserNameAndScopeName(userName, scopeName);
        userAreaLayer.setDistinctAreaGroups(userAreasEntityList);

        return userAreaLayer;
    }

    public List<AreaLayerDto> getAllNonUserAreaTypes(){
        List<AreaLayerDto> areas = areaLocationTypesDao.findSystemAreaLayerMapping();
        for (AreaLayerDto dto : areas ) {
            dto.setIdList(getAllAreasOfType(AreaType.fromValue(dto.getTypeName())));
        }
        return areas;
    }

    public Map<AreaType, List<BaseAreaDto>> getAllAreasOfTypes(List<AreaType> areaTypes){
        Map<AreaType, List<BaseAreaDto>> dtoMap = new HashMap<>();
        for (AreaType area: areaTypes) {
            dtoMap.put(area, getAllAreasOfType(area));
        }
        return dtoMap;
    }

    public List<BaseAreaDto> getAllAreasOfType(AreaType area){
        switch (area) {
            case EEZ :
                return AreaMapper.mapToBaseAreaDtoList(areaDao.getAllEezAreas());
            case FAO :
                return AreaMapper.mapToBaseAreaDtoList(areaDao.getAllFaoAreas());
            case GFCM :
                return AreaMapper.mapToBaseAreaDtoList(areaDao.getAllGfcmAreas());
            case PORTAREA:
                return AreaMapper.mapToBaseAreaDtoList(areaDao.getAllPortAreaAreas());
            case PORT:
                return AreaMapper.mapToBaseAreaDtoList(areaDao.getAllPorts());
            case RFMO:
                return AreaMapper.mapToBaseAreaDtoList(areaDao.getAllRfmoAreas());
            case STATRECT:
                return AreaMapper.mapToBaseAreaDtoList(areaDao.getAllStatRectAreas());
            case USERAREA:
                return AreaMapper.mapToBaseAreaDtoList(areaDao.getAllUserAreas(null, null));
        }
        return new ArrayList<>();
    }

}