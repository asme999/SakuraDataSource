package com.at999.util.jdbc.datasource.sakura.proxy;

import com.at999.util.jdbc.datasource.sakura.DataAccessInfo;
import com.at999.util.jdbc.datasource.sakura.pool.SakuraRestrictedPool;
import com.at999.util.jdbc.datasource.sakura.status.SakuraDataStatus;
import java.sql.Connection;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.IllegalAccessException;
public class SakuraConnectionHandler implements InvocationHandler{

	private Connection connection;
	private DataAccessInfo info;

	public SakuraConnectionHandler(Connection connection, DataAccessInfo info){
		this.connection = connection;
		this.info = info;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws IllegalAccessException, InvocationTargetException{
		if("close".equals(method.getName())){
			SakuraRestrictedPool srp = info.getConnectionPool();
			SakuraDataStatus sds = (SakuraDataStatus)srp.getConnectionPool().get(info.getDataAccess()).get(proxy);
			sds.usable(true);
			return null;
		}
		return method.invoke(connection, args);
	}

}
