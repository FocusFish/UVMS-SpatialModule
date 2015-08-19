package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stat_rect", schema = "spatial")
public class StatRectEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502872L;

	@Id
	@Column(name = "gid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int gid;

	public StatRectEntity() {
	}

	public int getGid() {
		return this.gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

}
