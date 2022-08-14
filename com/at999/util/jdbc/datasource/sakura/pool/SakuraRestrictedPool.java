package com.at999.util.jdbc.datasource.sakura.pool;

import com.at999.util.jdbc.datasource.sakura.DataAccess;
import com.at999.util.jdbc.datasource.sakura.DataStatus;
import com.at999.util.jdbc.datasource.sakura.pool.SakuraDataPool;
import java.util.HashMap;
import java.sql.Connection;

public class SakuraRestrictedPool extends SakuraDataPool{

	public SakuraRestrictedPool(DataAccess da){
		super.init(da);
	}

	public SakuraRestrictedPool(DataAccess da, HashMap<Connection , DataStatus> pool){
		super.init(da);
		super.pool.put(da, pool);
	}

	public HashMap<DataAccess, HashMap<Connection, DataStatus>> getConnectionPool(){
		return super.pool;
	}

}
