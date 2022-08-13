package com.at999.util.jdbc.datasource.sakura.pool;

import com.at999.util.jdbc.datasource.sakura.DataPolicy;
import com.at999.util.jdbc.datasource.sakura.DataAccess;
import com.at999.util.jdbc.datasource.sakura.DataAccessInfo;
import java.sql.DriverManager;
import java.sql.Connection;
import java.util.HashMap;
import java.util.logging.Logger;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public class SakuraDataPool implements DataAccess{

	protected HashMap<DataAccess, HashMap<Connection, Boolean>> pool;

	protected DataAccess point;

	public SakuraDataPool(){
		init();
	}

	public SakuraDataPool(DataAccess da){
		init(da);
	}
	
	protected void init(){
		this.pool = new HashMap<>();
	}

	protected void init(DataAccess da){
		this.point = da;
		init();
	}

	protected void initialPreconnect(DataAccess da, int size) 
			throws NullPointerException, ClassNotFoundException, SQLException{
		if(da == null)
			throw new NullPointerException();
		this.pool.put(da, new HashMap<>());
		da.getDataAccessInfo().getStartInitialzeTime();
		for(int i = 0; i < size; i++)
			if(tagUsage(da, wireConnection(da), false) == null)
				throw new NullPointerException();
		da.getDataAccessInfo().getFinishInitialzeTime();
		da.getDataAccessInfo().setCount(size);
	}

	public Connection tagUsage(DataAccess da, Connection con, boolean using){
		if(da != null){
			HashMap<Connection, Boolean> cm = this.pool.get(da); 
			if(cm == null)
				throw new NullPointerException();
			if(con != null)
				cm.put(con, using);
			da.getDataAccessInfo().setCount(cm.size());
		}
		return con;
	}

	public Connection wireConnection(DataAccess da) throws NullPointerException, ClassNotFoundException, SQLException{
		if(!da.isWired(true))
			throw new SQLException();
		return DriverManager.getConnection(da.getUrl(), da.getUsername(), da.getPassword());
	}

	public Connection getConnection(DataAccess da) throws NullPointerException, ClassNotFoundException, SQLException{
		if(da == null)
			throw new NullPointerException();
		da.getDataAccessInfo().push();
		return tagUsage(da, use(da).findOver(da), true);
	}

	public Connection getConnection() throws NullPointerException, ClassNotFoundException, SQLException{
		return getConnection(this.point);
	}

	public SakuraDataPool use(DataAccess da, int size, boolean direcation)
			throws NullPointerException, ClassNotFoundException, SQLException{
		if(this.point != da && direcation)
			this.point = da;
		if(!this.pool.containsKey(da))
			initialPreconnect(da, size);
		return this;
	}

	public SakuraDataPool use(DataAccess da, int size) throws NullPointerException, ClassNotFoundException, SQLException{
		return use(da, size, true);
	}

	public SakuraDataPool use(DataAccess da) throws NullPointerException, ClassNotFoundException, SQLException{
		return use(da, da.getDefaultSize(), true);
	}

	public Connection use(DataAccess da, boolean direcation)
			throws NullPointerException, ClassNotFoundException, SQLException{
		return use(da, da.getDefaultSize(), direcation).tagUsage(da, findOver(da), true);
	}

	public DataAccess getPoint(){
		return this.point;
	}

	public boolean isPoint(DataAccess da){
		return this.point == da;
	}

	protected Connection findOver(DataAccess da) throws NullPointerException, ClassNotFoundException, SQLException{
		HashMap<Connection, Boolean> pool = this.pool.get(da);
		if(pool == null)
			throw new NullPointerException();
		if(!da.getDataPolicy().isDefaultPolicy(da.getDataPolicy()))
			da.getDataPolicy().executePreGetPolicy(da.getDataAccessInfo(), new SakuraRestrictedPool(da, pool));
		for(Connection con : pool.keySet())
			if(!pool.get(con))
				return con;
		if(!da.getDataPolicy().isDefaultPolicy(da.getDataPolicy()))
			da.getDataPolicy().executePostGetPolicy(da.getDataAccessInfo(), new SakuraRestrictedPool(da, pool));
		return da.getDataPolicy().executeOverNotFoundPolicy(da.getDataAccessInfo(), new SakuraRestrictedPool(da, pool));
	}

	public boolean verifyAccess(String username, String password) throws NullPointerException{
		if(username == null || password == null)
			throw new NullPointerException();
		return getUsername().equals(username) && getPassword().equals(password);
	}

	public boolean verifyAccess(String driver, String url, String username, String password){
		boolean or = true;
		if(driver != null)
			or = getDriver().equals(driver);
		if(url != null && or)
			or = getUrl().equals(url);
		if(username != null && or)
			or = getUsername().equals(username);
		if(password != null && or)
			or = getPassword().equals(password);
		return or;
	}

	public boolean verifyAccess(DataAccess da, boolean together){
		if(da == null)
			throw new NullPointerException();
		boolean res = true;
		if(this.point == da)
			return res;
		if(verifyAccess(da.getDriver(), da.getUrl(), da.getUsername(), da.getPassword()))
			if(together)
				this.pool.put(da, this.pool.get(da));
			else
				res = true;
		else
			res = false;
		return res;
	}

	@Override
	public String toString(){
		return "Pool=" + this.pool;
	}

	public void close(){}

	@Override
	public boolean isWired(boolean register) throws ClassNotFoundException{
		return this.point.isWired(register);
	}

	@Override
	public DataPolicy getDataPolicy(){
		return this.point.getDataPolicy();
	}

	@Override
	public DataAccessInfo getDataAccessInfo(){
		return this.point.getDataAccessInfo();
	}

	@Override
	public int getDefaultSize(){
		return this.point.getDefaultSize();
	}

	@Override
	public void setDefaultSize(int size){
		this.point.setDefaultSize(size);
	}

	@Override
	public String getDriver() throws NullPointerException{
		return this.point.getDriver();
	}

	@Override
	public void setDriver(String driver) throws NullPointerException{
		this.point.setDriver(driver);
	}

	@Override
	public String getUrl() throws NullPointerException{
		return this.point.getUrl();
	}
	
	@Override
	public void setUrl(String url) throws NullPointerException{
		this.point.setUrl(url);
	}

	@Override
	public String getUsername() throws NullPointerException{
		return this.point.getUsername();
	}
	
	@Override
	public void setUsername(String username) throws NullPointerException{
		this.point.setUsername(username);
	}

	@Override
	public String getPassword() throws NullPointerException{
		return this.point.getPassword();
	}

	@Override
	public void setPassword(String password) throws NullPointerException{
		this.point.setPassword(password);
	}

	@Override
	public int getLoginTimeout() throws SQLException, NullPointerException{
		return this.point.getLoginTimeout();
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException, NullPointerException{
		this.point.setLoginTimeout(seconds);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException, NullPointerException{
		return this.point.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException, NullPointerException{
		this.point.setLogWriter(out);
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException, NullPointerException{
		return this.point.getParentLogger();
	}

}
