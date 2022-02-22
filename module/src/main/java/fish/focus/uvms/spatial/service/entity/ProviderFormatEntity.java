/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package fish.focus.uvms.spatial.service.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "provider_format")
public class ProviderFormatEntity {

	@Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_PROVIDER_FMT_GEN", sequenceName="provider_format_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PROVIDER_FMT_GEN")
	private Long id;

	@Column(name = "service_type", nullable = false, length = 10)
	private String serviceType;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "providerFormat", cascade = CascadeType.ALL)
	private Set<ServiceLayerEntity> serviceLayers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Set<ServiceLayerEntity> getServiceLayers() {
		return serviceLayers;
	}

	public void setServiceLayers(Set<ServiceLayerEntity> serviceLayers) {
		this.serviceLayers = serviceLayers;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProviderFormatEntity that = (ProviderFormatEntity) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(serviceType, that.serviceType) &&
				Objects.equals(serviceLayers, that.serviceLayers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, serviceType, serviceLayers);
	}
}