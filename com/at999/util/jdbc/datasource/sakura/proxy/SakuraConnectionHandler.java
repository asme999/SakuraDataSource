package com.at999.util.jdbc.datasource.sakura.proxy;

import com.at999.util.jdbc.datasource.sakura.DataAccessInfo;
import com.at999.util.jdbc.datasource.sakura.pool.SakuraRestrictedPool;
import com.at999.util.jdbc.datasource.sakura.status.SakuraDataStatus;
import java.sql.Connection;
import java.util.HashMap;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.IllegalAccessException;
public class SakuraConnectionHandler implements InvocationHandler{

	private static String ENHANCE_METHOD_FIX = "enhance";
	private static HashMap<String, Method> enhances;

	private Connection connection;
	private DataAccessInfo info;

	public SakuraConnectionHandler(Connection connection, DataAccessInfo info){
		this.connection = connection;
		this.info = info;
		if(this.enhances == null)
			setEnhances();
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws IllegalAccessException, InvocationTargetException{
		Method enhanceMethod = this.enhances.get(method.getName());
		if(enhanceMethod == null)
			return method.invoke(connection, args);
		return enhanceMethod.invoke(this, proxy, method, args);
	}

	public void setEnhances(HashMap<String, Method> enhances){
		if(this.enhances != null)
			enhances.putAll(this.enhances);
		this.enhances = enhances;
	}

	public void setEnhances(){
		this.enhances = new HashMap<>();
		Method[] ms = this.getClass().getMethods();
		int fix = ENHANCE_METHOD_FIX.length();
		for(Method tm : ms){
			String name = tm.getName();
			if(name.length() > fix && name.startsWith(ENHANCE_METHOD_FIX)){
				name = name.substring(fix, fix + 1).toLowerCase() + name.substring(fix + 1);
				this.enhances.put(name, tm);
			}
		}
	}

	public Object enhanceClose(Object proxy, Method method, Object[] args){
		SakuraRestrictedPool srp = this.info.getConnectionPool();
		SakuraDataStatus sds = (SakuraDataStatus)srp.getConnectionPool().get(this.info.getDataAccess()).get(proxy);
		sds.usable(true);
		return null;
	}

}
