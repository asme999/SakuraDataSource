package com.at999.util.jdbc.datasource.sakura.pool;

import com.at999.util.jdbc.datasource.sakura.DataAccess;
import com.at999.util.jdbc.datasource.sakura.pool.SakuraDataPool;
import java.util.HashMap;
import java.sql.Connection;

public class SakuraRestrictedPool extends SakuraDataPool{

	public SakuraRestrictedPool(DataAccess da){
		init(da);
	}

	public SakuraRestrictedPool(DataAccess da, HashMap<Connection , Boolean> pool){
		init(da);
		this.pool.put(da, pool);
	}

}
