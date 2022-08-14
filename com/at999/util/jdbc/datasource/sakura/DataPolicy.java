package com.at999.util.jdbc.datasource.sakura;

import com.at999.util.jdbc.datasource.sakura.policy.SakuraDataPolicy;
import com.at999.util.jdbc.datasource.sakura.pool.SakuraRestrictedPool;
import com.at999.util.jdbc.datasource.sakura.status.SakuraDataStatus;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;

public interface DataPolicy{

	DataPolicy defaultPolicy = SakuraDataPolicy.newInstance();

	default void executePreGetPolicy(DataAccessInfo info){}

	default void executePostGetPolicy(DataAccessInfo info){}

	default Connection executeOverNotFoundPolicy(DataAccessInfo info) throws ClassNotFoundException, SQLException{
		SakuraRestrictedPool srp = info.getConnectionPool();
		DataAccess da = srp.getPoint();
		Connection con = srp.wireConnection(da);
		Connection proxy = srp.wireConnection(da, con);
		return srp.tagUsage(da, proxy, new SakuraDataStatus(con), false);
	}

	default <T> T cast(Object o, Class<T> c){ return c.cast(o); }

	default boolean isDefaultPolicy(DataPolicy dp){
		return defaultPolicy == dp;
	}

/*
	default int initializeDefaultSizePolicy(DataAccessInfo info){
		return 8;
	}

	default int initializeSizePolicy(DataAccessInfo info){
		return initializeDefaultSizePolicy();
	}
*/

}
