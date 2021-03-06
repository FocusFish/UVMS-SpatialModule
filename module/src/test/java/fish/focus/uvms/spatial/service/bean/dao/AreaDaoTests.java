package fish.focus.uvms.spatial.service.bean.dao;

import fish.focus.uvms.spatial.service.bean.TransactionalTests;
import fish.focus.uvms.spatial.service.dao.AreaDao;
import fish.focus.uvms.spatial.service.entity.BaseAreaEntity;
import fish.focus.uvms.spatial.service.entity.EezEntity;
import fish.focus.uvms.spatial.service.entity.PortAreaEntity;
import fish.focus.uvms.spatial.service.entity.PortEntity;
import fish.focus.uvms.spatial.service.utils.EntityUtils;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class AreaDaoTests extends TransactionalTests {

    @Inject
    AreaDao areaDao;

    @Test
    public void disableAllAreasOfTypeEEZTest(){

        BaseAreaEntity entity = EntityUtils.getInstance("EEZ");
        areaDao.disableAllAreasOfType(entity);

        List<EezEntity> eezs = areaDao.getAllEezAreas();
        assertTrue(eezs.isEmpty());
    }

    @Test
    public void disableAllAreasOfTypePortTest(){

        BaseAreaEntity entity = EntityUtils.getInstance("PortArea");
        areaDao.disableAllAreasOfType(entity);

        List<PortAreaEntity> portAreas = areaDao.getAllPortAreaAreas();
        assertTrue(portAreas.isEmpty());
    }
}
