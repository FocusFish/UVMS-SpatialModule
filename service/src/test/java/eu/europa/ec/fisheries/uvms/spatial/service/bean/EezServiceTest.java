package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezTypeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by kopyczmi on 20-Aug-15.
 */
@RunWith(MockitoJUnitRunner.class)
public class EezServiceTest {

    private static final String EEZ_ID = "123";
    @Mock
    EezTypeMapper eezMapper;
    @Mock
    private CrudServiceBean commonGenericDAO;
    @InjectMocks
    private EezService service = new EezServiceBean();

    @Test
    public void testGetEezById() {
        EezSpatialRQ eezSpatialRQ = new EezSpatialRQ();
        eezSpatialRQ.setEezId(EEZ_ID);
        EezSpatialRQ getEezSpatialRQ = eezSpatialRQ;
        EezSpatialRS eezSpatialRS = service.getEezById(getEezSpatialRQ);
        assertNotNull(eezSpatialRS);
        ResponseMessageType responseMessage = eezSpatialRS.getResponseMessage();
        assertNotNull(responseMessage);
        assertNotNull(responseMessage.getSuccess());
        assertNull(eezSpatialRS.getEez());
    }
}