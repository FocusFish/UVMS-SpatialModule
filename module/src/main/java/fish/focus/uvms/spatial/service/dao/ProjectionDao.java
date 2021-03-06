/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.service.dao;

import fish.focus.uvms.spatial.service.entity.ProjectionEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ProjectionDao {

    @PersistenceContext
    private EntityManager em;


    public List<ProjectionEntity> findBySrsCode(Integer srsCode)  {
        TypedQuery<ProjectionEntity> query = em.createNamedQuery(ProjectionEntity.FIND_BY_SRS_CODE, ProjectionEntity.class);
        query.setParameter("srsCode",srsCode);
        return   query.getResultList();
    }

    public ProjectionEntity findProjectionById(Long id) {
        return em.find(ProjectionEntity.class, id);
    }

    public List<ProjectionEntity> findAll(){
        TypedQuery<ProjectionEntity> query = em.createNamedQuery(ProjectionEntity.FIND_ALL, ProjectionEntity.class);
        return query.getResultList();
    }
}