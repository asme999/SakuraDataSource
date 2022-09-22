package com.at999.util.jdbc.datasource.sakura.pool;

import com.at999.util.jdbc.datasource.sakura.DataPolicy;
import com.at999.util.jdbc.datasource.sakura.DataAccess;
import com.at999.util.jdbc.datasource.sakura.DataStatus;
import com.at999.util.jdbc.datasource.sakura.DataAccessInfo;
import com.at999.util.jdbc.datasource.sakura.access.SakuraDataAccess;
import com.at999.util.jdbc.datasource.sakura.info.SakuraDataAccessInfo;
import com.at999.util.jdbc.datasource.sakura.status.SakuraDataStatus;
import com.at999.util.jdbc.datasource.sakura.pool.SakuraRestrictedPool;
import com.at999.util.jdbc.datasource.sakura.proxy.SakuraProxyConnection;
import com.at999.util.jdbc.datasource.sakura.proxy.SakuraConnectionHandler;
import java.sql.DriverManager;
import java.sql.Connection;
import java.util.HashMap;
import java.util.logging.Logger;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public class SakuraDataPool implements DataAccess{

	protected HashMap<DataAccess, HashMap<Connection, DataStatus>> pool;

	protected DataAccess point;

	public SakuraDataPool(){
		init(new SakuraDataAccess());
	}

	public SakuraDataPool(DataAccess da){
		init(da);
	}
	
	protected void init(DataAccess da){
		this.point = da;
		this.pool = new HashMap<>();
	}

	protected void initialPreconnect(DataAccess da, int size) throws SQLException{
		if(da == null)
			throw new NullPointerException();
		this.pool.put(da, new HashMap<>());
		da.getDataAccessInfo().getStartInitialzeTime();
		for(int i = 0; i < size; i++){
			Connection con = wireConnection(da);
			Connection proxy = wireConnection(da, con);
			tagUsage(da, proxy, new SakuraDataStatus(con), true);
		}
		da.getDataAccessInfo().getFinishInitialzeTime();
		da.getDataAccessInfo().setCount(size);
		((SakuraDataAccessInfo)da.getDataAccessInfo()).setConnectionPool(new SakuraRestrictedPool(da, this.pool.get(da)));
	}

	public Connection tagUsage(DataAccess da, Connection proxy, SakuraDataStatus sds, boolean available){
		if(da == null || proxy == null || sds == null)
			throw new NullPointerException();
		HashMap<Connection, DataStatus> cm = this.pool.get(da); 
		if(!cm.containsKey(proxy))
			cm.put(proxy, sds);
		sds.usable(available);
		da.getDataAccessInfo().setCount(cm.size());
		return proxy;
	}

	protected Connection wireConnection(DataAccess da, Connection con, boolean original) throws SQLException{
		if(!da.isWired(true))
			throw new SQLException();
		if(con == null)
			con = DriverManager.getConnection(da.getUrl(), da.getUsername(), da.getPassword());
		if(original)
			return con;
		return SakuraProxyConnection.getConnection(con.getClass(), new SakuraConnectionHandler(con, da.getDataAccessInfo()));
	}

	public Connection wireConnection(DataAccess da, Connection originalConnection) throws SQLException{
		return wireConnection(da, originalConnection, false);
	}

	public Connection wireConnection(DataAccess da) throws SQLException{
		return wireConnection(da, null, true);
	}

	public Connection getPoolConnection(DataAccess da) throws SQLException{
		if(da == null)
			throw new NullPointerException();
		da.getDataAccessInfo().push();
		return use(da, true);
	}

	public Connection getPoolConnection() throws SQLException{
		return getPoolConnection(this.point);
	}

	public SakuraDataPool use(DataAccess da, int size, boolean direcation) throws SQLException{
		if(this.point != da && direcation)
			this.point = da;
		if(!this.pool.containsKey(da))
			if(!findAccess(da.getDriver(), da.getUrl(), da.getUsername()))
				initialPreconnect(da, size);
		return this;
	}

	public SakuraDataPool use(DataAccess da, int size) throws SQLException{
		return use(da, size, true);
	}

	public SakuraDataPool use(DataAccess da) throws SQLException{
		return use(da, da.getDefaultSize());
	}

	public Connection use(DataAccess da, boolean direcation) throws SQLException{
		if(da == null)
			throw new NullPointerException();
		DataAccess oda = null;
		Connection con = null;
		SakuraDataStatus sds = null;
		if(!this.pool.containsKey(da))
			con = use(da, da.getDefaultSize(), direcation).findOver(da);
		else
			oda = getAccess(da.getDriver(), da.getUrl(), da.getUsername());
		if(con == null){
			if(oda == null)
				throw new ExceptionInInitializerError();
			else
				con = findOver(oda);
		}
		sds = (SakuraDataStatus)da.getDataAccessInfo().getConnectionPool().pool.get(da).get(con);
		return tagUsage(da, con, sds, false);
	}

	protected Connection findOver(DataAccess da) throws SQLException{
		HashMap<Connection, DataStatus> pool = this.pool.get(da);
		if(pool == null)
			throw new NullPointerException();
		Connection connection = null;
		if(!da.getDataPolicy().isDefaultPolicy(da.getDataPolicy()))
			da.getDataPolicy().executePreGetPolicy(da.getDataAccessInfo());
		for(Connection con : pool.keySet()){
			if(pool.get(con).usable()){
				connection = con;
				break;
			}
		}
		if(!da.getDataPolicy().isDefaultPolicy(da.getDataPolicy()))
			da.getDataPolicy().executePostGetPolicy(da.getDataAccessInfo());
		if(connection == null)
			return da.getDataPolicy().executeOverNotFoundPolicy(da.getDataAccessInfo());
		return connection;
	}

	public boolean verifyAccess(String username, String password){
		if(username == null || password == null)
			throw new NullPointerException();
		return verifyAccess(null, null, username, password);
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

	public boolean verifyAccess(DataAccess da){
		return verifyAccess(da.getDriver(), da.getUrl(), da.getUsername(), da.getPassword());
	}

	public boolean verifyAccess(DataAccess da, boolean together) throws SQLException{
		if(da == null)
			throw new NullPointerException();
		boolean res = true;
		if(this.point == da)
			return res;
		if(verifyAccess(da.getDriver(), da.getUrl(), da.getUsername(), da.getPassword()))
			if(together)
				this.pool.put(da, this.pool.get(da));
			else
				use(da, da.getDefaultSize(), false);
		else
			res = false;
		return res;
	}

	public boolean findAccess(String driver, String url, String username){
		boolean and = true;
		if(this.pool.isEmpty())
			and = false;
		for(DataAccess tda : this.pool.keySet()){
			if(driver != null && !tda.getDriver().equals(driver))
				and = false;
			if(and && url != null && !tda.getUrl().equals(url))
				and = false;
			if(and && username != null && !tda.getUsername().equals(username)){
				and = false;
			}
		}
		return and;
	}

	public DataAccess getAccess(String driver, String url, String username){
		if(this.pool.isEmpty() || driver == null || url == null|| username == null)
			return null;
		for(DataAccess tda : this.pool.keySet())
			if(tda.getDriver().equals(driver) && tda.getUrl().equals(url) && tda.getUsername().equals(username))
				return tda;
		return null;
	}

	@Override
	public String toString(){
		return "Pool=" + this.pool;
	}

	public void close(){}

	@Override
	public boolean isWired(boolean register){
		return this.point.isWired(register);
	}

	public DataAccess getPoint(){
		return this.point;
	}

	public boolean isPoint(DataAccess da){
		return this.point == da;
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
	public String getDriver(){
		return this.point.getDriver();
	}

	@Override
	public void setDriver(String driver){
		this.point.setDriver(driver);
	}

	@Override
	public String getUrl(){
		return this.point.getUrl();
	}
	
	@Override
	public void setUrl(String url){
		this.point.setUrl(url);
	}

	@Override
	public String getUsername(){
		return this.point.getUsername();
	}
	
	@Override
	public void setUsername(String username){
		this.point.setUsername(username);
	}

	@Override
	public String getPassword(){
		return this.point.getPassword();
	}

	@Override
	public void setPassword(String password){
		this.point.setPassword(password);
	}

	@Override
	public int getLoginTimeout() throws SQLException{
		return this.point.getLoginTimeout();
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException{
		this.point.setLoginTimeout(seconds);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException{
		return this.point.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException{
		this.point.setLogWriter(out);
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException{
		return this.point.getParentLogger();
	}

}
