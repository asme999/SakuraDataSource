package com.at999.util.jdbc.datasource.sakura;

import com.at999.util.jdbc.datasource.sakura.pool.SakuraDataPool;
import com.at999.util.jdbc.datasource.sakura.access.SakuraDataAccess;
import java.util.logging.Logger;
import java.io.PrintWriter;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public class SakuraDataSource extends SakuraDataPool implements DataSource{

	public SakuraDataSource(){
		super();
	}

	public SakuraDataSource(DataAccess da){
		super(da);
	}

	public SakuraDataSource(String driver, String url, String username, String password){
		super(new SakuraDataAccess(driver, url, username, password));
	}

	public Connection getConnection(DataAccess da) throws SQLException{
		try{
			return use(da, false);
		}catch(Exception e){
			throw new SQLException(e);
		}
	}

	@Override
	public Connection getConnection() throws SQLException{
		try{
			return getPoolConnection();
		}catch(Exception e){
			throw new SQLException(e);
		}
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException{
		Connection con = null;
		try{
			if(!verifyAccess(username, password))
				con = use(new SakuraDataAccess(getDriver(), getUrl(), username, password), false);
			else
				con = getPoolConnection();
		}catch(Exception e){
			throw new SQLException(e);
		}
		return con;
	}
	
	public String getDriver() throws NullPointerException{
		return super.getDriver();
	}

	public void setDriver(String driver) throws NullPointerException{
		super.setDriver(driver);
	}

	public String getUrl() throws NullPointerException{
		return super.getUrl();
	}
	
	public void setUrl(String url) throws NullPointerException{
		super.setUrl(url);
	}

	public String getUsername() throws NullPointerException{
		return super.getUsername();
	}
	
	public void setUsername(String username) throws NullPointerException{
		super.setUsername(username);
	}

	public String getPassword() throws NullPointerException{
		return super.getPassword();
	}

	public void setPassword(String password) throws NullPointerException{
		super.setPassword(password);
	}

	@Override
	public int getLoginTimeout() throws SQLException, NullPointerException{
		return super.getLoginTimeout();
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException, NullPointerException{
		super.setLoginTimeout(seconds);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException, NullPointerException{
		return super.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException, NullPointerException{
		super.setLogWriter(out);
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException, NullPointerException{
		return super.getParentLogger();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException{
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException{
		if(true)
			throw new SQLException();
		return null;
	}

}
