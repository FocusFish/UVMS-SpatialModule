/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.service.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import fish.focus.uvms.spatial.service.entity.ReportConnectServiceAreasEntity;
import java.util.List;

@Stateless
public class ReportConnectServiceAreaDao {

    @PersistenceContext
    private EntityManager em;


    public void delete(ReportConnectServiceAreasEntity entity) {
        em.remove(entity);
    }

    public List<ReportConnectServiceAreasEntity> findReportConnectServiceAreas(long reportId) {
        TypedQuery<ReportConnectServiceAreasEntity> query = em.createNamedQuery(ReportConnectServiceAreasEntity.FIND_REPORT_SERVICE_AREAS, ReportConnectServiceAreasEntity.class);
        query.setParameter("reportId",reportId);
        return query.getResultList();
    }

    public void deleteReportConnectServiceAreas(Long id) {

        ReportConnectServiceAreasEntity obj = em.find(ReportConnectServiceAreasEntity.class,id);
        if(obj != null){
            em.remove(obj);
        }
    }

}