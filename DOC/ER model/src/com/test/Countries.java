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
 * Countries generated by hbm2java
 */
public class Countries implements java.io.Serializable {

	private int id;
	private Serializable geom;
	private String sovereignt;
	private String sovA3;
	private String type;
	private String admin;
	private String adm0A3;
	private String name;
	private String nameLong;
	private Double popEst;
	private Double gdpMdEst;
	private String incomeGrp;
	private String continent;
	private String regionUn;
	private String subregion;
	private String regionWb;

	public Countries() {
	}

	public Countries(int id) {
		this.id = id;
	}

	public Countries(int id, Serializable geom, String sovereignt,
			String sovA3, String type, String admin, String adm0A3,
			String name, String nameLong, Double popEst, Double gdpMdEst,
			String incomeGrp, String continent, String regionUn,
			String subregion, String regionWb) {
		this.id = id;
		this.geom = geom;
		this.sovereignt = sovereignt;
		this.sovA3 = sovA3;
		this.type = type;
		this.admin = admin;
		this.adm0A3 = adm0A3;
		this.name = name;
		this.nameLong = nameLong;
		this.popEst = popEst;
		this.gdpMdEst = gdpMdEst;
		this.incomeGrp = incomeGrp;
		this.continent = continent;
		this.regionUn = regionUn;
		this.subregion = subregion;
		this.regionWb = regionWb;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Serializable getGeom() {
		return this.geom;
	}

	public void setGeom(Serializable geom) {
		this.geom = geom;
	}

	public String getSovereignt() {
		return this.sovereignt;
	}

	public void setSovereignt(String sovereignt) {
		this.sovereignt = sovereignt;
	}

	public String getSovA3() {
		return this.sovA3;
	}

	public void setSovA3(String sovA3) {
		this.sovA3 = sovA3;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAdmin() {
		return this.admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getAdm0A3() {
		return this.adm0A3;
	}

	public void setAdm0A3(String adm0A3) {
		this.adm0A3 = adm0A3;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameLong() {
		return this.nameLong;
	}

	public void setNameLong(String nameLong) {
		this.nameLong = nameLong;
	}

	public Double getPopEst() {
		return this.popEst;
	}

	public void setPopEst(Double popEst) {
		this.popEst = popEst;
	}

	public Double getGdpMdEst() {
		return this.gdpMdEst;
	}

	public void setGdpMdEst(Double gdpMdEst) {
		this.gdpMdEst = gdpMdEst;
	}

	public String getIncomeGrp() {
		return this.incomeGrp;
	}

	public void setIncomeGrp(String incomeGrp) {
		this.incomeGrp = incomeGrp;
	}

	public String getContinent() {
		return this.continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getRegionUn() {
		return this.regionUn;
	}

	public void setRegionUn(String regionUn) {
		this.regionUn = regionUn;
	}

	public String getSubregion() {
		return this.subregion;
	}

	public void setSubregion(String subregion) {
		this.subregion = subregion;
	}

	public String getRegionWb() {
		return this.regionWb;
	}

	public void setRegionWb(String regionWb) {
		this.regionWb = regionWb;
	}

}