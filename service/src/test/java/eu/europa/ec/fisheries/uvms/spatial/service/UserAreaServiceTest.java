/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/


package eu.europa.ec.fisheries.uvms.spatial.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.Assert;
import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.UserAreaServiceBean;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserAreaServiceTest extends BaseUnitilsTest {

    @TestedObject
    private UserAreaService service = new UserAreaServiceBean();

    @InjectIntoByType
    private Mock<SpatialRepository> repoMock;

    @InjectIntoByType
    private Mock<AreaTypeNamesService> namesServiceMock;

    private GeometryFactory geomFactory = new GeometryFactory();

    private Point point;

    @Before
    public void setup(){
        point = geomFactory.createPoint(new Coordinate(20.0535983848415, 31.1417484902222));
    }

    @Test
    @SneakyThrows
    public void testSearchUserAreasByCriteria(){

        UserAreasEntity build = UserAreasEntity.builder().geom(point).build();

        List<UserAreasEntity> list = new ArrayList<>();
        list.add(build);
        repoMock.returns(list).listUserAreaByCriteria(null, null, null, false);

        List<UserAreaDto> userAreaDtos = service.searchUserAreasByCriteria("rep_power", "EC", "invalid", false);

        Assert.equals("POINT (20.0535983848415 31.1417484902222)", userAreaDtos.get(0).getExtent());
        Assert.equals("USERAREA", userAreaDtos.get(0).getAreaType());

    }

    @Test
    @SneakyThrows
    public void getUserAreaDetailsWithExtentByIdWithId(){

        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setId("1");
        repoMock.returns(UserAreasEntity.builder().geom(point).build()).findUserAreaById(null, null, null, null);

        List<AreaDetails> userAreaDetailsWithExtentById = service.getUserAreaDetailsWithExtentById(areaTypeEntry, "", true, "");

        AreaDetails areaDetails = userAreaDetailsWithExtentById.get(0);
        List<AreaProperty> areaProperties = areaDetails.getAreaProperties();

        assertEquals("createdOn", areaProperties.get(0).getPropertyName());
        assertNull(areaProperties.get(0).getPropertyValue());

        assertEquals("enabled", areaProperties.get(1).getPropertyName());
        assertEquals("false", areaProperties.get(1).getPropertyValue());

        assertEquals("startDate", areaProperties.get(2).getPropertyName());
        assertNull(areaProperties.get(2).getPropertyValue());

        assertEquals("scopeSelection", areaProperties.get(3).getPropertyName());
        assertNull(areaProperties.get(3).getPropertyValue());

        Assert.equals("datasetName", areaProperties.get(4).getPropertyName());
        assertNull(areaProperties.get(4).getPropertyValue());

        Assert.equals("areaDesc", areaProperties.get(5).getPropertyName());
        assertNull(areaProperties.get(5).getPropertyValue());

        Assert.equals("subType", areaProperties.get(6).getPropertyName());
        assertNull(areaProperties.get(6).getPropertyValue());

        Assert.equals("name", areaProperties.get(7).getPropertyName());
        assertNull(areaProperties.get(7).getPropertyValue());

        Assert.equals("endDate", areaProperties.get(8).getPropertyName());
        assertNull(areaProperties.get(8).getPropertyValue());

        Assert.equals("code", areaProperties.get(9).getPropertyName());
        assertNull(areaProperties.get(9).getPropertyValue());

        Assert.equals("centroid", areaProperties.get(10).getPropertyName());
        Assert.equals("POINT (20.0535983848415 31.1417484902222)", areaProperties.get(10).getPropertyValue());

        Assert.equals("geometry", areaProperties.get(11).getPropertyName());
        Assert.equals("POINT (20.0535983848415 31.1417484902222)", areaProperties.get(11).getPropertyValue());

        Assert.equals("1", areaDetails.getAreaType().getId());

    }


    @Test
    @SneakyThrows
    public void getUserAreaDetailsWithExtentByIdWithoutId(){

        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setId("1");
        repoMock.returns(null).findUserAreaById(null, null, null, null);

        List<AreaDetails> userAreaDetailsWithExtentById = service.getUserAreaDetailsWithExtentById(areaTypeEntry, "", true, "");

        AreaDetails areaDetails = userAreaDetailsWithExtentById.get(0);
        List<AreaProperty> areaProperties = areaDetails.getAreaProperties();

        Assert.equals("1", areaDetails.getAreaType().getId());
        Assert.equals(0, areaProperties.size());

    }


    @Test
    @SneakyThrows
    public void getUserAreaLayerDefinition(){

        UserAreaLayerDto dto = UserAreaLayerDto.builder()
                .isInternal(true).isLocation(true).geoName("geoName").areaTypeDesc("desc").serviceType("serviceType")
                .serviceUrl("serviceUrl").style("style").typeName("typeName").idList(Arrays.asList(100L)).build();
        namesServiceMock.returns(Arrays.asList(dto)).listUserAreaLayerMapping();

        UserAreasEntity userAreasEntity = new UserAreasEntity();
        Field id = userAreasEntity.getClass().getSuperclass().getSuperclass().getDeclaredField("id");
        makeModifiable(id);
        setValue(userAreasEntity, id, 2L);
        repoMock.returns(Arrays.asList(userAreasEntity)).findUserAreaByUserNameAndScopeName(null, null);

        UserAreaLayerDto response = service.getUserAreaLayerDefinition(null, null);

        assertEquals(1, response.getIdList().size());
        assertEquals(userAreasEntity.getId(), response.getIdList().get(0), 0);
        assertEquals(dto.getIsLocation(), response.getIsLocation());
        assertEquals(dto.getAreaTypeDesc(), response.getAreaTypeDesc());
        assertEquals(dto.getGeoName(), response.getGeoName());
        assertEquals(dto.getIsInternal(), response.getIsInternal());
        assertEquals(dto.getServiceType(), response.getServiceType());
        assertEquals(dto.getStyle(), response.getStyle());
        assertEquals(dto.getServiceUrl(), response.getServiceUrl());
        assertEquals(dto.getTypeName(), response.getTypeName());

    }

    /**
     *
     * Set the value of a field reflectively.
     */
    protected static void setValue(Object owner, Field field, Object value) throws Exception {
        makeModifiable(field);
        field.set(owner, value);
    }

    /**
     *
     * Force the field to be modifiable and accessible.
     */
    protected static void makeModifiable(Field nameField) throws Exception {
        nameField.setAccessible(true);
        int modifiers = nameField.getModifiers();
        Field modifierField = nameField.getClass().getDeclaredField("modifiers");
        modifiers = modifiers & ~Modifier.FINAL;
        modifierField.setAccessible(true);
        modifierField.setInt(nameField, modifiers);
    }
}
