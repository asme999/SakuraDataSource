package com.at999.util.jdbc.datasource.proxy;

import java.sql.Connection;
import java.lang.reflect.InvocationHandler;

public class SakuraConnectionHandler implements InvocationHandler{

	private Connection connection;

	public SakuraConnectionHandler(Connection connection, DataAccessInfo info){
		this.connection = connection;
	}

	public Object invoke(Object proxy, Method method, Object[] args){
		if("close".equals(method.getName())){

			return null;
		}
		return method.invoke(connection, args);
	}

}
