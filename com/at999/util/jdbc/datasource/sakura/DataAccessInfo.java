package com.at999.util.jdbc.datasource.sakura;

import com.at999.util.jdbc.datasource.sakura.DataAccess;
import com.at999.util.jdbc.datasource.sakura.DataStatus;
import com.at999.util.jdbc.datasource.sakura.pool.SakuraRestrictedPool;
import java.util.LinkedList;
import java.sql.Connection;

public interface DataAccessInfo{

	DataAccess getDataAccess();

	LinkedList<Long> getApplication();

	long getCreateTime();

	long getStartInitialzeTime();

	long getFinishInitialzeTime();

	int getCount();

	SakuraRestrictedPool getConnectionPool();

/*
	HashMap<Connection, SakuraDataStatus> getConnectionPool();

	void count(boolean up);
*/

	void setCount(int count);

	void push();

/*
	void setDataAccess(DataAccess da);

*/

/*
	void setDates(LinkedList<Long> lds);
*/

	default Object getExtendInfo(){return null;}

}
