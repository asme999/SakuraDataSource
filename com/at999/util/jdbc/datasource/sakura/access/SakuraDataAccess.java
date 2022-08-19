package com.at999.util.jdbc.datasource.sakura.access;

import com.at999.util.jdbc.datasource.sakura.DataPolicy;
import com.at999.util.jdbc.datasource.sakura.policy.SakuraDataPolicy;
import com.at999.util.jdbc.datasource.sakura.DataAccess;
import com.at999.util.jdbc.datasource.sakura.DataAccessInfo;
import com.at999.util.jdbc.datasource.sakura.info.SakuraDataAccessInfo;
import java.util.logging.Logger;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public class SakuraDataAccess implements DataAccess{

	private String driver;
	private String url;
	private String username;
	private String password;

	private DataAccessInfo info;

	private DataPolicy dp;

	private int defaultSize = 8;

	private int loginTimeout = 0;
	private PrintWriter logWriter;
	private Logger logger;


	public SakuraDataAccess(){
		init();
	}

	public SakuraDataAccess(String driver, String url, String username, String password) throws ClassNotFoundException{
		init(driver, url, username, password);
	}

	private void init(){
		if(this.info == null)
			this.info = new SakuraDataAccessInfo(this);
		if(this.dp == null){
			this.dp = SakuraDataPolicy.newInstance();
		}
	}

	public void init(String driver, String url, String username, String password) throws ClassNotFoundException{
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
		init();
		if(isWired(true)){
			Class.forName(this.driver);
		}
	}

	@Override
	public boolean isWired(boolean register) throws ClassNotFoundException{
		if(this.driver == null || this.url == null
				|| this.username == null || this.password == null)
			return false;
		if(register)
			Class.forName(this.driver);
		return true;
	}

	@Override
	public DataPolicy getDataPolicy(){
		return this.dp;
	}

	public SakuraDataAccess setDataPolicy(DataPolicy dp){
		this.dp = dp;
		return this;
	}

	@Override
	public DataAccessInfo getDataAccessInfo(){
		return this.info;
	}

	@Override
	public int getDefaultSize(){
		return this.defaultSize;
	}

	@Override
	public void setDefaultSize(int size){
		this.defaultSize = size;
	}

	@Override
	public String getDriver(){
		return this.driver;
	}

	public void setDriver(String driver){
		this.driver = driver;
	}

	@Override
	public String getUrl(){
		return this.url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}

	@Override
	public String getUsername(){
		return this.username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}

	@Override
	public String getPassword(){
		return this.password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	@Override
	public int getLoginTimeout() throws SQLException{
		return this.loginTimeout;
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException{
		this.loginTimeout = seconds;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException{
		return this.logWriter;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException{
		this.logWriter = out;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException{
		if(this.logger == null || !(this.logger instanceof java.util.logging.Logger))
			throw new SQLFeatureNotSupportedException();
		return this.logger;
	}

}
