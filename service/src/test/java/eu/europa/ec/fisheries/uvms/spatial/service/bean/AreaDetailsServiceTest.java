package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author padhyad
 * Unit test for Area details service
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AreaDetailsServiceTest {
	
	@Mock
	private CrudService crudServiceBean;
	
	@InjectMocks
	private AreaDetailsServiceBean areaDetailsServiceBean;
	
	
	@Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
	
	/**
	 * Test EEZ entity for valid response
	 */
	@Test
	public void getEezAreaDetailsTest() {
		List<AreaLocationTypesEntity> areaEntities = new ArrayList<AreaLocationTypesEntity>();
		areaEntities.add(getMockAreaTypeEntity("EEZ", true));		
		EezEntity eezEntity = getMockedEezEntity();		
		mockCrudServiceBean(areaEntities, eezEntity);
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType("eez");
        areaTypeEntry.setId("1");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
		AreaDetailsSpatialRequest request = areaDetailsSpatialRequest;
        AreaDetails areaDetails = areaDetailsServiceBean.getAreaDetails(request);
        assertNotNull(areaDetails);
		List<AreaProperty> list = areaDetails.getAreaProperty();
		assertEquals(list.isEmpty(), false);
	}
	
	/**
	 * Test RFMO entity for valid response
	 */
	@Test
	public void getRfmoAreaDetailsTest() {
		List<AreaLocationTypesEntity> areaEntities = new ArrayList<AreaLocationTypesEntity>();
		areaEntities.add(getMockAreaTypeEntity("RFMO", true));		
		RfmoEntity rfmoEntity = getMockedRfmoEntity();
		mockCrudServiceBean(areaEntities, rfmoEntity);
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType("rfmo");
        areaTypeEntry.setId("1");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
		AreaDetailsSpatialRequest request = areaDetailsSpatialRequest;
        AreaDetails areaDetails = areaDetailsServiceBean.getAreaDetails(request);
        assertNotNull(areaDetails);
		List<AreaProperty> list = areaDetails.getAreaProperty();
		assertEquals(list.isEmpty(), false);
	}
	
	/**
	 * Test for Invalid entity in the DB
	 */
	@Test(expected=SpatialServiceException.class)
	public void invalidEntityTest() {
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType("INVALID_ENTITY");
        areaTypeEntry.setId("1");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
		AreaDetailsSpatialRequest request = areaDetailsSpatialRequest;
		areaDetailsServiceBean.getAreaDetails(request);
	}
	
	/**
	 * Test for invalid for in input
	 */
	@Test(expected=SpatialServiceException.class)
	public void invalidRowTest() {
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType("EEZ");
        areaTypeEntry.setId("INVALID_ROW");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
        areaDetailsServiceBean.getAreaDetails(areaDetailsSpatialRequest);
	}
	
	
	/**
	 * Test for non existing row in DB
	 */
	@Test(expected=SpatialServiceException.class)
	public void nonExistingRowTest() {
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType("EEZ");
        areaTypeEntry.setId("10000");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
        areaDetailsServiceBean.getAreaDetails(areaDetailsSpatialRequest);
	}

	
	@SuppressWarnings("unchecked")
	public void mockCrudServiceBean(List<AreaLocationTypesEntity> returnList, Object entity) {
		Mockito.when(crudServiceBean.findEntityByNamedQuery(Mockito.any(Class.class), Mockito.any(String.class), Mockito.any(Map.class), Mockito.any(Integer.class))).thenReturn(returnList);
		Mockito.when(crudServiceBean.findEntityById(Mockito.any(Class.class), Mockito.any(Object.class))).thenReturn(entity);
	}
	
	private AreaLocationTypesEntity getMockAreaTypeEntity(String typeName, Boolean isSystemWide) {
		AreaLocationTypesEntity areaEntity = Mockito.mock(AreaLocationTypesEntity.class);
		Mockito.when(areaEntity.getTypeName()).thenReturn(typeName.toUpperCase());
		Mockito.when(areaEntity.getIsSystemWide()).thenReturn(isSystemWide);
		return areaEntity;
	}
	
	private EezEntity getMockedEezEntity() {
		EezEntity eezEntity = new EezEntity();
		eezEntity.setAreaM2(50.0);
		eezEntity.setCountry("Belgium");
		eezEntity.setDateChang(String.valueOf(new Date().getTime()));
		eezEntity.setEez("EEZ");
		eezEntity.setEezId(123);
		eezEntity.setGeom(new GeometryBuilder().point());
		eezEntity.setGid(1);
		eezEntity.setIso3digit("iso3digit");
		eezEntity.setLatitude(345.60);
		eezEntity.setLongitude(234.54);
		eezEntity.setMrgid(new BigDecimal("100"));
		eezEntity.setMrgidEez(123);
		eezEntity.setRemarks("This is Test");
		eezEntity.setSovereign("Test");
		eezEntity.setSovId(555);
		return eezEntity;
	}
	
	private RfmoEntity getMockedRfmoEntity() {
		RfmoEntity rfmoEntity =  new RfmoEntity();
		rfmoEntity.setGeom(new GeometryBuilder().point());
		rfmoEntity.setGid(1);
		rfmoEntity.setName("RFMO");
		rfmoEntity.setRfmo("Test RFMO");
		rfmoEntity.setTuna("no");
		return rfmoEntity;
	}
}
