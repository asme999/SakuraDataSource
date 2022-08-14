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

//	private SakuraDataPool sdp;
	
	public SakuraDataSource(){
			super.init();
	}

	public SakuraDataSource(DataAccess da){
		try{
			super.init(da);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public SakuraDataSource(String driver, String url, String username, String password){
		try{
			super.init(new SakuraDataAccess(driver, url, username, password));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

/*
	private void init(DataAccess da){
		this.sdp = new SakuraDataPool(da);
	}

	private void init(){
		init(new SakuraDataAccess());
	}

	public DataAccess currentDataAccess(){
		return super.getPoint();
	}

	public boolean isHere(DataAccess da){
		return super.isPoint(da);
	}
*/

	public Connection getConnection(DataAccess da) throws ClassNotFoundException, SQLException{
		return super.use(da, false);
	}

	@Override
	public Connection getConnection() throws SQLException{
		Connection con = null;
		try{
			con = super.getPoolConnection();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return con;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException{
		Connection con = null;
		try{
			if(!verifyAccess(username, password))
				con = super.use(new SakuraDataAccess(getDriver(), getUrl(), username, password), false);
			else
				con = super.getPoolConnection();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
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
