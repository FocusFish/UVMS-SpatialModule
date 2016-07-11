/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "provider_format")
public class ProviderFormatEntity extends BaseEntity {

	@Column(name = "service_type", nullable = false, length = 10)
	private String serviceType;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "providerFormat", cascade = CascadeType.ALL)
	private Set<ServiceLayerEntity> serviceLayers;

	public ProviderFormatEntity() {
        // why JPA why
    }

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Set<ServiceLayerEntity> getServiceLayers() {
		return this.serviceLayers;
	}

	public void setServiceLayers(Set<ServiceLayerEntity> serviceLayers) {
		this.serviceLayers = serviceLayers;
	}
}