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

import java.io.Serializable;

/**
 * ReportLayerMapping generated by hbm2java
 */
public class ReportLayerMapping implements java.io.Serializable {

	private long id;
	private AreaTypes areaTypes;
	private Layer layer;
	private LayerGroup layerGroup;
	private Report report;
	private int layerOrder;
	private String sqlFilter;
	private Serializable sld;

	public ReportLayerMapping() {
	}

	public ReportLayerMapping(long id, LayerGroup layerGroup, Report report,
			int layerOrder) {
		this.id = id;
		this.layerGroup = layerGroup;
		this.report = report;
		this.layerOrder = layerOrder;
	}

	public ReportLayerMapping(long id, AreaTypes areaTypes, Layer layer,
			LayerGroup layerGroup, Report report, int layerOrder,
			String sqlFilter, Serializable sld) {
		this.id = id;
		this.areaTypes = areaTypes;
		this.layer = layer;
		this.layerGroup = layerGroup;
		this.report = report;
		this.layerOrder = layerOrder;
		this.sqlFilter = sqlFilter;
		this.sld = sld;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AreaTypes getAreaTypes() {
		return this.areaTypes;
	}

	public void setAreaTypes(AreaTypes areaTypes) {
		this.areaTypes = areaTypes;
	}

	public Layer getLayer() {
		return this.layer;
	}

	public void setLayer(Layer layer) {
		this.layer = layer;
	}

	public LayerGroup getLayerGroup() {
		return this.layerGroup;
	}

	public void setLayerGroup(LayerGroup layerGroup) {
		this.layerGroup = layerGroup;
	}

	public Report getReport() {
		return this.report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public int getLayerOrder() {
		return this.layerOrder;
	}

	public void setLayerOrder(int layerOrder) {
		this.layerOrder = layerOrder;
	}

	public String getSqlFilter() {
		return this.sqlFilter;
	}

	public void setSqlFilter(String sqlFilter) {
		this.sqlFilter = sqlFilter;
	}

	public Serializable getSld() {
		return this.sld;
	}

	public void setSld(Serializable sld) {
		this.sld = sld;
	}

}