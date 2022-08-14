package com.at999.util.jdbc.datasource.sakura.info;

import com.at999.util.jdbc.datasource.sakura.DataAccess;
import com.at999.util.jdbc.datasource.sakura.DataAccessInfo;
import com.at999.util.jdbc.datasource.sakura.pool.SakuraRestrictedPool;
import java.util.LinkedList;
//import java.sql.Connection;

public class SakuraDataAccessInfo implements DataAccessInfo{

	private DataAccess da;
	private LinkedList<Long> application;
	private long createTime;
	private long startInitialzeTime;
	private long finishInitialzeTime;
	private int count;
//	private HashMap<Connection, DataStatus> connectionPool;
	private SakuraRestrictedPool connectionPool;

	public SakuraDataAccessInfo(DataAccess da) throws NullPointerException{
		init(da);
	}

	private void init(DataAccess da) throws NullPointerException{
		if(da == null)
			throw new NullPointerException();
		this.createTime = System.currentTimeMillis();
		this.da = da;
		this.application = new LinkedList<>();
		this.count = 0;
	}

	@Override
	public void push(){
		this.application.add(System.currentTimeMillis());
	}

	@Override
	public DataAccess getDataAccess(){
		return this.da;
	}

	@Override
	public LinkedList<Long> getApplication(){
		return this.application;
	}

	@Override
	public long getCreateTime(){
		return this.createTime;
	}

	@Override
	public long getStartInitialzeTime(){
		return this.startInitialzeTime == 0 ?
			this.startInitialzeTime = System.currentTimeMillis() :
			this.startInitialzeTime;
	}

	@Override
	public long getFinishInitialzeTime(){
		return this.finishInitialzeTime == 0 ?
			this.finishInitialzeTime = System.currentTimeMillis() :
			this.finishInitialzeTime;
	}

	@Override
	public int getCount(){
		return this.count;
	}

/*
	@Override
	public HashMap<Connection, DataStatus> getConnectionPool(){
		return this.connectionPool;
	}

	public void setConnectionPool(HashMap<Connection, DataStatus> connectionPool){
		this.connectionPool = connectionPool;
	}
*/
	
	@Override
	public SakuraRestrictedPool getConnectionPool(){
		return this.connectionPool;
	}

	public void setConnectionPool(SakuraRestrictedPool connectionPool){
		this.connectionPool = connectionPool;
	}

/*
	@Override
	public void count(boolean up){
		if(up)
			this.count++;
		if(!up && this.count > 0){
			this.count--;
		}
	}

	@Override
	public void count(boolean up){
		up ? this.count++ : this.count--;
		if(count < 0){
			throw new ArithmeticException();
		}
	}

*/

	@Override
	public void setCount(int count){
		this.count = count;
	}

}
