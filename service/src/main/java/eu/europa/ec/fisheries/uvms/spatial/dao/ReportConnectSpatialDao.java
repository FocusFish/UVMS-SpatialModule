package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import javax.persistence.EntityManager;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.*;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public class ReportConnectSpatialDao extends AbstractDAO<ReportConnectSpatialEntity> {

    private EntityManager em;

    public ReportConnectSpatialDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


    public ReportConnectSpatialEntity findByReportId(Long reportId) throws ServiceException {

        List<ReportConnectSpatialEntity> list =
                findEntityByNamedQuery(ReportConnectSpatialEntity.class,
                        ReportConnectSpatialEntity.FIND_BY_REPORT_ID, with("reportId", reportId).parameters(), 1);

        return list.get(0);

    }

    public List<ReportConnectSpatialEntity> findByConnectId(Long id) throws ServiceException {
        return findEntityByNamedQuery(ReportConnectSpatialEntity.class, ReportConnectSpatialEntity.FIND_BY_REPORT_CONNECT_ID, with("id", id).parameters(), 1);
    }

    public List<ReportConnectSpatialEntity> findByReportIdAndConnectId(Long reportId, Long id) throws ServiceException {
        return findEntityByNamedQuery(
                ReportConnectSpatialEntity.class, QueryNameConstants.FIND_BY_ID,
                with("reportId", reportId).and("id", id).parameters(), 1
        );
    }

    public void deleteById(List<Long> reportIds) throws ServiceException {

        deleteEntityByNamedQuery(ReportConnectSpatialEntity.class, ReportConnectSpatialEntity.DELETE_BY_ID_LIST,
                with("idList", reportIds).parameters()
        );
    }
}
