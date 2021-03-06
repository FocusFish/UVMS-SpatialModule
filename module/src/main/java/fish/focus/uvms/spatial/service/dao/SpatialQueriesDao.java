package fish.focus.uvms.spatial.service.dao;

import org.locationtech.jts.geom.Point;
import fish.focus.uvms.spatial.service.dto.BaseAreaDto;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Stateless
public class SpatialQueriesDao {


    @PersistenceContext
    private EntityManager em;


    public List<BaseAreaDto> getAreasByPoint(Point point) {

        Query q = em.createNativeQuery("SELECT type,gid,code,name from  spatial.get_areas_by_point( :point)", "BaseAreaDtoMapping")     //BaseAreaDtoMapping is defined in BaseAreaEntity
                .setParameter("point", point);

        return q.getResultList();

    }
}
