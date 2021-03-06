package fish.focus.uvms.spatial.service.bean.bean;

import fish.focus.uvms.spatial.model.schemas.*;
import fish.focus.uvms.spatial.service.bean.TransactionalTests;
import fish.focus.uvms.spatial.service.bean.AreaServiceBean;
import fish.focus.uvms.spatial.service.dto.BaseAreaDto;
import fish.focus.uvms.spatial.service.dto.PortDistanceInfoDto;
import fish.focus.uvms.spatial.service.entity.PortAreaEntity;
import fish.focus.uvms.spatial.service.entity.PortEntity;
import fish.focus.uvms.spatial.service.utils.MeasurementUnit;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class AreaServiceBeanTest extends TransactionalTests {


    @Inject
    AreaServiceBean areaServiceBean;

    @Test
    public void getPortAreasByPointTest(){
        double lon = 17.969;
        double lat = 58.916666666666664;

        List<PortAreaEntity> result = areaServiceBean.getPortAreasByPoint(lat,lon);

        assertEquals(1, result.size());
        assertEquals("SENYN", result.get(0).getCode());
        assertEquals("Nynäshamn", result.get(0).getName());
    }

    @Test
    public void getPortsByCodeTest(){
        String inputArray[] = {"SENYN", "SETOV", "SENOB", "SEAND", "SEKVK", "SELEI", "SESRT", "SERLD", "SEVEJ", "SESVL", "SEREK"};
        String outputCheckerArray[] = {"Nynäshamn", "Torekov", "Norrebro", "Ängelsbäcksstrand", "Kattvik", "Lervik", "Skäret", "Arild", "Vejbystrand", "Svanshall", "Rekekroken"};
        List<String> inputList = Arrays.asList(inputArray);

        List<PortEntity> result = areaServiceBean.getPortsByAreaCodes(inputList);
        assertEquals(11, result.size());


        List<String> outputCheckerList = Arrays.asList(outputCheckerArray);
        for (String s: outputCheckerList) {
            assertTrue(s ,result.stream().anyMatch(port -> s.equals((port.getName()))));
        }
    }

    @Test
    public void getPortsByEmptyListTest(){
        List<String> inputList = new ArrayList<>();

        List<PortEntity> result = areaServiceBean.getPortsByAreaCodes(inputList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findClosestPortByPositionTest(){
        double lon = 17.969;
        double lat = 58.916666666666664;

        PortDistanceInfoDto result = areaServiceBean.findClosestPortByPosition(lat,lon);
        assertNotNull(result);

        Assert.assertEquals("SENYN", result.getPort().getCode());
        Assert.assertEquals("Nynäshamn", result.getPort().getName());
        assertEquals(1981d, result.getDistance(), 10d);
    }

    @Test
    public void getAreasByPointTest(){
        double lon = 17.536166666666666;
        double lat = 59.391;
        String outputCheckArray[] = {"Swedish Exclusive Economic Zone", "International Commission for the Conservation of Atlantic Tuna", "ICES", "ATLANTIC, NORTHEAST"};

        List<BaseAreaDto> result = areaServiceBean.getAreasByPoint(lat, lon);

        assertEquals(4, result.size());

        List<String> outputCheckerList = Arrays.asList(outputCheckArray);
        for (String s: outputCheckerList) {
            assertTrue(s ,result.stream().anyMatch(base -> s.equals((base.getName()))));
        }
    }

    @Test
    public void getEnrichment(){
        double lon = 18.735362;
        double lat = 63.270487;
        String outputCheckArray[] = {"Swedish Exclusive Economic Zone", "International Commission for the Conservation of Atlantic Tuna", "Örnsköldsvik", "ICES", "Black Sea", "ATLANTIC, NORTHEAST"};


        SpatialEnrichmentRQ request = new SpatialEnrichmentRQ();
        PointType point = new PointType();
        point.setCrs(4326);
        point.setLatitude(lat);
        point.setLongitude(lon);
        request.setPoint(point);

        SpatialEnrichmentRS response = areaServiceBean.getSpatialEnrichment(request);

        assertEquals(3, response.getAreasByLocation().getAreas().size());
        assertEquals("SEOER", response.getAreasByLocation().getAreas().get(0).getCode());
        assertEquals("Örnsköldsvik", response.getAreasByLocation().getAreas().get(0).getName());
        assertEquals(AreaType.PORTAREA, response.getAreasByLocation().getAreas().get(0).getAreaType());

        /*assertEquals(6, response.getClosestAreas().getClosestAreas().size());
        List<String> outputCheckerList = Arrays.asList(outputCheckArray);
        for (String s: outputCheckerList) {
            assertTrue(s ,response.getClosestAreas().getClosestAreas().stream().anyMatch(base -> s.equals((base.getName()))));
        }*/

        assertEquals(1, response.getClosestLocations().getClosestLocations().size());
        assertEquals("SEOER", response.getClosestLocations().getClosestLocations().get(0).getCode());
        assertEquals("Örnsköldsvik", response.getClosestLocations().getClosestLocations().get(0).getName());
        Assert.assertEquals(2158d / MeasurementUnit.NAUTICAL_MILES.getRatio() , response.getClosestLocations().getClosestLocations().get(0).getDistance(), 0.01d);
    }


    @Test
    public void getAreasByCodeAllDifferentAreaTypesTest(){
        double lon = 17.536166666666666;
        double lat = 59.391;

        List<BaseAreaDto> areasByPoint = areaServiceBean.getAreasByPoint(lat, lon);

        assertEquals(4, areasByPoint.size());

        AreaByCodeRequest areaByCodeRequest = new AreaByCodeRequest();
        List<AreaSimpleType> areaList = new ArrayList<>();
        for (BaseAreaDto base: areasByPoint) {
            AreaSimpleType simpleType = new AreaSimpleType(base.getType().value(), base.getCode(), null);
            areaList.add(simpleType);
        }

        areaByCodeRequest.setAreaSimples(areaList);
        List<AreaSimpleType> result = areaServiceBean.getAreasByCode(areaByCodeRequest);

        assertEquals(4, result.size());
        for (AreaSimpleType simpleType: result) {
            assertNotNull(simpleType.getWkt());
            assertTrue(simpleType.getAreaType() + " : " + simpleType.getAreaCode() ,areasByPoint.stream().anyMatch(base -> simpleType.getAreaCode().equals((base.getCode()))));
        }
    }

    @Test
    public void getAreasByCodeAllSameAreaTypesTest(){
        String inputArray[] = {"SGP", "ATA", "IDN", "TLS", "PRT", "GBR",  "SJM", "USA", "SAU", "NZL", "EGY", "AUS", "COL", "IND", "VNM", "CHN", "MEX"};


        AreaByCodeRequest areaByCodeRequest = new AreaByCodeRequest();
        List<AreaSimpleType> areaList = new ArrayList<>();
        for (String s: inputArray) {
            AreaSimpleType simpleType = new AreaSimpleType(AreaType.EEZ.value(), s, null);
            areaList.add(simpleType);
        }

        areaByCodeRequest.setAreaSimples(areaList);
        List<AreaSimpleType> result = areaServiceBean.getAreasByCode(areaByCodeRequest);

        assertEquals(28, result.size());            //17 codes, some are used several times so the result is 28
        for (AreaSimpleType simpleType: result) {
            assertNotNull(simpleType.getWkt());
            assertTrue(simpleType.getAreaType() + " : " + simpleType.getAreaCode() ,Arrays.asList(inputArray).stream().anyMatch(base -> simpleType.getAreaCode().equals((base))));
        }
    }


}
