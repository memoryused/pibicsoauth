package com.sit.pibics.oauth.core.config.parameter.domain;

import com.sit.pibics.oauth.sql.TestSQL;
import com.sit.core.common.domain.CommonSQLPath;

public enum OAuthSQLPath {

	TEST_SQL(TestSQL.class, "com/sit/pibics/oauth/sql/Test.sql");
	;
	
	private ReferenceSQLPath sqlPath;

	private OAuthSQLPath(Class<?> className, String path) {
		this.sqlPath = new ReferenceSQLPath(className, path);
	}

	public CommonSQLPath getSqlPath() {
		return sqlPath;
	}

	public String getPath() {
		return sqlPath.getPath();
	}
}
