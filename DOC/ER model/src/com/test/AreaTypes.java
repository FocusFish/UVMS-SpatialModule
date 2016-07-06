/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries � European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package com.test;

// Generated Jul 28, 2015 2:11:57 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * AreaTypes generated by hbm2java
 */
public class AreaTypes implements java.io.Serializable {

	private int id;
	private String typeName;
	private String areaTypeDesc;
	private String areaDbTable;
	private String geoName;
	private int srsCode;
	private Set<ReportLayerMapping> reportLayerMappings = new HashSet<ReportLayerMapping>(
			0);
	private Set<AreaGroupMapping> areaGroupMappings = new HashSet<AreaGroupMapping>(
			0);

	public AreaTypes() {
	}

	public AreaTypes(int id, String typeName, String areaDbTable,
			String geoName, int srsCode) {
		this.id = id;
		this.typeName = typeName;
		this.areaDbTable = areaDbTable;
		this.geoName = geoName;
		this.srsCode = srsCode;
	}

	public AreaTypes(int id, String typeName, String areaTypeDesc,
			String areaDbTable, String geoName, int srsCode,
			Set<ReportLayerMapping> reportLayerMappings,
			Set<AreaGroupMapping> areaGroupMappings) {
		this.id = id;
		this.typeName = typeName;
		this.areaTypeDesc = areaTypeDesc;
		this.areaDbTable = areaDbTable;
		this.geoName = geoName;
		this.srsCode = srsCode;
		this.reportLayerMappings = reportLayerMappings;
		this.areaGroupMappings = areaGroupMappings;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getAreaTypeDesc() {
		return this.areaTypeDesc;
	}

	public void setAreaTypeDesc(String areaTypeDesc) {
		this.areaTypeDesc = areaTypeDesc;
	}

	public String getAreaDbTable() {
		return this.areaDbTable;
	}

	public void setAreaDbTable(String areaDbTable) {
		this.areaDbTable = areaDbTable;
	}

	public String getGeoName() {
		return this.geoName;
	}

	public void setGeoName(String geoName) {
		this.geoName = geoName;
	}

	public int getSrsCode() {
		return this.srsCode;
	}

	public void setSrsCode(int srsCode) {
		this.srsCode = srsCode;
	}

	public Set<ReportLayerMapping> getReportLayerMappings() {
		return this.reportLayerMappings;
	}

	public void setReportLayerMappings(
			Set<ReportLayerMapping> reportLayerMappings) {
		this.reportLayerMappings = reportLayerMappings;
	}

	public Set<AreaGroupMapping> getAreaGroupMappings() {
		return this.areaGroupMappings;
	}

	public void setAreaGroupMappings(Set<AreaGroupMapping> areaGroupMappings) {
		this.areaGroupMappings = areaGroupMappings;
	}

}