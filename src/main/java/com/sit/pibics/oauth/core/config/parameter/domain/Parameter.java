package com.sit.pibics.oauth.core.config.parameter.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import util.database.Database;


@XmlRootElement
public class Parameter implements Serializable {

	private static final long serialVersionUID = -578325849007499211L;

	// @XmlTransient
	// @XmlAttribute
	// @XmlElement
	private Application application;
	private Database[] database;
	private Map<String, Database> databaseMap;
	private Certification certification;
	
	public Application getApplication() {
		return application;
	}

	@XmlElement
	public void setApplication(Application application) {
		this.application = application;
	}

	@XmlTransient
	public Map<String, Database> getDatabaseMap() {
		if (getDatabase() == null) {
			return databaseMap;
		}

		if (databaseMap == null) {
			databaseMap = new LinkedHashMap<String, Database>();
			for (Database db : getDatabase()) {
				databaseMap.put(db.getKey(), db);
			}
		}
		return databaseMap;
	}

	public Database[] getDatabase() {
		return database;
	}

	@XmlElement
	public void setDatabase(Database[] database) {
		if (this.databaseMap != null) {
			this.databaseMap.clear();
			this.databaseMap = null;
		}
		this.database = database;
	}

	public Certification getCertification() {
		return certification;
	}

	public void setCertification(Certification certification) {
		this.certification = certification;
	}
}
