package com.at999.util.jdbc.datasource.sakura;

import com.at999.util.jdbc.datasource.sakura.policy.SakuraDataPolicy;
import com.at999.util.jdbc.datasource.sakura.pool.SakuraRestrictedPool;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;

public interface DataPolicy{

	DataPolicy defaultPolicy = SakuraDataPolicy.newInstance();

	default void executePreGetPolicy(DataAccessInfo info){}

	default void executePostGetPolicy(DataAccessInfo info){}

	default Connection executeOverNotFoundPolicy(DataAccessInfo info) throws ClassNotFoundException, SQLException{
		SakuraRestrictedPool srp = info.getConnectionPool();
		return pool.tagUsage(srp.getPoint(), cast(srp.wireConnection(srp.getPoint()), Connection.class), true);
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
