package com.at999.util.jdbc.datasource.sakura;

import javax.sql.CommonDataSource;

public interface DataAccess extends CommonDataSource{

	boolean isWired(boolean register) throws ClassNotFoundException;

	String getDriver() throws NullPointerException;

	void setDriver(String driver) throws NullPointerException;

	String getUrl() throws NullPointerException;

	void setUrl(String url) throws NullPointerException;

	String getUsername() throws NullPointerException;

	void setUsername(String username) throws NullPointerException;

	String getPassword() throws NullPointerException;

	void setPassword(String password) throws NullPointerException;

	int getDefaultSize();

	void setDefaultSize(int size);

	DataAccessInfo getDataAccessInfo();

	DataPolicy getDataPolicy();

/*
	void setDataAccessInfo(DataAccessInfo info);

	void setDataPolicy(DataPolicy dp);

*/

}
