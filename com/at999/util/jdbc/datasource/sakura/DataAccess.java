package com.at999.util.jdbc.datasource.sakura;

import javax.sql.CommonDataSource;

public interface DataAccess extends CommonDataSource{

	boolean isWired(boolean register) throws ClassNotFoundException;

	String getDriver();

	void setDriver(String driver);

	String getUrl();

	void setUrl(String url);

	String getUsername();

	void setUsername(String username);

	String getPassword();

	void setPassword(String password);

	int getDefaultSize();

	void setDefaultSize(int size);

	DataAccessInfo getDataAccessInfo();

	DataPolicy getDataPolicy();

/*
	void setDataAccessInfo(DataAccessInfo info);

	void setDataPolicy(DataPolicy dp);

*/

}
