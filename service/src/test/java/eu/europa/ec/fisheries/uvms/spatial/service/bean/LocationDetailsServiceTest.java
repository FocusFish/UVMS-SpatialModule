package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Point;

import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortsEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;

@RunWith(MockitoJUnitRunner.class)
public class LocationDetailsServiceTest {

	@Mock
	private CrudService crudServiceBean;
	
	@Mock
	private SpatialRepository repository;
	
	@InjectMocks
	private LocationDetailsServiceBean locationDetailsServiceBean;
	
	
	@Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
	
	/**
	 * Test Port by coordinates
	 */
	@Test
	public void getPortDetailsByCoordinates() {
		setMocks(getMockedPortsEntity());
		mockEntityByCoordinate(Arrays.asList(getMockedPortsEntity()));
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setLocationType("port");
		locationEntry.setLatitude(41.0);
		locationEntry.setLongitude(-9.5);
		locationEntry.setCrs(4326);
		LocationDetails locationDetails = locationDetailsServiceBean.getLocationDetails(locationEntry);
		assertNotNull(locationDetails.getLocationProperty());
		assertEquals(locationDetails.getLocationProperty().isEmpty(), false);
	}
	
	/**
	 * Invalid coordinate test
	 */
	@Test(expected=SpatialServiceException.class)
	public void inValidCoordinatesTest() {
		setMocks(getMockedPortsEntity());
		mockEntityByCoordinate(new ArrayList());
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setLocationType("port");
		locationEntry.setLatitude(410.0);
		locationEntry.setLongitude(-90.5);
		locationEntry.setCrs(4326);
		locationDetailsServiceBean.getLocationDetails(locationEntry);
	}
		
	/**
	 * Test Port entity for valid response
	 */
	@Test
	public void getPortDetailsTest() {
		setMocks(getMockedPortsEntity());	
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setId("1");
		locationEntry.setLocationType("PORT");        
        LocationDetails locationDetails = locationDetailsServiceBean.getLocationDetails(locationEntry);
        assertNotNull(locationDetails);
		List<LocationProperty> list = locationDetails.getLocationProperty();
		assertEquals(list.isEmpty(), false);
	}	
	
	/**
	 * Test for invalid Entry in input
	 */
	@Test(expected=SpatialServiceException.class)
	public void invalidRowTest() {
		setMocks(getMockedPortsEntity());		
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setId("INVALID_ROW");
		locationEntry.setLocationType("PORT");        
        locationDetailsServiceBean.getLocationDetails(locationEntry);
	}	
	
	/**
	 * Test for non existing row in DB
	 */
	@Test(expected=SpatialServiceException.class)
	public void nonExistingRowTest() {
		setMocks(null);	
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setId("100000");
		locationEntry.setLocationType("PORT");        
        locationDetailsServiceBean.getLocationDetails(locationEntry);
	}
	
	/**
	 * Test for non existing row in DB
	 */
	@Test(expected=SpatialServiceException.class)
	public void invalidTypeTest() {
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setId("1");
		locationEntry.setLocationType("INVALID_PORT");        
        locationDetailsServiceBean.getLocationDetails(locationEntry);
	}
	
	private void setMocks(PortsEntity portsEntity) {
		List<AreaLocationTypesEntity> locationEntities = new ArrayList<AreaLocationTypesEntity>();
		locationEntities.add(getMockAreaTypeEntity(LocationType.PORT.value()));
		mockCrudServiceBean(locationEntities, portsEntity);
	}
	
	@SuppressWarnings("unchecked")
	private void mockCrudServiceBean(List<AreaLocationTypesEntity> returnList, Object entity) {
		Mockito.when(crudServiceBean.findEntityByNamedQuery(Mockito.any(Class.class), Mockito.any(String.class), Mockito.any(Map.class), Mockito.any(Integer.class))).thenReturn(returnList);
		Mockito.when(crudServiceBean.findEntityById(Mockito.any(Class.class), Mockito.any(Object.class))).thenReturn(entity);
	}
	
	private void mockEntityByCoordinate(List list) {
		Mockito.when(repository.findAreaOrLocationByCoordinates(Mockito.any(Point.class), Mockito.any(String.class))).thenReturn(list);
	}
	
	private AreaLocationTypesEntity getMockAreaTypeEntity(String typeName) {
		AreaLocationTypesEntity areaEntity = Mockito.mock(AreaLocationTypesEntity.class);
		Mockito.when(areaEntity.getTypeName()).thenReturn(typeName.toUpperCase());
		return areaEntity;
	}
	
	private PortsEntity getMockedPortsEntity() {
		PortsEntity portsEntity = new PortsEntity();
		portsEntity.setGid(1);
		portsEntity.setName("TEST");
		portsEntity.setNatlscale(20.0);
		portsEntity.setScalerRank(123);
		portsEntity.setWebsite("www.test.com");
		portsEntity.setGeom(new GeometryBuilder().point());
		portsEntity.setFeaturecla("Test feature");
		return portsEntity;
	}
}
