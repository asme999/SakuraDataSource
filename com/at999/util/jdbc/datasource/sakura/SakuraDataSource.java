package com.at999.util.jdbc.datasource.sakura;

import com.at999.util.jdbc.datasource.sakura.pool.SakuraDataPool;
import com.at999.util.jdbc.datasource.sakura.access.SakuraDataAccess;
import java.util.logging.Logger;
import java.io.PrintWriter;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public class SakuraDataSource implements DataSource{

	private SakuraDataPool sdp;
	
	public SakuraDataSource(){
		init();
	}

	public SakuraDataSource(DataAccess da){
		init(da);
	}

	public SakuraDataSource(String driver, String url, String username, String password){
		try{
			init(new SakuraDataAccess(driver, url, username, password));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void init(DataAccess da){
		this.sdp = new SakuraDataPool(da);
	}

	private void init(){
		init(new SakuraDataAccess());
	}

	public DataAccess currentDataAccess(){
		return this.sdp.getPoint();
	}

	public boolean isHere(DataAccess da){
		return this.sdp.isPoint(da);
	}

	public Connection getConnection(DataAccess da) throws ClassNotFoundException, SQLException{
		return this.sdp.use(da, false);
	}

	@Override
	public Connection getConnection() throws SQLException{
		Connection con = null;
		try{
			con = this.sdp.getConnection();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return con;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException{
		if(!this.sdp.verifyAccess(username, password)){
			try{
				return this.sdp.use(
						new SakuraDataAccess(this.sdp.getDriver(), this.sdp.getUrl(), username, password)
						, false);
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		}
		return getConnection();
	}
	
	public String getDriver() throws NullPointerException{
		return this.sdp.getDriver();
	}

	public void setDriver(String driver) throws NullPointerException{
		this.sdp.setDriver(driver);
	}

	public String getUrl() throws NullPointerException{
		return this.sdp.getUrl();
	}
	
	public void setUrl(String url) throws NullPointerException{
		this.sdp.setUrl(url);
	}

	public String getUsername() throws NullPointerException{
		return this.sdp.getUsername();
	}
	
	public void setUsername(String username) throws NullPointerException{
		this.sdp.setUsername(username);
	}

	public String getPassword() throws NullPointerException{
		return this.sdp.getPassword();
	}

	public void setPassword(String password) throws NullPointerException{
		this.sdp.setPassword(password);
	}

	@Override
	public int getLoginTimeout() throws SQLException, NullPointerException{
		return this.sdp.getLoginTimeout();
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException, NullPointerException{
		this.sdp.setLoginTimeout(seconds);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException, NullPointerException{
		return this.sdp.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException, NullPointerException{
		this.sdp.setLogWriter(out);
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException, NullPointerException{
		return this.sdp.getParentLogger();
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
