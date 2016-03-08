package eu.europa.ec.fisheries.uvms.spatial.util;

import eu.europa.ec.fisheries.uvms.spatial.entity.*;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;

public enum SpatialTypeEnum {
	
	EEZ("EEZ", QueryNameConstants.EEZ_BY_COORDINATE, QueryNameConstants.EEZ_COLUMNS, EezEntity.class),
	RFMO("RFMO", QueryNameConstants.RFMO_BY_COORDINATE, QueryNameConstants.RFMO_COLUMNS, RfmoEntity.class),
	GFCM("GFCM", QueryNameConstants.GFCM_BY_COORDINATE, GfcmEntity.class),
	RAC("RAC", QueryNameConstants.RAC_BY_COORDINATE, RacEntity.class),
	S_TAT_RECT("STatRect", QueryNameConstants.STAT_RECT_BY_COORDINATE, StatRectEntity.class),
	FAO("FAO", QueryNameConstants.FAO_BY_COORDINATE, FaoEntity.class),
	PORT("PORT", QueryNameConstants.PORT_BY_COORDINATE, PortsEntity.class),
	PORTAREA("PORTAREA", QueryNameConstants.PORTAREA_BY_COORDINATE, PortsEntity.class),
	USERAREA("USERAREA", QueryNameConstants.USERAREA_BY_COORDINATE, QueryNameConstants.USERAREA_COLUMNS, UserAreasEntity.class);
	
	private String type;
	private String nativeQuery;
	private Class entityClass;
	private String namedQuery;
	
	private SpatialTypeEnum(String type, String nativeQuery, Class entityClass) {
		this(type, nativeQuery, "", entityClass);
	}
	
	private SpatialTypeEnum(String type, String nativeQuery, String namedQuery, Class entityClass) {
		this.type = type;
		this.nativeQuery = nativeQuery;
		this.entityClass = entityClass;
		this.namedQuery = namedQuery;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getNativeQuery() {
		return this.nativeQuery;
	}
	
	public String getNamedQuery() {
		return this.namedQuery;
	}
	
	public Class getEntityClass() {
		return this.entityClass;
	}
    
	public static Class getEntityClassByType(String value) {
		for (SpatialTypeEnum areaType : SpatialTypeEnum.values()) {
			if(areaType.getType().equalsIgnoreCase(value)) {
				return areaType.getEntityClass();
			}
		}
		throw new IllegalArgumentException(value);
	}
	
	public static String getNativeQueryByType(String value) {
		for (SpatialTypeEnum areaType : SpatialTypeEnum.values()) {
			if(areaType.getType().equalsIgnoreCase(value)) {
				return areaType.getNativeQuery();
			}
		}
		throw new IllegalArgumentException(value);
	}
	
	public static String getNamedQueryByType(String value) {
		for (SpatialTypeEnum areaType : SpatialTypeEnum.values()) {
			if(areaType.getType().equalsIgnoreCase(value)) {
				return areaType.getNamedQuery();
			}
		}
		throw new IllegalArgumentException(value);
	}
}
