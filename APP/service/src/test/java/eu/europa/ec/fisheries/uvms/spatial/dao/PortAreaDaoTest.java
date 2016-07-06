/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries � European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertNull;

public class PortAreaDaoTest extends BaseSpatialDaoTest {

    private PortAreaDao dao = new PortAreaDao(em);

    @Test
    @SneakyThrows
    public void shouldReturnIntersectedPortArea(){
        List intersects = dao.findByIntersect(new GeometryBuilder().point(1, 1));
    }

    @Test
    @SneakyThrows
    public void testFindOne(){
        PortAreasEntity one = dao.findEntityById(PortAreasEntity.class, 1L);
        assertNull(one);
    }
}